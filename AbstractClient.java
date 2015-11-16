
import java.net.Socket;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * An abstract class implementing a Client which talks to a Server.
 */
public abstract class AbstractClient implements GameConstants {
	String host;
	int port;
	ObjectOutputStream out;
	ObjectInputStream in;
	Socket socket;

	GameState gamestate;

	/**
	 * Create a new AbstractClient with internal gamestate.
	 */
	public AbstractClient(String host, int port) {
		this(host, port, null);
	}

	/**
	 * Create a new AbstractClient.
	 * @param host The name of the server where we want to connect.
	 * @param port The port of the server where we're connecting.
	 */
	public AbstractClient(String host, int port, GameState gamestate) {
		try {
			socket = new Socket(host, port);
			out = new ObjectOutputStream( socket.getOutputStream() );
			in = new ObjectInputStream( socket.getInputStream() );
		}
		catch (IOException ioe) {
			System.out.println("Unable to connect to host.");
		}

		if (gamestate == null)
			this.gamestate = new GameState();
		else
			this.gamestate = gamestate;
	}

	/**
	 * Create a character, request a unique ID from the server,
	 * send a GameDelta to the server (to add it to the server's 
	 * GameState.)  Return a reference to the Character. 
	 */
	public Character requestCharacter(int type) {
		Character c = gamestate.createCharacter(null);
		c.setType(type);

		writeGameDelta( new GameDelta(UID_REQUEST, c.getBoxCollider().getLocation(), c.getHealth(), type) );

		GameDelta initDelta = readGameDelta();
		if (initDelta != null)
			c.setUniqueID(initDelta.uniqueID);

		return c;
	}

	/**
	 * Write the given GameDelta to the Server.
	 * @param gd The GameDelta to write.
	 */
	public void writeGameDelta(GameDelta gd) {
                try {
                        out.writeObject(gd);
                        out.flush();
                }
                catch (IOException ioe) {}
	}

	/**
         * Read the next GameDelta from the connection.
         * @return The GameDelta that was read.
         */
	public GameDelta readGameDelta() {
		try { return (GameDelta) in.readObject(); }
		catch (IOException ioe) { System.out.println(ioe);}
		catch (ClassNotFoundException cnfe) {}
		return null;
	}

	/**
	 * Request updates for all characters in the Server.
	 */
	public void requestUpdate() {
		writeGameDelta( new GameDelta(UPDATE_REQUEST) );
		int n = readGameDelta().uniqueID;
		System.out.println(n + " records to update");
		for (int i = 0; i < n; i++) {
			GameDelta gd = readGameDelta();
			System.out.println( "\t" + gd );
			gamestate.applyGameDelta(gd);
		}
	}

	/**
	 * Close the Client.
	 */
	public void close() {
		try { out.close(); socket.close(); }
		catch (IOException ioe) {}
	}
}
