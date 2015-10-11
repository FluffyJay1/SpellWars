
package monsters;

import java.util.ArrayList;
import java.util.List;

import mechanic.Point;
import mechanic.GameElement;

public class Monster extends GameElement {
	public Monster(){
		super();
	}
	public ArrayList<Point> pathFind(boolean[][] pathgrid, Point loc, Point target) {
		double[][] efficiency = new double[pathgrid.length][pathgrid[0].length];
		
		if (Point.equals(loc, target)) {
			ArrayList<Point> solution = new ArrayList<Point>();
			solution.add(target);
			return solution;
		}
		return null;
	}
}
