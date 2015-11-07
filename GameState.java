import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class GameState {
	// The key is a unique identifier assigned to each Client and enemy by the 
	// Server.  it is simply a counter maintained by the server
	Integer ENEMY = 0;
	Integer PLAYER = 1;

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
		if (players.containsKey( gd.uniqueID ))
			c = players.get( gd.uniqueID );
		else if (enemies.containsKey( gd.uniqueID ))
			c = enemies.get( gd.uniqueID );
		else {
			addCharacter(gd);
		}
			
			c.setLocation( gd.coords );
			c.setHealth( gd.health );
	}

	/**
	 * Add a Character to players or enemies, depending on the type.
	 * @param gd The GameDelta describing a non-extant Character.
	 */
	private void addCharacter(GameDelta gd) {
		Character c = null;
		BufferedImage playerImage = null;
		try {
			//TODO differentiate image by gd.type
			playerImage = ImageIO.read( new File("character.png") );
		}
		catch (IOException ioe) { System.out.println(ioe); }
		c = new Character(playerImage, gd.coords);

		Integer aUniqueID = prevId + 1;
		prevId++;

		if (gd.type == ENEMY)
			enemies.put(aUniqueID, c);
		else if (gd.type == PLAYER)
			players.put(aUniqueID, c);
	}
}
