import java.awt.geom.*;
import java.awt.image.BufferedImage;

/**
 * The BoxCollider class will hold a BufferedImage of the representing character/scenic object
 * Based on the image it will make a box collider of appropriate length.
 * When the location needs to be moved it will be moved with set location
 * A scenic object should not need it's location moved, but the method will
 * allow it to be for the initial setting, which will be done with the constructor
 * 
 * It will store an array of vectors representing the points of the box collider
 * It will also use the image passed in the constructor to store the necessary
 * width and height of the collider values (height and width of the original image
 */
class BoxCollider extends Rectangle2D.Float {

	/**
	 * Create a new BoxCollider.
	 * @param x The x coordinate of its upper left-hand corner.
	 * @param y The y coordinate of its upper left-hand corner.
	 * @param w The width of this BoxCollider.
	 * @param h The height of this BoxCollider.
	 */
	public BoxCollider(float x, float y, float w, float h) {
		super(x, y, w, h);
	}

	/**
	 * Create a new BoxCollider from an image, using its dimensions.
	 * @param image The image whose dimensions will determine this 
	 * 	BoxCollider.
	 */
	public BoxCollider(BufferedImage image){
		//make new boxcollider based on image
		//scenic objects only need 2 points
		super();
		float width = (float) image.getWidth();
		float height = (float) image.getHeight();
		this.setRect(0f, 0f, width, height);
	}

	/**
	 * Set the location of this BoxCollider.
	 * @param location A Vector representing the upper-left hand coordinates
	 * 	of this BoxCollider.
	 */
	public void setLocation(Vector location){
		this.setRect(location.x, location.y, (float) this.getWidth(), (float) this.getHeight());
	}

	/**
	 * Return the location of this BoxCollider.
	 * @return The upper left-hand coordinates of this BoxCollider.
	 */
	public Vector getLocation() {
		return new Vector((float) this.getX(), (float) this.getY()); 
	}

	/**
	 * Return a String representation of this BoxCollider.
	 * @return A String representation of this BoxCollider.
	 */
	public String toString(){
		String ret = String.format(
			"BoxCollider: (%f, %f), %f X %f", 
			this.getX(),
			this.getY(),
			this.getWidth(),
			this.getHeight()
		);
		return ret;
	}
}
