import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class GameState implements GameConstants {
	// The key is a unique identifier assigned to each Client and enemy by the 
	// Server.  it is simply a counter maintained by the server
	HashMap<Integer, Character> players;
	HashMap<Integer, Character> enemies;
	List<Scenic> obstacles;

	HashMap<Integer, Vector> prevCoords; // Key: unique identifier for client
	HashMap<Integer, Integer> prevHealths;

	int prevId;

	public GameState() {
		prevId = -1;
		players = new HashMap<>();
		enemies = new HashMap<>();
		obstacles = new ArrayList<>();
		prevCoords = new HashMap<>();
		prevHealths = new HashMap<>();
	}

	/**
	 * Apply the given GameDelta.  Updates coordinates and health of a
	 * given Character.
	 * @param gd The GameDelta to apply.
	 */
	public void applyGameDelta(GameDelta gd) {
		Character c = null;
		if (players.containsKey( gd.uniqueID )) {
			c = players.get( gd.uniqueID );
			c.setLocation( gd.coords );
                        c.setHealth( gd.health );
		}
		else if (enemies.containsKey( gd.uniqueID )) {
			c = enemies.get( gd.uniqueID );
			c.setLocation( gd.coords );
                        c.setHealth( gd.health );
		}
		else {
			addCharacter(gd);
		}
	}

	/**
	 * Create a GameDelta from a Character in this GameState.
	 * @param uid The unique ID for this Character.
	 * @return A new GameDelta signifying the changes.
	 */
	public GameDelta createGameDelta(Integer uid) {
		if (! (players.containsKey(uid) || enemies.containsKey(uid)))
			return null;

		Character c = players.containsKey(uid) ? players.get(uid) : enemies.get(uid);
		int type = players.containsKey(uid) ? PLAYER : ENEMY;

		GameDelta gd = new GameDelta( uid, c.getBoxCollider().getLocation(), c.getHealth(), type ); 
		return gd;
	}

	/**
	 * Call createGameDelta, passing the unique ID of the given Character.
	 * @param c The Character whose unique ID will be used and who will
	 * 	be used to create the GameDelta.
	 * @return The GameDelta representing the new stats of this Character.
	 */
	public GameDelta createGameDelta(Character c) {
		return createGameDelta( c.getUniqueID() );
	}

	/**
	 * Add a Character to players or enemies, depending on the type.
	 * @param gd The GameDelta describing a non-extant Character.
	 */
	private void addCharacter(GameDelta gd) {
		Character c = createCharacter(gd);
		add(c);
	}

	/**
	 * Add a Character to players or enemies, depending on the type.
	 * @param c The Character to be addded.
	 */
	public void add(Character c) {
		if (c.getType() == ENEMY)
			enemies.put(c.getUniqueID(), c);
		else
			players.put(c.getUniqueID(), c);
	}

	/**
	 * Make a new Character from the given GameDelta.  If the given GameDelta
	 * is null, create a blank Character.
	 * @param gd The GameDelta.  If blank, use defaults.
	 * @return A new Character.
	 */
	public Character createCharacter(GameDelta gd) {
		Character c = null;
                BufferedImage playerImage = null;
                try {
                        //TODO differentiate image by gd.type
                        playerImage = ImageIO.read( new File("character.png") );
                }
                catch (IOException ioe) { System.out.println(ioe); }

		if (gd != null) {
                	c = new Character(playerImage, gd.coords);
			c.setHealth( gd.health );
		}
		else 
			c = new Character(playerImage, new Vector(0f, 0f));

                Integer aUniqueID = prevId + 1;
                prevId++;
                c.setUniqueID(aUniqueID);

		return c;
	}


	public String toString() {
		String ret = "";
		ret += "Players\n";
		for (Integer uid : players.keySet())
			ret += "\t" + players.get(uid) + "\n";

		System.out.println("\nEnemies\n");
		for (Integer uid : enemies.keySet())
			ret += "\t" + enemies.get(uid) + "\n";

		return ret;
	}
}
