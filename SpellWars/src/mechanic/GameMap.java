package mechanic;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import particlesystem.ParticleBase;
import particlesystem.ParticleEmitter;
import particlesystem.Trail;
import projectile.Grenade;
import projectile.Projectile;
import shield.Shield;
import ui.UI;
import unit.Player;
import unit.Unit;

public class GameMap {
	UI ui;
	Point mouseLoc;
	float frametime;
	Point mapTopLeft; //THE TOP LEFT POINT OF THE MAP
	float mWidth; //MAP WIDTH
	float mHeight; //MAP HEIGHT
	int gWidth; //GRID WIDTH
	int gHeight; //GRID HEIGHT
	Panel panelGrid[][];
	public static final char ID_NEUTRAL = 8; //1000
	public static final char ID_LEFT = 4; //0100
	public static final char ID_RIGHT = 0; //0000
	public static final char ID_UP = 2; //0010
	public static final char ID_DOWN = 6; //0110
	public static final char ID_UPLEFT = 3; //0011
	public static final char ID_UPRIGHT = 1;//0001
	public static final char ID_DOWNLEFT = 5;//0101
	public static final char ID_DOWNRIGHT = 7;//0111
	public static final Color SHADOW_COLOR = new Color(120, 120, 120, 120);
	public static Image particle_genericRed;
	public static Image particle_genericYellow;
	public static Image particle_genericBlue;
	public static Image particle_genericWhite;
	public static Image particle_explosion;
	public static Image particle_heal;
	public static Image particle_timedilationgood;
	public static Image particle_timedilationbad;
	static boolean imagesLoaded = false;
	ArrayList<GameElement> elementList = new ArrayList<GameElement>();
	ArrayList<GameElement> elementBuffer = new ArrayList<GameElement>();
	ArrayList<GameElement> elementRemoveBuffer = new ArrayList<GameElement>();
	ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	ArrayList<ParticleBase> particleList = new ArrayList<ParticleBase>();
	ArrayList<ParticleBase> particleBuffer = new ArrayList<ParticleBase>();
	ArrayList<ParticleBase> particleRemoveBuffer = new ArrayList<ParticleBase>();
	boolean isPaused;
	
