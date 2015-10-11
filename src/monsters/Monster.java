
package monsters;

import java.util.ArrayList;
import java.util.List;

import mechanic.Point;
import mechanic.GameElement;

public class Monster extends GameElement {
	public ArrayList<Point> pathFind(boolean[][] pathgrid, Point loc, Point target) {
		if (Point.equals(loc, target)) {
			ArrayList<Point> solution = new ArrayList<Point>();
			solution.add(target);
			return solution;
		}
		
		double[][] efficiency = new double[pathgrid.length][pathgrid[0].length];
		ArrayList<Point> queue = new ArrayList<Point>();
		queue.add(loc);
		efficiency[(int) loc.getX()][(int) loc.getY()] = 0;
		
		while (queue.size() > 0) {
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
						efficiency[(int) findEffi.getX()][(int) findEffi.getY()] = trial;
						
						// Find if point is occupied
						if (!pathgrid[(int) findEffi.getX()][(int) findEffi.getY()]) { // Assume true in pathgrid means occupied, otherwise, remove negation operator
							queue.add(findEffi);
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
						efficiency[(int) findEffi.getX()][(int) findEffi.getY()] = trial;
						
						// Assume true in pathgrid means occupied, otherwise, remove negation operator
						if (!pathgrid[(int) findEffi.getX()][(int) findEffi.getY()] && !pathgrid[(int) adj1.getX()][(int) adj1.getY()] 
								&& !pathgrid[(int) adj2.getX()][(int) adj2.getY()]) {
							queue.add(findEffi);
						}
					}
				}
			}
		}
		
		
	}
}
