import java.util.concurrent.*;

import java.net.Socket;

import java.io.IOException;

/**
 * An abstract class implementing a Client which talks to a Server.
 */
public abstract class AbstractClient implements GameConstants {
	ExecutorService executor;
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

		Socket socket = null;
		executor = Executors.newFixedThreadPool(2);
		try {
			socket = new Socket(host, port);
			
			executor.execute(new OutputHandler(socket, gamestate));
			executor.execute(new InputHandler(socket, gamestate));
		}
		catch (IOException ioe) {
			System.out.println("Problem connectin to server AC41");
		}
	}

	/**
	 * Close the Client.
	 */
	public void close() {
		executor.shutdown();
	}
}
