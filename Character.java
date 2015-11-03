
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The character is an image, and a location, it will create it's own boxcollider
 * All you must do is pass it the image, and its location of starting. You may set health later
 * and there are methods for getting it's box collider and last good location. You should set the
 * last good location anytime a collision is NOT detected. Consequently there is a method
 * to set the location, this will in turn set the boxCollider to a new location
 */
class Character extends Actor implements Runnable{
	
	private Vector lastGoodLocation; //last good location as determined by collisiondetection
	private int health;
	private BoxCollider boxCollider; //boxCollider for this Character. Should have 4 points
	int playerID;
	DataOutputStream out;
	DataInputStream in;
	Character[] user = new Character[10];
	//Create a new character and send information to server
	public Character(BufferedImage image,Vector location, DataOutputStream out, DataInputStream in, Character[] user, int pID){
		//character is an image, location, and a boxCollider, health set to 10 until changed
		super(image, location);
		this.out = out;
		this.in = in;
		this.user = user;
		this.playerID = pID;
		lastGoodLocation = location;
		setBoxCollider(image);
		boxCollider.setLocation(location);
		health = 10;
	}
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
	//collect information for character/player and send to server
	public void run() {
		try{
			out.writeInt(playerID);
		}catch(IOException ioe){
			System.out.println("Failed to send PlayerID");
		}
		while(true){
			try{
				//read information collected by DataInputStream from the game and assign to variables
				int playerIDin = in.readInt();//player ID
				float xin = in.readFloat();//x coordinate
				float yin = in.readFloat();//y coordinate
				for(int i = 0; i<5; i++){
					//as long as user is not equal to null send information collected by server to client class
					if(user[i] != null){
						user[i].out.writeInt(playerIDin);//player ID
						user[i].out.writeFloat(xin);//x coordinate
						user[i].out.writeFloat(yin);//y coordinate
					}
				}
			}
		catch(IOException e){
			user[playerID] = null;
		}
		}
		
	}
}
}