	public GameMap(int gridWidth, int gridHeight, float mapWidth, float mapHeight, Point mapTopLeft){
		this.gWidth = gridWidth;
		this.gHeight = gridHeight;
		this.mapTopLeft = mapTopLeft;
		this.mouseLoc = new Point();
		this.mWidth = mapWidth; 
		this.mHeight = mapHeight; 
		this.panelGrid = new Panel[gridWidth][gridHeight];
		for(int x = 0; x < gridWidth/2; x++) {
			for(int y = 0; y < gridHeight; y++) {
				this.panelGrid[x][y] = new Panel(x, y, ID_LEFT, PanelState.NORMAL, this); 
				this.panelGrid[gridWidth - x - 1][gridHeight - y - 1] = new Panel(gridWidth - x - 1, gridHeight - y - 1, ID_RIGHT, PanelState.NORMAL, this); 
			}
		}
		if(gridWidth%2 == 1) {
			for(int y = 0; y < gridHeight/2; y++) {
				this.panelGrid[gridWidth/2][y] = new Panel(gridWidth/2, y, ID_LEFT, PanelState.NORMAL, this); 
				this.panelGrid[gridWidth/2][gridHeight - y - 1] = new Panel(gridWidth/2, gridHeight - y - 1, ID_RIGHT, PanelState.NORMAL, this); 
			}
			if(gridHeight%2 == 1) {
				this.panelGrid[gridWidth/2][gridHeight/2] = new Panel(gridWidth/2, gridHeight/2, ID_NEUTRAL, PanelState.NORMAL, this); 
			}
		}
		if(!imagesLoaded) {
			try {
				particle_genericRed = new Image("res/particle_genericRed.png");
				particle_genericYellow = new Image("res/particle_genericYellow.png");
				particle_genericBlue = new Image("res/particle_genericBlue.png");
				particle_genericWhite = new Image("res/particle_genericWhite.png");
				particle_explosion = new Image("res/particle_explosion.png");
				particle_heal = new Image("res/particle_heal.png");
				particle_timedilationgood = new Image("res/particle_timedilationgood.png");
				particle_timedilationbad = new Image("res/particle_timedilationbad.png");
				imagesLoaded = true;
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.isPaused = false;
	}
	
	public void passFrameTime(float frametime) {
		this.frametime = frametime;
	}
	public void update() {
		//Updates everything
		if(!this.isPaused) {
			for(int x = 0; x < this.panelGrid.length; x++) {
				for(int y = 0; y < this.panelGrid[0].length; y++) {
					this.panelGrid[x][y].update(this.frametime);
				}
			}
		}
		for(int i = 0; i < elementList.size(); i++) {
			GameElement element = elementList.get(i);
			element.passFrameTime(frametime);
			if(!element.isPaused) {
				//element.passFrameTime(frametime);
				if(element.getRemove()){
					elementRemoveBuffer.add(element);
					if(element instanceof Unit && ((Unit)element).panelStandingOn != null) {
						((Unit)element).panelStandingOn.unitStandingOnPanel = null;
						((Unit)element).panelStandingOn = null;
					}
					element.setMap(null);
				} else {
					if(element.getHP() <= 0 && (element instanceof Unit || (element instanceof Shield && ((Shield)element).removeOnKill && !((Shield)element).isDead))) { 
						element.onDeath();
						if(element instanceof Shield) {
							((Shield)element).isDead = true;
						}
						element.setRemove(true);
					}
					element.update(); 
					element.updateStatusEffects();
					element.updateRegardsToParent();
				}
			}
			if(element instanceof Unit && ((Unit)element).spellCastIgnorePause) {
				((Unit)element).updateSpellTimers();
			}
		}
		for(int i = 0; i < particleList.size(); i++) {
			ParticleBase particle = particleList.get(i);
			if(!particle.isPaused) {
				particle.passFrameTime(frametime);
				particle.move();
				if(particle.getRemove()){
					particleRemoveBuffer.add(particle);
				}
			}
		}
		this.updateLists();
	}
	public void updateLists() {
		elementList.addAll(elementBuffer);
		elementList.removeAll(elementRemoveBuffer);
		projectiles.removeAll(elementRemoveBuffer);
		elementBuffer.clear();
		elementRemoveBuffer.clear();
		particleList.addAll(particleBuffer);
		particleList.removeAll(particleRemoveBuffer);
		particleBuffer.clear();
		particleRemoveBuffer.clear();
	}
	public void pauseAll() {
		for(GameElement e : this.elementList) {
			e.setPause(true);
		}
		///* Might pause the spell too
		for(GameElement e : this.elementBuffer) {
			e.setPause(true);
		}
		//*/
		for(ParticleBase p : this.particleList) {
			p.setPause(true);
		}
		for(ParticleBase p : this.particleBuffer) {
			p.setPause(true);
		}
		this.isPaused = true;
	}
	public void unpauseAll() {
		for(GameElement e : this.elementList) {
			e.setPause(false);
		}
		for(GameElement e : this.elementBuffer) {
			e.setPause(false);
		}
		for(ParticleBase p : this.particleList) {
			p.setPause(false);
		}
		for(ParticleBase p : this.particleBuffer) {
			p.setPause(false);
		}
		this.isPaused = false;
	}
	public boolean isPaused() {
		return this.isPaused;
	}
	public void passMousePosition(Point point) {
		this.mouseLoc = point;
	}
	public Point getMousePosition() {
		return this.mouseLoc;
	}
	public void draw(Graphics g) {
		/*
		for(int x = 0; x < gWidth; x++){
			for(int y = 0; y < gHeight; y++) {
				g.setColor(Color.blue);
				if(this.panelGrid[x][y].teamID == ID_LEFT) {
					g.setColor(Color.red);
				}
				if(this.panelGrid[x][y].teamID == ID_NEUTRAL) {
					g.setColor(Color.gray);
				}
				float size = 15;
				Rectangle rect = new Rectangle(x*size,y*size,size,size);
				g.fill(rect);
			}
		}
		*/
		for(int i = 0; i < elementList.size(); i++) { //resets hasbeendrawn
			GameElement temp = elementList.get(i);
			temp.hasBeenDrawn = false;
		}
		for(int x = 0; x < this.panelGrid.length; x++) { //draws panels
			for(int y = 0; y < this.panelGrid[0].length; y++) {
				this.panelGrid[x][y].draw(g);
			}
		}
		for(int i = 0; i < elementList.size(); i++) { //draw elements shadows
			GameElement temp = elementList.get(i);
			temp.drawShadow(g);
			g.resetTransform();
		}
		for(int x = 0; x < this.panelGrid.length; x++) { //draws units on panels + their shields, and projectiles on those panels
			for(int y = 0; y < this.panelGrid[0].length; y++) {
				GameElement temp = this.panelGrid[x][y].unitStandingOnPanel;
				if(temp != null) {
					temp.draw(g);
					temp.hasBeenDrawn = true;
					for(Shield s : ((Unit) temp).getShields()) {
						s.draw(g);
						s.hasBeenDrawn = true;
					}
				}
				for(Projectile p : this.projectiles) {
					if(Point.equals(p.getGridLoc(), new Point(x,y))) {
						p.draw(g);
						p.hasBeenDrawn = true;
					}
				}
			}
		}
		if(this.isPaused) {
			Rectangle rect = new Rectangle(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
			g.setColor(new Color(0, 0, 0, 100));
			g.fill(rect);
		}
		for(int i = 0; i < elementList.size(); i++) { //draw elements that haven't been drawn before, excluding projectiles (since they should've been drawn before)
			GameElement temp = elementList.get(i);
			if(!temp.hasBeenDrawn && !(temp instanceof Projectile)) {
				temp.draw(g);
				temp.hasBeenDrawn = true;
				g.resetTransform();
			}
		}
		for(int i = 0; i < particleList.size(); i++) //draw particles
		{
			particleList.get(i).draw(g);
		}
		for(int i = 0; i < elementList.size(); i++) { //draw the elements' status icons
			elementList.get(i).drawStatusIcons(g);
		}
		
	}
	public UI getUI() {
		return this.ui;
	}
	public void setUI(UI ui) {
		this.ui = ui;
	}
	public void addGameElement(GameElement e) {
		if(!this.elementList.contains(e) && !this.elementBuffer.contains(e)) {
			e.setMap(this);
			e.passFrameTime(this.frametime);
			elementBuffer.add(e);
			if(e instanceof Projectile) {
				this.projectiles.add((Projectile) e);
			}
		}
	}
	public void addUnit(Unit u, boolean respectPanelTeam) {
		if(this.pointIsInGrid(u.gridLoc) && this.getPanelAt(u.gridLoc).unitStandingOnPanel == null && !(!u.ignoreHoles && this.getPanelAt(u.gridLoc).getPanelState() == PanelState.HOLE)
				&& (u.canMoveToLoc(u.gridLoc, this) || !respectPanelTeam)) {
			this.addGameElement(u);
			this.getPanelAt(u.gridLoc).unitStandingOnPanel = u;
			u.panelStandingOn = this.getPanelAt(u.gridLoc);
			u.changeLoc(this.gridToPosition(u.gridLoc)); 
			
		} else {
			for(Point p : Point.proximity8(u.gridLoc)) {
				if(this.pointIsInGrid(p) && this.getPanelAt(p).unitStandingOnPanel == null && !(!u.ignoreHoles && this.getPanelAt(p).getPanelState() == PanelState.HOLE)
						&& (u.canMoveToLoc(p, this) || !respectPanelTeam)) {
					this.addGameElement(u);
					u.gridLoc = p;
					this.getPanelAt(p).unitStandingOnPanel = u;
					u.panelStandingOn = this.getPanelAt(p);
					u.changeLoc(this.gridToPosition(p));
					break;
				}
			}
		}
	}
	public void addUnit(Unit u) {
		this.addUnit(u, true);
	}
	public void addParticle(ParticleBase p){
		particleBuffer.add(p);
	}
	public void addParticleEmitter(ParticleEmitter e) {
		if(!this.elementList.contains(e) && !elementBuffer.contains(e)) {
			e.setMap(this);
			e.passFrameTime(this.frametime);
			elementBuffer.add(e);
		}
	}
	public Point getGridDimensions() {
		return new Point(this.gWidth, gHeight);
	}
	public Panel getPanelAt(Point loc){
		Point roundedLoc = Point.roundToNearestInteger(loc);
		if(this.pointIsInGrid(roundedLoc)) {
			return this.panelGrid[(int)roundedLoc.x][(int)roundedLoc.y];
		} else {
			return null;
		}
	}
	public ArrayList<Panel> getPanels() {
		ArrayList<Panel> panels = new ArrayList<Panel>();
		for(int x = 0; x < this.panelGrid.length; x++) {
			for(int y = 0; y < this.panelGrid[0].length; y++) {
				panels.add(this.panelGrid[x][y]);
			}
		}
		return panels;
	}
	public ArrayList<Projectile> getProjectiles() {
		return this.projectiles;
	}
	public ArrayList<Panel> getPanelsOfTeam(int teamID){
		ArrayList<Panel> panels = new ArrayList<Panel>();
		for(int x = 0; x < this.panelGrid.length; x++) {
			for(int y = 0; y < this.panelGrid[0].length; y++) {
				if(this.panelGrid[x][y].teamID == teamID) {
					panels.add(this.panelGrid[x][y]);
				}
			}
		}
		return panels;
	}
	public ArrayList<GameElement> findElementsInArea(Point loc, float radius) {
		ArrayList<GameElement> elements = new ArrayList<GameElement>();
		for (GameElement e : this.elementList) {
			if(Point.getDistance(e.getLoc(), loc) <= radius) {
				elements.add(e);
			}
		}
		return elements;
	}
	public ArrayList<GameElement> getElementsAtPanel(Panel panel) {
		ArrayList<GameElement> elements = new ArrayList<GameElement>();
		for (GameElement e : this.elementList) {
			if(Point.equals(this.positionToGrid(e.getLoc()), panel.loc)) {
				elements.add(e);
			}
		}
		return elements;
	}
	public boolean pointIsInGrid(Point loc) {
		if(loc.x < this.gWidth && loc.y < this.gHeight && loc.x >= 0 && loc.y >= 0) {
			return true;
		}
		return false;
	}
	public void addPointIfInGrid(ArrayList<Point> list, Point loc) {
		if(this.pointIsInGrid(loc)) {
			list.add(loc);
		}
	}
	public ArrayList<Point> getValidProximityPoints4(Point loc) {
		ArrayList<Point> points = new ArrayList<Point>();
		for(Point p : Point.proximity4(loc)) {
			this.addPointIfInGrid(points, p);
		}
		return points;
	}
	public ArrayList<Panel> getValidProximityPanels4(Point loc) {
		ArrayList<Panel> panels = new ArrayList<Panel>();
		for(Point p : this.getValidProximityPoints4(loc)) {
			panels.add(this.getPanelAt(p));
		}
		return panels;
	}
	public ArrayList<Point> getValidProximityPoints8(Point loc) {
		ArrayList<Point> points = new ArrayList<Point>();
		for(Point p : Point.proximity8(loc)) {
			this.addPointIfInGrid(points, p);
		}
		return points;
	}
	public ArrayList<Panel> getValidProximityPanels8(Point loc) {
		ArrayList<Panel> panels = new ArrayList<Panel>();
		for(Point p : this.getValidProximityPoints8(loc)) {
			panels.add(this.getPanelAt(p));
		}
		return panels;
	}
	//Takes a position on the map and converts it into grid coordinates
	public Point positionToGrid(Point loc) {
		//Squishes it down and floors the value
		int gridposX = (int)(this.gWidth * (loc.getX() - this.mapTopLeft.getX()) / this.mWidth);
		int gridposY = (int)(this.gHeight * (loc.getY() - this.mapTopLeft.getY()) / this.mHeight);
		//If for whatever reason the position is out of bounds, it will set it to the closest one
		if(gridposX > this.gWidth - 1)
			gridposX = this.gWidth - 1;
		if(gridposX < 0)
			gridposX = 0;
		if(gridposY > this.gHeight - 1)
			gridposY = this.gHeight - 1;
		if(gridposY < 0)
			gridposY = 0;
		//Return the grid position as a point
		return new Point(gridposX, gridposY);
	}
	
	//Takes a position on the grid and converts it into map coordinates
	public Point gridToPosition(Point loc) {
		float x = (float) (loc.getX() + 0.5) * this.mWidth / this.gWidth;
		float y = (float) (loc.getY() + 0.5) * this.mHeight / this.gHeight;
		x += this.mapTopLeft.getX();
		y += this.mapTopLeft.getY();
		return new Point(x, y);
	}
	public Point getSizeOfPanel() {
		return new Point(this.mWidth/this.gWidth, this.mHeight/this.gHeight);
	}
	public Point[] getCornerPositionsOfGridPoints(Point p1, Point p2) {
		double leftx, rightx;
		double topy, bottomy;
		Point p1p = this.gridToPosition(p1);
		Point p2p = this.gridToPosition(p2);
		if(p1p.x < p2p.x) {
			leftx = p1p.x;
			rightx = p2p.x;
		} else {
			leftx = p2p.x;
			rightx = p1p.x;
		}
		leftx -= this.getSizeOfPanel().x/2;
		rightx += this.getSizeOfPanel().x/2;
		if(p1p.y < p2p.y) {
			topy = p1p.y;
			bottomy = p2p.y;
		} else {
			topy = p2p.y;
			bottomy = p1p.y;
		}
		topy -= this.getSizeOfPanel().y/2;
		bottomy += this.getSizeOfPanel().y/2;
		Point[] points = {new Point(leftx, topy), new Point(rightx, bottomy)};
		return points;
	}
	public static char getOppositeDirection(char dir) {
		char tempDir = (char) (dir + 4);
		if(tempDir >= 8){
			tempDir -= 8;
		}
		return tempDir;
	}
	public static char clampDirection(char dir) {
		char tempDir = dir;
		while(tempDir >= 8) {
			tempDir -= 8;
		}
		while(tempDir < 0) {
			tempDir += 8;
		}
		return tempDir;
	}
	public static char directionBetweenTwoPoints(Point p1, Point p2) {
		return GameMap.clampDirection((char) (Point.roundToNearestInteger(Point.getDirectionDeg(p1, p2) / 45) - 1));
	}
	public static Point getFuturePoint(Point p, char dir) {
		Point moveVec = new Point();
		switch(dir) {
		case GameMap.ID_UP:
			moveVec.y = -1;
			break;
		case GameMap.ID_DOWN:
			moveVec.y = 1;
			break;
		case GameMap.ID_LEFT:
			moveVec.x = -1;
			break;
		case GameMap.ID_RIGHT:
			moveVec.x = 1;
			break;
		case GameMap.ID_UPLEFT:
			moveVec.x = -1;
			moveVec.y = -1;
			break;
		case GameMap.ID_UPRIGHT:
			moveVec.x = 1;
			moveVec.y = -1;
			break;
		case GameMap.ID_DOWNLEFT:
			moveVec.x = -1;
			moveVec.y = 1;
			break;
		case GameMap.ID_DOWNRIGHT:
			moveVec.x = 1;
			moveVec.y = 1;
			break;
		default:
			break;
		}
		return Point.add(p, moveVec);
	}
	public void printPanels() {
		for(int row = 0; row < this.getGridDimensions().y; row++) {
			for(int column = 0; column < this.getGridDimensions().x; column++) {
				System.out.print(new Point(column, row).toString() + " hasUnit " + this.panelGrid[column][row].unitStandingOnPanel != null + " ");
			}
			System.out.print("\n");
		}
	}
}
