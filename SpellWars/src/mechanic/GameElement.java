
package mechanic;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Triangulator;

import projectile.Projectile;
import states.StateGame;
import statuseffect.StackingProperty;
import statuseffect.StatusEffect;
import ui.Text;
import ui.TextFormat;

public abstract class GameElement {

	public static final float HEALTH_BAR_WIDTH = 80;
	public static final float HEALTH_BAR_OFFSET = 30;
	public static final int STATUS_EFFECT_ICON_SPACING = 20;
	public GameMap map;
	private int level;
	private int maxLevel;
	public double hp;
	public double maxHP;
	private double speed;
	
	public float finalSpeedModifier;
	public float finalDamageModifier;
	
	private double orientation; // in degrees
	private Point loc;
	private float drawHeight;
	private double size; // as a ratio (scale thing)
	private float collisionRadius; // kinda like a hitbox, mainly for stuff like projectiles
	
	private ArrayList<StatusEffect> statuseffects = new ArrayList<StatusEffect>();
	public boolean statusFinalModifierValueUpdate; //whether or not the final modifier values need to be updated
	private ArrayList<StatusEffect> statuseffectsremovebuffer = new ArrayList<StatusEffect>();
	/*
	//I don't think we need these in each game element, since these should only belong to the towers/projectiles
	private double dmg;
	private DamageType type;
	private double reload; // in frames
	private double aoe; // in pixels
	private double range;
	private GameElement target;
	*/
	private Image pic;
	private String imagepath;
	protected boolean remove;
	protected boolean disconnected;
	private float frametime;
	boolean isPaused;
	GameElement parent;
	public boolean hasBeenDrawn; //IF THE ELEMENT IS A UNIT AND IT HAS BEEN DRAWN SINCE IT WAS ON A LOWER GRID LOC
	
	private Color drawColorMultiplier;
	
	public GameElement() {
		this(new Point());
	}
	
	public GameElement(Point loc) {
		this(1, 1, 0, 0, loc, 1, 0, "res/blank.png");
	}
	
	/* INCOMPLETE */ public GameElement(double hp, double maxHP, double speed, double orientation, Point loc, double size, float collisionRadius, String imagePath) {
		this.changeMaxHP(maxHP);
		this.changeHP(hp);
		this.changeSpeed(speed);
		this.changeOrientation(orientation);
		this.changeLoc(loc);
		this.size = size;
		this.setImage(imagePath);
		this.remove = false;
		this.finalSpeedModifier = 1;
		this.finalDamageModifier = 1;
		this.frametime = 0;
		this.drawHeight = 0;
		this.hasBeenDrawn = false;
		this.drawColorMultiplier = Color.white;
		this.disconnected = false;
	}
	/*
	public GameElement(double hp, double maxHP, double speed, double orientation, Point loc, double size, float collisionRadius, Image image) {
		this.changeMaxHP(maxHP);
		this.changeHP(hp);
		this.changeSpeed(speed);
		this.changeOrientation(orientation);
		this.changeLoc(loc);
		this.size = size;
		this.pic = image;
		this.remove = false;
		this.finalSpeedModifier = 1;
		this.finalDamageModifier = 1;
		this.frametime = 0;
		this.drawHeight = 0;
		this.hasBeenDrawn = false;
		this.drawColorMultiplier = Color.white;
		this.disconnected = false;
	}
	*/
	
	/**
	 * Access instance variable hp
	 * 
	 * @return the value of instance variable hp
	 */
	public double getHP() {
		return this.hp;
	}
	
	/**
	 * Modify instance variable hp
	 * 
	 * If hp < 0, sets hp to 0 and flags it to be removed next frame,
	 * while also calling the "onDeath" function and returning true,
	 * if it is the killing blow
	 * 
	 * If hp > maxHP, sets hp to maxHP
	 * 
	 * @param hp	The new value of hp
	 * @return Whether or not that killed the element
	 */
	public boolean changeHP(double hp) {
		if (hp <= 0) {
			this.hp = 0;
			if(this.remove == false) {
				if(!this.isPaused && !(this instanceof Projectile)) {
					this.onDeath();
					this.setRemove(true);
				}
				return true;
			}
			return false;
		} else if (hp > this.maxHP) {
			this.hp = this.maxHP;
			return false;
		} else {
			this.hp = hp;
			return false;
		}
	}
	
