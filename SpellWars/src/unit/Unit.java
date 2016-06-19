package unit;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Panel;
import mechanic.PanelState;
import mechanic.Point;
import projectile.Projectile;
import shield.Shield;
import spell.Spell;
import ui.Text;
import ui.TextFormat;



public class Unit extends GameElement {
	public static final int HP_Y_OFFSET = 12;
	public static final int CAST_BAR_OFFSET = 35;
	public static final int CAST_BAR_WIDTH = 60;
	public static final int CAST_BAR_HEIGHT = 10;
	public static final double SHADOW_SCALE = 0.9;
	public static final float EXTRA_SHIELDS_Y_OFFSET = -18;
	public static final float EXTRA_SHIELDS_SCALE_BONUS = 0.28f;
	//public static final Font HP_FONT = new UnicodeFont();
	public int direction;
	float moveCooldown;
	boolean think;
	float thinkInterval;
	float thinkTimer;
	
	public float spellCastTimer;
	float spellCastMaxTime;
	public boolean isCasting;
	public boolean isCoolingDown;
	public Spell spellBeingCast;
	
	public Point gridLoc;
	public int teamID;
	public Panel panelStandingOn;
	public boolean ignoreHoles;
	public boolean ignoreTeam;
	boolean thinkWithMove;
	boolean drawShadow;
	
	boolean drawMoveCooldown;
	boolean drawSpellCasting;
	Color drawColor;
	
	float stunTimer;
	
	ArrayList<Shield> shields;
	ArrayList<Shield> shieldsRemoveBuffer;
	
	Color HPTextColor;
	public Unit(double hp, double maxHP, double speed, int direction, Point gridLoc, String imagePath, int teamID) {
		super(hp, maxHP, speed, 0, new Point(), 1, 0, imagePath);
		this.gridLoc = gridLoc;
		this.direction = direction;
		this.moveCooldown = (float) (1/this.getFinalSpeed());
		this.think = false;
		this.thinkInterval = -1;
		this.thinkTimer = 0;
		this.teamID = teamID;
		this.thinkWithMove = false;
		this.drawMoveCooldown = false;
		this.drawSpellCasting = true;
		this.HPTextColor = Color.darkGray;
		this.spellCastTimer = 0;
		this.spellCastMaxTime = 1;
		this.isCasting = false;
		this.spellBeingCast = null;
		this.drawShadow = true;
		this.stunTimer = 0;
		this.drawColor = Color.white;
		this.isCoolingDown = false;
		this.ignoreTeam = false;
		this.shields = new ArrayList<Shield>();
		this.shieldsRemoveBuffer = new ArrayList<Shield>();
	}
	public void addShield(Shield shield) {
		if(!this.shields.contains(shield)) {
			this.shields.add(shield);
		}
	}
	public ArrayList<Shield> getShields() {
		return this.shields;
	}
	@Override
	public boolean doDamage(double damage){
		return this.doDamage(damage, false, null);
	}
	
