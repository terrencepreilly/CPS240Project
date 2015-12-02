import java.io.ObjectOutputStream;
import java.io.IOException;

import java.net.Socket;

import java.util.List;

/**
 * A task for handling all output from a host's GameState.
 */
class OutputHandler implements Runnable {
	Socket socket;
	GameState gamestate;
	ObjectOutputStream out;
	Long prevSent;

	private int prev;

	/**
	 * Create a new instance of this task.
	 * @param socket A Socket connecting this host to another.
	 * @param gamestate The GameState to check for updates.
	 * @return A new instance of OutputHandler.
	 */
	public OutputHandler(Socket socket, GameState gamestate) {
		this.socket = socket;
		this.gamestate = gamestate;
		try {
			out = new ObjectOutputStream( socket.getOutputStream() );
		} catch (IOException ioe) { ioe.printStackTrace(); }
		prevSent = System.currentTimeMillis();
	}

	/**
	 * Start this task. (I.e. Check the GameState for updates on Characters,
	 * create GameDeltas, and write them to the Socket.)
	 */
	public void run() {
		try {
			while (true) {
				List<GameDelta> l = gamestate.getUpdate(prevSent); 
				for (GameDelta gd : l) {
					if (gd != null) {
						out.writeObject(gd);
						out.flush();
					}
				}
				prevSent = System.currentTimeMillis();
				// make room for other processes
				Thread.sleep(10L);
			}
		}
		catch (java.net.SocketException se) {}
		catch (IOException ioe) { ioe.printStackTrace(); }
		catch (InterruptedException ie) { ie.printStackTrace(); }
	}
}
