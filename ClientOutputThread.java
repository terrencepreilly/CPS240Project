import java.io.ObjectOutputStream;
import java.net.Socket;

import java.io.IOException;

public class ClientOutputThread extends AbstractClientThread implements GameConstants {
	private ObjectOutputStream out;

	/**
	 * Create a new ClientOutputThread, which connects an ObjectOutputStream
	 * to the Socket for the Server.  Will only send new deltas from the 
	 * gamestate, whenever they are received.
	 */
	public ClientOutputThread(String host, int port, GameState gamestate) {
		super(host, port, gamestate);
		try {
			out = new ObjectOutputStream( socket.getOutputStream() );
		}
		catch (IOException ioe) {
			System.out.println("Unable to connect to server for output.");
		}
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
	 * Requests any updates of the characters from GameState, and sends them.
	 */
	@Override
	public void run() {
		while (running) {
			GameDelta writeMe = gamestate.getUpdate();
			if (writeMe != null)
				writeGameDelta(writeMe);
		}
	}
}
