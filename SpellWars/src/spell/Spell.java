package spell;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;
import statuseffect.StatusCorrupt;
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
	public boolean isFinished;
	float textTimer;
	boolean activated;
	boolean spellExecuted;
	public float castTime;
	public float backswingTime;
	boolean pausedByStuns;
	boolean disableUnitControl;
	boolean castPause; //different from map pause, this one is usually controlled by spells
	
	boolean fieldEffect;
	String fieldEffectName;
	
	static final float TEXT_DURATION = 1.2f;
	static final float TEXT_Y_OFFSET = 200;
	Text spellText;
	
	public double sortWeight;
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
		this.isFinished = false;
		this.castPause = false;
		this.fieldEffect = false;
		this.sortWeight = 0;
		this.spellText = new Text(null, Point.add(this.owner.getLoc(), new Point(-200, -215)), 400, 18, 30, 22, 30, Color.white, "", TextFormat.CENTER_JUSTIFIED);
	}
	/*
	public Spell(String name, float castTime, float backswingTime, String description, String imagepath, boolean pauseWhenActivated) {
		this(null, castTime, backswingTime, name, description, imagepath, pauseWhenActivated);
	}*/
	public Spell(Unit owner, float castTime, float backswingTime, String name, String description, String imagepath, boolean pauseWhenActivated, boolean pausedByStuns) {
		this(owner, castTime, backswingTime, name, description, imagepath, pauseWhenActivated);
		this.pausedByStuns = pausedByStuns;
	}
	@Override
	public void onSetMap() {
		this.spellText.setUI(this.getMap().getUI());
		this.spellText.setUseOutline(true);
		this.spellText.setElementToRemoveWith(this);
		this.onSpellSetMap();
	}
	public void onSpellSetMap() {
		
	}
	public void setOwner(Unit owner) {
		this.owner = owner;
	}
	public Unit getOwner(){
		return this.owner;
	}
	public void registerFieldEffect(String effect) {
		this.fieldEffect = !effect.equals("");
		this.fieldEffectName = effect;
	}
	public String getFieldEffectName() {
		return this.fieldEffectName;
	}
	public void activate() {
		if(this.fieldEffect) {
			Text queueText = new Text(this.getMap().getUI(), Point.add(this.owner.getLoc(), new Point(-200, -215)), 400, 14, 22, 18, 30, Color.white, this.name.toUpperCase() + " has been placed in the field effect queue.", TextFormat.CENTER_JUSTIFIED);
			queueText.setDuration(2);
			queueText.setUseOutline(true);
			queueText.setOutlineColor(new Color(90, 90, 90));
			if(this.getMap().getActiveFieldEffect() == null || !this.getMap().getActiveFieldEffect().equals(this)) {
				this.getMap().addFieldEffect(this);
				this.getMap().getUI().addUIElement(queueText);
			}
			if(this.getMap().getActiveFieldEffect().equals(this)) {
				queueText.setText("");
				queueText.setRemove(true);
				this.startSpell();
			}
		} else {
			this.startSpell();
		}
	}
	private void startSpell() {
		this.activated = true;
		if(this.disableUnitControl) {
			this.owner.unitControl++;
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
		this.isFinished = true;
		if(this.disableUnitControl) {
			this.owner.unitControl--;
		}
		if(this.pauseWhenActivated) {
			this.map.unpauseAll();
		}
		this.setRemove(true);
	}
	public void corrupt() {
		if(this.owner.getStatusEffectCount("corrupt") < 3) {
			this.owner.addStatusEffect(new StatusCorrupt(this.owner));
		}
	}
	@Override
	public void update() {
		if(this.activated && this.textTimer <= 0 && !this.spellExecuted && this.pauseWhenActivated) {
			this.onActivate();
			this.spellExecuted = true;
		}
		if(this.spellExecuted && !this.castPause) {
			if(!(this.owner.isStunned() && this.pausedByStuns)) {
				this.onSpellUpdate();
				if(this.think) {
					this.thinkTimer -= this.getFrameTime();
					while(this.thinkTimer <= 0 && !this.isFinished) {
						this.onThink();
						this.thinkTimer += this.thinkInterval;
					}
				}
			}
		}
		if(this.activated && this.pauseWhenActivated) {
			if(this.textTimer >= 0) {
				this.textTimer -= this.getFrameTime();
			} else {
				this.spellText.setRemove(true);
			}
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
			} else {
				spellText.setLetterHeight(30);
				spellText.changeLoc(Point.add(this.owner.getLoc(), new Point(-200, -215)));
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
		} else if(this.activated) {
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
	public void castPause() {
		this.castPause = true;
	}
	public void castUnpause() {
		this.castPause = false;
	}
}
