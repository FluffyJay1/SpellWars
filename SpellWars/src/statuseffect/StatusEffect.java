package statuseffect;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

import mechanic.Game;
import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;
import states.StateGame;

public class StatusEffect {
	public static final int ICON_SIDE_LENGTH = 16;
	public static final Color DURATION_INDICATOR_FADED_COLOR = new Color(130, 130, 180);
	private GameElement owner;
	boolean remove;
	StackingProperty stackingProperty;
	float interval;
	float moveSpeedModifier; //as a multiplier
	float attackSpeedModifier; //as a percent
	float attackDamageModifier; //as a multiplier
	float maxDuration;
	float duration; //in seconds
	boolean isPermanent;
	float damagePerInterval;
	String id;
	Image icon;
	private String imagepath;
	//INTRODUCE LEVELS
	int level;
	boolean muteEffect, drawIcon;
	
	float damagetimer; //internal use only
	int numSources; //how many auras are applying this same effect
	
	private Color ownerColorModifier;
	
	public StatusEffect(GameElement owner, StackingProperty stackingProperty, String id, float duration, int level) {
		super();
		this.setOwner(owner);
		this.stackingProperty = stackingProperty;
		this.maxDuration = duration;
		this.duration = duration;
		this.id = id;
		this.damagetimer = 0;
		this.attackDamageModifier = 1;
		this.attackSpeedModifier = 0;
		this.moveSpeedModifier = 1;
		this.interval = 1;
		this.damagePerInterval = 0;
		this.remove = false;
		this.onCreate();
		this.numSources = 1;
		if(duration == -1) {
			this.isPermanent = true;
		} else {
			this.isPermanent = false;
		}
		this.level = level;
		this.muteEffect = false;
		this.drawIcon = true;
		this.ownerColorModifier = Color.white;
	}
	public int getLevel() {
		return this.level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public void changeNumSources(int num) {
		this.numSources += num;
	}
	public int getNumSources() {
		return this.numSources;
	}
	public void setMute(boolean mute) {
		if(mute != this.muteEffect) {
			this.getOwner().statusFinalModifierValueUpdate = true;
		}
		this.muteEffect = mute;
	}
	public void setDrawIcon(boolean drawIcon) {
		this.drawIcon = drawIcon;
	}
	public boolean getDrawIcon() {
		return this.drawIcon;
	}
	public StatusEffect clone(){
		StatusEffect effect = new StatusEffect(this.getOwner(), this.stackingProperty, this.id, this.maxDuration, this.level);
		effect.damagetimer = damagetimer;
		effect.attackDamageModifier = attackDamageModifier;
		effect.attackSpeedModifier = attackSpeedModifier;
		effect.moveSpeedModifier = moveSpeedModifier;
		effect.interval = interval;
		effect.damagePerInterval = damagePerInterval;
		effect.isPermanent = isPermanent;
		effect.icon = this.icon;
		effect.numSources = this.numSources;
		effect.drawIcon = this.drawIcon;
		effect.muteEffect = this.muteEffect;
		return effect;
	}
	public void update(float frametime) { //updates w/ time between frames
		//System.out.println(this.numSources);
		if(this.numSources <= 0) {
			this.setRemove(true);
		}
		this.timingUpdate(frametime);
		this.onUpdate();
	}
	public boolean hasIcon() {
		return this.icon != null;
	}
	public void drawIcon(Graphics g, Point loc) {
		if(this.drawIcon) {
			float ratio = this.getDuration()/this.getMaxDuration();
			double radiansFromTopCounterClockwise = ratio * Math.PI * 2;
			if(ratio == 1) {
				g.drawImage(this.icon, (float)loc.getX(), (float)loc.getY());
			} else {
				Polygon poly = new Polygon();
				poly.addPoint(0, -ICON_SIDE_LENGTH/2);
				Point varyingPolyPoint = new Point(Math.tan(radiansFromTopCounterClockwise) * -ICON_SIDE_LENGTH/2, -ICON_SIDE_LENGTH/2);
				if(ratio >= (double)1/8) {
					poly.addPoint(-ICON_SIDE_LENGTH/2, -ICON_SIDE_LENGTH/2);
					varyingPolyPoint = new Point(-ICON_SIDE_LENGTH/2, Math.tan(Math.PI/2 - radiansFromTopCounterClockwise) * -ICON_SIDE_LENGTH/2);
				}
				if(ratio >= (double)3/8) {
					poly.addPoint(-ICON_SIDE_LENGTH/2, ICON_SIDE_LENGTH/2);
					varyingPolyPoint = new Point(Math.tan(Math.PI - radiansFromTopCounterClockwise) * -ICON_SIDE_LENGTH/2,ICON_SIDE_LENGTH/2);
				}
				if(ratio >= (double)5/8) {
					poly.addPoint(ICON_SIDE_LENGTH/2, ICON_SIDE_LENGTH/2);
					varyingPolyPoint = new Point(ICON_SIDE_LENGTH/2, Math.tan(3 * Math.PI/2 - radiansFromTopCounterClockwise) * ICON_SIDE_LENGTH/2);
				}
				if(ratio >= (double)7/8) {
					poly.addPoint(ICON_SIDE_LENGTH/2, -ICON_SIDE_LENGTH/2);
					varyingPolyPoint = new Point(Math.tan(-radiansFromTopCounterClockwise) * ICON_SIDE_LENGTH/2, -ICON_SIDE_LENGTH/2);
				}
				poly.addPoint((float)varyingPolyPoint.getX(), (float)varyingPolyPoint.getY());
				//WORKING AROUND THE FITTING IMAGE INSIDE SHAPE
				poly.addPoint(-0.01f, 0.01f);
				poly.addPoint(ICON_SIDE_LENGTH/2, ICON_SIDE_LENGTH/2); 
				poly.addPoint(0.01f, -0.01f);
				poly.setLocation((float)loc.getX() + ICON_SIDE_LENGTH/2, (float)loc.getY() + ICON_SIDE_LENGTH/2);
				g.setColor(Color.white);
				g.drawImage(this.icon, (float)loc.getX(), (float)loc.getY(), DURATION_INDICATOR_FADED_COLOR);
				if(StateGame.isServer)
				this.getOwner().getMap().addToDrawInfo(GameMap.getDrawDataI(this.imagepath, loc.getX(), loc.getY(), ICON_SIDE_LENGTH, ICON_SIDE_LENGTH, 0, DURATION_INDICATOR_FADED_COLOR.getRedByte(), DURATION_INDICATOR_FADED_COLOR.getGreenByte(), DURATION_INDICATOR_FADED_COLOR.getBlueByte(), DURATION_INDICATOR_FADED_COLOR.getAlphaByte(), 0));
				g.texture(poly, this.icon, true);
				if(StateGame.isServer)
				this.getOwner().getMap().addToDrawInfo(GameMap.getDrawDataSI(this.imagepath, loc.getX(), loc.getY(), ratio));
			}
		}
	}
	/*
	public void setIcon(Image icon) {
		this.icon = icon;
	}
	*/
	public void setIcon(String path){
		if(Game.images.containsKey(path)) {
			this.icon = Game.images.get(path).getScaledCopy(ICON_SIDE_LENGTH, ICON_SIDE_LENGTH);
		} else {
			try {
				this.icon = new Image(path);
				Game.images.put(path, this.icon.copy());
				this.icon = this.icon.getScaledCopy(ICON_SIDE_LENGTH, ICON_SIDE_LENGTH);
			} catch (SlickException e) {
				System.out.println("Unable to load status effect icon");
				e.printStackTrace();
			} finally {
				System.out.println("loaded into memory: " + path);
			}
		}
		this.imagepath = path;
	}
	public Image getIcon() {
		return this.icon;
	}
	public void timingUpdate(float frametime) { //updates things related to time
		if(!this.isPermanent) {
			this.duration -= frametime;
			if(this.duration <= 0) {
				this.remove = true;
			}
		}
		
		this.damagetimer -= frametime;
		if(this.damagetimer <= 0 && this.remove == false) { //if it's time to deal damage and it's not flagged for removal
			if(!this.muteEffect) {
				getOwner().doDamage(damagePerInterval);
				this.onInterval();
			}
			damagetimer += interval;
		}
	}
	/*
	 * Runs when the status effect is created
	 */
	public void onCreate() {
		
	}
	/*
	 * This runs once every second, to be overridden by special status effects
	 */
	public void onInterval() {
		
	}
	/*
	 *  This runs every frame, if a status effect calls for it
	 */
	public void onUpdate() {
		
	}
	public StackingProperty getStackingProperty() {
		return this.stackingProperty;
	}
	public boolean getRemove() {
		return this.remove;
	}
	public void setRemove(boolean remove) {
		this.remove = remove;
	}
	public void setPermanent(boolean isPermanent){
		this.isPermanent = true;
	}
	public boolean getPermanent() {
		return this.isPermanent;
	}
	public float getMoveSpeedModifier() {
		if(!this.muteEffect) {
			return this.moveSpeedModifier;
		}
		return 1;
	}
	public void setMoveSpeedModifier(float mod) {
		this.moveSpeedModifier = mod;
		if(getOwner() != null) {
			this.getOwner().statusFinalModifierValueUpdate = true;
		}
	}
	public float getAttackSpeedModifier() {
		if(!this.muteEffect) {
			return this.attackSpeedModifier;
		}
		return 0;
	}
	public void setAttackSpeedModifier(float mod) {
		this.attackSpeedModifier = mod;
		if(getOwner() != null) {
			this.getOwner().statusFinalModifierValueUpdate = true;
		}
	}
	public float getAttackDamageModifier() {
		if(!this.muteEffect) {
			return this.attackDamageModifier;
		}
		return 1;
	}
	public void setAttackDamageModifier(float mod) {
		this.attackDamageModifier = mod;
		if(getOwner() != null) {
			this.getOwner().statusFinalModifierValueUpdate = true;
		}
	}
	public void setDuration(float duration) {
		this.duration = duration;
	}
	public float getDuration(){
		return this.duration;
	}
	public float getMaxDuration() {
		return this.maxDuration;
	}
	public String getStatusType() {
		return this.id;
	}
	public GameElement getOwner() {
		return owner;
	}
	public void setOwner(GameElement owner) {
		this.owner = owner;
	}
	public void setColorModifier(Color color) {
		this.ownerColorModifier = color;
	}
	public Color getColorModifier() {
		return this.ownerColorModifier;
	}
}
