import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class GameState implements GameConstants {
	// The key is a unique identifier assigned to each Client and enemy by the 
	// Server.  it is simply a counter maintained by the server
	HashMap<Integer, Character> characters;
	List<Scenic> obstacles;

	public GameState() {
		characters = new HashMap<>();
		obstacles = new ArrayList<>();
	}

	/**
	 * Apply the given GameDelta.  Updates coordinates and health of a
	 * given Character.
	 * @param gd The GameDelta to apply.
	 */
	public void applyGameDelta(GameDelta gd) {
		Character c = null;
		if (characters.containsKey( gd.uniqueID )) {
			c = characters.get( gd.uniqueID );
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
		if (! characters.containsKey(uid))
			return null;

		Character c = characters.get(uid);
		int type = c.getType();

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
		if (characters.containsKey( c.getUniqueID() ))
			return createGameDelta( c.getUniqueID() );
		else {
			return new GameDelta( c.getUniqueID(), c.getBoxCollider().getLocation(), c.getHealth(), c.getType() );
		}
	}

	/**
	 * Add a Character to characters.
	 * @param gd The GameDelta describing a non-extant Character.
	 */
	private void addCharacter(GameDelta gd) {
		Character c = createCharacter(gd);
		add(c);
	}

	/**
	 * Add a Character to characters.
	 * @param c The Character to be addded.
	 */
	public void add(Character c) { characters.put(c.getUniqueID(), c); }

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
			c.setUniqueID( gd.uniqueID );
			c.setType(gd.type);
		}
		else {
			c = new Character(playerImage, new Vector(0f, 0f));
			c.setType(ENEMY);
		}

		return c;
	}

	/**
	 * Return all IDs held in this GameState.
	 * @return A set of all IDs in this GameState.
	 */
	public Set<Integer> getIDs() { return characters.keySet(); }


	public String toString() {
		String ret = "";
		ret += "Characters\n";
		for (Integer uid : characters.keySet())
			ret += "\t" + characters.get(uid) + "\n";

		return ret;
	}
}
