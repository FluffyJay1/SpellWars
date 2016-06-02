
package mechanic;

import java.util.ArrayList;

public class Point {
	public double x;
	public double y;
	
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
	 * Prints out the point's state
	 */
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
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
	 * Modify instance variable x
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
	 * Change instance variable x
	 * 
	 * @param x The value to add to x
	 */
	public void addX(double x) {
		this.x += x;
	}
	
	/**
	 * Change instance variable x
	 * 
	 * @param x The value to add to x
	 */
	public void addY(double y) {
		this.y += y;
	}
	
	/**
	 * Adds the values of two points together
	 * 
	 * @param p The point to add
	 */
	public void add(Point p) {
		this.x += p.x;
		this.y += p.y;
	}
	
	/**
	 * Subtracts the values of two points
	 * 
	 * @param p The point to subtract
	 */
	public void subract(Point p) {
		this.x -= p.x;
		this.y -= p.y;
	}
	
	/**
	 * Scales a vector
	 * 
	 * @param s A scalar
	 */
	public void scale(double s) {
		this.x *= s;
		this.y *= s;
	}
	
	/**
	 * Adds two points from a static context
	 * @param p1 A point to add
	 * @param p2 Another point to add
	 * @return The result of the addition
	 */
	public static Point add(Point p1, Point p2) {
		return new Point(p1.x + p2.x, p1.y + p2.y);
	}
	
	/**
	 * Subtracts two points from a static context
	 * @param p1 A point to subtract from
	 * @param p2 A point to subtract with
	 * @return The result of the subtraction
	 */
	public static Point subtract(Point p1, Point p2) {
		return new Point(p1.x - p2.x, p1.y - p2.y);
	}
	
