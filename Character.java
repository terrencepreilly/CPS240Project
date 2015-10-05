
import java.awt.image.BufferedImage;


/*
 * The character is an image, and a location, it will create it's own boxcollider
 * All you must do is pass it the image, and it's location of starting. You may set health later
 * and there are methods for getting it's box collider and last good location. You should set the
 * last good location anytime a collision is NOT detected. Consequently there is a method
 * to set the location, this will in turn set the boxCollider to a new location
 */

public class Character extends Actor{
	
	private Vector lastGoodLocation; //last good location as determined by collisiondetection
	private int health;
	private BoxCollider boxCollider; //boxCollider for this Character. Should have 4 points
	
	//constructor
	public Character(BufferedImage image, Vector location){
		//character is an image, location, and a boxCollider, health set to 10 until changed
		super(image, location);
		lastGoodLocation = location;
		setBoxCollider(image);
		boxCollider.setLocation(location);
		health = 10;
	}
	//accessors
	public int getHealth(){
		return health;
	}
	public Vector getLastGoodLocation(){
		return lastGoodLocation;
	}
	
	//mutators
	public void setLastGoodLocation(Vector lastGoodLocation){
		this.lastGoodLocation = lastGoodLocation;
		//on a valid change of lastGoodLocation set box collider new position as well
		boxCollider.setLocation(lastGoodLocation);
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	public void setLocation(Vector location)
	{
		super.setLocation(location);
		boxCollider.setLocation(location);
	}
	//implement methods from Actor
	public BoxCollider getBoxCollider(){
		return boxCollider;
	}
	protected void setBoxCollider(BufferedImage image){
		boxCollider = new BoxCollider(image);
	}
	//toString method
	public String toString(){
		return "This characters location: " + this.getLocation() + "and health: " + health + ".";
	}
}
