
import java.awt.image.BufferedImage;


/**
 * The character is an image, and a location, it will create it's own boxcollider
 * All you must do is pass it the image, and its location of starting. You may set health later
 * and there are methods for getting it's box collider and last good location. You should set the
 * last good location anytime a collision is NOT detected. Consequently there is a method
 * to set the location, this will in turn set the boxCollider to a new location
 */
class Character extends Actor{
	
	private Vector lastGoodLocation; //last good location as determined by collisiondetection
	private int health;
	private BoxCollider boxCollider; //boxCollider for this Character. Should have 4 points
	private int direction; //direction the character is facing to allow manipulation via degrees
	
	/**
	 * Create a new Character.
	 * @param image The image of this Character.
	 * @param location The location of this Character.
	 * @return A new Character.
	 */
	public Character(BufferedImage image, Vector location){
		//character is an image, location, and a boxCollider, health set to 10 until changed
		super(image, location);
		lastGoodLocation = location;
		setBoxCollider(image);
		boxCollider.setLocation(location);
		health = 10;
		direction = 90; //default, facing north
	}

	/**
	 * Get the health of this character.
	 * @return The current health of this Character.
	 */
	public int getHealth(){
		return health;
	}

	/**
	 * Return the last good location of this character.
	 * @return The last good location of this character.
	 */
	// TODO Change this so that the character slides along the box.
	public Vector getLastGoodLocation(){
		return lastGoodLocation;
	}
	
	/**
	 * Set the last good location of this Character.
	 * @param lastGoodLocation The upper, left-hand coordinates of where
	 * 	this Character was last located.
	 */
	public void setLastGoodLocation(Vector lastGoodLocation){
		this.lastGoodLocation = lastGoodLocation;
		//on a valid change of lastGoodLocation set box collider new position as well
		boxCollider.setLocation(lastGoodLocation);
	}
	
	/**
	 * Set the health of this Character.
	 * @param health The health to give this Character.
	 */
	public void setHealth(int health){
		this.health = health;
	}

	/**
	 * Set the location of this Character.
	 * @param location The location of this Character.
	 */
	public void setLocation(Vector location) {
		super.setLocation(location);
		boxCollider.setLocation(location);
	}
	
	/**
	 * Set direction faced by character as a degree "based on unit circle" e.g. right = 0 degrees, left = 180 degrees, etc
	 * @param dValue The value of the direction to be set, passed as a degree value.
	 */
	public void setDirection(int dValue){
		direction = dValue % 360; //if user sets as 90, will be 90, if set as 360, will be 0,
	}

	/**
	 * @return the direction faced by this character.
	 */
	public int getDirection(){
		return direction;
	}
	
	/**
	 * Get the BoxCollider of this Character.
	 * @return The BoxCollider for this Character.
	 */ 
	public BoxCollider getBoxCollider(){
		return boxCollider;
	}

	/**
	 * Set the BoxCollider for this Character.
	 * @param image The image which determines the dimensions of this 
	 * 	Character's BoxCollider.
	 */
	protected void setBoxCollider(BufferedImage image){
		boxCollider = new BoxCollider(image);
	}

	/**
	 * Return a String representation of this Character.
	 * @return A String representation of this Character.
	 */
	public String toString(){
		return "This characters location: " + this.getLocation() + "and health: " + health + ".";
	}
}
