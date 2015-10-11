
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
		return "(" + x + ", " + y + ")";
	}

	public float magnetude() {
		return (new Vector(0, 0)).distance(this);
	}

	public float distance(Vector v2) {
		float xdist = (float) (x - v2.x);
		float ydist = (float) (y - v2.y);
		return (float) Math.sqrt( Math.pow(xdist, 2.0) + Math.pow(ydist, 2.0) );
	}

	public Vector subtract(Vector v2) {
		return new Vector(x - v2.x, y - v2.y);
	}

	public Vector add(Vector v2) {
		return new Vector(x + v2.x, y + v2.y);
	}

	public Vector multiply(float multiplier) {
		return new Vector(x * multiplier, y * multiplier);
	}

	public Vector divide(float denominator) {
		return new Vector(x / denominator, y / denominator);
	}

	public Vector divide(Vector denominator) {
		return new Vector( x = denominator.x, y / denominator.y);
	}
}
