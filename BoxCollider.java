import java.awt.geom.*;
import java.awt.image.BufferedImage;

/*
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

public class BoxCollider extends Rectangle2D.Float {

	public BoxCollider(float x, float y, float w, float h) {
		super(x, y, w, h);
	}

	public BoxCollider(BufferedImage image){
		//make new boxcollider based on image
		//scenic objects only need 2 points
		super();
		float width = (float) image.getWidth();
		float height = (float) image.getHeight();
		this.setRect(0f, 0f, width, height);
	}

	//mutators
	public void setLocation(Vector location){
		this.setRect(location.x, location.y, (float) this.getWidth(), (float) this.getHeight());
	}

	public Vector getLocation() {
		return new Vector((float) this.getX(), (float) this.getY()); 
	}

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

	public static void main(String[] args) {
		BoxCollider b1 = new BoxCollider( 0.0f, 0.0f, 1.0f, 1.0f);
		BoxCollider b2 = new BoxCollider( 1.5f, 1.5f, 2.5f, 2.5f);
		System.out.printf("%s\n%s\n", b1, b2);
		System.out.println(b1.intersects(b2));
	}
}
