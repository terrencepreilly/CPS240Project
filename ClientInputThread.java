import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientInputThread extends AbstractClientThread implements GameConstants {
	private ObjectInputStream in;

	/**
	 * Create a new ClientInputThread, which connects an ObjectInputSTream
	 * to the Socket for the Server.  Will read and apply any deltas received.
	 */
	public ClientInputThread(String host, int port, GameState gamestate) {
		super(host, port, gamestate);

		try {
			in = new ObjectInputStream( socket.getInputStream() );
		}
		catch (IOException ioe) {
			System.out.println("Problem reading from Server.");
		}
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
	 * Receive and pass on any GameDeltas from the Server.
	 */
	@Override
	public void run() {
		while (running) {
			gamestate.applyGameDelta(readGameDelta());
		}
	}
}
