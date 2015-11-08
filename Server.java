import java.net.ServerSocket;
import java.net.Socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.IOException;


/**
 * A simple server for our game which holds a GameState object to 
 * handle incoming GameDeltas. The server holds enemies, objects,
 * and assigns unique ids to all Characters in the server and in 
 * each Client.
 */
//TODO Handle unique IDs somehow.  Andle different types of requests.
// (I.e. when a client wants to create a new character and needs a 
// unique id.  Use an Integer in the DataInputStream, and define constants
// in GameConstants)
public class Server implements GameConstants {
	ServerSocket serverSocket;
	Socket socket;
	ObjectInputStream in;
	ObjectOutputStream out;

	Character enemy;
	GameState gamestate;

	private int prevID;

	/**
	 * Initialize the server at the given port.
	 * @param port The port at which to open this Server.
	 * @return A new Server.
	 */
	//TODO Connect multiple Clients
	public Server(int port) {
		try {
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();
			in = new ObjectInputStream( socket.getInputStream() );
			out = new ObjectOutputStream( socket.getOutputStream() );
		} catch (IOException ioe) {}

		gamestate = new GameState();
		enemy = gamestate.createCharacter(null);
		enemy.setType(ENEMY);
		gamestate.add(enemy);
		enemy.setUniqueID(0);
		prevID = 0;
	}

	/**
	 * Close the Server.
	 */
	public void close() {
		try { in.close(); socket.close(); serverSocket.close(); }
		catch (IOException ioe) {}
	}

	/**
	 * Send a GameDelta of the enemy to the connected Client.
	 */
	//TODO Send a GameDelta for each enemy, to each client.
	public void writeGameDelta() {
		GameDelta gd = gamestate.createGameDelta( enemy );
		writeGameDelta(gd);
	}

	/**
	 * Send the given GameDelta.
	 * @param gd The GameDelta to send to the socket.
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
	 * Read the next GameDelta from the connection, and apply it.  If the
	 * uniqueID of the GameDelta is -1, redefine as prevID+1, apply, and 
	 * return the updated GameDelta.
	 */
	public void readAndApply() {
		GameDelta gd = readGameDelta();
		if (gd == null)
			return;
		if (gd.uniqueID == -1) {
			prevID++;
			gd.uniqueID = prevID;
			writeGameDelta(gd);
			gamestate.applyGameDelta(gd);
		}
		else
			gamestate.applyGameDelta(gd);
	}

	public static void main(String[] args) {
		Server s = new Server(8000);
		s.readAndApply();
		System.out.println(s.gamestate);
		s.close();
	}

}
