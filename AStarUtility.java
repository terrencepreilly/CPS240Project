import java.util.ArrayList;
import java.util.LinkedList;
import java.util.LinkedHashSet;
import java.util.ListIterator;
import java.util.Collection;

/**
 * A utility class for translating using the AStar algorithm.  Translates the 
 * map space into nodes, builds lists of empty spaces, etc.
 */
class AStarUtility {

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
