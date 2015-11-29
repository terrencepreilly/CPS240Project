import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.Queue;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.geom.Point2D;
import java.util.concurrent.locks.*;

public class GameState implements GameConstants {
	HashMap<Integer, Character> characters; // key is uniqueID
	HashMap<Integer, GameDelta> updateFlags;
	List<Scenic> obstacles;
	private Lock lock;

	public GameState() {
		characters = new HashMap<>();
		obstacles = new ArrayList<>();
		updateFlags = new HashMap<>();
		lock = new ReentrantLock();
	}

	public Lock getLock() {
		return lock;
	}

	/**
	 * Apply the given GameDelta.  Updates coordinates and health of a
	 * given Character. Adds obstacles indiscriminately.
	 * @param gd The GameDelta to apply.
	 */
	public synchronized void applyGameDelta(GameDelta gd) {
		if (gd.type == OBSTACLE) {
			boolean add = true;
			for (Scenic obstacle : obstacles) {
				if (gd.locUpdate.compareTo(obstacle.getLocation()) == 0)
					add = false;
			}
			if (add)
				obstacles.add(createObstacle(gd));
		}
		else {
			Character c = null;
			if (characters.containsKey( gd.uniqueID )) {
				c = characters.get( gd.uniqueID );
				c.setLocation( gd.locUpdate );
				c.setHealth( gd.health );
			}
			else {
				addCharacter(gd);
			}
		}	
	}

	/**
	 * Create a GameDelta from a Character in this GameState.
	 * @param uid The unique ID for this Character.
	 * @return A new GameDelta signifying the changes.
	 */
	public synchronized GameDelta createGameDelta(Integer uid) {
		if (! characters.containsKey(uid))
			return null;

		Character c = characters.get(uid);
		int type = c.getType();

		GameDelta gd = new GameDelta( uid, c.getLocation(), c.getHealth(), type, System.currentTimeMillis()); 
		return gd;
	}

	/**
	 * Call createGameDelta, passing the unique ID of the given Character.
	 * @param c The Character whose unique ID will be used and who will
	 * 	be used to create the GameDelta.
	 * @return The GameDelta representing the new stats of this Character.
	 */
	public synchronized GameDelta createGameDelta(Character c) {
		if (characters.containsKey( c.getUniqueID() ))
			return createGameDelta( c.getUniqueID() );
		else {
			return new GameDelta( c.getUniqueID(), c.getLocation(), c.getHealth(), c.getType(), System.currentTimeMillis() );
		}
	}

	/**
	 * Return a new GameDelta for the given Obstacle, whether or not it is 
	 * in the GameState.
	 * @return A GameDelta representing an Obstacle.
	 */
	public synchronized GameDelta createGameDelta(Scenic o) {
		return new GameDelta( 0, o.getLocation(), 0, OBSTACLE, System.currentTimeMillis());
	}

	/**
	 * Add a Character to characters.
	 * @param gd The GameDelta describing a non-extant Character.
	 */
	private synchronized void addCharacter(GameDelta gd) {
		Character c = createCharacter(gd);
		add(c);
	}

	/**
	 * Add a Character to characters.
	 * @param c The Character to be addded.
	 */
	public synchronized void add(Character c) { 
		characters.put(c.getUniqueID(), c); 
	}

	/**
	 * Flag a character for updates.
	 */
	public synchronized void flagForUpdate(Integer uid) {
		updateFlags.put( uid, createGameDelta(uid) );
	}

	/**
	 * Flag a character for updates.
	 */
	public synchronized void flagForUpdate(Character c) {
		flagForUpdate( c.getUniqueID() );
	}

	/**
	 * Return a list of updates occuring after this time.
	 */
	public synchronized List<GameDelta> getUpdate(long prevUpdate) {
		List<GameDelta> l = new LinkedList<>();
		for (Integer key : updateFlags.keySet()) {
			if (updateFlags.get(key).timestamp > prevUpdate)
				l.add(updateFlags.get(key));
		}
		return l;
	}

