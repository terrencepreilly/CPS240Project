import java.net.ServerSocket;
import java.net.Socket;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.EOFException;

import java.util.Set;
import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.*;


/**
 * A simple server for our game which holds a GameState object to 
 * handle incoming GameDeltas. The server holds enemies, objects,
 * and assigns unique ids to all Characters in the server and in 
 * each Client.
 */
public class Server implements GameConstants {
	private ServerSocket serverSocket;
	private GameState gamestate;
	private int port;

	private ExecutorService executor;
	private boolean running;

	private SynchronizedIDCounter syncID;

	private Random rand;

	/**
	 * Initialize the server at the given port.
	 * @param port The port at which to open this Server.
	 * @return A new Server.
	 */
	public Server(int port) {
		gamestate = new GameState();
		rand = new Random(RANDSEED);
		addObstacles();

		executor = Executors.newCachedThreadPool();
		running = true;
		this.port = port;
		syncID = new SynchronizedIDCounter();
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException ioe) { ioe.printStackTrace(); }
	}

	/**
	 * Create a given number of obstacles at random locations seeded with 
	 * RANDSEED, so that it is the same each time.
	 */
	private void addObstacles() {
		for (int i = 0; i < OBSTACLE_SPAWN; i++) {
			float randX = (float) rand.nextFloat() * SCREEN_WIDTH;
			float randY = (float) rand.nextFloat() * SCREEN_HEIGHT;
			gamestate.applyGameDelta(
				new GameDelta(
					-1,
					new Vector(randX, randY),
					0,
					OBSTACLE,
					System.currentTimeMillis()
				)
			);
		}
		addBorderObstacles();
	}

	/**
	 * Add borders so that the characters cannot go off of the screen.
	 */
	private void addBorderObstacles() {
		ArrayList<Vector> borderLocations = new ArrayList<>(
			Arrays.asList( new Vector[] {
				new Vector(0f, 0f - SCREEN_HEIGHT),
				new Vector(SCREEN_WIDTH, 0f),
				new Vector(SCREEN_HEIGHT, 0f),
				new Vector(0-SCREEN_WIDTH, 0f)
			})
		);
		for (Vector bl : borderLocations) {
			gamestate.applyGameDelta(
				new GameDelta(
					-1,
					bl,
					0,
					BORDER_OBSTACLE,
					System.currentTimeMillis()
				)
			);
		}
	}

	/**
	 * Connect any clients while the server is running.
	 * Add the new thread to threads. (To join until all
	 * have closed.)
	 */
	public void connectClients() {
		try {
			while (running) {
				Socket socket = serverSocket.accept();

				// Pass the values to OutputHandler so that
				// updates from a uid aren't 
				LinkedList uids = serveUniqueIDs(socket);

				executor.execute(new ServerOutputHandler(socket, gamestate, uids));
				executor.execute(new ServerInputHandler(socket, gamestate));
			}
		}
		catch (IOException ioe) { ioe.printStackTrace(); }

	} 

// setFlagForUpdate for any updates coming from server.

	/**
	 * Send a unique ID to the connecting Client.
	 * @param socket The Socket connecting this Server to the Client.
	 * @return A list containing each unique ID that was sent.
	 */
	public LinkedList serveUniqueIDs(Socket socket) throws IOException {
		DataOutputStream dos = new DataOutputStream(
			socket.getOutputStream() );
		DataInputStream dis = new DataInputStream(
			socket.getInputStream() );

		int rec = dis.readInt();
		LinkedList<Integer> ret = new LinkedList<>();
		while (rec != END_UID_REQUEST) {
			ret.addLast( syncID.next() );
			dos.writeInt( ret.getLast() );
			rec = dis.readInt();
		}

		return ret;
	}

	/**
	 * Close the Server.
	 */
	public void close() {
		try { 
			executor.shutdown();
			serverSocket.close(); 
		}
		catch (IOException ioe) {}
	}

	public static void main(String[] args) {
		Server s = new Server(8000);
		s.connectClients();
		s.close();
	}

}
