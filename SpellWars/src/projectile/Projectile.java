package projectile;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Panel;
import mechanic.PanelState;
import mechanic.Point;
import states.StateGame;
import unit.Unit;

public class Projectile extends GameElement {
	//public static final Font HP_FONT = new UnicodeFont();
	public static final double SHADOW_SCALE = 0.8;
	public int direction;
	public Point vel;
	float moveCooldown;
	boolean think;
	float thinkInterval;
	float thinkTimer;
	boolean thinkWithMove;
	boolean updateWithMove;
	Point gridLoc;
	Point futureLoc;
	Point pastFrameGridLoc;
	public int teamID;
	boolean simpleProjectile;
	boolean destroyOnImpact;
	boolean ignoreHoles;
	public boolean useMoveVec;
	ArrayList<Unit> unitsHit;
	float imageScale;
	public boolean flashPanel;
	public boolean drawShadow;
	
	double damage;
	public boolean canDoDamage;
	boolean penetratesShields;
	
	public Projectile(double damage, double speed, int direction, Point gridLoc, String imagePath, int teamID, boolean destroyOnImpact, boolean simpleProjectile, boolean ignoreHoles) {
		super(0, 0, speed, 0, new Point(), 1, 0, imagePath);
		this.useMoveVec = false;
		this.gridLoc = gridLoc;
		this.pastFrameGridLoc = Point.clone(gridLoc);
		this.futureLoc = gridLoc;
		this.direction = direction;
		this.moveCooldown = 0;
		this.think = false;
		this.thinkInterval = -1;
		this.thinkTimer = 0;
		this.thinkWithMove = false;
		this.teamID = teamID;
		this.simpleProjectile = simpleProjectile;
		if(speed != 0) {
			//this.setThinkInterval((float)(1/this.getFinalSpeed()));
			this.setThinkIntervalWithMove(true);
		}
		this.updateWithMove = false;
		this.destroyOnImpact = destroyOnImpact;
		this.ignoreHoles = ignoreHoles;
		unitsHit = new ArrayList<Unit>();
		this.vel = new Point();
		this.imageScale = 1;
		this.flashPanel = true;
		this.drawShadow = false;
		this.damage = damage;
		this.canDoDamage = true;
		this.penetratesShields = false;
	}
	public Projectile(double damage, double speed, Point moveVec, Point gridLoc, String imagePath, int teamID, boolean destroyOnImpact, boolean simpleProjectile, boolean ignoreHoles) {
		this(damage, speed, GameMap.ID_NEUTRAL, gridLoc, imagePath, teamID, destroyOnImpact, simpleProjectile, ignoreHoles);
		this.vel = moveVec;
		this.useMoveVec = true;
	}
	public void setDirection(char dir){
		this.direction = dir;
	}
	public void setVel(Point vel) {
		this.vel = vel;
	}
	public void setPenetrateShields(boolean penetratesShields) {
		this.penetratesShields = penetratesShields;
	}
	@Override
	public void onSetMap() {
		this.changeLoc(this.getMap().gridToPosition(this.gridLoc));
		if(!this.getMap().pointIsInGrid(this.getGridLoc())) {
			this.setRemove(true);
			this.unitsHit.clear();
		}
		this.onProjectileSetMap();
	}
	public void onProjectileSetMap() {
		
	}
	@Override
	public void setSpeed(double speed) {
		if(speed != 0) {
			this.setThinkInterval((float)(1/this.getFinalSpeed()));
		}
		this.changeSpeed(speed);
	}
	public void setFlashPanel(boolean flashPanel) {
		this.flashPanel = flashPanel;
	}
	public void flash() {
		if(this.flashPanel && this.getMap().getPanelAt(this.getGridLoc()) != null) {
			this.getMap().getPanelAt(this.getGridLoc()).panelFlash();
		}
	}
	@Override
	public double getFrameTime() {
		if(this.updateWithMove) {
			return this.getActualFrameTime() * this.getFinalSpeed() / this.getSpeed() * this.getTimeScale();
		}
		return this.getActualFrameTime() * this.getTimeScale(); 
	}
	@Override
	public void update() {
		this.flash();
		if(this.think) {
			if(this.thinkWithMove) {
				this.thinkTimer -= this.getFrameTime() * this.getFinalSpeed();
			} else {
				this.thinkTimer -= this.getFrameTime();
			}
			while(this.thinkTimer <= 0) {
				this.onThink();
				this.flash();
				this.gridLoc = this.futureLoc;
				if(this.simpleProjectile) {
					if(this.useMoveVec) {
						this.move(this.vel, false, true);
					} else {
						this.move(this.direction, false, true);
					}
				}
				if(this.thinkWithMove) {
					this.thinkTimer += 1;
				} else {
					this.thinkTimer += this.thinkInterval;
				}
				//System.out.println("gridLoc: " + this.gridLoc.toString());
				//System.out.println("futureloc: " + this.futureLoc.toString());
				//System.out.println("getGridLoc: " + this.getGridLoc().toString());
				//System.out.println("thinkTimer / thinkInterval: " + this.thinkTimer / this.thinkInterval);
				//System.out.println("");
			}
		}
		this.changeLoc(this.getMap().gridToPosition(Point.interpolate(this.gridLoc, this.futureLoc, 1 - (this.thinkTimer))));
		//Point targetLoc = this.getGridLoc();
		for(Point targetLoc : Point.getPointsBetween(this.pastFrameGridLoc, this.getGridLoc())) {
			if(this.getMap().pointIsInGrid(targetLoc)) {
				Unit target = this.getMap().getPanelAt(targetLoc).unitStandingOnPanel;
				if(target != null && target.teamID != this.teamID && !this.unitsHit.contains(target) && !this.getRemove()){
					if(this.destroyOnImpact) {
						this.setRemove(true);
						this.unitsHit.clear();
						if(this.canDoDamage) {
							target.doDamage(this.getFinalDamage(), true, this.penetratesShields, this);
							if(this.teamID != target.teamID) {
								this.onTargetHit(target);
							}
						}
						break;
					} else {
						if(this.canDoDamage) {
							target.doDamage(this.getFinalDamage(), true, this.penetratesShields, this);
							if(this.teamID != target.teamID) {
								this.onTargetHit(target);
								this.unitsHit.add(target);
							}
						}
					}
				}
				if(!this.ignoreHoles && this.getMap().getPanelAt(targetLoc).getPanelState() == PanelState.HOLE) {
					this.setRemove(true);
					this.unitsHit.clear();
				}
			}
		}
		
		if(!this.getMap().pointIsInGrid(this.getGridLoc())) {
			this.setRemove(true);
			this.unitsHit.clear();
		}
		this.pastFrameGridLoc = Point.clone(this.getGridLoc());
		if(this.moveCooldown > 0) {
			this.moveCooldown -= this.getFrameTime();
		}
		this.onProjectileUpdate();
	}
	public void onProjectileUpdate() {
		
	}
	public void resetUnitsHit() {
		this.unitsHit.clear();
	}
	public void addToUnitsHit(Unit u) {
		if(!this.unitsHit.contains(u)) {
			this.unitsHit.add(u);
		}
	}
	public void onThink() {
		
	}
	public void onTargetHit(Unit target) {
		
	}
	public void setDamage(double damage) {
		this.damage = damage;
		if(this.damage < 0) {
			this.damage = 0;
		}
	}
	public void changeDamage(double damage) {
		this.damage += damage;
		if(this.damage < 0) {
			this.damage = 0;
		}
	}
	public double getDamage() {
		return this.damage;
	}
	public double getFinalDamage() {
		return this.damage * this.finalDamageOutputModifier;
	}
	public Point getGridLoc() {
		if(1 - this.thinkTimer < 0) {
			return Point.roundToNearestInteger(this.gridLoc);
		} else if(1 - this.thinkTimer > 1) {
			return Point.roundToNearestInteger(this.futureLoc);
		} else {
			return Point.roundToNearestInteger(Point.interpolate(this.gridLoc, this.futureLoc, 1 - this.thinkTimer));
		}
	}
	public void setThinkInterval(float interval) {
		if(!this.think) {
			this.thinkTimer = 0;
		}
		this.think = true;
		this.thinkInterval = interval;
	}
	public void setThinkIntervalWithMove(boolean thinkWithMove){
		this.thinkWithMove = thinkWithMove;
		if(thinkWithMove) {
			this.setThinkInterval((float) (1/this.getFinalSpeed()));
		}
	}
	public void setImageScale(float imageScale){
		this.imageScale = imageScale;
	}
	public void onDraw() {
		
	}
	@Override
	public void draw(Graphics g){
		this.onDraw();
		this.flash();
		Point loc = Point.subtract(this.getMap().gridToPosition(this.getGridLoc()), Point.scale(this.getMap().getSizeOfPanel(), 0.4));
		/*
		g.setColor(Color.yellow);
		Rectangle rect = new Rectangle((float)loc.x, (float)loc.y, (float)this.getMap().getSizeOfPanel().x * 0.8f, (float)this.getMap().getSizeOfPanel().y * 0.8f);
		g.fill(rect);
		*/
		if(this.getImage() != null){
			Image endPic = this.getImage().getFlippedCopy(this.direction == GameMap.ID_LEFT, false).getScaledCopy(this.imageScale);
			endPic.rotate(-(float)this.getOrientation());
			float width = endPic.getWidth();
			float height = endPic.getHeight();
			g.drawImage(endPic, (float) this.getLoc().x - width/2, (float) this.getLoc().y - height/2 - this.getDrawHeight());
			int hflipvflip = 0;
			if(this.direction == GameMap.ID_LEFT) {
				hflipvflip += 2;
			}
			if(StateGame.isServer)
			this.getMap().addToDrawInfo(GameMap.getDrawDataI(this.getImagePath(), this.getLoc().x - width/2, this.getLoc().y - height/2 - this.getDrawHeight(), width, height, -this.getOrientation(), 255, 255, 255, 255, hflipvflip));
		}
		/*
		Point loc = Point.subtract(this.getMap().gridToPosition(this.getGridLoc()), new Point(15, 15));
		g.drawRect((float)loc.x, (float)loc.y, 30, 30);
		loc = Point.subtract(this.getMap().gridToPosition(this.gridLoc), new Point(5, 5));
		g.setColor(Color.green);
		g.drawRect((float)loc.x, (float)loc.y, 10, 10);
		loc = Point.subtract(this.getMap().gridToPosition(this.futureLoc), new Point(5, 5));
		g.setColor(Color.yellow);
		g.drawRect((float)loc.x, (float)loc.y, 10, 10);
		*/
	}
	@Override
	public void drawShadow(Graphics g) {
		if(this.drawShadow) {
			Image endPic = this.getImage().getFlippedCopy(this.direction == GameMap.ID_LEFT, false);
			endPic = endPic.getScaledCopy((float) this.imageScale);
			float width = endPic.getWidth();
			float shadowRatio = (float) (this.getMap().getSizeOfPanel().x / this.getMap().getSizeOfPanel().y);
			g.setColor(GameMap.SHADOW_COLOR);
			g.fillOval((float) (this.getLoc().x - SHADOW_SCALE * width/2), (float) (this.getLoc().y - SHADOW_SCALE * width/(2 * shadowRatio)), (float)(width * SHADOW_SCALE), (float)(SHADOW_SCALE * width/shadowRatio));
			if(StateGame.isServer)
			this.getMap().addToDrawInfo(GameMap.getDrawDataE(this.getLoc().x - SHADOW_SCALE * width/2, this.getLoc().y - SHADOW_SCALE * width/(2 * shadowRatio), width * SHADOW_SCALE, SHADOW_SCALE * width/shadowRatio, GameMap.SHADOW_COLOR.getRedByte(), GameMap.SHADOW_COLOR.getGreenByte(), GameMap.SHADOW_COLOR.getBlueByte(), GameMap.SHADOW_COLOR.getAlphaByte()));
		}
	}
	public void move(int direction, boolean respectCooldown, boolean putCooldown) {
		if(((respectCooldown && this.moveCooldown <= 0) || !respectCooldown) && !this.isPaused() && !this.getRemove()) {
			Point moveVec = new Point();
			switch(direction) {
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
			Point futurepoint = Point.add(this.futureLoc, moveVec); //this.futureLoc was once this.getGridLoc()
			//if(this.getMap().pointIsInGrid(futurepoint)) {
				this.futureLoc = futurepoint;
				if(putCooldown) {
					if(this.getFinalSpeed() != 0) {
						this.moveCooldown += (float) (1/this.getFinalSpeed());
					}
				}
			//} else {
			//	this.setRemove(true);
			//}
			this.changeLoc(this.getMap().gridToPosition(this.gridLoc)); 
		}
	}
	public void move(Point moveVec, boolean respectCooldown, boolean putCooldown) {
		if(((respectCooldown && this.moveCooldown <= 0) || !respectCooldown) && !this.isPaused()) {
			Point futurepoint = Point.add(this.futureLoc, moveVec);
			//if(this.getMap().pointIsInGrid(futurepoint)) {
				this.futureLoc = futurepoint;
				if(putCooldown) {
					if(this.getFinalSpeed() != 0) {
						this.moveCooldown += (float) (1/this.getFinalSpeed());
					}
				}
			//} else {
			//	this.setRemove(true);
			//}
			this.changeLoc(this.getMap().gridToPosition(this.gridLoc)); 
		}
	}
}
