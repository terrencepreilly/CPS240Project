
import java.net.Socket;

import java.io.IOException;

/**
 * A class for handling character updates and updates to the GameState.
 */
public abstract class AbstractClientThread extends Thread implements GameConstants {
	Socket socket;
	boolean running;
	GameState gamestate;

	/**
	 * Create a new AbstractClientThread.  Will initialize all networking 
	 * components to null, instantiate the gamestate, and set running to true.
	 */
	public AbstractClientThread(String host, int port, GameState gamestate) { 
		try {
			socket = new Socket(host, port);
		}
		catch (IOException ioe) {
			System.out.println("Problem connecting to Server.");
		}

		running = true;

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
        /*public Character requestCharacter(int type) {
                Character c = gamestate.createCharacter(null);
                c.setType(type);

                writeGameDelta( new GameDelta(UID_REQUEST, c.getBoxCollider().getLocation(), c.getHealth(), type) );

                GameDelta initDelta = readGameDelta();
                if (initDelta != null)
                        c.setUniqueID(initDelta.uniqueID);

                return c;
        }*/

	/**
	 * Will send or receive data depending on which ServerThread is 
	 * implemented.
	 */
	public abstract void run();

	/**
	 * Kills this Thread.
	 */
	public void killThread() { running = false; }

	/**
	 * Close the ClientThread.
	 */
	public void close() {
		try { 
			socket.close();
		}
		catch (IOException ioe) {}
		catch (NullPointerException npe) {}
	}
}
