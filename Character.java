import javax.vecmath.Vector2f;
import java.awt.image.BufferedImage;

abstract class Character {
	int health;
	Vector2f location;
	
	public abstract BufferedImage getImage( /*Index for animation?*/ );

	public Vector2f getLocation() {
		return location;
	}

	public void updateX(float x) {
		location.x = x;
	}
	
	public void updateY(float y) {
		location.y = y;
	}
	
}
