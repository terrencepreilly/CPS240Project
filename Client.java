
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class Client implements GameConstants {
	Socket socket;
	ObjectInputStream in;
	ObjectOutputStream out;
	GameState gamestate;
	Character player;
	String host;
	int port;

	public Client(String host, int port) {
		try {
			socket = new Socket(host, port);
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException ioe) { System.out.println(ioe); }

		this.host = host;
		this.port = port;

		gamestate = new GameState();
		player = gamestate.createCharacter(null);
		player.setType(PLAYER);
		gamestate.add(player);
	}

	public void movePlayer(float dy, float dx) {
		player.setLocation( new Vector(dy, dx) );
		GameDelta gd = gamestate.createGameDelta(player.getUniqueID());
		try {
			out.writeObject(gd);
			out.flush();
		}
		catch (IOException ioe) { System.out.println(ioe); }
	}
	
	public void updateMap() {
		try {
			gamestate.applyGameDelta( (GameDelta) in.readObject() );
		} 
		catch (IOException ioe) { System.out.println(ioe); }
		catch (ClassNotFoundException cnfe) { System.out.println(cnfe); }
	}

	public void printStatus() {
		System.out.println(gamestate.toString());
	}

	public static void main(String[] args) {
		Client c = new Client("localhost", 8000);
		System.out.println("Before:\n");
		c.printStatus();
		c.movePlayer(10f, 10f);
		c.updateMap();
		System.out.println("After:\n");
		c.printStatus();
	}
}
