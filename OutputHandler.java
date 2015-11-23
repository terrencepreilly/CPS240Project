import java.io.ObjectOutputStream;
import java.io.IOException;

import java.net.Socket;

class OutputHandler implements Runnable {
	private Socket socket;
	private GameState gamestate;
	private ObjectOutputStream out;

	private int prev;

	public OutputHandler(Socket socket, GameState gamestate) {
		System.out.println("OUTPUTHANDLER:\tconstructor");
		this.socket = socket;
		this.gamestate = gamestate;
		try {
			out = new ObjectOutputStream( socket.getOutputStream() );
		} catch (IOException ioe) { ioe.printStackTrace(); }
		System.out.println("OUTPUTHANDLER:\tconstructor\tfinish");
	}

	public void run() {
		System.out.println("OUTPUTHANDLER:\trun\tbegin");
		try {
			while (true) {
				GameDelta gd = gamestate.getUpdate();
				if (gd != null) {
					System.out.println("OUTPUTHANDLER:\trun\tsending" + gd);
					out.writeObject(gd);
					out.flush();
				}

				// make room for other processes
//				Thread.sleep(10L);
			}
		}
		catch (IOException ioe) { ioe.printStackTrace(); }
//		catch (InterruptedException ie) { ie.printStackTrace(); }
		System.out.println("OUTPUTHANDLER:\trun\tfinish");
	}
}
