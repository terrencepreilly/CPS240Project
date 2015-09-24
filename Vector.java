package manipulation.util;

/*
 * A vector represents a point in 2d space
 * it has an x coordinate and a y coordinate
 * they are public and can be accessed by Vector.x/Vector.y
 */

public class Vector {
	public float x;
	public float y;
	
	public Vector(float x, float y){
		this.x = x;
		this.y = y;
	}
	public String toString(){
		return "X: " + x + " Y: " + y;
	}
}
