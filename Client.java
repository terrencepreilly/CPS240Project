import java.util.LinkedList;
import java.util.Arrays;

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
		// Request an ID from the Server.
		super(host, port, gamestate, 
			new LinkedList<>( Arrays.asList( new Integer[] { 
				PLAYER, END_UID_REQUEST 
			}))
		);
		player = charactersCreated.get(0);
		((Actor) player).setLocation(new Vector(SCREEN_WIDTH/2, SCREEN_HEIGHT/2));
	}

	/**
	 * Get the player.
	 * @return The player for this Client.
	 */
	public Character getPlayer() { return player; }
}
