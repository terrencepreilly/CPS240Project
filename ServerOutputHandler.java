import java.util.HashSet;
import java.util.LinkedList;
import java.net.Socket;
import java.io.IOException;

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
        }

	public void run() {
		writeInitialGameState();
                try {
                        while (true) {
				// TODO getUpdate pops the update off of the list,
				// but that means ONLY ONE CLIENT receives it.
                                GameDelta gd = gamestate.getUpdate();
                                if (gd != null && !uids.contains(gd.uniqueID)) { 
                                        out.writeObject(gd);
                                        out.flush();
                                }

                                // make room for other processes
                                Thread.sleep(1L);
                        }
                }
                catch (IOException ioe) { ioe.printStackTrace(); }
                catch (InterruptedException ie) { ie.printStackTrace(); }

	}
}