	/**
	 * Access instance variable maxHP
	 * 
	 * @return	The value of maxHP
	 */
	public double getMaxHP() {
		return this.maxHP;
	}
	
	/**
	 * Modify instance variable maxHP
	 * 
	 * Also calls changeHP to maintain same health percentage
	 * 
	 * @param maxHP
	 */
	public void changeMaxHP(double maxHP) {
		double ratio = this.hp / this.maxHP;
		
		this.maxHP = maxHP;
		
		this.changeHP(this.hp * ratio);
	}
	
	/**
	 * Access instance variable speed
	 * 
	 * @return	The value of speed
	 */
	public double getSpeed() {
		return this.speed;
	}
	public double getFinalSpeed() {
		return this.speed * this.finalSpeedModifier;
	}
	
	/**
	 * Modify instance variable speed
	 * 
	 * If speed is negative, speed will be set as 0
	 * 
	 * @param speed	The new value of speed
	 */
	public void changeSpeed(double speed) {
		if (speed < 0) { // Speed cannot be negative!
			this.speed = 0;
		} else {
			this.speed = speed;
		}
	}
	
	/**
	 * Access instance variable orientation
	 * 
	 * @return	The value of orientation (in degrees)
	 */
	public double getOrientation() {
		return this.orientation;
	}
	
	/**
	 * Modify instance variable orientation
	 * 
	 * If orientation is greater than 360 or less than 0, orientation will be converted to equivalent value between 0 and 360 before being set
	 * 
	 * @param orientation	The new value of orientation (in degrees)
	 */
	public void changeOrientation(double orientation) {
		while (orientation > 360) {
			orientation -= 360;
		}
		while (orientation < 0) {
			orientation += 360;
		}
		
		this.orientation = orientation;
	}
	
	/**
	 * Access instance variable loc
	 * 
	 * @return	The location of the GameElement
	 */
	public Point getLoc() {
		return this.loc;
	}
	
	/**
	 * Modify instance variable loc
	 * 
	 * @param loc	The new location of the GameElement
	 */
	public void changeLoc(Point loc) {
		this.loc = loc;
	}
	/**
	 * Changes the loc without changing the pointer
	 * 
	 * @param loc The new location of the GameElement
	 */
	public void setLoc(Point loc) {
		this.loc.x = loc.x;
		this.loc.y = loc.y;
	}
	

	/*
	 * Called when hp < 0
	 */
	public void onDeath() {
		
	}
	public void onSetMap() {
		
	}
	public void move() {
		
	}
	/*
	 * Moves towards the point based on its speed
	 * Notes: MAP COORDINATES
	 */
	public void moveTowardsPoint(Point loc) {
		Point moveVector = Point.getVector(this.getLoc(), loc);
		if(Point.equals(moveVector, new Point()) == false) //if it is nonzero
		{
			moveVector.normalize();
			moveVector.scale(this.getSpeed() * this.finalSpeedModifier * this.getFrameTime());
			this.getLoc().add(moveVector);
		}
	}
	/*
	 * If it could reach the point within one frame of motion, it will return true
	 * Notes: MAP COORDINATES
	 */
	public boolean isReasonableDistanceAwayFrom(Point loc) {
		return Point.getDistance(this.getLoc(), loc) < this.getSpeed() * this.finalSpeedModifier * this.getFrameTime();
	}
	public boolean isReasonableDistanceAwayFromColliding(Point loc) {
		return Point.getDistance(this.getLoc(), loc) - this.getCollisionRadius() < this.getSpeed() * this.finalSpeedModifier * this.getFrameTime();
	}
	public boolean isReasonableDistanceAwayFromColliding(Point loc, float radius) {
		return Point.getDistance(this.getLoc(), loc) - this.getCollisionRadius() - radius < this.getSpeed() * this.finalSpeedModifier * this.getFrameTime();
	}
	public boolean isReasonableDistanceAwayFromColliding(GameElement e) {
		return Point.getDistance(this.getLoc(), e.getLoc()) - this.getCollisionRadius() - e.getCollisionRadius() < this.getSpeed() * this.finalSpeedModifier * this.getFrameTime();
	}
	
