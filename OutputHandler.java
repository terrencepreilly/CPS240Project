import java.io.ObjectOutputStream;
import java.io.IOException;

import java.net.Socket;

class OutputHandler implements Runnable {
	Socket socket;
	GameState gamestate;
	ObjectOutputStream out;

	private int prev;

	public OutputHandler(Socket socket, GameState gamestate) {
		this.socket = socket;
		this.gamestate = gamestate;
		try {
			out = new ObjectOutputStream( socket.getOutputStream() );
		} catch (IOException ioe) { ioe.printStackTrace(); }
	}

	public void run() {
		try {
			while (true) {
				GameDelta gd = gamestate.getUpdate(); // NULLPointerException
				if (gd != null) {
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
