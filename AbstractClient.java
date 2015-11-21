
import java.net.Socket;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * An abstract class implementing a Client which talks to a Server.
 */
public abstract class AbstractClient implements GameConstants {
	ClientOutputThread outthread;
	ClientInputThread inthread;
	GameState gamestate;

	/**
	 * Create a new AbstractClient with internal gamestate.
	 */
	public AbstractClient(String host, int port) { this(host, port, null); }

	/**
	 * Create a new AbstractClient.
	 * @param host The name of the server where we want to connect.
	 * @param port The port of the server where we're connecting.
	 */
	public AbstractClient(String host, int port, GameState gamestate) {
		if (gamestate == null)
			this.gamestate = new GameState();
		else
			this.gamestate = gamestate;

		outthread = new ClientOutputThread(host, port, gamestate);
		inthread = new ClientInputThread(host, port, gamestate);
		outthread.start();
		inthread.start();
	}

	/**
	 * Close the Client.
	 */
	public void close() {
		try {
			outthread.killThread();
			outthread.join();
			inthread.killThread();
			inthread.join();
		}
		catch (InterruptedException ie) {
			System.out.println("Unable to close threads.");
		}
	}
}
