
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

import monsters.Monster;
import projectiles.Projectile;
import towers.Tower;

public class GameMap {
	
	int spawnX;
	int spawnY;
	int gWidth; //GRID WIDTH
	int gHeight; //GRID HEIGHT
	boolean[][] pathGrid;
	ArrayList<GameElement> elementList = new ArrayList<GameElement>();
	ArrayList<GameElement> elementBuffer = new ArrayList<GameElement>();
	
	public GameMap(int height, int width, int spawnX, int spawnY){
		gWidth = width;
		gHeight = height;
		this.spawnX = spawnX;
		this.spawnY = spawnY;
		pathGrid = new boolean[gWidth][gHeight];
	}
	
	public GameMap(int height, int width){
		this(height, width, 0, 0);
	}
	
	public void update() {
		//Updates everything (positions)
		for(int i = 0; i < elementList.size(); i++) {
			elementList.get(i).update();
			if(elementList.get(i).getRemove()){
				elementList.remove(i);
			}
			//todo: set each game element's boolean list to the map's
		}
		elementList.addAll(elementBuffer);
		elementBuffer.clear();
	}
	public void draw(Graphics g) {
		for(int i = 0; i < elementList.size(); i++) {
			GameElement temp = elementList.get(i);
			//temp.getImage().rotate((float)Math.toDegrees(temp.getOrientation())); DECOMMENT THIS SECTION ASAP
			temp.draw(g);
		}
	}
	
	public void placeTower(Tower theTower) { //Will snap the tower to the grid and also change the boolean pathfinding array so that that square is blocked
		Tower tempTower = theTower;  //TODO: Convert loc position to grid position
		elementBuffer.add(theTower);
		//pathGrid[(int) tempTower.getLoc().x][(int) tempTower.getLoc().y] = true;  DECOMMENT THIS SECTION ASAP (only used for demos)
	}
	
	public void spawnCreep(Monster creep) {
		elementBuffer.add(creep);
	}
	
	public ArrayList<GameElement> getElements(){
		return elementList;
	}
	
	public void addProjectile(Projectile p){
		elementBuffer.add(p);
	}
}
