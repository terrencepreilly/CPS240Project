import java.net.Socket;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.EOFException;

/**
 * A task for handling all input to a host's GameState.
 */
class InputHandler implements Runnable {
	Socket socket;
	GameState gamestate;
	ObjectInputStream in;

	/**
	 * Create a new instance of this task.
	 * @param socket The socket connecting this Client to a Server or 
	 * 	vice versa.
	 * @param gamestate The GameState to update.
	 * @return A new instance fo InputHandler.
	 */
	public InputHandler(Socket socket, GameState gamestate) {
		this.socket = socket;
		this.gamestate = gamestate;
		try {
			in = new ObjectInputStream( socket.getInputStream() );
		} catch (IOException ioe) { ioe.printStackTrace(); }
	}

	/**
	 * Start this task. (I.e. Read GameDeltas and apply them to the 
	 * GameState.)
	 */
	public void run() {
		try {
			while (true) {
				GameDelta gd = (GameDelta) in.readObject();
				gamestate.applyGameDelta(gd);
			}
		}
		catch (EOFException eofe) { eofe.printStackTrace(); }
		catch (IOException ioe) { ioe.printStackTrace(); }
		catch (ClassNotFoundException cnfe) { cnfe.printStackTrace(); }
	}
}
