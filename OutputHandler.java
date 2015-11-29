import java.io.ObjectOutputStream;
import java.io.IOException;

import java.net.Socket;

import java.util.List;

class OutputHandler implements Runnable {
	Socket socket;
	GameState gamestate;
	ObjectOutputStream out;
	Long prevSent;

	private int prev;

	public OutputHandler(Socket socket, GameState gamestate) {
		this.socket = socket;
		this.gamestate = gamestate;
		try {
			out = new ObjectOutputStream( socket.getOutputStream() );
		} catch (IOException ioe) { ioe.printStackTrace(); }
		prevSent = System.currentTimeMillis();
	}

	public void run() {
		try {
			while (true) {
				List<GameDelta> l = gamestate.getUpdate(prevSent); // NULLPointerException
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
		catch (IOException ioe) { ioe.printStackTrace(); }
		catch (InterruptedException ie) { ie.printStackTrace(); }
	}
}
