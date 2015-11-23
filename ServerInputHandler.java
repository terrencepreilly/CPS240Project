import java.net.Socket;
import java.io.IOException;
import java.io.EOFException;

public class ServerInputHandler extends InputHandler {
	public ServerInputHandler(Socket socket, GameState gamestate) {
		super(socket, gamestate);
	}

	public void run() {
		try {
			while (true) {
				GameDelta gd = (GameDelta) in.readObject();

				gamestate.applyGameDelta(gd);
				gamestate.flagForUpdate(gd.uniqueID);
			}
		}
		catch (EOFException eofe) { eofe.printStackTrace(); }
		catch (IOException ioe) { ioe.printStackTrace(); }
		catch (ClassNotFoundException cnfe) { cnfe.printStackTrace(); }
	}
}
