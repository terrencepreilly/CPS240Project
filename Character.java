import javax.vecmath.Vector2f;
import java.awt.image.BufferedImage;

abstract class Character {
	int health;
	Vector2f location;
	
	public abstract BufferedImage getImage( /*Index for animation?*/ );

	public Vector2f getLocation() {
		return Vector2f;
	}

	// public void updateLocation(float x, float y); ???
}
