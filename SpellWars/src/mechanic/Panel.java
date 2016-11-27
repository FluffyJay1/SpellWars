package mechanic;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import states.StateGame;
import statuseffect.PanelAura;
import statuseffect.StatusFrost;
import statuseffect.StatusHoly;
import statuseffect.StatusLava;
import statuseffect.StatusMud;
import unit.Unit;

public class Panel {
	Point loc;
	//INCLUDE A BUNCH OF IMAGES FOR DIFFERENT PANEL STATES
	public int teamID; //0 is neutral, 1 is left, 2 is right
	PanelState state;
	GameMap map;
	public Unit unitStandingOnPanel;
	public static final float PANEL_FLASH_DURATION = 2;
	public static final float PANEL_FLASH_INTERVAL = 0.2f;
	public static final double PANEL_FLASH_SIZE = 0.8;
	public static final Color PANEL_FLASH_COLOR = new Color(230, 230, 0, 200);
	public static final double PANEL_FLASH_IMPORTANT_SIZE = 0.6;
	public static final Color PANEL_FLASH_IMPORTANT_COLOR = new Color(220, 20, 0, 200);
	public static final float CRACK_RESET_TIME = 15;
	public static final float HOLE_RESET_TIME = 15;
	public static final float LAVA_RESET_TIME = 20; //originally 20
	public static final float MUD_RESET_TIME = 40;
	public static final float HOLY_RESET_TIME = 30;
	public static final float LAVA_DAMAGE_PER_SECOND = 10;
	public static final float MUD_SPEED_MODIFIER = 0.5f;
	public static final float HOLY_DAMAGE_INPUT_MODIFIER = 0.5f;
	public static final float HOLY_HEAL_INPUT_MODIFIER = 2;
	float panelResetTimer;
	PanelAura aura;
	boolean willBecomeHole; //used when the player stands on the cracked panel, to figure out when to make it a hole
	public static Image normal;
	public static Image cracked;
	public static Image hole;
	public static Image blueoutline;
	public static Image redoutline;
	public static Image mud;
	public static Image holy;
	Animation lava;
	public static boolean imagesLoaded = false;
	boolean drawProjectileFlash;
	boolean drawImportantFlash;
	float projectileFlashTimer;
	float importantFlashTimer;
	public Panel(int x, int y, int teamID, PanelState state, GameMap map) {
		this.loc = new Point(x,y);
		this.teamID = teamID;
		this.panelResetTimer = 0;
		this.aura = new PanelAura(map, this);
		this.setPanelState(state);
		this.map = map;
		this.willBecomeHole = false;
		if(!imagesLoaded) {
			try {
				normal = new Image("res/panel/normal.png", false, Image.FILTER_NEAREST);
				cracked = new Image("res/panel/cracked.png", false, Image.FILTER_NEAREST);
				hole = new Image("res/panel/hole.png", false, Image.FILTER_NEAREST);
				redoutline = new Image("res/panel/redoutline.png", false, Image.FILTER_NEAREST);
				blueoutline = new Image("res/panel/blueoutline.png", false, Image.FILTER_NEAREST);
				//lavaAnimation = new Image("res/panel/lava_animation.png", false, Image.FILTER_NEAREST);
				mud = new Image("res/panel/mud.png", false, Image.FILTER_NEAREST);
				holy = new Image("res/panel/holy.png", false, Image.FILTER_NEAREST);
				imagesLoaded = true;
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.lava = new Animation("res/panel/lava_animation.png", 6, 1, (float)this.map.getSizeOfPanel().x, (float)this.map.getSizeOfPanel().y, 2f, true, false);
		this.lava.changeLoc(map.gridToPosition(new Point(x, y)));
		this.projectileFlashTimer = 0;
		this.importantFlashTimer = 0;
	}
	public void setTeamID(int id) {
		this.teamID = id;
	}
	public PanelState getPanelState() {
		return this.state;
	}
	public void setPanelState(PanelState state) {
		this.state = state;
		this.aura.setEffect(null);
		switch(state) {
		case NORMAL:
			this.panelResetTimer = 0;
		case CRACKED:
			this.panelResetTimer = CRACK_RESET_TIME;
			break;
		case HOLE:
			this.panelResetTimer = HOLE_RESET_TIME;
			this.willBecomeHole = false;
			break;
		case LAVA:
			this.panelResetTimer = LAVA_RESET_TIME;
			this.aura.setEffect(new StatusLava(LAVA_DAMAGE_PER_SECOND));
			break;
		case MUD:
			this.panelResetTimer = MUD_RESET_TIME;
			this.aura.setEffect(new StatusMud(MUD_SPEED_MODIFIER));
			break;
		case HOLY:
			this.panelResetTimer = HOLY_RESET_TIME;
			this.aura.setEffect(new StatusHoly(HOLY_DAMAGE_INPUT_MODIFIER, HOLY_HEAL_INPUT_MODIFIER));
			break;
		default:
				break;
		}
	}
	public void crackLight() {
		if(this.state == PanelState.CRACKED && this.unitStandingOnPanel == null) {
			this.setPanelState(PanelState.HOLE);
		} else if (this.state != PanelState.HOLE) {
			this.setPanelState(PanelState.CRACKED);
		}
	}
	public void crackHeavy() {
		if(this.unitStandingOnPanel == null) {
			this.setPanelState(PanelState.HOLE);
		} else {
			this.setPanelState(PanelState.CRACKED);
		}
	}
	public void clearState() {
		this.setPanelState(PanelState.NORMAL);
	}
	public void update(float frametime) {
		this.aura.update();
		if(panelResetTimer <= 0) {
			this.setPanelState(PanelState.NORMAL);
		} else {
			this.panelResetTimer -= frametime;
			
		}
		if(this.state == PanelState.HOLE) {
			
		} else if(this.state == PanelState.CRACKED) {
			if(this.unitStandingOnPanel != null) {
				this.willBecomeHole = true;
			} else if(this.willBecomeHole) {
				this.setPanelState(PanelState.HOLE);
			}
		} else if(this.state == PanelState.LAVA) {
			this.lava.update(frametime);
		}
	}
	public void updatePanelFlash(float frametime) {
		if(this.projectileFlashTimer > 0) {
			this.projectileFlashTimer -= frametime;
		}
		if(this.importantFlashTimer > 0) {
			this.importantFlashTimer -= frametime;
		}
	}
	public Point getLoc() {
		return this.loc;
	}
	public void panelFlash() {
		this.drawProjectileFlash = true;
	}
	public void panelFlash(float duration) {
		this.drawProjectileFlash = true;
		if(this.projectileFlashTimer <= duration) {
			this.projectileFlashTimer = duration;
		}
	}
	public void panelFlashImportant() {
		this.drawImportantFlash = true;
	}
	public void panelFlashImportant(float duration) {
		this.drawImportantFlash = true;
		if(this.importantFlashTimer <= duration) {
			this.importantFlashTimer = duration;
		}
	}
	public void draw(Graphics g) {
		//INCLUDE DIFFERENT PANEL STATES LATER
		Point panelSize = this.map.getSizeOfPanel();
		if(this.teamID == GameMap.ID_LEFT) {
			//g.setColor(Color.red);
			g.drawImage(redoutline.getScaledCopy((int)panelSize.x,(int)panelSize.y), (float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2));
			if(StateGame.isServer)
			this.map.addToDrawInfo(GameMap.getDrawDataFN("res/panel/redoutline.png", this.map.gridToPosition(loc).x - panelSize.x/2, this.map.gridToPosition(loc).y - panelSize.y/2, panelSize.x, panelSize.y, 0, 255, 255, 255, 255, 0));
		} else if(this.teamID == GameMap.ID_RIGHT) {
			//g.setColor(Color.blue);
			g.drawImage(blueoutline.getScaledCopy((int)panelSize.x,(int)panelSize.y), (float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2));
			if(StateGame.isServer)
			this.map.addToDrawInfo(GameMap.getDrawDataFN("res/panel/blueoutline.png", this.map.gridToPosition(loc).x - panelSize.x/2, this.map.gridToPosition(loc).y - panelSize.y/2, panelSize.x, panelSize.y, 0, 255, 255, 255, 255, 0));
		} else {
			//g.setColor(Color.gray);
		}
		switch(this.state){
		case NORMAL:
			g.drawImage(normal.getScaledCopy((int)panelSize.x,(int)panelSize.y), (float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2));
			if(StateGame.isServer)
			this.map.addToDrawInfo(GameMap.getDrawDataFN("res/panel/normal.png", this.map.gridToPosition(loc).x - panelSize.x/2, this.map.gridToPosition(loc).y - panelSize.y/2, panelSize.x, panelSize.y, 0, 255, 255, 255, 255, 0));
			break;
		case CRACKED:
			g.drawImage(cracked.getScaledCopy((int)panelSize.x,(int)panelSize.y), (float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2));
			if(StateGame.isServer)
			this.map.addToDrawInfo(GameMap.getDrawDataFN("res/panel/cracked.png", this.map.gridToPosition(loc).x - panelSize.x/2, this.map.gridToPosition(loc).y - panelSize.y/2, panelSize.x, panelSize.y, 0, 255, 255, 255, 255, 0));
			break;
		case HOLE:
			g.drawImage(hole.getScaledCopy((int)panelSize.x,(int)panelSize.y), (float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2));
			if(StateGame.isServer)
			this.map.addToDrawInfo(GameMap.getDrawDataFN("res/panel/hole.png", this.map.gridToPosition(loc).x - panelSize.x/2, this.map.gridToPosition(loc).y - panelSize.y/2, panelSize.x, panelSize.y, 0, 255, 255, 255, 255, 0));
			break;
		case LAVA:
			lava.draw(g, this.map);
			break;
		case MUD:
			g.drawImage(mud.getScaledCopy((int)panelSize.x,(int)panelSize.y), (float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2));
			if(StateGame.isServer)
			this.map.addToDrawInfo(GameMap.getDrawDataFN("res/panel/mud.png", this.map.gridToPosition(loc).x - panelSize.x/2, this.map.gridToPosition(loc).y - panelSize.y/2, panelSize.x, panelSize.y, 0, 255, 255, 255, 255, 0));
			break;
		case HOLY:
			g.drawImage(holy.getScaledCopy((int)panelSize.x,(int)panelSize.y), (float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2));
			if(StateGame.isServer)
			this.map.addToDrawInfo(GameMap.getDrawDataFN("res/panel/holy.png", this.map.gridToPosition(loc).x - panelSize.x/2, this.map.gridToPosition(loc).y - panelSize.y/2, panelSize.x, panelSize.y, 0, 255, 255, 255, 255, 0));
			break;
		default:
			break;
		}
		if(this.panelResetTimer < PANEL_FLASH_DURATION && this.panelResetTimer > 0 && this.panelResetTimer % PANEL_FLASH_INTERVAL > PANEL_FLASH_INTERVAL/2) {
			g.drawImage(normal.getScaledCopy((int)panelSize.x,(int)panelSize.y), (float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2));
			if(StateGame.isServer)
			this.map.addToDrawInfo(GameMap.getDrawDataFN("res/panel/normal.png", this.map.gridToPosition(loc).x - panelSize.x/2, this.map.gridToPosition(loc).y - panelSize.y/2, panelSize.x, panelSize.y, 0, 255, 255, 255, 255, 0));
		}
		/*
		Rectangle rect = new Rectangle((float)(this.map.gridToPosition(loc).x - panelSize.x/2),(float)(this.map.gridToPosition(loc).y - panelSize.y/2), (float)panelSize.x, (float)panelSize.y);
		g.fill(rect);
		g.setColor(Color.black);
		g.draw(rect);
		*/
		if(this.drawProjectileFlash || this.projectileFlashTimer > 0) {
			Point loc = Point.subtract(this.map.gridToPosition(this.getLoc()), Point.scale(panelSize, PANEL_FLASH_SIZE/2));
			g.setColor(PANEL_FLASH_COLOR);
			Rectangle rect = new Rectangle((float)loc.x, (float)loc.y, (float)panelSize.x * (float)PANEL_FLASH_SIZE, (float)panelSize.y * (float)PANEL_FLASH_SIZE);
			g.fill(rect);
			if(StateGame.isServer)
			this.map.addToDrawInfo(GameMap.getDrawDataR(loc.x, loc.y, panelSize.x * PANEL_FLASH_SIZE, panelSize.y * PANEL_FLASH_SIZE, PANEL_FLASH_COLOR.getRedByte(), PANEL_FLASH_COLOR.getGreenByte(), PANEL_FLASH_COLOR.getBlueByte(), PANEL_FLASH_COLOR.getAlphaByte()));
			this.drawProjectileFlash = false;
		}
		if(this.drawImportantFlash || this.importantFlashTimer > 0) {
			Point loc = Point.subtract(this.map.gridToPosition(this.getLoc()), Point.scale(panelSize, PANEL_FLASH_IMPORTANT_SIZE/2));
			g.setColor(PANEL_FLASH_IMPORTANT_COLOR);
			Rectangle rect = new Rectangle((float)loc.x, (float)loc.y, (float)panelSize.x * (float)PANEL_FLASH_IMPORTANT_SIZE, (float)panelSize.y * (float)PANEL_FLASH_IMPORTANT_SIZE);
			g.fill(rect);
			if(StateGame.isServer)
			this.map.addToDrawInfo(GameMap.getDrawDataR(loc.x, loc.y, panelSize.x * PANEL_FLASH_IMPORTANT_SIZE, panelSize.y * PANEL_FLASH_IMPORTANT_SIZE, PANEL_FLASH_IMPORTANT_COLOR.getRedByte(), PANEL_FLASH_IMPORTANT_COLOR.getGreenByte(), PANEL_FLASH_IMPORTANT_COLOR.getBlueByte(), PANEL_FLASH_IMPORTANT_COLOR.getAlphaByte()));
			this.drawImportantFlash = false;
		}
	}
}
