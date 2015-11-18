import java.util.Set;

import java.net.ServerSocket;

import java.io.IOException;
import java.util.ArrayList;


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

	private ArrayList<ServerThread> threads;
	private boolean running;

	private SynchronizedIDCounter idcounter;

	/**
	 * Initialize the server at the given port.
	 * @param port The port at which to open this Server.
	 * @return A new Server.
	 */
	public Server(int port) {
		gamestate = new GameState();
		idcounter = new SynchronizedIDCounter();
		threads = new ArrayList<ServerThread>();
		running = true;
		this.port = port;
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
				System.out.println("Waiting for thread...");
				threads.add(
					new ServerThread(
						serverSocket.accept(),
						gamestate,
						idcounter
					)
				);
				System.out.println("Connection found, starting thread");
				threads.get( threads.size() - 1).start();
			}
		} catch (IOException ioe) {}

	} 

	/**
	 * Close the Server.
	 */
	public void close() {
		System.out.println("Closing threads");
		try { 
			for (ServerThread st : threads) {
				System.out.println("\tKilling " + st.getName());
				st.killThread();
				st.join();
			}
			serverSocket.close(); 
		}
		catch (IOException ioe) {}
		catch (InterruptedException ie) { System.out.println(ie); }
	}

	public static void main(String[] args) {
		Server s = new Server(8000);
		s.connectClients();
		s.close();
	}

}
