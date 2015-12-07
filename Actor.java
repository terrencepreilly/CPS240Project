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
public abstract class Actor implements GameConstants {
	protected BoxCollider boxCollider;
	protected BufferedImage image;
	protected Vector location;
	protected Integer type;
	private LinkedList<Vector> path;		// The path of this actor to take
	private boolean simpleStep;		// Whether to use simpleStep algorithm
	int direction;

	/**
	 * Create a new, blank Actor instance.
	 * @return A blank Actor instance.
	 */
	public Actor() {
		this.boxCollider = null;
		this.image = null;
		this.location = null;
		this.path = null;
		this.type = OBSTACLE;
	}

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
		this.simpleStep = Math.random() < ASTAR_RATE ? false : true; // 20% chance of using AStar instead of simpleStep
		if (image != null) {
			this.boxCollider = new BoxCollider(image);
			this.boxCollider.setLocation(location);
		}
		else
			this.boxCollider = new BoxCollider(ENEMY_WIDTH, ENEMY_HEIGHT, 0f, 0f);
		this.type = OBSTACLE;
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
         * Get the type of Actor.
         * @return The type of Actor (by default, OBSTACLE).
         */
        public Integer getType() { return type; }

	/**
         * Set the type of Actor.
         * @param type From GameConstants.
         */
        public void setType(int type) { this.type = type; }

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
		if (type == ENEMY) {
			// update the direction
			float dy = location.y - this.location.y;
			float dx = location.x - this.location.x;
			if (Math.abs(dy) > Math.abs(dx)) {
				if (dy > 0)
					this.direction = DOWN;
				else
					this.direction = UP;
			}
			else {
				if (dx < 0)
					this.direction = LEFT;
				else
					this.direction = RIGHT;
			}
		}
		this.location = location;
		this.boxCollider.setLocation(location);
	}

	/**
	 * Return the BoxCollider of this Actor.
	 * @return The BoxCollider of this Actor.
	 */
	public BoxCollider getBoxCollider() {
		return this.boxCollider;
	}

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
			ArrayList<Point> spaces;
			if (USE_CORNER_ASTAR)
				spaces = AStarUtility.getCornerSpaces(
					dmW, 
					dmH, 
					this.getBoxCollider(), 
					allActors, 
					allObjects 
				);
			else
				spaces = AStarUtility.getSpaces(
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
			if (location.distance(p) < ACTOR_SPEED) {
				setLocation(location.add(p));
				path = null;
			}
			else {
				float divisor = p.magnetude() / ACTOR_SPEED;
				Vector step = p.divide(divisor);
				setLocation(this.location.add(step));
			}
		}
		else if (path != null && path.size() > 0) {
			Vector p = path.peek();
			if (location.distance(location.add(p)) < ACTOR_SPEED) { 
				setLocation(location.add(p));
				path.pop();
				if (path.size() == 0)
					path = null;
			}
			else {
				float divisor = p.magnetude() / ACTOR_SPEED;
				Vector step = p.divide(divisor);
				setLocation(location.add(step));
				Vector newp = path.pop().subtract(step);
				path.addFirst(newp);
			}
		}
	}
}
