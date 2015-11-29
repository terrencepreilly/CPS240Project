
import java.awt.image.BufferedImage;


/**
 * A scenic is an actor that cannot move. It has a set boxCollider
 * and location (extended from actor). Once set the location cannot be changed.
 */
class Scenic extends Actor {

	private BoxCollider boxCollider;
	
	/**
	 * Create a new Scenic instance.
	 * @param image The image of this scenic (determines BoxCollider size.)
	 * @param location The location of the upper, left-hand corner of 
	 * 	this image.
	 */
	public Scenic(BufferedImage image, Vector location){
		super(image, location);
		setBoxCollider(image); //create a boxCollider when created
		boxCollider.setLocation(location); //set initial location for boxcollider
	}
	
	/**
	 * Set the BoxCollider for this Scenic.
	 * @param image The image determining the BoxCollider size.
	 */
	public void setBoxCollider(BufferedImage image){ 
		boxCollider = new BoxCollider(image);
	}

	/**
	 * Get the BoxCollider for this Scenic.
	 * @return The BoxCollider for this Scenic.
	 */
	public BoxCollider getBoxCollider() { return boxCollider; }
}
