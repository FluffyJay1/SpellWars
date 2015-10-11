
package monsters;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Queue;

import mechanic.GameElement;
import mechanic.Point;

public class Monster extends GameElement {
	public Monster() {
		super();
	}
	
	/**
	 * A pathfinding algorithm to find a traversable path from a starting point to a target point
	 * 
	 * @param pathgrid	A grid of occupied (inaccessible) grid squares
	 * @param loc		The starting point
	 * @param target	The target point
	 * @return			An ArrayList<Point> with the first sub-target at the end of list, .get(0) returns the target
	 */
	public ArrayList<Point> pathFind(boolean[][] pathgrid, Point loc, Point target) {
		double[][] efficiency = new double[pathgrid.length][pathgrid[0].length];
		ArrayList<Point> queue = new ArrayList<Point>();
		queue.add(loc);
		efficiency[(int) loc.getX()][(int) loc.getY()] = 0;
		
		while (queue.size() > 0) {
			// Sort queue
			queue.sort((Point p1, Point p2) -> {
				return (int) sortByEfficiency(efficiency, p1, p2);
			});
			
			Point base = queue.get(0);
			double baseEffi = efficiency[(int) base.getX()][(int) base.getY()];
			ArrayList<Point> adjacent = Point.proximity8(base);
			for (int index = adjacent.size(); index > 0; index--) {
				Point test = adjacent.get(index);
				if (test.getX() < 0 || test.getY() < 0) {
					adjacent.set(index, null);
				}
			}
			
			// Find efficiency of closer points and decide if add to queue
			for (int index = 0; index < adjacent.size(); index += 2) {
				Point findEffi = adjacent.get(index);
				if (findEffi != null) {
					double control = efficiency[(int) findEffi.getX()][(int) findEffi.getY()];
					double trial = baseEffi + 1; // Straight path
					if (trial < control) {
						// Find if point is occupied
						if (!pathgrid[(int) findEffi.getX()][(int) findEffi.getY()]) { // Assume true in pathgrid means occupied, otherwise, remove negation operator
							queue.add(findEffi);
							efficiency[(int) findEffi.getX()][(int) findEffi.getY()] = trial;
						}
					}
				}
			}
			for (int index = 1; index < adjacent.size(); index += 2) {
				Point findEffi = adjacent.get(index);
				Point adj1 = adjacent.get(index - 1);
				Point adj2;
				if (index + 1 < adjacent.size()) {
					adj2 = adjacent.get(index + 1);
				} else {
					adj2 = adjacent.get(0);
				}
				if (findEffi != null) {
					double control = efficiency[(int) findEffi.getX()][(int) findEffi.getY()];
					double trial = baseEffi + Math.sqrt(2); // Diagonal path
					if (trial < control) {
						// Assume true in pathgrid means occupied, otherwise, remove negation operator
						if (!pathgrid[(int) findEffi.getX()][(int) findEffi.getY()] && !pathgrid[(int) adj1.getX()][(int) adj1.getY()] 
								&& !pathgrid[(int) adj2.getX()][(int) adj2.getY()]) {
							queue.add(findEffi);
							efficiency[(int) findEffi.getX()][(int) findEffi.getY()] = trial;
						}
					}
				}
			}
			
			if (Point.equals(base, target)) { // Test if target has been found
				queue.clear(); // While loop will not execute again
			}
		}
		
		double iterateEfficiency = efficiency[(int) target.getX()][(int) target.getY()];
		ArrayList<Point> foundPath = new ArrayList<Point>();
		foundPath.add(target);
		Point current = target;
		while (iterateEfficiency > 0) {
			ArrayList<Point> adjacent = Point.proximity8(current);
			for (int index = adjacent.size(); index > 0; index--) {
				Point test = adjacent.get(index);
				if (test.getX() < 0 || test.getY() < 0) {
					adjacent.set(index, null);
				}
			}
			iterateEfficiency = efficiency[(int) current.getX()][(int) current.getY()];
			double closestEfficiency = Integer.MIN_VALUE;
			int closestIndex = 0;
			for (int index = adjacent.size(); index > 0; index--) {
				if (adjacent.get(index) != null) {
					double localEffi = efficiency[(int) adjacent.get(index).getX()][(int) adjacent.get(index).getY()];
					if (localEffi < iterateEfficiency && localEffi > closestEfficiency) {
						closestEfficiency = localEffi;
						closestIndex = index;
					}
				}
			}
			iterateEfficiency = closestEfficiency;
			current = adjacent.get(closestIndex);
			foundPath.add(current);
		}
		return foundPath;
	}
	
	/**
	 * Determine which of 2 points has better efficiency (lower is better)
	 * 
	 * Parameters must be instantiated
	 * Returns negative if p1 has better efficiency than p2
	 * Returns positive if p1 has worse efficiency than p2
	 * Returns 0 if they have same efficiency (rare case due to use of doubles)
	 * 
	 * @param efficiency	A 2d array of all efficiency scores
	 * @param p1			First point to compare
	 * @param p2			Second point to compare
	 * @return				A value that represents which point has better efficiency
	 */
	private double sortByEfficiency(double[][] efficiency, Point p1, Point p2) {
		double ef1 = efficiency[(int) p1.getX()][(int) p1.getY()];
		double ef2 = efficiency[(int) p1.getX()][(int) p1.getY()];
		return ef1 - ef2;
	}
}
