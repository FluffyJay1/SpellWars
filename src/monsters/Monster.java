
package monsters;

import java.util.ArrayList;
import java.util.List;

import mechanic.Point;
import mechanic.GameElement;

public class Monster extends GameElement {
	public ArrayList<Point> pathFind(boolean[][] pathgrid, Point loc, Point target) {
		return pathFindRec(pathgrid, loc, target, new int[pathgrid.length][pathgrid[0].length]);
	}
	
	private ArrayList<Point> pathFindRec(boolean[][] pathgrid, Point loc, Point target, int[][] grid){
		if (Point.equals(loc, target)) {
			ArrayList<Point> solution = new ArrayList<Point>();
			solution.add(target);
			return solution;
		}
	}
}
