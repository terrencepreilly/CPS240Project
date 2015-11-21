
/**
 * A player game Client, extends AbstractClient to talk with Server.
 */
public class Client extends AbstractClient implements GameConstants {
	Character player;

	/**
	 * Create a new Client, with no previously set up GameState.
	 * @param host The IP to connect to.
	 * @param port The port the Server socket is using.
	 * @return A newly instantiated Client with one player.
	 */
	public Client(String host, int port) {
		this(host, port, null);
	}

	/**
	 * Create a new Client.
         * @param host The IP to connect to.
         * @param port The port the Server socket is using.
	 * @param gamestate The GameState to set up.  If null, create a new one.
         * @return A newly instantiated Client with one player.
	 */
	public Client(String host, int port, GameState gamestate) {
		super(host, port, gamestate);

		GameDelta request = new GameDelta(UID_REQUEST, new Vector(0f, 0f), 10, PLAYER);
		outthread.writeGameDelta(request);
		GameDelta response = inthread.readGameDelta();

		player = gamestate.createCharacter( response );

		gamestate.add(player);
	}

	/**
	 * Write a GameDelta to the server to update the position of player.
	 */
	public void writeGameDelta() {
		GameDelta gd = gamestate.createGameDelta( player );
		outthread.writeGameDelta(gd);
	}

	/**
	 * Get the player.
	 * @return The player for this Client.
	 */
	public Character getPlayer() { return player; }
}
