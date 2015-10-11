
package monsters;

import java.util.ArrayList;
import java.util.List;

import mechanic.Point;
import mechanic.GameElement;

public class Monster extends GameElement {
	public ArrayList<Point> pathFind(boolean[][] pathgrid, Point loc, Point target) {
		double[][] efficiency = new double[pathgrid.length][pathgrid[0].length];
		ArrayList<Point> queue = new ArrayList<Point>();
		queue.add(loc);
		efficiency[(int) loc.getX()][(int) loc.getY()] = 0;
		
		while (queue.size() > 0) {
			for (int direction = 0; direction < 4; direction++) {
				
			}
		}
		
		if (Point.equals(loc, target)) {
			ArrayList<Point> solution = new ArrayList<Point>();
			solution.add(target);
			return solution;
		}
	}
}
