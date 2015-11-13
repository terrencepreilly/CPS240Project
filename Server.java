import java.util.Set;

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
		enemy.setUniqueID(0);
		enemy.setLocation(new Vector(100f, 100f));
		gamestate.add(enemy);
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
                catch (IOException ioe) {
			System.out.println("Problem writing GameDelta\n");
			ioe.printStackTrace();
		}
        }

	/**
	 * Read the next GameDelta from the connection.
	 * @return The GameDelta that was read.
	 */
	public GameDelta readGameDelta() {
                try { return (GameDelta) in.readObject(); }
                catch (IOException ioe) { 
			//System.out.println("Problem reading GameDelta\n");
		//	ioe.printStackTrace(); 
		}
                catch (ClassNotFoundException cnfe) {
			System.out.println("Problem translating data\n");
			cnfe.printStackTrace();
		}
                return null;
        }

	/**
	 * Read the next GameDelta from the connection, and apply it.  If the
	 * uniqueID of the GameDelta is -1, redefine as prevID+1, apply, and 
	 * return the updated GameDelta.
	 * @return True if a valid GameDelta was read. False otherwise.
	 */
	//TODO update all clients with received delta.
	public boolean readAndApply() {
		GameDelta gd = readGameDelta();
		if (gd == null) {
			return false;
		}

		if (gd.uniqueID == UID_REQUEST) {
			prevID++;
			gd.uniqueID = prevID;
			writeGameDelta(gd);
			gamestate.applyGameDelta(gd);
		}
		else if (gd.uniqueID == UPDATE_REQUEST) {
			Set<Integer> uids = gamestate.getIDs();
			writeGameDelta( new GameDelta(uids.size()) );

			for (Integer id : uids)
				writeGameDelta( gamestate.createGameDelta(id) );
		}
		else {
			gamestate.applyGameDelta(gd);
		}
		return true;
	}

	public static void main(String[] args) {
		Server s = new Server(8000);
		s.readAndApply(); 
		s.readAndApply();
		s.readAndApply();
		System.out.println(s.gamestate);
		s.close();
	}

}
