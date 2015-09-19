import javax.vecmath.Vector2f;
import java.awt.image.BufferedImage;

abstract class Character {
	private int health;
	
	//a single point in 2D coordinates representing the center of our characters image
	private Vector2f location;
	
	//the box collider utilizes an array of Vector2f values
	//it will basically be 4 points. This will allow us to detect when
	//2 characters Vector2f arrays (represented as a box) have intersected
	//first Vector2f will represent the top 2 coordinates of the box, the second
	//Vector2f will represent the bottom 2 coordinates.
	//then when deteced take appropriate action (move away, take damage, etc)
	private Vector2f [] boxcollider;
	
	public abstract BufferedImage getImage( /*Index for animation?*/ );
	
	public Vector2f getLocation() {
		return location;
	}
	public Vector2f[] getBoxCollider() {
		return boxCollider;
	}

	public void updateX(float x) {
		location.x = x;
	}
	
	public void updateY(float y) {
		location.y = y;
	}
	
}
