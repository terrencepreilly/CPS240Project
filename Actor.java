import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collection;

/*
 * The actor is an abstract class meant to be extended by
 * characters AND objects. They represent physical entities
 * on the screen that have IMAGES and locations at the
 * very least. They have assessor/mutators methods for the 
 * location and image
 */
public abstract class Actor {
	protected BufferedImage image;
	protected Vector location;
	private LinkedList<Vector> path;		// The path of this actor to take
	private boolean simpleStep;		// Whether to use simpleStep algorithm
					// or AStar
	private float stepSize;
	
	/**
	 * Create a new Actor object.
	 * @param image The image for this character.
	 * @param location The location of this character.
	 * @return A new Actor.
	 */
	public Actor(BufferedImage image, Vector location){
		this.image = image;
		this.location = location;
		this.path = null;
		this.simpleStep = false; // TODO determine when to use simpleStep. 
		this.stepSize = 1.5f;
	}

	/**
	 * Get the image for this Actor for drawing to the screen.
	 * @return The image of this Actor.
 	 */
	public BufferedImage getImage(){
		return image;
	}

	/**
	 * Get the location of this Actor.
	 * @return A vector describing the upper-left coordinates of this
	 * 	Actor.
 	 */
	public Vector getLocation(){
		return location;
	}

	/**
	 * Set the image for this Actor.
	 * @param image The image for this Actor.
	 */
	public void setImage(BufferedImage image){
		this.image = image;
	}

	/**
	 * Set the location for this Actor.
	 * @param location A vector describing the upper-left corner location
	 * 	of this Actor.
	 */
	public void setLocation(Vector location){
		this.location = location;
	}

	/**
	 * Set the boxCollider for this Actor (determines the boundaries
	 * of the Actor.
	 * @param image The image whose dimensions describe the boundaries.
	 */
	protected abstract void setBoxCollider(BufferedImage image);

	/**
	 * Get the boxCollider for this Actor.
	 * @return The boxCollider for this Actor.
	 */
	public abstract BoxCollider getBoxCollider();

	/**
	 * Build a path from current location to goal, using either the
	 * simpleStep algorithm or the A* algorithm.
	 * @param goal A Vector describing where this path will lead.
	 * @param dmW The width dimension of Actor (for spliting the screen into
	 * 	nodes.)
	 * @param dmH The hight dimension of Actor (for splitting the screen into
	 * 	nodes.)
	 * @param allActors A Collection of all the Actors on the board.
	 * @param allObjects An ArrayList of all the Objects on the board.
	 * @param mcBc The boxCollider for the main character.
	 */
	public void buildPath(Vector goal, int dmW, int dmH, Collection<Character> allActors, Collection<Scenic> allObjects, BoxCollider mcBC) {
		if (simpleStep) {
			path = new LinkedList<Vector>();
			path.add( goal.subtract(location) );
		}
		else if (path == null) {
			ArrayList<Point> spaces = AStarUtility.getSpaces(
				dmW,
				dmH,
				this.getBoxCollider(),
				allActors,
				allObjects
			);

			LinkedList<Point> pointPath = AStarUtility.getPath(
				spaces,
				this.getBoxCollider(),
				mcBC
			);
			this.path = AStarUtility.getVectorPath(
				pointPath,
				this.getBoxCollider()
			);
		}
	}

	/**
	 * Take a step in the direction of the current path.  If there is no path,
	 * build one.
	 */
	public void step() {
		if (simpleStep && path != null) {
			Vector p = path.peek();
			if (location.distance(p) < stepSize) {
				setLocation(location.add(p));
				path = null;
			}
			else {
				float divisor = p.magnetude() / stepSize;
				Vector step = p.divide(divisor);
				setLocation(location.add(step));
			}
		}
		else if (path != null && path.size() > 0) {
			Vector p = path.peek();
			if (location.distance(location.add(p)) < stepSize) { 
				setLocation(location.add(p));
				path.pop();
				if (path.size() == 0)
					path = null;
			}
			else {
				float divisor = p.magnetude() / stepSize;
				Vector step = p.divide(divisor);
				setLocation(location.add(step));
				Vector newp = path.pop().subtract(step);
				path.addFirst(newp);
			}
		}
	}
}
