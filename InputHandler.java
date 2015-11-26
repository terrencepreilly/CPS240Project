import java.net.Socket;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.EOFException;

class InputHandler implements Runnable {
	Socket socket;
	GameState gamestate;
	ObjectInputStream in;

	public InputHandler(Socket socket, GameState gamestate) {
		System.out.println("INPUTHANDLER:\tconstructor\tstart");
		this.socket = socket;
		this.gamestate = gamestate;
		System.out.println("INPUTHANDLER:\tconstructor\tmiddle");
		try {
			in = new ObjectInputStream( socket.getInputStream() );
			System.out.println("INPUTHANDLER:\tconstructor\tin instantiated");
		} catch (IOException ioe) { ioe.printStackTrace(); }
		System.out.println("INPUTHANDLER:\tconstructor\tfinish");
	}

	public void run() {
		System.out.println("INPUTHANDLER:\trun");
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
