
import java.awt.image.BufferedImage;


/*
 * a scenic is an actor that cannot move. It has a set boxCollider
 * and location (extended from actor). Once set the location cannot be changed.
 * It you want a movable scenic object use a character class.
 */

public class Scenic extends Actor {

	private BoxCollider boxCollider; //boxCollider for this Scenic object. Should have 2 vectors
	
	//constructor
	public Scenic(BufferedImage image, Vector location){
		super(image, location);
		setBoxCollider(image); //create a boxCollider when created
		boxCollider.setLocation(location); //set initial location for boxcollider
	}
	
	//actor implemented methods
	public void setBoxCollider(BufferedImage image){
		boxCollider = new BoxCollider(image);
	}
	public BoxCollider getBoxCollider() {
		return boxCollider;
	}
}
