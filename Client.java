
import java.net.Socket;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * A basic Game Client.  Each player will run a Client, connected to a Server.
 * (Either on their own machine or on another.)
 */
//TODO request updates for each character from server
public class Client implements GameConstants {
	String host;
	int port;
	ObjectOutputStream out;
	ObjectInputStream in;
	Socket socket;

	GameState gamestate;
	Character player;

	/**
	 * Create a new Client.
	 * @param host The name of the server where we want to connect.
	 * @param port The port of the server where we're connecting.
	 */
	public Client(String host, int port) {
		try {
			socket = new Socket(host, port);
			out = new ObjectOutputStream( socket.getOutputStream() );
			in = new ObjectInputStream( socket.getInputStream() );
		}
		catch (IOException ioe) {}

		gamestate = new GameState();
		player = gamestate.createCharacter(null);
		player.setType(PLAYER);

		writeGameDelta();

		GameDelta initDelta = readGameDelta();
		if (initDelta != null)
			player.setUniqueID(initDelta.uniqueID);

		gamestate.add(player);
	}

	/**
	 * Send a GameDelta of the player to the connected Server.
	 */
	// TODO Call this for every movement of player, and for each
	// enemy killed
        public void writeGameDelta() {
		GameDelta gd = gamestate.createGameDelta( player );
		writeGameDelta(gd);
        }

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

	//TODO delete after testing
	public void testMoveAndUpdateCharacter() {
		player.setLocation( new Vector(500f, 500f) );
		GameDelta gd = gamestate.createGameDelta(player);
		System.out.println(gamestate.createGameDelta(player));
		writeGameDelta(gd);
//		System.out.println(gamestate);
//		System.out.println("-\t\t\t" + gd);
	}

	/**
	 * Close the Client.
	 */
	public void close() {
		try { out.close(); socket.close(); }
		catch (IOException ioe) {}
	}
	
	public static void main(String[] args) {
		Client c = new Client("localhost", 8000);
//		System.out.println(c.player);
		c.testMoveAndUpdateCharacter();
//		System.out.println(c.player);
//		System.out.println("\n\n" + c.gamestate);
		c.requestUpdate();
		System.out.println( c.gamestate );
		c.close();
	}
}