	public boolean doDamage(double damage, boolean ignoreShields, Projectile source){ //now with damage source
		boolean isKillingBlow = false;
		if(damage > 0) {
			boolean damageBlocked = false;
			if(!ignoreShields) {
				for(int index = this.shields.size() - 1; index >= 0; index--) {
					if(this.shields.get(index).blockDamage && this.shields.get(index).getHP() > 0) { //if there is at least one shield, blocks the whole instance of damage
						this.shields.get(index).doDamage(damage); //damages the most recent shield
						if(source != null) {
							this.shields.get(index).onHitByProjectile(source);
						}
						damageBlocked = true;
						break;
					}
				}
			}
			if(!damageBlocked) {
				isKillingBlow = this.changeHP(this.getHP() - damage);
				if(source != null) {
					this.onHitByProjectile(source);
				}
				for(Shield s : this.shields) {
					s.onOwnerDamaged(damage);
				}
			}
		} else {
			this.changeHP(this.getHP() - damage); //heal
		}
		return isKillingBlow;
	}
	public void onHitByProjectile(Projectile source) {
		
		
	}
	public void setDrawColor(Color drawColor) {
		this.drawColor = drawColor;
	}
	public Color getDrawColor(Color drawColor) {
		return this.drawColor;
	}
	public void setIgnoreHoles(boolean ignoreHoles) {
		this.ignoreHoles = ignoreHoles;
	}
	@Override
	public void setRemove(boolean state){
		this.remove = state;
		if(!this.isPaused() && this.panelStandingOn != null) {
			this.panelStandingOn.unitStandingOnPanel = null;
		}
	}
	@Override
	public void onSetMap() {
		this.changeLoc(this.getMap().gridToPosition(this.gridLoc));
		this.panelStandingOn = this.getMap().getPanelAt(this.gridLoc);
	}
	@Override
	public void update() {
		int shieldsDrawn = 0;
		for(Shield s : this.shields) {
			s.setDrawOffset(EXTRA_SHIELDS_Y_OFFSET * shieldsDrawn);
			s.setSize(1 + ((float)(shieldsDrawn) * EXTRA_SHIELDS_SCALE_BONUS));
			shieldsDrawn++;
			if(s.getRemove()) {
				this.shieldsRemoveBuffer.add(s);
			}
		}
		this.shields.removeAll(shieldsRemoveBuffer);
		
		this.changeLoc(this.getMap().gridToPosition(this.gridLoc));
		if(this.stunTimer > 0) {
			this.stunTimer -= this.getFrameTime();
		} else {
			if(this.moveCooldown > 0) {
				this.moveCooldown -= this.getFrameTime();
			}
			if(this.think) {
				if(this.thinkWithMove) {
					this.thinkTimer -= this.getFrameTime() * this.getFinalSpeed();
				} else {
					this.thinkTimer -= this.getFrameTime();
				}
				while(this.thinkTimer <= 0) {
					this.onThink();
					if(this.thinkWithMove) {
						this.thinkTimer += 1;
					} else {
						this.thinkTimer += this.thinkInterval;
					}
				}
			}
			/*
			 * FLOW:
			 * Start cast-> CastTime -> Activate Spell -> Backswing time -> End cast
			 */
			if(this.isCasting && !this.isPaused()) {
				if(this.spellCastTimer <= 0) {
					if(!this.isPaused() && !this.getRemove() && this.spellBeingCast != null && !this.isCoolingDown) {
						this.spellBeingCast.setOwner(this);
						this.getMap().addGameElement(this.spellBeingCast);
						this.spellBeingCast.activate();
						this.spellCastTimer = this.spellBeingCast.backswingTime; //NOW COOLING DOWN
						this.spellCastMaxTime = this.spellBeingCast.backswingTime;
						this.isCoolingDown = true;
						if(this.spellBeingCast.backswingTime == 0) {
							this.isCasting = false;
							this.isCoolingDown = false;
						}
						//this.spellBeingCast = null;
					} else if(this.isCoolingDown) { //IF IT HAS COOLED DOWN
						this.isCasting = false;
						this.isCoolingDown = false;
					}
				} else {
					this.spellCastTimer -= this.getFrameTime();
				}
			}
		}
		this.onUpdate();
	}
	public void onUpdate() {
		
	}
	public void onThink() {
		
	}
	public void setThinkInterval(float interval) {
		this.think = true;
		this.thinkTimer = interval;
		this.thinkInterval = interval;
	}
	public void setThinkIntervalWithMove(boolean thinkWithMove){
		this.thinkWithMove = thinkWithMove;
		if(thinkWithMove) {
			this.setThinkInterval((float) (1/this.getFinalSpeed()));
		}
	}
	public void stun(float duration) {
		if(this.stunTimer < duration){
			this.stunTimer = duration;
		}
	}
	public boolean isStunned() {
		return this.stunTimer > 0;
	}
	@Override
	public void draw(Graphics g){
		int shieldsDrawn = 0;
		for(Shield s : this.shields) {
			s.setDrawOffset(EXTRA_SHIELDS_Y_OFFSET * shieldsDrawn);
			s.setSize(1 + ((float)(shieldsDrawn) * EXTRA_SHIELDS_SCALE_BONUS));
			shieldsDrawn++;
			if(s.getRemove()) {
				this.shieldsRemoveBuffer.add(s);
			}
		}
		this.shields.removeAll(shieldsRemoveBuffer);
		if(this.getImage() != null){
			Image endPic = this.getImage().getFlippedCopy(this.direction == GameMap.ID_LEFT, false);
			endPic = endPic.getScaledCopy((float) this.getSize());
			float width = endPic.getWidth();
			float height = endPic.getHeight();
			Color col = Color.white;
			if(this.drawMoveCooldown && this.moveCooldown > 0) {
				col = new Color(205, 205, 235);
			}
			if(this.drawSpellCasting && this.isCasting) {
				col = new Color(220, 180, 90);
				float barwidth = 0;
				if(!this.isCoolingDown){
					g.setColor(new Color((int)((double)255 * (1-this.spellCastTimer/this.spellCastMaxTime)), 30, 30));
					barwidth = CAST_BAR_WIDTH * (1-this.spellCastTimer/this.spellCastMaxTime);
				} else {
					g.setColor(new Color(120, 150, 185));
					barwidth = CAST_BAR_WIDTH * this.spellCastTimer/this.spellCastMaxTime;
				}
				Rectangle rect = new Rectangle((float) this.getLoc().x - CAST_BAR_WIDTH/2, (float)this.getLoc().y + CAST_BAR_OFFSET,
						barwidth, CAST_BAR_HEIGHT);
				g.fill(rect);
			}
			if(this.stunTimer > 0 && this.stunTimer % 0.15 > 0.075) {
				col = new Color(160, 160, 0);
			}
			col = col.multiply(this.drawColor);
			g.drawImage(endPic, (float) this.getLoc().x - width/2, (float) this.getLoc().y - height/2 - this.getDrawHeight(), col);
		}
		this.drawSpecialEffects(g);
		g.setColor(this.HPTextColor);
		//g.setFont(font);
		//g.drawString("" + (int)this.getHP(), (float) this.getLoc().x - g.getFont().getWidth("" + (int)this.getHP())/2, (float) this.getLoc().y + HP_Y_OFFSET);
		Text text = new Text(this.getMap().getUI(), Point.add(this.getLoc(), new Point(-200, HP_Y_OFFSET)), 400, 12, 18, 14, 22, Color.white, "" + (int)this.getHP(), TextFormat.CENTER_JUSTIFIED);
		text.setUseOutline(true);
		text.setOutlineColor(this.HPTextColor);
		text.setRemoveNextFrame(true);
		this.getMap().getUI().addUIElement(text);
	}
	@Override
	public void drawShadow(Graphics g) {
		if(this.drawShadow) {
			Image endPic = this.getImage().getFlippedCopy(this.direction == GameMap.ID_LEFT, false);
			endPic = endPic.getScaledCopy((float) this.getSize());
			float width = endPic.getWidth();
			float shadowRatio = (float) (this.getMap().getSizeOfPanel().x / this.getMap().getSizeOfPanel().y);
			g.setColor(GameMap.SHADOW_COLOR);
			g.fillOval((float) (this.getLoc().x - SHADOW_SCALE * width/2), (float) (this.getLoc().y - SHADOW_SCALE * width/(2 * shadowRatio)), (float)(width * SHADOW_SCALE), (float)(SHADOW_SCALE * width/shadowRatio));
		}
	}
	public void drawSpecialEffects(Graphics g) {
		
	}
	public void move(int direction, boolean respectCooldown, boolean putCooldown, boolean respectCasting, boolean respectStun, boolean respectPause) {
		if(((respectCooldown && this.moveCooldown <= 0) || !respectCooldown) && ((respectCasting && !this.isCasting) || !respectCasting)
				&& (!this.isPaused() || !respectPause) && !this.getRemove() && ((respectStun && this.stunTimer <= 0) || !respectStun)) {
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
			Point futurepoint = Point.add(this.getMap().positionToGrid(this.getLoc()), moveVec);
			this.moveTo(futurepoint, putCooldown);
		}
	}
	public void moveRandom4(boolean respectCooldown, boolean putCooldown, boolean respectCasting, boolean respectStun, boolean respectPause) {
		if(((respectCooldown && this.moveCooldown <= 0) || !respectCooldown) && ((respectCasting && !this.isCasting) || !respectCasting)
				&& (!this.isPaused() || !respectPause) && !this.getRemove() && ((respectStun && this.stunTimer <= 0) || !respectStun)) {
			ArrayList<Point> availablePoints = new ArrayList<Point>();
			for(Point p : Point.proximity4(this.gridLoc)) {
				if(this.canMoveToLoc(p)) {
					availablePoints.add(p);
				}
			}
			if(availablePoints.size() > 0) {
				int randomIndex = (int)(Math.random() * availablePoints.size() - 0.000000001);
				if(randomIndex == availablePoints.size()) {
					randomIndex--;
				}
				this.moveTo(availablePoints.get(randomIndex), putCooldown);
			}
		}
	}
	public void moveTo(Point loc, boolean putCooldown) {
		if(this.canMoveToLoc(loc) && !this.getRemove()) {
			//this.getMap().getPanelAt(this.gridLoc).unitStandingOnPanel = null;
			this.panelStandingOn.unitStandingOnPanel = null;
			this.gridLoc = loc;
			this.getMap().getPanelAt(loc).unitStandingOnPanel = this;
			if(putCooldown) {
				this.moveCooldown += (float) (1/this.getFinalSpeed());
			}
			this.panelStandingOn = this.getMap().getPanelAt(this.gridLoc);
			this.changeLoc(this.getMap().gridToPosition(this.gridLoc)); 
		}
	}
	public boolean canMoveToLoc(Point loc) {
		return this.getMap().pointIsInGrid(loc) && (this.getMap().getPanelAt(loc).teamID == this.teamID || this.ignoreTeam) && this.getMap().getPanelAt(loc).unitStandingOnPanel == null
				&& !(!this.ignoreHoles && this.getMap().getPanelAt(loc).getPanelState() == PanelState.HOLE);
	}
	public boolean castSpell(Spell spell, boolean ignoreStun, boolean ignoreCast) {
		if((!this.isCasting || ignoreCast) && ((!ignoreStun && this.stunTimer <= 0) || ignoreStun)) {
			if(this.isCasting && ignoreCast) {
				this.interruptCast();
			}
			if(spell.castTime == 0) {
				this.spellBeingCast = spell;
				spell.setOwner(this);
				this.getMap().addGameElement(spell);
				spell.activate();
				this.isCasting = true;
				this.spellCastTimer = spell.backswingTime;
				this.spellCastMaxTime = spell.backswingTime;
				this.isCoolingDown = true;
				if(spell.backswingTime == 0 && !spell.pauseWhenActivated) {
					this.isCasting = false;
					this.isCoolingDown = false;
				}
			} else {
				this.spellBeingCast = spell;
				this.spellCastTimer = spell.castTime;
				this.spellCastMaxTime = spell.castTime;
				this.isCasting = true;
			}
			return true;
		}
		return false;
	}
	public boolean castSpell(Spell spell) {
		return this.castSpell(spell, false, false);
	}
	public void interruptCast() {
		if(!this.getRemove()) {
			if(this.spellBeingCast != null && this.isCoolingDown) {
				this.spellBeingCast.finishSpell();
			}
			this.isCasting = false;
			this.spellCastTimer = 0;
			this.spellBeingCast = null;
			this.isCoolingDown = false;
		}
	}
}
