import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * A class which implements the A* Pathfinding Algorithm.
 */
class AStar {
	private ArrayList<Point> spaces; 	// Open, available places to move
	private Point start;
	private Point goal; 
	private HashMap<Point, Double> fscores;	// Cost from here to goal
	private HashMap<Point, Double> gscores;	// Cost from start to here
	private HashSet<Point> openset;		// unvisited points
	private HashSet<Point> closedset; 	// visited Points

	/**
	 * Construct a new AStar instance.
	 * @param spaces An ArrayList of open spaces.
	 * @param start The starting Point.
	 * @param goal The ending Point.
	 * @return A new AStar instance.
	 */
	public AStar(ArrayList<Point> spaces, Point start, Point goal) {
		this.spaces = spaces;
		this.start = start;
		this.goal = goal;
		this.fscores = new HashMap<Point, Double>( spaces.size() );
		this.gscores = new HashMap<Point, Double>( spaces.size() );
		this.openset = new HashSet<Point>( spaces.size() );
		this.closedset = new HashSet<Point>( spaces.size() );
		fscores.put(start, Double.MAX_VALUE);
		fscores.put(goal, Double.MAX_VALUE);
		gscores.put(start, Double.MAX_VALUE);
		gscores.put(goal, Double.MAX_VALUE);

		for (Point space : spaces) {
			fscores.put(space, Double.MAX_VALUE);
			gscores.put(space, Double.MAX_VALUE);
		}
	}

	/**
	 * Get the distance between two points.
	 * @param a The first point.
	 * @param b The second point.
	 * @return The distance between the two points. (Always a positive value).
	 */
	private double distance(Point a, Point b) {
		Double xdist = new Double( a.x - b.x );
		Double ydist = new Double( a.y - b.y );
		return Math.sqrt( Math.pow(xdist, 2.0) + Math.pow(ydist, 2.0) );
	}

	/**
	 * Calculate the heuristic cost estimate for a given point. 
	 * Here, implemented as the distance from this point to the goal.
	 * @param p The Point.
	 * @return The heuristic cost estimate for Point p.
	 */
	private double heuristicCost(Point p) {
		return distance(p, goal);
	}

	// TODO Slow! Implement as HashSet
	/**
	 * Check to see if this Point is in the set of spaces.
	 * @param p The Point to check.
	 * @return True if p is in the set of open spaces, false otherwise.
	 */
	private boolean spacesContains(Point p) {
		for (Point q : spaces) {
			if (q.equals(p))
				return true;
		}
		return false;
	}

	// TODO implement fscore as an Ordered Map, to speed this up.
	/**
	 * Return the Point with the lowest fscore in the openset.
	 * @return The Point with the lowest fscore in the openset.
	 */
	private Point lowestFInOpen() {
		Point lowestP = null;
		for (Point p : openset) {
			if (lowestP == null) 
				lowestP = p;
			else if (fscores.get(p) < fscores.get(lowestP))
				lowestP = p;
		}
		return lowestP;
	}

	/**
	 * Get the neighbors of this Point.
	 * @param p The Point for which to find neighbors.
	 * @return The neighbors of this Point, p.
	 */
	private ArrayList<Point> getNeighbors(Point p) {
		ArrayList<Point> arr = new ArrayList<Point>();
		for (int y = -1; y <= 1; y++) {
			for (int x = -1; x <= 1; x++) {
				Point npoint = new Point( p.x + x, p.y + y);
				if ( p.compareTo(npoint) != 0 && spacesContains( npoint ) ) {
					arr.add( npoint );
				}
			}
		}
		return arr;
	}

	/**
	 * Construct the path from point start to a using the given HashMap.
	 * Note that constructPath only works when passed a reference to a 
	 * Point it contains. (It uses .equals, not .compareTo).
	 * @param a The ending point.
	 * @param allPaths The HashMap of paths from one node to another.
	 * @return A LinkedHashSet starting at start and leading to goal.
	 */
	private LinkedHashSet<Point> constructPath(Point a, HashMap<Point, Point> allPaths) {
		LinkedHashSet<Point> ret = new LinkedHashSet<Point>();
		ret.add(a);
		Point current = a;
		while (allPaths.containsKey(current)) {
			current = allPaths.get(current);
			ret.add(current);
		}
		return ret;
	}

	// TODO Refactor this method to make smaller, more readable.
	/**
	 * Return the path from start to goal as a LinkedHashSet (no nodes repeat.)
	 * @return The path from start to goal.
	 */
	public LinkedHashSet<Point> AStar() {
		HashMap<Point, Point> allPaths = new HashMap<Point, Point>();
		openset.add( start );
		gscores.remove( start );
		gscores.put( start, 0.0 );
		fscores.remove(start);
		fscores.put(start, gscores.get(start) + heuristicCost(start));
		Point current = null;

		while (openset.size() > 0) {
			current = lowestFInOpen();

			if (current.equals(goal))
				return constructPath(current, allPaths);

			openset.remove(current);
			closedset.add(current);

			for (Point neighbor : getNeighbors(current)) {
				if (closedset.contains(neighbor))
					continue;
				double temp_g = gscores.get(current) + distance(current, neighbor);
				if (	! openset.contains(neighbor) || 
					temp_g < gscores.get(neighbor) 	) {
					allPaths.put(neighbor, current);
					gscores.remove(neighbor);
					gscores.put(neighbor, temp_g);
					fscores.remove(neighbor);
					fscores.put(neighbor, gscores.get(neighbor) + heuristicCost(neighbor));
					if (! openset.contains(neighbor))
						openset.add(neighbor);
				}
			}
		}

		return null;
	}
}
