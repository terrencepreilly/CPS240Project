import java.util.HashSet;
import java.util.LinkedList;
import java.net.Socket;
import java.io.IOException;
import java.util.List;

/**
 * A task for handling all output from a Server.
 */
class ServerOutputHandler extends OutputHandler {
	/**
	 * Create a new ServerOutputHandler task.
 	 * @param socket The connection from this Server to a Client.
	 * @param gamestate The Server's GameState.
	 * @return A new instance of ServerOutputHandler.
	 */
	public ServerOutputHandler(Socket socket, GameState gamestate) {
		super(socket, gamestate);
		prevSent = System.currentTimeMillis();
	}

	/**
	 * Write out the GameState to the Client at the time of its creation.
	 */
	private void writeInitialGameState() {
                try {
                        for (Integer uid: gamestate.characters.keySet()) {
                                GameDelta gd = gamestate.createGameDelta(uid);
                                out.writeObject(gd);
                        }
                        for (Scenic o : gamestate.obstacles) {
                                GameDelta gd = gamestate.createGameDelta(o);
                                out.writeObject(gd);
                        }
                        out.flush();
                } catch (IOException ioe) { ioe.printStackTrace(); }
		prevSent = System.currentTimeMillis();
        }

	/**
	 * Start the ServerOutputHandler. (I.e. wait for changes to the GameState,
	 * which come from the ServerInputHandler, and write them to the Client 
	 * through the socket.
	 */
	public void run() {
		writeInitialGameState();
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
                                Thread.sleep(1L);
                        }
                }
		catch (java.net.SocketException se) { }
                catch (IOException ioe) { ioe.printStackTrace(); }
                catch (InterruptedException ie) { ie.printStackTrace(); }
	}
}
