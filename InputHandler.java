import java.net.Socket;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.EOFException;

class InputHandler implements Runnable {
	Socket socket;
	GameState gamestate;
	ObjectInputStream in;

	public InputHandler(Socket socket, GameState gamestate) {
		this.socket = socket;
		this.gamestate = gamestate;
		try {
			in = new ObjectInputStream( socket.getInputStream() );
		} catch (IOException ioe) { ioe.printStackTrace(); }
	}

	public void run() {
		try {
			while (true) {
				GameDelta gd = (GameDelta) in.readObject();
				System.out.println("INPUTHANDLER:\trun\tReceived: " + gd);
				gamestate.applyGameDelta(gd);
				System.out.println("INPUTHANDLER:\trun\tapplied delta");
			}
		}
		catch (EOFException eofe) { eofe.printStackTrace(); }
		catch (IOException ioe) { ioe.printStackTrace(); }
		catch (ClassNotFoundException cnfe) { cnfe.printStackTrace(); }
	}
}
