import java.util.concurrent.*;
import java.util.LinkedList;
import java.util.List;

import java.net.Socket;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * An abstract class implementing a Client which talks to a Server.
 */
public abstract class AbstractClient implements GameConstants {
	ExecutorService executor;
	GameState gamestate;
	List<Character> charactersCreated;

	/**
	 * Create a new AbstractClient with internal gamestate.
	 */
	public AbstractClient(String host, int port) { this(host, port, null, null); }

	/**
	 * Create a new AbstractClient.
	 * @param host The name of the server where we want to connect.
	 * @param port The port of the server where we're connecting.
	 * @param ctypes The types of characters to populate this server
	 * 	with.  At most one PLAYER. Must end with END_UID_REQUEST.
	 */
	public AbstractClient(String host, int port, GameState gamestate,
	List<Integer> ctypes) {
		System.out.println("ABSTRACTCLIENT:\tconstructor\tctypes: " + ctypes.size() );
		charactersCreated = new LinkedList<>();
		if (gamestate == null)
			this.gamestate = new GameState();
		else
			this.gamestate = gamestate;

		Socket socket = null;
		executor = Executors.newFixedThreadPool(2);
		try {
			socket = new Socket(host, port);

			populateGameState(socket, ctypes);

			System.out.println("ABSTRACTCLIENT:\tconstructor\tcreateOutputHandler");
			executor.execute(new OutputHandler(socket, this.gamestate));
			System.out.println("ABSTRACTCLIENT:\tconstructor\tcreateInputHandler");
			executor.execute(new InputHandler(socket, this.gamestate));
			System.out.println("ABSTRACTCLIENT:\tconstructor\tconstructor\tfinished");
		}
		catch (IOException ioe) {
			System.out.println("Problem connectin to server AC41");
		}
	}

	public void populateGameState(Socket socket, List<Integer> ctypes) 
	throws IOException {
		System.out.println("ABSTRACTCLIENT:\tpopulateGameState");
		DataOutputStream dos = new DataOutputStream( 	
			socket.getOutputStream());
		DataInputStream dis = new DataInputStream(
			socket.getInputStream());
		System.out.println("ABSTRACTCLIENT:\tpopulateGameState\tInput and Output streams created");

		for (Integer i : ctypes) {
			dos.writeInt( i );
			if (i == END_UID_REQUEST)
				break;
			System.out.println("ABSTRACTCLIENT:\tpopulateGameState\t" + i + " written");
			int uid = dis.readInt();
			System.out.println("ABSTRACTCLIENT:\tpopulateGameState\t" + uid + " read");
			Character c = gamestate.createCharacter( new GameDelta(
				uid, new Vector(0f, 0f), 10, i) );
			System.out.println("ABSTRACTCLIENT:\tpopulateGameState\tcreated " + c);
			gamestate.add(c);
			System.out.println("ABSTRACTCLIENT:\tpopulateGameState\tadded Character");
			gamestate.flagForUpdate(c);
			System.out.println("ABSTRACTCLIENT:\tpopulateGameState\tflagged for Update");
			charactersCreated.add(c);
		}
		System.out.println("ABSTRACTCLIENT:\tpopulateGameState\tfinished");
	}

	/**
	 * Close the Client.
	 */
	public void close() {
		executor.shutdown();
	}
}
