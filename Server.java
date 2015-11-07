import java.net.ServerSocket;
import java.net.Socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.IOException;

public class Server {
	ServerSocket serverSocket;
	Socket socket;
	ObjectInputStream in;
	ObjectOutputStream out;

	Character enemy;
	GameState gamestate;

	public Server(int port) {
		try {
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();
			in = new ObjectInputStream( socket.getInputStream() );
			out = new ObjectOutputStream( socket.getOutputStream() );
		} catch (IOException ioe) {}

		gamestate = new GameState();
		enemy = gamestate.createCharacter(null);
		gamestate.add(enemy);
	}

	public void close() {
		try { in.close(); socket.close(); serverSocket.close(); }
		catch (IOException ioe) {}
	}

	public void writeGameDelta() {
		try { 
			GameDelta gd = gamestate.createGameDelta( enemy );
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

	public static void main(String[] args) {
		Server s = new Server(8000);
		s.writeGameDelta();
		System.out.println(s.readGameDelta());
		s.close();
	}

}
