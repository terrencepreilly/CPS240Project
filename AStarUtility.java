import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.LinkedHashSet;
import java.util.ListIterator;
import java.util.Collection;

/**
 * A utility class for translating using the AStar algorithm.  Translates the 
 * map space into nodes, builds lists of empty spaces, etc.
 */
class AStarUtility implements GameConstants {

	/**
	 * Build a list of spaces the Actor can occupy.
	 * @param displayWidth The width of the display.
	 * @param displayHeight The height of the display.
	 * @param moveCharBC The boxCollider for the character who will
	 * 	be moving.
	 * @param allActors A Collection of all the Actors on the board.
	 * @param allObjects An ArrayList of all the Objects on the board.
	 * @return An ArrayList<Point> of nodes that the moving Actor can 
	 * 	traverse.
	 */
	public static ArrayList<Point> getSpaces(int displayWidth, int displayHeight, BoxCollider moveCharBC, Collection<Character> allActors, Collection<Scenic> allObjects) {
		ArrayList<Point> ret = new ArrayList<Point>();
		// 1) split the screen into moveCharBC size chunks
		float width = (float) moveCharBC.getWidth();
		float height = (float) moveCharBC.getHeight();
		int dWidth  = displayWidth  / (int) width; 
		int dHeight = displayHeight / (int) height;

		// create a box collider at 0, 0 of size moveCharBC
		Ghost g = new Ghost();
		g.setBoxCollider(new BoxCollider(0f, 0f, width, height));
		for (int y = 0; y < dHeight; y++) {
			for (int x = 0; x < dWidth; x++) {
				// move boxCollider to this space
				Vector newLoc = new Vector(x*width, y*height);
				g.setLocation( newLoc );
				boolean isFree = true;
				// check if free, add to list
				for (Character c : allActors) {
					if( c.getBoxCollider().intersects(g.getBoxCollider()) )
						isFree = false;
				}
				for (Scenic a : allObjects) {
					if ( a.getBoxCollider().intersects(g.getBoxCollider()) )
						isFree = false;
				}
				if (isFree)
					ret.add( new Point(x, y) );
			}
		}

		return ret;
	}

	/**
	 * Get spaces adjacent to character/scenic/display corners.
	 * @param displayWidth The width of the display.
	 * @param displayHeight The height of the display.
	 * @param moveCharBC The BoxCollider for the moving Character.
	 * @param allActors All the Actors in the game.
	 * @param allObjects All the Scenic objects in the game.
 	 * @return An ArrayList holding all open spaces at the corners of the 
	 * 	objects in the game.
	 */
	public static ArrayList<Point> getCornerSpaces(int displayWidth, 
	int displayHeight, BoxCollider moveCharBC, Collection<Character> allActors,
	Collection<Scenic> allObjects) {
		ArrayList<Point> ret = new ArrayList<>();
		for (Character c : allActors) {
			ret.addAll( getFilteredPoints(c, moveCharBC, displayWidth, displayHeight) );
		}
		for (Scenic s : allObjects) {
			List<Point> lp = getPoint(s, moveCharBC);
			ret.addAll( getFilteredPoints(s, moveCharBC, displayWidth, displayHeight) );
		}
		return ret;
	}

	/**
	 * Filter the points to remove extraneous/overlapping points.
	 * @param a The given Actor around which points are being found.
	 * @param moveCharBC The BoxCollider for the moving Character.
	 * @param displayWidth The width of the display.
	 * @param displayHeight The height of the display.
	 */
	public static List<Point> getFilteredPoints(Actor a, 
	BoxCollider moveCharBC, int displayWidth, int displayHeight) {
		return filterExtraneous( getPoint(a, moveCharBC), displayWidth, 
		displayHeight, moveCharBC );
	}

	/**
	 * Filter extraneous points (those outside of the display).
	 * @param l The list of points to filter.
	 * @param displayWidth The width of the display.
	 * @param displayHeight the height of the display.
	 * @param moveCharBC The BoxCollider of the character who is moving.
	 */
	public static List<Point> filterExtraneous(List<Point> l, int displayWidth,
	int displayHeight, BoxCollider moveCharBC) {
		List<Point> ret = new LinkedList<>();
		for (Point p : l) {
			if ( !(p.x < 0 || p.y > 0 || p.x+moveCharBC.getWidth() > displayWidth || p.y+moveCharBC.getHeight() > displayHeight) ) {
				ret.add(p);
			}
		}
		return ret;
	}

	/**
	 * Get the four points corresponding to this Actor's corners.
	 * @param a The Actor around which to find points.
	 * @param moveCharBC the Character which is moving.
	 */
	public static List<Point> getPoint(Actor a, BoxCollider moveCharBC) {
		List<Point> ret = new LinkedList<>();
		int ax = (int) a.getBoxCollider().getLocation().x;
		int aw = (int) a.getBoxCollider().getWidth();
		int ay = (int) a.getBoxCollider().getLocation().y;
		int ah = (int) a.getBoxCollider().getHeight();
		int mw = (int) moveCharBC.getWidth();
		int mh = (int) moveCharBC.getHeight();
		ret.add( new Point(ax-mw, ay-mh) );
		ret.add( new Point(ax+aw, ay-mh) );
		ret.add( new Point(ax-mw, ay+ah) );
		ret.add( new Point(ax+aw, ay+ah) );
		return ret;
	}

	/**
	 * Build the actual path using AStar.
	 * @param spaces An ArrayList of nodes the character can occupy.
	 * @param mcharBC The boxCollider of the character who is moving.
	 * @param hero The boxCollider of the main character (the goal.)
	 */
	public static LinkedList<Point> getPath(ArrayList<Point> spaces, BoxCollider mCharBC, BoxCollider hero) {
		Point start = new Point( 
			(int) (mCharBC.getX() / mCharBC.getWidth()),
			(int) (mCharBC.getY() / mCharBC.getHeight())
		);
		Point goal = new Point(
			(int) (hero.getX() / mCharBC.getWidth()),
			(int) (hero.getY() / mCharBC.getHeight())
		);
		spaces.add(start);
		spaces.add(goal);
		AStar a = new AStar(spaces, start, goal);
		return a.AStar();
	}

	/**
	 * Takes the path given by AStar and returns an ArrayList of vectors
	 * representing the same path.
	 * @param path The path given in Points.
	 * @return An ArrayList of Vectors giving the same path.  Concatenates
	 * vectors which have the same direction.
	 */
	public static LinkedList<Vector> getVectorPath( LinkedList<Point> path, BoxCollider mCharBC) {
		LinkedList<Vector> alv = new LinkedList<Vector>();
		Vector prev = mCharBC.getLocation();
		ListIterator<Point> li = path.listIterator(path.size());
		while (li.hasPrevious()) { // BACKWARDS!
			Point p = li.previous();
			Vector next = new Vector( (float) (mCharBC.getWidth() * p.x), (float) (mCharBC.getHeight() * p.y));
			alv.add( next.subtract(prev) );
			prev = next;
		}
		if (alv.get(0).magnetude() == 0.0f) // First link is not needed
			alv.remove(0);
		return alv;
	}
}
