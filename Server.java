import java.net.Socket;
import java.net.ServerSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class Server {
	ObjectInputStream in;
	ObjectOutputStream out;
	ServerSocket server;
	Socket socket;

	GameState gamestate;
	Character enemy;

	public Server(int port) {
		try {
			server = new ServerSocket(port);
			socket = server.accept();
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch(IOException ioe) { System.out.println(ioe); }

		gamestate = new GameState();
		enemy = gamestate.createCharacter(null);
		gamestate.add(enemy);

		try {
			// receive GameDeltas
			GameDelta gd = (GameDelta) in.readObject();
			gamestate.applyGameDelta(gd);
			// update enemy location
			// send GameDelta
			out.writeObject( gamestate.createGameDelta( enemy.getUniqueID() ) );
		} 
		catch (IOException ioe) { System.out.println(ioe); }
		catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe);
		}
	}

	public static void main(String[] args) {
		Server s = new Server(8000);
	}
}
