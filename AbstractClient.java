import java.util.concurrent.*;
import java.util.LinkedList;
import java.util.List;

import java.net.Socket;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * An abstract class implementing a Client which talks to a Server.
 */
public abstract class AbstractClient implements GameConstants {
	ExecutorService executor;
	GameState gamestate;
	List<Character> charactersCreated;

	/**
	 * Create a new AbstractClient with internal gamestate.
	 */
	public AbstractClient(String host, int port) { this(host, port, null, null); }

	/**
	 * Create a new AbstractClient.
	 * @param host The name of the server where we want to connect.
	 * @param port The port of the server where we're connecting.
	 * @param ctypes The types of characters to populate this server
	 * 	with.  At most one PLAYER. Must end with END_UID_REQUEST.
	 */
	public AbstractClient(String host, int port, GameState gamestate,
	List<Integer> ctypes) {
		charactersCreated = new LinkedList<>();
		if (gamestate == null)
			this.gamestate = new GameState();
		else
			this.gamestate = gamestate;

		Socket socket = null;
		executor = Executors.newFixedThreadPool(2);
		try {
			socket = new Socket(host, port);

			populateGameState(socket, ctypes);

			executor.execute(new OutputHandler(socket, this.gamestate));
			executor.execute(new InputHandler(socket, this.gamestate));
		}
		catch (IOException ioe) {
		}
	}

	/**
	 * Populate the GameState from the Server.
 	 * @param socket The Client socket, to read from the Server.
	 * @param ctypes The types of characters to place.
	 */
	public void populateGameState(Socket socket, List<Integer> ctypes) 
	throws IOException {
		DataOutputStream dos = new DataOutputStream( 	
			socket.getOutputStream());
		DataInputStream dis = new DataInputStream(
			socket.getInputStream());

		for (Integer i : ctypes) {
			dos.writeInt( i );
			if (i == END_UID_REQUEST)
				break;
			int uid = dis.readInt();
			Character c = gamestate.createCharacter( new GameDelta(
				uid, new Vector(0f, 0f), 10, i, System.currentTimeMillis()));
			gamestate.add(c);
			gamestate.flagForUpdate(c);
			gamestate.addToNoUpdate(c.getUniqueID());
			charactersCreated.add(c);
		}
	}

	/**
	 * Close the Client.
	 */
	public void close() {
		executor.shutdown();
	}
}