	/**
	 * Make a new Character from the given GameDelta.  If the given GameDelta
	 * is null, create a blank Character.
	 * @param gd The GameDelta.  If blank, use defaults.
	 * @return A new Character.
	 */
	public synchronized Character createCharacter(GameDelta gd) {
		Character c = null;
                BufferedImage playerImage = null;
                try {
                        //TODO differentiate image by gd.type
                        playerImage = ImageIO.read( new File(PLR_IMAGE_FILENAME) );
                }
                catch (IOException ioe) { System.out.println(ioe); }

		if (gd != null) {
                	c = new Character(playerImage, gd.locUpdate);
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
	 * Make a new Obstacle.
	 */
	public synchronized Scenic createObstacle(GameDelta gd) {
		Scenic s = null;
		BufferedImage obstacleImage = null;
		try {
			obstacleImage = ImageIO.read( new File(OBS_IMAGE_FILENAME));
		} catch (IOException ioe) { System.out.println(ioe); }

		s = new Scenic(obstacleImage, gd.locUpdate);
		return s;
	}

	/**
	 * Return all IDs held in this GameState.
	 * @return A set of all IDs in this GameState.
	 */
	public synchronized Set<Integer> getIDs() { return characters.keySet(); }

	/**
	 * Find the edge which this Character is closest to, and push the
	 * Character outside, only updating the necessary coordinates. (To
	 * ensure that the Character slides on the outside if the incident
	 * is not perpendicular.)
	 * @param obsBC The obstacle's BoxCollider.
	 * @param chaBC the Characters BoxCollider.
	 * @return A Vector of where to reset this Character's location.
	 */
        private synchronized Vector getAGoodLocation(BoxCollider obsBC, BoxCollider chaBC) {
                // Find an overlapping vertex.
                Point2D.Float overlaps = null;
                for (Point2D.Float pf : chaBC.getVertices()) {
                        if (obsBC.contains( pf ))
                                overlaps = pf;
                } 

                // find closest edge 
                Point2D.Float top = new Point2D.Float(0f, obsBC.getLocation().y);
                Point2D.Float left = new Point2D.Float(obsBC.getLocation().x, 0f);
                Point2D.Float right = new Point2D.Float(obsBC.getLocation().x + (float) obsBC.getWidth(), 0f);
                Point2D.Float bottom = new Point2D.Float(0f, obsBC.getLocation().y + (float) obsBC.getHeight());

                Point2D.Float closest = null;
                for (Point2D.Float pf : Arrays.asList( new Point2D.Float[] { top, left, right, bottom } ) ) {
                        if (closest == null || overlaps.distance(closest) > overlaps.distance(pf))
                                closest = pf;
                }

                // Update one coordinate to push outside.
                Vector addVec = new Vector(0f, 0f);
                if (closest.equals(top)) 
                        addVec.y = -1*(overlaps.y - closest.y) - 0.1f;  
                else if (closest.equals(left))
                        addVec.x = -1*(overlaps.x - closest.x) - 0.1f;
                else if (closest.equals(right))
                        addVec.x = closest.x - overlaps.x + 0.1f; 
                else if (closest.equals(bottom))
                        addVec.y = closest.y - overlaps.y + 0.1f; 
                
                return chaBC.getLocation().add(addVec);
        }

	/**
	 * Detect collision for a single character and restor to a good location.
	 */
	public synchronized void detectCollisions(Character c) {
		boolean needToReset = false;
		BoxCollider cbc = c.getBoxCollider();
		for (Scenic s : obstacles) {
			BoxCollider sbc = s.getBoxCollider();
			if (sbc.intersects(cbc)) {
				needToReset = true;
				c.setLocation(getAGoodLocation(sbc, cbc));
			}
		}

		for (Integer cuid2 : characters.keySet()) {
			Character c2 = characters.get(cuid2);
			BoxCollider c2bc = c2.getBoxCollider();
			if ( !(cuid2 == c.getUniqueID()) && cbc.intersects(c2bc)) {
				needToReset = true;
				c.setLocation(c.getLastGoodLocation());
			}
		}

		if (! needToReset) {
			c.setLastGoodLocation(c.getLocation());
		}
	}

	/**
	 * Get the map of all Characters.
	 * @return A HashMap of all Characters.
	 */
	public synchronized HashMap<Integer, Character> getCharacters() { 
		return characters; 
	}

	/**
	 * Get the list of all Obstacles.
	 * @return A List of all obstacles.
	 */
	public synchronized List<Scenic> getObstacles() { return obstacles; }

	public synchronized String toString() {
		String ret = "";
		ret += "Characters\n";
		for (Integer uid : characters.keySet())
			ret += "\t" + characters.get(uid) + "\n";

		return ret;
	}
}
