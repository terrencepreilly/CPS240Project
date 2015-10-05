import java.awt.image.BufferedImage;
import java.util.LinkedList;

/*
 * The actor is an abstract class meant to be extended by
 * characters AND objects. They represent physical entities
 * on the screen that have IMAGES and locations at the
 * very least.
 * 
 * They have assessor/mutators methods for the location and image
 */

public abstract class Actor {
	protected BufferedImage image;
	protected Vector location;
	LinkedList<Vector> path;		// The path of this actor to take
	boolean simpleStep;		// Whether to use simpleStep algorithm
					// or AStar
	float stepSize;
	
	//constructor
	public Actor(BufferedImage image, Vector location){
		this.image = image;
		this.location = location;
		this.path = null;
		this.simpleStep = true;
		this.stepSize = 1.5f;
	}
	//accessors
	public BufferedImage getImage(){
		return image;
	}
	public Vector getLocation(){
		return location;
	}
	//mutators
	public void setImage(BufferedImage image){
		this.image = image;
	}
	public void setLocation(Vector location){
		this.location = location;
	}
	//abstract methods
	protected abstract void setBoxCollider(BufferedImage image);
	public abstract BoxCollider getBoxCollider();

	public void buildPath(Vector goal) {
		if (simpleStep) {
			path = new LinkedList<Vector>();
			path.add( goal.subtract(location) );
		}
	}

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
	}
}
