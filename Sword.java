import java.awt.image.BufferedImage;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Float;

public class Sword implements Weapon {

	private int damage; //damage done by this sword
	private int length; //length of sword in pixels (character is about 111 pixels high for reference)
	private BufferedImage image;
	private Arc2D.Float boxCollider;
	
	/**
	 * @param image the image for the sword in question
	 * @param damage the damage assigned to this weapon
	 * @param length the length of this sword (important for the arc collider's length/size)
	 */
	public Sword(BufferedImage image, int damage, int length){
		this.image = image;
		this.damage = damage;
		this.length = length;
		boxCollider = new Arc2D.Float(0,0,0,0,0,0, java.awt.geom.Arc2D.OPEN);
	}
	
	/**
	 * This will return the damage of our sword
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * This method creates and returns an arc collider for a simulated sword swing
	 * @param c the character class, needed for positioning and creating the ArcCollider 
	 * 		  (based on angle - characters direction variable - and character location)
	 * @return an Arc2D.Float "boxcollider" to perform intersect tests to see if our sword has "hit" an enemy
	 */
	public Arc2D.Float getBoxCollider(Character c){
		Vector location = c.getLocation();
		int angle = c.getDirection();
		
		boxCollider.setArc(location.x + 20, location.y - 30, 100, 30 + length, angle - 45, angle + 45, java.awt.geom.Arc2D.CHORD);
		return boxCollider;
	}
	/**
	 * Give the length of the sword
	 * @return the length of the sword
	 */
	public int getLength(){
		return length;
	}
	/**
	 * @return the image for this sword
	 */
	public BufferedImage getImage(){
		return image;
	}	
}
