
import java.awt.image.BufferedImage;


/**
 * Represents a moveable, actionable Character in the game.
 */
class Character extends Actor implements GameConstants {
	
	private Vector lastGoodLocation; //last good location as determined by collisiondetection
	private int health;
	private BoxCollider boxCollider; //boxCollider for this Character. Should have 4 points
	private Integer uniqueID;
	private Integer type;
	
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
		uniqueID = -1;
		type = 0;
	}

	/**
	 * Set the type of Character. (Either PLAYER or ENEMY).
	 * @param type From GameConstants.
	 */
	public void setType(int type) { this.type = type; }

	/**
	 * Get the type of Character.
	 * @return The type of Character, either ENEMY (default) or PLAYER.
	 */
	public Integer getType() { return type; }

	/**
	 * Set the unique id for this Character. Can only be called once.
	 * @param i The new unique id.
	 */
	public void setUniqueID(int i) {
		if (uniqueID == -1 && i >= 0)
			uniqueID = i;
	}

	/**
	 * Get this Character's unique id.
	 * @return This Character's unique id.
	 */
	public Integer getUniqueID() { return uniqueID; }

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
		boxCollider.setLocation(location);
	}

	public Vector getLocation() {
		return boxCollider.getLocation();
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
		return this.getLocation() + " HP: " + health + " ID: " + uniqueID + " (" + (type == ENEMY ? "enemy" : "player") +  " " + type + ")";
	}
}
