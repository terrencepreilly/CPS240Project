import java.net.Socket;
import java.io.IOException;
import java.io.EOFException;

/**
 * A task for handling input to a Server.
 */
public class ServerInputHandler extends InputHandler {

	/**
	 * Create a new instance of ServerinputHandler.
	 * @param socket The socket connecting this Server to a client. 
	 * @param gamestate The GameState this ServerInputHandler should 
	 * 	update.
	 */
	public ServerInputHandler(Socket socket, GameState gamestate) {
		super(socket, gamestate);
	}

	/**
	 * Start this task. (I.e. Receive GameDeltas from a Client, and update
	 * 	the GameState.)
	 */
	public void run() {
		try {
			while (true) {
				GameDelta gd = (GameDelta) in.readObject();

				gamestate.applyGameDelta(gd);
				gamestate.flagForUpdate(gd.uniqueID);
			}
		}
		catch (java.net.SocketException se) { System.out.println("Player Quit");}
		catch (EOFException eofe) { eofe.printStackTrace(); }
		catch (IOException ioe) { ioe.printStackTrace(); }
		catch (ClassNotFoundException cnfe) { cnfe.printStackTrace(); }
	}
}
