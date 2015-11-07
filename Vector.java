
import java.awt.geom.Point2D;

/**
 * A vector represents a point in 2d space it has an x coordinate 
 * and a y coordinate they are public and can be accessed by 
 * Vector.x/Vector.y
 */
class Vector implements java.io.Serializable {
	public float x;
	public float y;
	
	/**
	 * Create a new Vector at (x, y).
	 * @param x The x coordinate of this Vector.
	 * @param y the y coordinate of this Vector.
	 * @return A new Vector.
	 */
	public Vector(float x, float y){
		this.x = x;
		this.y = y;
	}

	/**
	 * Return a String representation of this Vector.
	 * @return A String representation of this Vector.
	 */
	public String toString(){
		return "(" + x + ", " + y + ")";
	}

	/**
	 * Return the magnetude of this Vector. (The distance from the
	 * origin to the point (x, y)).
	 * @return The magnetude of this Vector.
	 */
	public float magnetude() {
		return (new Vector(0, 0)).distance(this);
	}

	/**
	 * Return the distance between this Vector and another.
	 * @param v2 The second Vector.
	 * @return The distance between this Vector and v2.
	 */
	public float distance(Vector v2) {
		float xdist = (float) (x - v2.x);
		float ydist = (float) (y - v2.y);
		return (float) Math.sqrt( Math.pow(xdist, 2.0) + Math.pow(ydist, 2.0) );
	}

	/**
	 * Subtract v2 from this Vector, return as a new Vector.
	 * @param v2 The amount to subtract from this Vector.
	 * @return A new Vector which is the difference between this and v2.
	 */
	public Vector subtract(Vector v2) {
		return new Vector(x - v2.x, y - v2.y);
	}

	/**
	 * Add v2 to this Vector, return as a new Vector.
	 * @param v2 The amount to add to this Vector.
	 * @return A new Vector which is the summation of this and v2.
	 */
	public Vector add(Vector v2) {
		return new Vector(x + v2.x, y + v2.y);
	}

	/**
	 * Multiply this Vector by a constant, return as a new Vector.
	 * @param multiplier The amount this Vector should be multiplied by.
	 * @return A new Vector which is bigger than this by a factor of 
	 * 	multiplier.
	 */
	public Vector multiply(float multiplier) {
		return new Vector(x * multiplier, y * multiplier);
	}

	/**
	 * Divide this Vector by a constant, return as a new Vector.
	 * @param denominator The constant by which we divide this Vector.
	 * @return A new Vector which is smaller than this by a factor of
	 * 	denominator.
	 */
	public Vector divide(float denominator) {
		return new Vector(x / denominator, y / denominator);
	}

	/**
	 * Divide the x and y components of this Vector by those of denominator.
	 * @param denominator The Vector whose components we will use to divide
	 * 	this Vector.
	 * @return A new Vector whose x component is denominator.x times smaller
	 * 	than this.x, and whose y component is denominator.y times smaller
	 * 	than this.y.
	 */
	public Vector divide(Vector denominator) {
		return new Vector( x = denominator.x, y / denominator.y);
	}


	/**
	 * Convert this Vector to a Point2D.Float.
	 * @return A Point2D.Float corresponding to this Vector.
	 */
	public Point2D.Float toPoint2DFloat() {
		return new Point2D.Float(x, y);
	}
}
