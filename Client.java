
import java.net.Socket;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class Client {
	String host;
	int port;
	ObjectOutputStream out;
	ObjectInputStream in;
	Socket socket;

	GameState gamestate;
	Character player;

	public Client(String host, int port) {
		try {
			socket = new Socket(host, port);
			out = new ObjectOutputStream( socket.getOutputStream() );
			in = new ObjectInputStream( socket.getInputStream() );
		}
		catch (IOException ioe) {}

		gamestate = new GameState();
		player = gamestate.createCharacter(null);
		player.setType(GameConstants.PLAYER);
		gamestate.add(player);
	}

        public void writeGameDelta() {
                try {
                        GameDelta gd = gamestate.createGameDelta( player );
                        out.writeObject(gd);
                        out.flush();
                }
                catch (IOException ioe) {}
        }

	public GameDelta readGameDelta() {
		try { return (GameDelta) in.readObject(); }
		catch (IOException ioe) { System.out.println(ioe);}
		catch (ClassNotFoundException cnfe) {}
		return null;
	}

	public void close() {
		try { out.close(); socket.close(); }
		catch (IOException ioe) {}
	}
	
	public static void main(String[] args) {
		String message = "Hello world!";
		Client c = new Client("localhost", 8000);
		c.writeGameDelta();
		System.out.println(c.readGameDelta());
		c.close();
	}
}
