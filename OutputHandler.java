import java.io.ObjectOutputStream;
import java.io.IOException;

import java.net.Socket;

class OutputHandler implements Runnable {
	private Socket socket;
	private GameState gamestate;
	private ObjectOutputStream out;

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
				GameDelta gd = gamestate.getUpdate();
				if (gd != null) {
					out.writeObject(gd);
					out.flush();
					System.out.println("Sent: " + gd);
				}

				// make room for other processes
				Thread.sleep(10L);
			}
		}
		catch (IOException ioe) { ioe.printStackTrace(); }
		catch (InterruptedException ie) { ie.printStackTrace(); }
	}
}
