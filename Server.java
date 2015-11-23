import java.net.ServerSocket;
import java.net.Socket;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.EOFException;

import java.util.Set;
import java.util.ArrayList;
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

	/**
	 * Initialize the server at the given port.
	 * @param port The port at which to open this Server.
	 * @return A new Server.
	 */
	public Server(int port) {
		gamestate = new GameState();
		executor = Executors.newCachedThreadPool();
		running = true;
		this.port = port;
		syncID = new SynchronizedIDCounter();
	}

	/**
	 * Connect any clients while the server is running.
	 * Add the new thread to threads. (To join until all
	 * have closed.)
	 */
	public void connectClients() {
		System.out.println("SERVER:\tconnectClients");
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("SERVER:\tconnectClients\tserverSocket instantiated");
			while (running) {
				Socket socket = serverSocket.accept();

				System.out.println("SERVER:\tconnectClients\tsocket connection accpted");
				serveUniqueIDs(socket);

				System.out.println("SERVER:\tconnectClients\tInputHandler created");
				executor.execute(new OutputHandler(socket, gamestate));
				executor.execute(new ServerInputHandler(socket, gamestate));
				System.out.println("SERVER:\tconnectClients\tOutputHandler created");
			}
		}
		catch (IOException ioe) {}

	} 

// setFlagForUpdate for any updates coming from server.

	/**
	 * 
	 */
	public void serveUniqueIDs(Socket socket) throws IOException {
		System.out.println("SERVER:\tserveUniqueIDs");
		DataOutputStream dos = new DataOutputStream(
			socket.getOutputStream() );
		DataInputStream dis = new DataInputStream(
			socket.getInputStream() );

		int rec = dis.readInt();
		while (rec != END_UID_REQUEST) {
			System.out.println("SERVER:\tserveUniqueIDs\t" + rec + " served");
			dos.writeInt( syncID.next() );
			rec = dis.readInt();
		}
		System.out.println("SERVER:\tserveUniqueIDs\tfinished");
	}

	/**
	 * Close the Server.
	 */
	public void close() {
		System.out.println("Closing threads");
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
