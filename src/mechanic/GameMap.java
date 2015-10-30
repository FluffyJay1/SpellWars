
package mechanic;

import java.util.ArrayList;

import monsters.Monster;

import org.newdawn.slick.Color;
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



import particlesystem.ParticleBase;
import particlesystem.ParticleEmitter;
import projectiles.Projectile;
import towers.Tower;

public class GameMap {
	
	int spawnX;
	int spawnY;
	int windowWidth; //WINDOW WIDTH
	int windowHeight; //WINOW HEIGHT
	int gWidth; //GRID WIDTH
	int gHeight; //GRID HEIGHT
	Point mouseLoc;
	float frametime;
	boolean[][] pathGrid;
	ArrayList<GameElement> elementList = new ArrayList<GameElement>();
	ArrayList<GameElement> elementBuffer = new ArrayList<GameElement>();
	ArrayList<ParticleBase> particleList = new ArrayList<ParticleBase>();
	ArrayList<ParticleBase> particleBuffer = new ArrayList<ParticleBase>();
	
	public GameMap(int height, int width, int spawnX, int spawnY){
		gWidth = width;
		gHeight = height;
		this.spawnX = spawnX;
		this.spawnY = spawnY;
		pathGrid = new boolean[gWidth][gHeight];
		this.mouseLoc = new Point();
		this.windowWidth = 800; //MAGIC NUMBERSSSSS
		this.windowHeight = 800; 
	}
	
	public GameMap(int height, int width){
		this(height, width, 0, 0);
	}
	
	public void update() {
		//Updates everything (positions)
		for(int i = 0; i < elementList.size(); i++) {
			elementList.get(i).update(); 
			elementList.get(i).passFrameTime(frametime);
			if(elementList.get(i).getRemove()){
				elementList.remove(i);
			}
			//todo: set each game element's boolean list to the map's
		}
		for(int i = 0; i < particleList.size(); i++) {
			particleList.get(i).move();
			particleList.get(i).passFrameTime(frametime);
			if(particleList.get(i).getRemove()){
				particleList.remove(i);
			}
		}
		elementList.addAll(elementBuffer);
		elementBuffer.clear();
		particleList.addAll(particleBuffer);
		particleBuffer.clear();
	}
	public void draw(Graphics g) {
		for(int i = 0; i < elementList.size(); i++) {
			GameElement temp = elementList.get(i);
			//temp.getImage().rotate((float)Math.toDegrees(temp.getOrientation())); DECOMMENT THIS SECTION ASAP
			temp.draw(g);
			//BELOW IS FOR DEBUGGING PURPOSES
			if(temp instanceof Tower) {
				if(((Tower) temp).targetIsTargetable()) {
					g.setColor(Color.red);
					g.drawOval((float)((Tower) temp).getTarget().getLoc().getX() - 50, (float)((Tower) temp).getTarget().getLoc().getY() - 50, 100, 100);
				} else {
					g.setColor(Color.white);
				}
				g.drawOval((float)temp.getLoc().getX() - ((Tower) temp).getAttackRange(), (float)temp.getLoc().getY() - ((Tower) temp).getAttackRange(), ((Tower) temp).getAttackRange() * 2, ((Tower) temp).getAttackRange() * 2);
			//ABOVE IS FOR DEBUGGING PURPOSES
			}
			g.setColor(Color.red);
			g.drawRect((float)temp.getLoc().getX() - 50, (float)temp.getLoc().getY() - 50, (float)temp.getHP() / 5, 10);
		}
		for(int i = 0; i < particleList.size(); i++)
		{
			particleList.get(i).draw(g);
		}
		this.drawGridHighlight(g);
	}
	//draws highlights on the map about which grid box the mouse is in
	public void drawGridHighlight(Graphics g) {
		Point loc = gridToPosition(positionToGrid(this.mouseLoc));
		float boxWidth = this.windowWidth / this.gWidth;
		float boxHeight = this.windowHeight / this.gHeight;
		g.setColor(Color.cyan);
		g.drawRect((float)loc.getX() - boxWidth / 2, (float)loc.getY() - boxHeight / 2, boxWidth, boxHeight);
	}
	//Takes a position on the map and converts it into grid coordinates
	public Point positionToGrid(Point loc) {
		int gridposX = (int)((this.gWidth * (loc.getX() / this.windowWidth)) + 0.5);
		int gridposY = (int)((this.gHeight * (loc.getY() / this.windowHeight)) + 0.5);
		if(gridposX > this.gWidth - 1)
			gridposX = this.gWidth - 1;
		if(gridposX < 0)
			gridposX = 0;
		if(gridposY > this.gHeight - 1)
			gridposY = this.gHeight - 1;
		if(gridposY < 0)
			gridposY = 0;
		return new Point(gridposX, gridposY);
	}
	//Takes a position on the grid and converts it into map coordinates
	public Point gridToPosition(Point loc) {
		float x = (float) loc.getX() * this.windowWidth / this.gWidth;
		float y = (float) loc.getY() * this.windowHeight / this.gHeight;
		return new Point(x, y);
	}
	
	public void placeTower(Tower theTower) { //Will snap the tower to the grid and also change the boolean pathfinding array so that that square is blocked
		Tower tempTower = theTower;  //TODO: Convert loc position to grid position
		Point gridPos = positionToGrid(theTower.getLoc());
		Point loc = gridToPosition(gridPos);
		tempTower.getLoc().changeX(loc.getX());
		tempTower.getLoc().changeY(loc.getY());
		elementBuffer.add(theTower);
		pathGrid[(int) gridPos.getX()][(int) gridPos.getY()] = true;
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
	public void addParticle(ParticleBase p){
		particleBuffer.add(p);
	}
	public void addParticleEmitter(ParticleEmitter e) {
		elementBuffer.add(e);
	}
	public void passFrameTime(float d){
		this.frametime = d;
	}
	public void passMousePosition(int x, int y) {
		this.mouseLoc.changeX(x);
		this.mouseLoc.changeY(y);
	}
}
