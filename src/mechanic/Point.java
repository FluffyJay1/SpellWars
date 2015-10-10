
package mechanic;

public class Point {
	double x;
	double y;
	
	/**
	 * Constructs a Point object at the origin
	 * 
	 */
	public Point() {
		this(0, 0);
	}
	
	/**
	 * Constructs a Point object with given coordinates
	 * 
	 * @param x	The given x value
	 * @param y	The given y value
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Access instance variable x
	 * 
	 * @return	The value of instance variable x
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * Access instance variable y
	 * 
	 * @return	The value of instance variable y
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Modify instance variable y
	 * 
	 * @param x	The new x value
	 */
	public void changeX(double x) {
		this.x = x;
	}
	
	/**
	 * Modify instance variable y
	 * 
	 * @param y	The new y value
	 */
	public void changeY(double y) {
		this.y = y;
	}
	
	/**
	 * Find the magnitude of the vector between two points
	 * 
	 * Parameters should be instantiated
	 * 
	 * @param p1	A point
	 * @param p2	A point
	 * @return		The the magnitude of the vector from the original point to the destination point
	 */
	public static double getDistance(Point p1, Point p2) { // Returns the magnitude, always non-negative
		Point vec = getVector(p1, p2);
		return Math.sqrt(Math.pow(vec.x, 2) + Math.pow(vec.y, 2)); // Use distance formula
	}
	
	/**
	 * Find angle in degrees of the vector between two points
	 * 
	 * Parameters should be instantiated
	 * 
	 * @param start	The original point
	 * @param end	The destination point
	 * @return		The angle of the vector in degrees from the original point to the destination point
	 */
	public static double getDirectionDeg(Point start, Point end) {
		return Math.toDegrees(getDirectionRad(start, end));
	}
	
	/**
	 * Find angle in degrees of a vector
	 * 
	 * Parameters should be instantiated
	 * 
	 * @param vec	The vector
	 * @return		The angle of the vector in degrees
	 */
	public static double getDirectionDeg(Point vec) {
		return Math.toDegrees(getDirectionRad(vec));
	}
	
	/**
	 * Find angle in radians of the vector between two points
	 * 
	 * Parameters should be instantiated
	 * 
	 * @param start	The original point
	 * @param end	The destination point
	 * @return		The angle of the vector in radians from the original point to the destination point
	 */
	public static double getDirectionRad(Point start, Point end) {
		return getDirectionRad(getVector(start, end));
	}
	
	/**
	 * Find angle in radians of a vector
	 * 
	 * Parameters should be instantiated
	 * 
	 * @param vec	The vector
	 * @return		The angle of the vector in radians
	 */
	public static double getDirectionRad(Point vec) {
		if (vec.y >= 0) {
			return Math.acos(vec.y / vec.x);
		}
		return 2 * Math.PI - Math.acos(vec.y / vec.x);
	}
	
	/**
	 * Find the vector between two points
	 * 
	 * Parameters should be instantiated
	 * 
	 * @param start	The original point
	 * @param end	The destination point
	 * @return		The vector from the original point to the destination point
	 */
	public static Point getVector(Point start, Point end) {
		double dx;
		double dy;
		
		if (start == null || end == null) { // Point does not exist!
			return new Point(0, 0); // If a point does not exist, there is no vector.
		}
		
		// Find direct vector
		dx = end.x - start.x;
		dy = end.y - start.y;
		
		return new Point(dx, dy);
	}
	
	/**
	 * Find the short distance vector that puts your closest to target
	 * 
	 * @param loc				The current location
	 * @param target			The target location
	 * @param possibleFactors	The number of angles in 2pi that we examine
	 * @return					The short distance vector that brings you closest to target
	 */
	public static Point getClosestAngleVector(Point loc, Point target, int possibleFactors) {
		double interval = 2 * Math.PI / possibleFactors;
		double exact = getDirectionRad(loc, target);
		double closestFactor = 0;
		
		for (double angleFactor = 1; angleFactor < possibleFactors; angleFactor++) {
			if (Math.abs(angleFactor * interval - exact) < Math.abs(closestFactor * interval - exact)) {
				closestFactor = angleFactor;
			}
		}
		if (closestFactor % 2 == 0) {
			return makeStateInteger(getVector(1, closestFactor * interval));
		}
		return makeStateInteger(getVector(1, closestFactor * interval));
	}
	
	/**
	 * Round a point's state to the nearest integer values
	 * 
	 * Parameters must be instantiated
	 * 
	 * @param p	The point with the given state
	 * @return	A different point with integer state (stored as double)
	 */
	public static Point makeStateInteger(Point p) {
		Point perfect = new Point();
		perfect.x = (int) (p.x += 0.5);
		perfect.y = (int) (p.y += 0.5);
		return perfect;
	}
	
	/**
	 * Find the vector given a distance and a direction
	 * 
	 * Parameters should be instantiated
	 * 
	 * @param distance	The distance to travel
	 * @param direction	The direction to travel (in radians)
	 * @return			The vector you will travel
	 */
	public static Point getVector(double distance, double direction) {
		if (distance < 0) { // You cannot travel negative distance!
			return new Point(); // No traveled vector
		}
		
		while (direction < 0) {
			direction += 2 * Math.PI;
		}
		while (direction > 2 * Math.PI) {
			direction -= 2 * Math.PI;
		}
		
		if (direction < Math.PI / 2) {
			return new Point(Math.cos(direction) * distance, Math.sin(direction) * distance); // Quadrant I
		}
		if (direction < Math.PI) {
			return new Point(-Math.cos(direction) * distance, Math.sin(direction) * distance); // Quadrant II
		}
		if (direction < Math.PI * 3 / 2) {
			return new Point(Math.cos(direction) * distance, -Math.sin(direction) * distance); // Quadrant III
		}
		return new Point(-Math.cos(direction) * distance, -Math.sin(direction) * distance); // Quadrant IV
	}
	
	/**
	 * Find the point you would arrive at given a starting point, a distance, and a direction
	 * 
	 * Parameters should be instantiated
	 * 
	 * @param start		The starting point
	 * @param distance	The distance to travel
	 * @param direction	The direction to travel (in radians)
	 * @return			The point you will end up at
	 */
	public static Point getFuturePoint(Point start, double distance, double direction) {
		Point vec = getVector(distance, direction);
		return getFuturePoint(start, vec);
	}
	
	/**
	 * Find the point you would arrive at given a starting point, and a traveling vector
	 * 
	 * Parameters should be instantiated
	 * 
	 * @param start		The starting point
	 * @param vec		The vector to travel
	 * @return			The point you will end up at
	 */
	public static Point getFuturePoint(Point start, Point vec) {
		return new Point(start.x + vec.x, start.y + vec.y);
	}
	
	/**
	 * Initializes and instantiates a different Point object with the same state
	 * 
	 * @param p	The point to clone
	 */
	public static Point clone(Point p) {
		return new Point(p.x, p.y);
	}
	
	/**
	 * Figure out if 2 points have the same state
	 * 
	 * Parameters must be initialized
	 * 
	 * @param p1	The first point
	 * @param p2	The second point
	 * @return		Whether or not they have the same state
	 */
	public static boolean equals(Point p1, Point p2) {
		return p1.x == p2.x && p1.y == p2.y;
	}
}
