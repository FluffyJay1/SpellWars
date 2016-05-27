package mechanic;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import unit.Unit;

public class Panel {
	Point loc;
	//INCLUDE A BUNCH OF IMAGES FOR DIFFERENT PANEL STATES
	public int teamID; //0 is neutral, 1 is left, 2 is right
	PanelState state;
	GameMap map;
	public Unit unitStandingOnPanel;
	public static final float HOLE_RESET_TIME = 15;
	float holeResetTimer;
	boolean willBecomeHole; //used when the player stands on the cracked panel, to figure out when to make it a hole
	public static Image normalRed;
	public static Image normalBlue;
	public static Image crackedRed;
	public static Image crackedBlue;
	public static Image holeRed;
	public static Image holeBlue;
	public static boolean imagesLoaded = false;
	boolean drawProjectileFlash;
	boolean drawImportantFlash;
	public Panel(int x, int y, int teamID, PanelState state, GameMap map) {
		this.loc = new Point(x,y);
		this.teamID = teamID;
		this.state = state;
		this.map = map;
		this.holeResetTimer = 0;
		this.willBecomeHole = false;
		if(!imagesLoaded) {
			try {
				normalRed = new Image("res/panel/normal_red.png", false, Image.FILTER_NEAREST);
				normalBlue = new Image("res/panel/normal_blue.png", false, Image.FILTER_NEAREST);
				crackedRed = new Image("res/panel/cracked_red.png", false, Image.FILTER_NEAREST);
				crackedBlue = new Image("res/panel/cracked_blue.png", false, Image.FILTER_NEAREST);
				holeRed = new Image("res/panel/hole_red.png", false, Image.FILTER_NEAREST);
				holeBlue = new Image("res/panel/hole_blue.png", false, Image.FILTER_NEAREST);
				imagesLoaded = true;
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void setTeamID(int id) {
		this.teamID = id;
	}
	public PanelState getPanelState() {
		return this.state;
	}
	public void setPanelState(PanelState state) {
		this.state = state;
		if(state == PanelState.HOLE) {
			this.holeResetTimer = HOLE_RESET_TIME;
			this.willBecomeHole = false;
		}
	}
	public void update(float frametime) {
		if(this.state == PanelState.HOLE) {
			if(holeResetTimer <= 0) {
				this.setPanelState(PanelState.NORMAL);
			} else {
				this.holeResetTimer -= frametime;
			}
		} else if(this.state == PanelState.CRACKED) {
			if(this.unitStandingOnPanel != null) {
				this.willBecomeHole = true;
			} else if(this.willBecomeHole) {
				this.setPanelState(PanelState.HOLE);
			}
		}
	}
	public Point getLoc() {
		return this.loc;
	}
	public void panelFlash() {
		this.drawProjectileFlash = true;
	}
	public void panelFlashImportant() {
		this.drawImportantFlash = true;
	}
	public void draw(Graphics g) {
		//INCLUDE DIFFERENT PANEL STATES LATER
		Point panelSize = this.map.getSizeOfPanel();
		if(this.teamID == GameMap.ID_LEFT) {
			//g.setColor(Color.red);
			switch(this.state){
			case NORMAL:
				g.drawImage(normalRed.getScaledCopy((int)panelSize.x,(int)panelSize.y), (float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2));
				break;
			case CRACKED:
				g.drawImage(crackedRed.getScaledCopy((int)panelSize.x,(int)panelSize.y), (float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2));
				break;
			case HOLE:
				g.drawImage(holeRed.getScaledCopy((int)panelSize.x,(int)panelSize.y), (float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2));
				if(this.holeResetTimer < 2 && this.holeResetTimer % 0.15 > 0.075) {
					g.drawImage(normalRed.getScaledCopy((int)panelSize.x,(int)panelSize.y), (float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2));
				}
				break;
			default:
				break;
			}
		} else if(this.teamID == GameMap.ID_RIGHT) {
			//g.setColor(Color.blue);
			switch(this.state){
			case NORMAL:
				g.drawImage(normalBlue.getScaledCopy((int)panelSize.x,(int)panelSize.y), (float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2));
				break;
			case CRACKED:
				g.drawImage(crackedBlue.getScaledCopy((int)panelSize.x,(int)panelSize.y), (float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2));
				break;
			case HOLE:
				g.drawImage(holeBlue.getScaledCopy((int)panelSize.x,(int)panelSize.y), (float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2));
				if(this.holeResetTimer < 2 && this.holeResetTimer % 0.15 > 0.075) {
					g.drawImage(normalBlue.getScaledCopy((int)panelSize.x,(int)panelSize.y), (float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2));
				}
				break;
			default:
				break;
			}
		} else {
			//g.setColor(Color.gray);
		}
		/*
		Rectangle rect = new Rectangle((float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2), (float)panelSize.x, (float)panelSize.y);
		g.fill(rect);
		g.setColor(Color.black);
		g.draw(rect);
		*/
		if(this.drawProjectileFlash) {
			Point loc = Point.subtract(this.map.gridToPosition(this.getLoc()), Point.scale(panelSize, 0.4));
			g.setColor(Color.yellow);
			Rectangle rect = new Rectangle((float)loc.x, (float)loc.y, (float)panelSize.x * 0.8f, (float)panelSize.y * 0.8f);
			g.fill(rect);
			this.drawProjectileFlash = false;
		}
		if(this.drawImportantFlash) {
			Point loc = Point.subtract(this.map.gridToPosition(this.getLoc()), Point.scale(panelSize, 0.3));
			g.setColor(Color.red);
			Rectangle rect = new Rectangle((float)loc.x, (float)loc.y, (float)panelSize.x * 0.6f, (float)panelSize.y * 0.6f);
			g.fill(rect);
			this.drawImportantFlash = false;
		}
	}
}
