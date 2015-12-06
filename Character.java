
import java.awt.image.BufferedImage;
import java.util.LinkedList;


/**
 * Represents a moveable, actionable Character in the game.
 */
class Character extends Actor implements GameConstants {
	
	private Vector lastGoodLocation; //last good location as determined by collisiondetection
	private int health;
	private Integer uniqueID;

	// variables related to attacking and position
	private int direction;
	private LinkedList<Weapon> weapons = new LinkedList<Weapon>();
	private boolean isAttacking;
	private boolean isPlayer;
	private double attackCounter;
	
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
		health = DEFAULT_HEALTH;
		direction = UP; // default, facing North
		uniqueID = -1;
		type = ENEMY;
		isAttacking = false;
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
	 * Return a String representation of this Character.
	 * @return A String representation of this Character.
	 */
	public String toString(){
		return this.getLocation() + " HP: " + health + " ID: " + uniqueID + " (" + (type == ENEMY ? "enemy" : "player") +  " " + type + ")";
	}

	public void playerStep(double delta) {
		float xCoord = location.x;
                float yCoord = location.y;
                float properXCoord = xCoord - ((float) SCREEN_WIDTH / 2f);
                float properYCoord = ((float) SCREEN_HEIGHT / 2f) - yCoord;
                float directionX;
                float directionY;
                directionX = ((float) Math.cos(Math.toRadians(direction)) * (float) delta * PLAYER_SPEED * 60f);
                directionY = ((float) Math.sin(Math.toRadians(direction)) * (float) delta * PLAYER_SPEED * 60f);
                properXCoord += directionX;
                properYCoord += directionY;
                float nxCoord = properXCoord + ((float) SCREEN_WIDTH / 2f);
                float nyCoord = ((float) SCREEN_HEIGHT / 2f) - properYCoord;

		this.location = new Vector(nxCoord, nyCoord);
		this.boxCollider.setLocation(this.location);
	}
}
