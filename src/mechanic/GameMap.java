
package mechanic;

import java.util.ArrayList;
import org.newdawn.slick.Graphics;
/*
 * My Ideas:
 * -Each GameElement has a boolean array variable that gets set to the map's boolean array in Update
 * -Each GameElement can access that information and do whatever they want with it
 * -This means that monsters can use it to pathfind
 * -Since this is controlled by the Map, the Map doesn't have to include projectiles into the grid
 * -Whenever a tower is placed, the tower game element's position gets snapped to the grid and the Map's boolean array gets updated, and so will each GameElement's boolean array
 * -This means that their pathfinding gets updated as well
 */

import mechanic.GameElement;
import towers.Tower;

public class GameMap {
	
	int gWidth; //GRID WIDTH
	int gHeight; //GRID HEIGHT
	Boolean[][] pathGrid = new Boolean[gWidth][gHeight];
	ArrayList<GameElement> elementList = new ArrayList<>();
	
	public GameMap(int height, int width){
		gWidth = width;
		gHeight = height;
	}
	
	public void update() {
		//Updates everything (positions)
		for(int i = 0; i < elementList.size(); i++) {
			elementList.get(i).move();
			//todo: set each game element's boolean list to the map's
		}
	}
	public void draw(Graphics g) {
		for(int i = 0; i < elementList.size(); i++) {
			GameElement temp = elementList.get(i);
			g.drawImage(temp.getImage(), (float)temp.getX(), (float)temp.getY()); //Takes the image of each game element and draws them at their loc (point)
		}
	}
	public void placeTower(Tower theTower) { //Will snap the tower to the grid and also change the boolean pathfinding array so that that square is blocked
		Tower tempTower = theTower;  //TODO: Convert loc position to grid position
		//tempTower.loc.x /=
	}
	public void spawnCreep() {
		
	}
}
