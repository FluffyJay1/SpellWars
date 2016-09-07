package spell;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;
import ui.Text;
import ui.TextFormat;
import unit.Unit;

public class Spell extends GameElement {
	boolean think;
	float thinkInterval;
	float thinkTimer;
	Unit owner;
	String name;
	String description;
	public boolean pauseWhenActivated;
	float textTimer;
	boolean activated;
	boolean spellExecuted;
	public float castTime;
	public float backswingTime;
	boolean pausedByStuns;
	boolean disableUnitControl;
	static final float TEXT_DURATION = 1.2f;
	static final float TEXT_Y_OFFSET = 200;
	Text spellText;
	public Spell(Unit owner, float castTime, float backswingTime, String name, String description, String imagepath, boolean pauseWhenActivated) {
		super();
		this.owner = owner;
		this.setImage(imagepath);
		this.name = name;
		this.description = description;
		this.pauseWhenActivated = pauseWhenActivated;
		this.textTimer = TEXT_DURATION; 
		this.activated = false;
		this.spellExecuted = false;
		this.think = false;
		this.thinkInterval = -1;
		this.thinkTimer = 0;
		this.castTime = castTime;
		this.backswingTime = backswingTime;
		this.pausedByStuns = false;
		this.disableUnitControl = false;
		this.spellText = new Text(null, Point.add(this.owner.getLoc(), new Point(-200, -215)), 400, 18, 30, 22, 30, Color.white, "", TextFormat.CENTER_JUSTIFIED);
	}
	public Spell(String name, float castTime, float backswingTime, String description, String imagepath, boolean pauseWhenActivated) {
		this(null, castTime, backswingTime, name, description, imagepath, pauseWhenActivated);
	}
	public Spell(Unit owner, float castTime, float backswingTime, String name, String description, String imagepath, boolean pauseWhenActivated, boolean pausedByStuns) {
		this(owner, castTime, backswingTime, name, description, imagepath, pauseWhenActivated);
		this.pausedByStuns = pausedByStuns;
	}
	@Override
	public void onSetMap() {
		this.spellText.setUI(this.getMap().getUI());
		this.spellText.setUseOutline(true);
		this.spellText.setElementToRemoveWith(this);
	}
	public void setOwner(Unit owner) {
		this.owner = owner;
	}
	public void activate() {
		this.activated = true;
		if(this.disableUnitControl) {
			this.owner.unitControl = false;
		}
		if(this.pauseWhenActivated) {
			this.getMap().pauseAll();
			this.setPause(false);
			this.getMap().getUI().addUIElement(this.spellText);
		}
		if(!this.pauseWhenActivated) {
			this.onActivate();
			this.spellExecuted = true;
		}
	}
	public void finishSpell() {
		if(this.disableUnitControl) {
			this.owner.unitControl = true;
		}
		if(this.pauseWhenActivated) {
			this.map.unpauseAll();
		}
		this.setRemove(true);
	}
	@Override
	public void update() {
		if(this.activated && this.textTimer <= 0 && !this.spellExecuted && this.pauseWhenActivated) {
			this.onActivate();
			this.spellExecuted = true;
		}
		if(this.spellExecuted) {
			if(!(this.owner.isStunned() && this.pausedByStuns)) {
				this.onSpellUpdate();
				if(this.think) {
					this.thinkTimer -= this.getFrameTime();
					if(this.thinkTimer <= 0) {
						this.onThink();
						this.thinkTimer += this.thinkInterval;
					}
				}
			}
		}
		if(this.activated && this.textTimer >= 0 && this.pauseWhenActivated) {
			this.textTimer -= this.getFrameTime();
		}
	}
	public void onSpellUpdate() {
		
	}
	public void onThink() {
		
	}
	public void setThinkInterval(float interval) {
		this.think = true;
		this.thinkTimer = interval;
		this.thinkInterval = interval;
	}
	public void onActivate() {
		
	}
	public void onStartCast() {
		
	}
	@Override
	public void draw(Graphics g) {
		if(this.activated && this.textTimer >= 0 && this.pauseWhenActivated){
			this.spellText.setText(this.name.toUpperCase());
			/*
			Point loc = new Point();
			if(this.owner != null) {
				loc = Point.add(owner.getLoc(), new Point(25*(0.5 - this.textTimer/TEXT_DURATION),-50));
			}
			g.setColor(Color.white);
			g.scale(2 - this.textTimer/TEXT_DURATION, 2 - this.textTimer/TEXT_DURATION);
			loc.scale(1/(2 - this.textTimer/TEXT_DURATION));
			loc.add(new Point(0, -150 * ((this.textTimer/TEXT_DURATION) - (this.textTimer/TEXT_DURATION) * (this.textTimer/TEXT_DURATION))));
			g.drawString(this.name, (float)loc.x - g.getFont().getWidth(this.name)/2, (float)loc.y);
			*/
			float ratio = this.textTimer/TEXT_DURATION;
			if(ratio > 0.8) {
				spellText.setLetterHeight((int)((1 - ratio) * 150));
				spellText.changeLoc(Point.add(this.owner.getLoc(), new Point(-200, -200 - (1 - ratio) * 75)));
			} else if(ratio < 0.2) {
				spellText.setLetterHeight((int)(ratio * 150));
				spellText.changeLoc(Point.add(this.owner.getLoc(), new Point(-200, -200 - ratio * 75)));
			} 
			/*
			Text text = new Text(this.getMap().getUI(), Point.add(this.owner.getLoc(), new Point(-200, -215)), 400, 18, 30, 22, 30, Color.white, this.name.toUpperCase(), TextFormat.CENTER_JUSTIFIED);;
			if(ratio > 0.8) {
				text.setLetterHeight((int)((1 - ratio) * 150));
				text.changeLoc(Point.add(this.owner.getLoc(), new Point(-200, -200 - (1 - ratio) * 75)));
			} else if(ratio < 0.2) {
				text.setLetterHeight((int)(ratio * 150));
				text.changeLoc(Point.add(this.owner.getLoc(), new Point(-200, -200 - ratio * 75)));
			} 
			text.setUseOutline(true);
			text.setRemoveNextFrame(true);
			this.getMap().getUI().addUIElement(text);
			*/
		} else {
			this.drawSpecialEffects(g);
		}
	}
	public void drawSpecialEffects(Graphics g) {
		
	}
	public String getName() {
		return this.name;
	}
	public String getDescription() {
		return this.description;
	}
}
