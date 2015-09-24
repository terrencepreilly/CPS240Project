package actors.util;

import java.awt.image.BufferedImage;
import manipulation.util.*;

/*
 * The actor is an abstract class meant to be extended by
 * characters AND objects. They represent physical entities
 * on the screen that have IMAGES and locations at the
 * very least.
 * 
 * They have assessor/mutators methods for the location and image
 */

public abstract class Actor {
	protected BufferedImage image;
	protected Vector location;
	
	//constructor
	public Actor(BufferedImage image, Vector location){
		this.image = image;
		this.location = location;
	}
	//accessors
	public BufferedImage getImage(){
		return image;
	}
	public Vector getLocation(){
		return location;
	}
	//mutators
	public void setImage(BufferedImage image){
		this.image = image;
	}
	public void setLocation(Vector location){
		this.location = location;
	}
	//abstract methods
	protected abstract void setBoxCollider(BufferedImage image);
	public abstract BoxCollider getBoxCollider();
}
