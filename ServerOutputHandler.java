import java.util.HashSet;
import java.util.LinkedList;
import java.net.Socket;
import java.io.IOException;
import java.util.List;

class ServerOutputHandler extends OutputHandler {
	// UIDs not to update for this client. (The client's own characters.
	HashSet<Integer> uids;

	public ServerOutputHandler(Socket socket, GameState gamestate, 
	LinkedList uids) {
		super(socket, gamestate);
		this.uids = new HashSet<>();
		for (Object o : uids) {
			if (o instanceof Integer)
				this.uids.add( (Integer) o );
		}
		prevSent = System.currentTimeMillis();
	}

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

	public void run() {
		writeInitialGameState();
                try {
                        while (true) {
                                List<GameDelta> l = gamestate.getUpdate(prevSent);
				for (GameDelta gd : l) {
					if (gd != null && !uids.contains(gd.uniqueID)) { 
						out.writeObject(gd);
						out.flush();
					}
				}
				prevSent = System.currentTimeMillis();
                                // make room for other processes
                                Thread.sleep(1L);
                        }
                }
                catch (IOException ioe) { ioe.printStackTrace(); }
                catch (InterruptedException ie) { ie.printStackTrace(); }
	}
}
