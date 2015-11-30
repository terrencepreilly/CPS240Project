import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * The character is an image, and a location, it will create it's own boxcollider
 * All you must do is pass it the image, and its location of starting. You may set health later
 * and there are methods for getting it's box collider and last good location. You should set the
 * last good location anytime a collision is NOT detected. Consequently there is a method
 * to set the location, this will in turn set the boxCollider to a new location
 */
public class Character extends Actor{
	
	public static final float ATTACK_SPEED = .50f; //final for attack speed (seconds)
	
	//variables related to position and collision detection
	private Vector lastGoodLocation; //last good location as determined by collisiondetection
	private int health;
	private BoxCollider boxCollider; //boxCollider for this Character. Should have 4 points
	private int direction; //direction the character is facing to allow manipulation via degrees
	
	//variables related to attacking
	private LinkedList<Weapon> weapons = new LinkedList<Weapon>();
	private boolean isAttacking;
	private boolean isPlayer;
	private double attackCounter; //hold counter
	
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
		health = 100;
		direction = 90; //default, facing north
		isPlayer = false; //default not a player
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
	 * Sets whether the player is a human or not, default Character is set to false!
	 * When creating new Characters that are human controlled ENSURE you call this with true passed
	 * @param isPlayer true if human controlled character
	 */
	public void isPlayer(boolean isPlayer){
		this.isPlayer = isPlayer;
	}
	/**
	 *
	 * @return returns if this character is a player controlled by a human
	 */
	public boolean isPlayer(){
		return isPlayer;
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
	 * initiate attack status!!!
	 */
	public void attack(){
		if(isAttacking && attackCounter > ATTACK_SPEED){
			isAttacking = false;
			attackCounter = 0;
		} else if(!isAttacking) {
			isAttacking = true;
		}
	}
	
	public void updateCharacterV(double delta){
		attackCounter += delta;
		if(isAttacking&& attackCounter > ATTACK_SPEED){
			isAttacking = false;
			attackCounter = 0;
		}
	}
	/**
	 * 
	 * @param isAttacking sets attacking flag
	 */
	public void isAttacking(boolean isAttacking){
		this.isAttacking = isAttacking;
	}
	public boolean isAttacking(){
		return isAttacking;
	}
	
	public Weapon getWeapon(){
		return weapons.get(0);
	}
	
	public void addWeapon(Weapon w){
		weapons.add(w);
	}

	/**
	 * Return a String representation of this Character.
	 * @return A String representation of this Character.
	 */
	public String toString(){
		return "This characters location: " + this.getLocation() + "and health: " + health + ".";
	}
}
