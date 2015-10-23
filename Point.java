
/**
 * A 2D Integer point class to be used with AStar.
 * @author Terrence Reilly
 */
class Point implements Comparable {
	public int x;
	public int y;

	/**
	 * Create a new Point at x, y.
	 * @param x The X-Coordinate of this point. (A standardized measure.)
	 * @param y The Y-Coordinate of this point. (A standardized measure.)
	 * @return The new Point.
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Create a new Point from a Vector.
	 * @param v The vector containing the location of this Point.
	 * @return The new Point.
	 */
	public Point(Vector v) {
		this.x = (int) v.x;
		this.y = (int) v.y;
	}

	/**
	 * Compare this Point to another Point.
	 * @param o The other Point.
	 * @return 0 if this Point is the same as o, otherwise -1 or 1.
	 */
	public int compareTo(Object o) {
		Point p = (Point) o;
		if (p.x == this.x)
			return (new Integer(this.y)).compareTo(p.y);
		return (new Integer(this.x)).compareTo(p.x);
	}

	/**
	 * Tell if this Point is at the same coordinate as p.
	 * @param p The other Point.
	 * @return True if this Point is at the same coordinate as p.
	 */
	public boolean equals(Point p) {
		if (p.x == this.x)
			return p.y == this.y;
		return false;
	}

	/**
	 * Return a String representation of this Point of the form (X, y).
	 * @return a String representation of this Point.
	 */
	public String toString() {
		return String.format("(%d, %d)", x, y);
	}

	/**
	 * Return the center point of this Point as a vector.
	 * @param width The width of this Point.
	 * @param height The height of this Point.
	 * @param xoffset The x offset of this Point (how far away from the actual
	 * 	(0,0) of the display the top left corner is.
	 * @param yoffset The y offset of this Point.
	 */
	public Vector centerPoint(int xoffset, int yoffset, int width, int height) {
		return new Vector( ( (float) width) / 2 + xoffset, ((float) height)/2 + yoffset);
	}
}
