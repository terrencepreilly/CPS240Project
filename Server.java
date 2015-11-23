import java.net.ServerSocket;
import java.net.Socket;

import java.io.IOException;

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
		try {
			serverSocket = new ServerSocket(port);
			while (running) {
				Socket socket = serverSocket.accept();

				new DataOutputStream(
					socket.getOutputStream()
				).writeInt( syncID.next() );

				executor.execute(new InputHandler(socket, gamestate));
				executor.execute(new OutputHandler(socket, gamestate));
			}
		} catch (IOException ioe) {}

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
