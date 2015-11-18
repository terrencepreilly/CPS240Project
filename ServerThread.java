import java.util.Set;

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
public class ServerThread extends Thread implements GameConstants {
	private Socket socket;
	ObjectInputStream in;
	ObjectOutputStream out;

	boolean running;

	Character enemy;
	GameState gamestate;

	private SynchronizedIDCounter idcounter;

	/**
	 * Initialize the server at the given port.
	 * @param port The port at which to open this Server.
	 * @return A new Server.
	 */
	//TODO Connect multiple Clients
	public ServerThread(Socket socket, GameState gamestate, 
	SynchronizedIDCounter idcounter) {
		try {
			this.socket = socket;
			in = new ObjectInputStream( socket.getInputStream() );
			out = new ObjectOutputStream( socket.getOutputStream() );
		} catch (IOException ioe) {}

		this.gamestate = gamestate;
		this.idcounter = idcounter; // reference to SynchronizedIDCounter
		enemy = this.gamestate.createCharacter(null);
		enemy.setType(ENEMY);
		enemy.setUniqueID(idcounter.next());
		enemy.setLocation(new Vector(100f, 100f));
		this.gamestate.add(enemy);
		running = true;
	} 

	/**
	 * Close the Server.
	 */
	public void close() {
		try { in.close(); socket.close(); } 
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
                catch (IOException ioe) { }
                catch (ClassNotFoundException cnfe) {
			System.out.println("Problem translating data\n");
			cnfe.printStackTrace();
		}
                return null;
        }

	/**
	 * Read the next GameDelta from the connection, and apply it. 
	 * Handle client requests through GameDelta.uniqueID and GameConstants.
	 * @return True if a valid GameDelta was read. False otherwise.
	 */
	public boolean readAndApply() {
		GameDelta gd = readGameDelta();
		if (gd == null) {
			return false;
		}

		if (gd.uniqueID == UID_REQUEST) {
			gd.uniqueID = idcounter.next();
			writeGameDelta(gd);
			gamestate.applyGameDelta(gd);
		}
		else if (gd.uniqueID == UPDATE_REQUEST) {
			Set<Integer> uids = gamestate.getIDs();
			writeGameDelta( new GameDelta(uids.size()) );

			for (Integer id : uids)
				writeGameDelta( gamestate.createGameDelta(id) );
		}
		else if (gd.uniqueID == THREAD_KILL) {
			running = false; 
		}
		else {
			gamestate.applyGameDelta(gd);
		}
		return true;
	}

	/**
	 * Kills this thread.
	 */
	public void killThread() { running = false; }

	/**
	 * Run the server. That is, take requests from client, update
	 * enemy locations, and write to client.
	 */
	public void run() {
		while (running) {
			// TODO update character locations
			// Note, only one thread should update character location
			// perhaps denote a given thread to be the updater
			readAndApply();
		}
		close();
	}
}
