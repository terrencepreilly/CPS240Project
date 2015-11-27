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

	//TODO Don't pass the locations back to the client you got it from.
	public void run() {
                try {
                        while (true) {
                                GameDelta gd = gamestate.getUpdate();
                                if (gd != null && !uids.contains(gd.uniqueID)) { //Glitchy 
//				if (gd != null) { // TODO WTF?
                                        out.writeObject(gd);
                                        out.flush();
                                }

                                // make room for other processes
                                Thread.sleep(10L);
                        }
                }
                catch (IOException ioe) { ioe.printStackTrace(); }
                catch (InterruptedException ie) { ie.printStackTrace(); }

	}
}
