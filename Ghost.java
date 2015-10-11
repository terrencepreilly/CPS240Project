import java.awt.image.BufferedImage;

/**
 * An actor used in detecting free spaces by AStarUtility. Has no image.
 * @author Terrence Reilly
 */
public class Ghost extends Actor {
	private BoxCollider bc;

	/**
	 * Create a new Ghost at 0, 0.
	 */
	public Ghost() {
		super(null, new Vector(0, 0));
	}

	/**
	 * Sets the image for this Actor.
	 * @deprecated Replaced by {@link #setBoxCollider()} 
	 */
	public void setBoxCollider(BufferedImage image) {}

	/**
	 * Set the BoxCollider for this Ghost.
	 * @param bc The BoxCollider, with Width and Height calculated.
	 */
	public void setBoxCollider(BoxCollider bc) {
		this.bc = bc;
	}

	/**
	 * Get the image for this Actor.
	 * @deprecated Not relevant for Ghost subclass.
	 */
	public BufferedImage getImage() { return null; }

	/**
	 * Set the image for this Actor.
	 * @deprecated Not relevant for Ghost subclass.
	 */
	public void setImage(BufferedImage image) {}

	/**
	 * Get the BoxCollider for this Ghost. (For updates, collision detection,
	 * etc.
	 * @return The BoxCollider for this Ghost.
	 */
	public BoxCollider getBoxCollider() {
		return this.bc;
	}

	/**
	 * Set the location for this Ghost and its BoxCollider.
	 * @param location A vector giving the magnitude of the move.
	 */
	public void setLocation(Vector location) {
		this.location = location;
		this.bc.setLocation(location);
	}

	public String toString() {
		String ghost = String.format("Ghost: %f, %f", location.x, location.y );
		String boxCol = this.bc.toString();
		return ghost + "\n\t" + boxCol;
	}
}