	/**
	 * Multiplies a point by a scalar and returns the result
	 * @param p The point to scale
	 * @param scale The number to multiply by
	 * @return The result of the multiplication
	 */
	public static Point scale(Point p, double scale) {
		return new Point(p.x * scale, p.y * scale);
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
	 * If the vector is 0, then it returns 0
	 * 
	 * Parameters should be instantiated
	 * 
	 * @param vec	The vector
	 * @return		The angle of the vector in radians (if non-zero)
	 */
	public static double getDirectionRad(Point vec) {
		if(!(vec.x == 0 && vec.y == 0)) {
			if (vec.x >= 0) {
				return -Math.atan(vec.y / vec.x);
			}
			return Math.PI - Math.atan(vec.y / vec.x);
		}
		System.out.println("ERROR: A zero vector was passed to a getDirection function!"); //TODO: Remove debug
		return 0;
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
			System.out.println("ERROR: A null Point was passed to the getVector function!"); //TODO: Remove debug
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
			System.out.println("ERROR: A negative distance was passed to a getVector(distance, direction) function!"); //TODO: Remove debug
			//return new Point(); // No traveled vector
		}
		/*
		while (direction < 0) {
			direction += 2 * Math.PI;
		}
		while (direction > 2 * Math.PI) {
			direction -= 2 * Math.PI;
		}
		*/
		/*
		if (direction < Math.PI / 2) {
			return new Point(Math.cos(direction) * distance, -Math.sin(direction) * distance); // Quadrant I
		}
		if (direction < Math.PI) {
			return new Point(-Math.cos(direction) * distance, -Math.sin(direction) * distance); // Quadrant II
		}
		if (direction < Math.PI * 3 / 2) {
			return new Point(-Math.cos(direction) * distance, Math.sin(direction) * distance); // Quadrant III
		}
		*/
		return new Point(Math.cos(direction) * distance, -Math.sin(direction) * distance); // Quadrant IV
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
	
	/**
	 * Find the 4 closest points to given point
	 * 
	 * Parameter must be initialized
	 * All indices are 1 unit away
	 * 
	 * @param p	Given point
	 * @return	An ArrayList<Point> with the 4 points 1 unit away
	 */
	public static ArrayList<Point> proximity4(Point p) {
		ArrayList<Point> adjacents = new ArrayList<Point>();
		adjacents.add(new Point(p.x + 1, p.y));
		adjacents.add(new Point(p.x, p.y + 1));
		adjacents.add(new Point(p.x - 1, p.y));
		adjacents.add(new Point(p.x, p.y - 1));
		return adjacents;
	}
	//variation where the distance of the points are changed
	public static ArrayList<Point> proximity4(Point p, double distance) {
		ArrayList<Point> adjacents = new ArrayList<Point>();
		adjacents.add(new Point(p.x + distance, p.y));
		adjacents.add(new Point(p.x, p.y + distance));
		adjacents.add(new Point(p.x - distance, p.y));
		adjacents.add(new Point(p.x, p.y - distance));
		return adjacents;
	}
	//another variation with height and width
		public static ArrayList<Point> proximity4(Point p, double width, double height) {
			ArrayList<Point> adjacents = new ArrayList<Point>();
			adjacents.add(new Point(p.x + width, p.y));
			adjacents.add(new Point(p.x, p.y + height));
			adjacents.add(new Point(p.x - width, p.y));
			adjacents.add(new Point(p.x, p.y - height));
			return adjacents;
		}
	
	/**
	 * Find the 8 closest points to given point
	 * 
	 * Parameter must be initialized
	 * Even indices are 1 unit away
	 * Odd indices are sqrt2 units away
	 * Adjacent indices are adjacent points (0, and size() - 1 are adjacent)
	 * 
	 * @param p	Given point
	 * @return	An ArrayList<Point> with the 8 points within sqrt2 units away
	 */
	public static ArrayList<Point> proximity8(Point p) {
		ArrayList<Point> adjacents = new ArrayList<Point>();
		adjacents.add(new Point(p.x + 1, p.y));
		adjacents.add(new Point(p.x - 1, p.y));
		adjacents.add(new Point(p.x, p.y + 1));
		adjacents.add(new Point(p.x, p.y - 1));
		adjacents.add(new Point(p.x + 1, p.y + 1));
		adjacents.add(new Point(p.x - 1, p.y + 1));
		adjacents.add(new Point(p.x - 1, p.y - 1));
		adjacents.add(new Point(p.x + 1, p.y - 1));
		return adjacents;
	}
	/**
	 * Normalizes a vector (distance = 1)
	 */
	public void normalize() {
		double tempx = x/getDistance(this, new Point());
		double tempy = y/getDistance(this, new Point());
		x = tempx;
		y = tempy;
	}
	/**
	 * Interpolates between two points, based on a ratio
	 * 
	 * @param p1 The first point
	 * @param p2 The second point
	 * @param ratio The ratio
	 * @return a point that is between the two points
	 */
	public static Point interpolate(Point p1, Point p2, double ratio) {
		double tempx = p1.x + ratio * (p2.x - p1.x);
		double tempy = p1.y + ratio * (p2.y - p1.y);
		return new Point(tempx, tempy);
	}
	/**
	 * Gets an arraylist of points between two points, as if it is rasterizing them
	 * @param point1 The first point
	 * @param point2 The second point
	 * @return The list of points that are between the two points
	 */
	public static ArrayList<Point> getPointsBetween(Point point1, Point point2) {
		ArrayList<Point> points = new ArrayList<Point>();
		Point p1 = Point.roundToNearestInteger(point1);
		Point p2 = Point.roundToNearestInteger(point2);
		if((p2.x-p1.x) != 0 && (p2.y-p1.y)/(p2.x-p1.x) <= 1 && (p2.y-p1.y)/(p2.x-p1.x) >= -1) { //slope is between -1 and 1
			double slope = (p2.y-p1.y)/(p2.x-p1.x);
			int change = 1;
			if(p2.x < p1.x) {
				change = -1;
			}
			for(int i = Point.roundToNearestInteger(p1.x); i != Point.roundToNearestInteger(p2.x); i += change) {
				points.add(Point.roundToNearestInteger(new Point(i, (slope * (i-p1.x) + p1.y))));
			}
			points.add(p2);
			return points;
		} else {
			double slope = (p2.x-p1.x)/(p2.y-p1.y);
			int change = 1;
			if(p2.y < p1.y) {
				change = -1;
			}
			for(int i = Point.roundToNearestInteger(p1.y); i != Point.roundToNearestInteger(p2.y); i += change) {
				points.add(Point.roundToNearestInteger(new Point((slope * (i-p1.y) + p1.x), i)));
			}
			points.add(p2);
			return points;
		}
	}
	public void roundToNearestInteger() {
		this.x = Point.roundToNearestInteger(this.x);
		this.y = Point.roundToNearestInteger(this.y);
	}
	public static int roundToNearestInteger(double d) {
		if(d >= 0) {
			if(d - (int)d >= 0.5) {
				return (int)d + 1;
			} else {
				return (int)d;
			}
		} else {
			if(-d - (int)-d >= 0.5) {
				return (int)d - 1;
			} else {
				return (int)d;
			}
		}
	}
	public static Point roundToNearestInteger(Point p) {
		Point tempP = Point.clone(p);
		tempP.roundToNearestInteger();
		return tempP;
	}
	public static ArrayList<Point> getIntegerPointsInCircle(double radius){
		ArrayList<Point> points = new ArrayList<Point>();
		for(int x = (int) -radius; x <= radius; x++){
			for(int y = (int) -radius; y <= radius; y++) {
				if(getDistance(new Point(), new Point(x,y)) <= radius) {
					points.add(new Point(x,y));
				}
			}
		}
		return points;
	}
}
