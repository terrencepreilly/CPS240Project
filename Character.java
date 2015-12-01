
import java.awt.image.BufferedImage;
import java.util.LinkedList;


/**
 * Represents a moveable, actionable Character in the game.
 */
class Character extends Actor implements GameConstants {
	
	private Vector lastGoodLocation; //last good location as determined by collisiondetection
	private int health;
<<<<<<< HEAD
	private BoxCollider boxCollider; //boxCollider for this Character. Should have 4 points
	private int direction; //direction the character is facing to allow manipulation via degrees
=======
	private Integer uniqueID;

	// variables related to attacking and position
	private int direction;
	private LinkedList<Weapon> weapons = new LinkedList<Weapon>();
	private boolean isAttacking;
	private boolean isPlayer;
	private double attackCounter;
>>>>>>> fiddleTCP
	
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
<<<<<<< HEAD
		setBoxCollider(image);
		boxCollider.setLocation(location);
		health = 10;
		direction = 90; //default, facing north
=======
		health = 100;
		direction = UP; // default, facing North
		uniqueID = -1;
		type = ENEMY;
	}

	/**
	 * Set direction faced by Character as a degree. (Right = 0, left = 180,
	 * etc.)
	 * @param direction The direction to set for this Character.
	 */
	public void setDirection(int direction) {
		this.direction = direction % 360;
	}

	/**
	 * Get the direction this Character faces.
	 * @return The direction this Character faces.
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * Set attack status to true.
	 */
	public void attack() {
		if (isAttacking && attackCounter > ATTACK_SPEED) {
			isAttacking = false;
			attackCounter = 0;
		}
		else if (!isAttacking) {
			isAttacking = true;
		}
	}

	// TODO used?
	/**
	 *
	 */
	public void updateCharacterV(double delta) {
		attackCounter += delta;
		if (isAttacking && attackCounter > ATTACK_SPEED) {
			isAttacking = false;
			attackCounter = 0;
		}
	}

	/**
	 * Set this Character to be attacking or not attacking.
	 * @param isAttacking Whether or not this Character is attacking.
	 */
	public void setAttacking(boolean isAttacking) {
		this.isAttacking = isAttacking;
	}

	/**
	 * Returns true if this Character is attacking, otherwise false.
	 * @return True if this Character is attacking, otherwise false.
	 */
	public boolean getAttacking() {
		return isAttacking;
	}

	/**
	 * Gets the Weapon at the front of the Weapon list.
 	 * @return The Weapon at the front of the Weapon list.
	 */
	public Weapon getWeapon() {
		return weapons.get(0);
	}

	/**
	 * Add a Weapon to this Weapon list.
	 * @param w The Weapon to add to the Weapon list.
	 */
	public void addWeapon(Weapon w) {
		weapons.add(w);
>>>>>>> fiddleTCP
	}

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
<<<<<<< HEAD
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
=======
>>>>>>> fiddleTCP
	 * Return a String representation of this Character.
	 * @return A String representation of this Character.
	 */
	public String toString(){
		return this.getLocation() + " HP: " + health + " ID: " + uniqueID + " (" + (type == ENEMY ? "enemy" : "player") +  " " + type + ")";
	}
}