	public Image getImage(){
		return this.pic;
	}
	
	public double getX(){
		return this.loc.x;
	}
	
	public double getY(){
		return this.loc.y;
	}
	
	public void update(){
		
	}
	
	public void setImage(String path){
		if(Game.images.containsKey(path)) {
			this.pic = Game.images.get(path).copy();
		} else {
			try {
				this.pic = new Image(path);
				Game.images.put(path, this.pic.copy());
			} catch (SlickException e) {
				System.out.println("Unable to load: " + path);
				e.printStackTrace();
			} finally {
				System.out.println("loaded into memory: " + path);
			}
		}
		this.imagepath = path;
	}
	public String getImagePath() {
		return this.imagepath;
	}
	/*
	public void setImage(Image image) {
		this.pic = image;
	}
	*/
	
	public boolean getRemove(){
		return remove;
	}
	
	public void setRemove(boolean state){
		remove = state;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setSize(double size) {
		this.size = size;
	}
	
	public double getSize() {
		return this.size;
	}
	/**
	 * Deals damage to a GameElement
	 * 
	 * @param damage The damage to deal
	 * @param damageType The type of damage to deal
	 * @return Whether or not that was the killing blow
	 */
	public boolean doDamage(double damage){
		boolean isKillingBlow = this.changeHP(this.hp - damage);
		if(damage > 0) {
			this.onDamaged(damage);
		}
		return isKillingBlow;
	}
	/**
	 * Called when damage is dealt
	 * 
	 * @param damage The damage it would deal
	 * @param type The type of damage
	 */
	public void onDamaged(double damage) {
		
	}
	public void setPause(boolean pause) {
		this.isPaused = pause;
	}
	public boolean isPaused() {
		return this.isPaused;
	}
	public void setDrawHeight(float height) {
		this.drawHeight = height;
	}
	public void draw(Graphics g){
		if(this.getImage() != null){
			Image endPic = this.pic.getScaledCopy((float) size);
			endPic.rotate(-(float)orientation);
			float width = endPic.getWidth();
			float height = endPic.getHeight();
			g.drawImage(endPic, (float) this.loc.x - width/2, (float) this.loc.y - height/2 - this.getDrawHeight(), this.drawColorMultiplier);
			if(StateGame.isServer)
			this.getMap().addToDrawInfo(GameMap.getDrawDataI(this.imagepath, this.loc.x-width/2, this.loc.y-height/2, width, height, -orientation, this.drawColorMultiplier.getRedByte(), this.drawColorMultiplier.getGreenByte(), this.drawColorMultiplier.getBlueByte(), this.drawColorMultiplier.getAlphaByte(), 0));
			this.resetDrawColorModifier();
		}
	}
	public Color getDrawColorModifier() {
		return this.drawColorMultiplier;
	}
	public void resetDrawColorModifier() {
		this.drawColorMultiplier = Color.white;
	}
	public void drawShadow(Graphics g) {
		
	}
	public void drawStatusIcons(Graphics g) {
		Point drawLoc = Point.add(this.getLoc(), new Point(-HEALTH_BAR_WIDTH/2, -HEALTH_BAR_OFFSET - 16 - this.drawHeight));
		int iconsPerRow = (int)(HEALTH_BAR_WIDTH / STATUS_EFFECT_ICON_SPACING);
		int iconNum = 0;
		ArrayList<String> alreadyDrawnStatusTypes = new ArrayList<String>(); //ONLY FOR STACKABLES
		for(StatusEffect s : this.getStatusEffects()) {
			boolean isAlreadyDrawn = false;
			for(int i = 0; i < alreadyDrawnStatusTypes.size(); i++) {
				if(s.getStatusType().equals(alreadyDrawnStatusTypes.get(i))) {
					isAlreadyDrawn = true;
					break;
				}
			}
			if(!isAlreadyDrawn) {
				alreadyDrawnStatusTypes.add(s.getStatusType());
			}
			if(s.getDrawIcon() && s.hasIcon() && s.getRemove() == false && 
					(((s.getStackingProperty() == StackingProperty.STACKABLE_REFRESH_DURATION || s.getStackingProperty() == StackingProperty.STACKABLE_INDEPENDENT) && !isAlreadyDrawn)
							||(s.getStackingProperty() != StackingProperty.STACKABLE_REFRESH_DURATION && s.getStackingProperty() != StackingProperty.STACKABLE_INDEPENDENT))) { //if it is stackable and it hasn't been drawn before, or if it isn't stackable
				if(iconNum >= iconsPerRow){
					iconNum = 0;
					drawLoc.add(new Point(-HEALTH_BAR_WIDTH, -STATUS_EFFECT_ICON_SPACING));
				}
				
				s.drawIcon(g, drawLoc);
				if(s.getStackingProperty() == StackingProperty.STACKABLE_REFRESH_DURATION) {
					int numStacks = this.getStatusEffectCount(s.getStatusType());
					String text = "" + numStacks;
					Text t = new Text(this.getMap().getUI(), Point.add(drawLoc, new Point(-StatusEffect.ICON_SIDE_LENGTH/8, StatusEffect.ICON_SIDE_LENGTH/3)), StatusEffect.ICON_SIDE_LENGTH, 6, 8, 6, 8,  Color.white, text, TextFormat.RIGHT_JUSTIFIED);
					t.setUseOutline(true);
					t.setOutlineColor(Color.black);
					this.getMap().getUI().addUIElement(t);
					t.setRemoveNextFrame(true);
				}
				if(s.getStackingProperty() == StackingProperty.STACKABLE_INDEPENDENT) {
					int numStacks = this.getStatusEffectCount(s.getStatusType());
					String text = "" + numStacks;
					Text t = new Text(this.getMap().getUI(), Point.add(drawLoc, new Point(-StatusEffect.ICON_SIDE_LENGTH/8, StatusEffect.ICON_SIDE_LENGTH/3)), StatusEffect.ICON_SIDE_LENGTH, 6, 8, 6, 8,  Color.white, text, TextFormat.RIGHT_JUSTIFIED);
					t.setUseOutline(true);
					t.setOutlineColor(Color.black);
					this.getMap().getUI().addUIElement(t);
					t.setRemoveNextFrame(true);
				}
				drawLoc.add(new Point(STATUS_EFFECT_ICON_SPACING, 0));
				iconNum++;
			}
			if(s.getRemove() == false && 
					(((s.getStackingProperty() == StackingProperty.STACKABLE_REFRESH_DURATION || s.getStackingProperty() == StackingProperty.STACKABLE_INDEPENDENT) && !isAlreadyDrawn)
							||(s.getStackingProperty() != StackingProperty.STACKABLE_REFRESH_DURATION && s.getStackingProperty() != StackingProperty.STACKABLE_INDEPENDENT))) { //if it is stackable and it hasn't been drawn before, or if it isn't stackable
				this.drawColorMultiplier = this.drawColorMultiplier.multiply(s.getColorModifier());
			}
		}
	}
	public double getFrameTime() {
		return frametime;
	}
	public void passFrameTime(float frametime) {
		this.frametime = frametime;
	}
	public GameMap getMap() {
		return this.map;
	}
	public void setMap(GameMap map) {
		if(this.map != map) {
			if(map != null) {
				this.map = map;
				this.onSetMap();
			} else {
				this.disconnected = true;
			}
		}
	}
	public boolean isDisconnected() {
		return this.disconnected;
	}
	public float getCollisionRadius() {
		return this.collisionRadius;
	}
	public void setCollisionRadius(float radius) {
		if(radius < 0) {
			this.collisionRadius = 0;
			System.out.println("ERROR: Attempted to set a negative collision radius!"); //TODO: Remove debug
		} else {
			this.collisionRadius = radius;
		}
		
	}
	public GameElement getParent() {
		return this.parent;
	}
	public void setParent(GameElement parent) {
		this.parent = parent;
		this.loc = Point.add(parent.getLoc(), new Point(0, -parent.drawHeight));
	}
	public void updateRegardsToParent() {
		if(this.getParent() != null) {
			if(this.getParent().getRemove()) {
				this.setRemove(true);
			} else {
				this.loc = Point.add(parent.getLoc(), new Point(0, -parent.drawHeight));
			}
		}
	}
	//STATUS EFFECTS///////////////////////////////////////////////////////////////////
	public ArrayList<StatusEffect> getStatusEffects() {
		ArrayList<StatusEffect> effects = new ArrayList<StatusEffect>();
		effects.addAll(this.statuseffects);
		effects.removeAll(this.statuseffectsremovebuffer);
		return effects;
	}
	public void addStatusEffect(StatusEffect effect) { //adds the status effect from the parameter, handles stacking, SHOULD ALWAYS USE
		effect.setOwner(this);
		/*
		 * UNSTACKABLE REFRESH DURATION:
		 * Doesn't stack, whenever a new stack is added and there's an existing stack,
		 * it simply sets the old stack to the duration of the new one
		 */
		if(effect.getStackingProperty() == StackingProperty.UNSTACKABLE_REFRESH_DURATION) {
			//boolean foundExistingStack = false;
			for(StatusEffect e : this.statuseffects) { //goes through all status effects
				if(e.getStatusType() == effect.getStatusType()) { //if it finds an existing status effect with the same type
					if(e.getLevel() > effect.getLevel()) {
						effect.setMute(true);
						effect.setDrawIcon(false);
					} else if (e.getLevel() == effect.getLevel()) {
						e.setDuration(e.getMaxDuration()); //resets the duration to the new one
						effect.setMute(true);
						effect.setDrawIcon(false);
					} else {
						e.setMute(true);
						e.setDrawIcon(false);
					}
					//foundExistingStack = true;
				}
			}
			this.statuseffects.add(effect); //adds the effect if there isn't already one
			this.statusFinalModifierValueUpdate = true; //flags for concantonating all the bonuses
		}
		/*
		 * UNSTACKABLE REPLACE
		 * Doesn't stack, whenever a new stack is added, the older ones get removed
		 */
		if(effect.getStackingProperty() == StackingProperty.UNSTACKABLE_REPLACE) {
			for(StatusEffect e : this.statuseffects) { //goes through all status effects
				if(e.getStatusType() == effect.getStatusType()) { //if it finds an existing status effect with the same type
					if(e.getLevel() > effect.getLevel()) {
						effect.setMute(true);
						effect.setDrawIcon(false);
					} else if (e.getLevel() == effect.getLevel()) {
						e.setRemove(true);
					} else {
						e.setMute(true);
						e.setDrawIcon(false);
					}
				}
			}
			this.statuseffects.add(effect); //adds the effect in the end
			this.statusFinalModifierValueUpdate = true; //flags for concantonating all the bonuses
		}
		/*
		 * STACKABLE REFRESH DURATION
		 * Stacks, whenever a new stack is added, all the old stacks get their durations set to the new one
		 */
		if(effect.getStackingProperty() == StackingProperty.STACKABLE_REFRESH_DURATION) {
			for(StatusEffect e : this.statuseffects) { //goes through all status effects
				if(e.getStatusType() == effect.getStatusType()) { //if it finds an existing status effect with the same type
					e.setDuration(effect.getDuration()); //sets the duration of the old one to the new one
				}
			}
			this.statuseffects.add(effect); //adds the effect in the end
			this.statusFinalModifierValueUpdate = true; //flags for concantonating all the bonuses
		}
		/*
		 * STACKABLE INDEPENDENT
		 * Stacks, each stack is completely independent
		 */
		if(effect.getStackingProperty() == StackingProperty.STACKABLE_INDEPENDENT) {
			this.statuseffects.add(effect); //adds the effect in the end
			this.statusFinalModifierValueUpdate = true; //flags for concantonating all the bonuses
		}
	}
	public void removeStatusEffect(String id) {
		for(StatusEffect e : this.statuseffects) { //goes through all status effects
			if(e.getStatusType() == id) { //if it finds an existing status effect with the same type
				e.setRemove(true);
			}
		}
	}
	public boolean hasStatusEffect(String id) {
		for(StatusEffect e : this.statuseffects) { //goes through all status effects
			if(e.getStatusType() == id && !e.getRemove()) { //if it finds an existing status effect with the same type
				return true;
			}
		}
		return false;
	}
	public boolean hasStatusEffect(String id, int level) {
		for(StatusEffect e : this.statuseffects) { //goes through all status effects
			if(e.getStatusType() == id && !e.getRemove() && e.getLevel() == level) { //if it finds an existing status effect with the same type
				return true;
			}
		}
		return false;
	}
	public StatusEffect getFirstStatusEffect(String id) {
		for(StatusEffect e : this.statuseffects) { //goes through all status effects
			if(e.getStatusType() == id && !e.getRemove()) { //if it finds an existing status effect with the same type
				return e;
			}
		}
		return null;
	}
	public StatusEffect getFirstStatusEffect(String id, int level) {
		for(StatusEffect e : this.statuseffects) { //goes through all status effects
			if(e.getStatusType() == id && !e.getRemove() && e.getLevel() == level) { //if it finds an existing status effect with the same type
				return e;
			}
		}
		return null;
	}
	public ArrayList<StatusEffect> getStatusEffects(String id) {
		ArrayList<StatusEffect> effects = new ArrayList<StatusEffect>();
		for(StatusEffect e : this.statuseffects) { //goes through all status effects
			if(e.getStatusType() == id && !e.getRemove()) { //if it finds an existing status effect with the same type
				effects.add(e);
			}
		}
		return effects;
	}
	public int getStatusEffectCount(String type) {
		int count = 0;
		for(StatusEffect e : this.statuseffects) { //goes through all status effects
			if(e.getStatusType() == type && !e.getRemove()) { //if it finds an existing status effect with the same type
				count++;
			}
		}
		return count;
	}
	public void updateFinalModifiers() {//updates the final speed and damage and resistance modifiers
		float sModifier = 1;
		float aModifier = 1;
		for(StatusEffect e : this.statuseffects) { //Concantonates all the bonuses into one value
			sModifier *= e.getMoveSpeedModifier();
			aModifier *= e.getAttackDamageModifier();
		}
		this.finalSpeedModifier = sModifier;
		this.finalDamageModifier = aModifier;
	}
	public void updateStatusEffects() { //each status effect runs its own code
		for(StatusEffect e : this.statuseffects) {
			e.update(this.frametime); //update
			if(e.getRemove()) {
				this.statuseffectsremovebuffer.add(e); //if it needs to be removed, it gets added to a buffer
				if(e.getStackingProperty() == StackingProperty.UNSTACKABLE_REFRESH_DURATION //Unmute the next highest level effect
						|| e.getStackingProperty() == StackingProperty.UNSTACKABLE_REPLACE) {
					ArrayList<StatusEffect> effects = new ArrayList<StatusEffect>();
					effects = this.getStatusEffects(e.getStatusType());
					
					int highestLevel = -1;
					int highestLevelIndex = -1;
					for(int i = 0; i < effects.size(); i++) { //Find the index of the highest level ability
						if(effects.get(i).getLevel() > highestLevel) {
							highestLevel = effects.get(i).getLevel();
							highestLevelIndex = i;
						} else if(effects.get(i).getLevel() == highestLevel) {
							effects.get(i).setRemove(true);
						}
					}
					if(highestLevel > -1 && highestLevelIndex > -1) {
						effects.get(highestLevelIndex).setMute(false);
						effects.get(highestLevelIndex).setDrawIcon(true);
					}
				}
				this.statusFinalModifierValueUpdate = true;
			}
		}
		this.statuseffects.removeAll(statuseffectsremovebuffer); //all the aformentioned objects then get removed all at once
		if(this.statusFinalModifierValueUpdate) { //if it needs to be updated
			this.updateFinalModifiers();
			this.statusFinalModifierValueUpdate = false;
		}
	}
	//END OF STATUS EFFECTS/////////////////////////////////////////
	
	//LEVELING////////
	public int getLevel() {
		return this.level;
	}
	public int getMaxLevel() {
		return this.maxLevel;
	}
	public void setLevel(int level) {
		if(level < 1) {
			this.level = 1;
		} else if (level > maxLevel) {
			this.level = maxLevel;
		} else {
			this.level = level;
		}
	}
	public void setMaxLevel(int maxLevel) {
		if(maxLevel < 1) {
			this.maxLevel = 1;
		} else {
			this.maxLevel = maxLevel;
		}
	}
	public void levelUp() {
		this.setLevel(this.level + 1);
		this.onLevelUp();
	}
	public void onLevelUp() {
		
	}
	//END OF LEVELING///

	public float getDrawHeight() {
		return drawHeight;
	}
}
