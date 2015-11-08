
import java.net.Socket;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * A basic Game Client.  Each player will run a Client, connected to a Server.
 * (Either on their own machine or on another.)
 */
// TODO Request a unique id for the player from the server.  Update the 
// player and send the resulting GameDelta to the Server.
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
		gamestate.add(player);

		writeGameDelta();
		GameDelta initDelta = readGameDelta();
		if (initDelta != null) 
			player.setUniqueID(initDelta.uniqueID);
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

	/**
	 * Close the Client.
	 */
	public void close() {
		try { out.close(); socket.close(); }
		catch (IOException ioe) {}
	}
	
	public static void main(String[] args) {
		Client c = new Client("localhost", 8000);
		System.out.println(c.player);
		c.close();
	}
}
