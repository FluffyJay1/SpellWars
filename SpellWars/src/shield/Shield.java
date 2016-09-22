package shield;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;
import projectile.Projectile;
import states.StateGame;
import ui.Text;
import ui.TextFormat;
import unit.Unit;

public class Shield extends GameElement {
	private static final double HP_Y_OFFSET = -80;
	public static final double SHIELD_DISABLED_OPACITY = 0.2;
	private static final double SHIELD_NATURAL_OPACITY = 0.5;
	public static final double SHIELD_OPACITY_THRESHOLD = 0.01;
	private static final double SHIELD_HIT_OPACITY = 1;
	private static final double SHIELD_HIT_FLASH_DURATION = 0.15;
	private static final double SHIELD_DISABLE_FLASH_DURATION = 0.45;
	private static final double SHIELD_ENABLE_FLASH_DURATION = 0.5;
	private static final double SHIELD_DIE_FLASH_DURATION = 0.3;

	Unit owner;
	
	boolean think;
	public boolean blockDamage;
	public boolean removeOnKill;
	public boolean isDead;
	float thinkInterval;
	float thinkTimer;
	
	public double opacity;
	
	public Color HPTextColor;
	float drawOffset; //when layering multiple shields on top of each other
	boolean drawHP;
	Text hpText;
	
	public Shield(Unit owner, double hp, double maxhp, String imagePath) {
		super(hp, maxhp, 0, 0, owner.getLoc(), 1, 0, imagePath);
		this.owner = owner;
		this.think = false;
		this.thinkInterval = -1;
		this.thinkTimer = 0;
		this.owner.addShield(this);
		this.HPTextColor = new Color(0, 0, 255);
		this.drawHP = true;
		this.blockDamage = true;
		this.removeOnKill = true;
		this.opacity = SHIELD_DISABLED_OPACITY;
		this.isDead = false;
		this.hpText = new Text(null, Point.add(this.getLoc(), new Point(-200, HP_Y_OFFSET + this.drawOffset)), 400, 10, 16, 12, 20, Color.white, "", TextFormat.CENTER_JUSTIFIED);
		this.hpText.setUseOutline(true);
		this.hpText.setOutlineColor(this.HPTextColor);
		this.hpText.setElementToRemoveWith(this);
	}
	@Override
	public void onSetMap() {
		this.getMap().getUI().addUIElement(hpText);
	}
	public void setDrawOffset(float y) {
		this.drawOffset = y;
	}
	@Override
	public boolean doDamage(double damage){
		boolean isKillingBlow = this.changeHP(this.hp - damage);
		if(damage > 0) {
			this.onDamaged(damage);
			this.opacity = SHIELD_HIT_OPACITY;
		}
		if(isKillingBlow) {
			//this.opacity = SHIELD_DISABLED_OPACITY;
		}
		return isKillingBlow;
	}
	@Override
	public boolean changeHP(double hp) {
		if (hp <= 0) {
			this.hp = 0;
			if(this.remove == false) {
				if(this.removeOnKill) {
					this.isDead = true;
				}
				if(!this.isPaused()) {
					this.onDeath();
					/*
					if(this.removeOnKill) {
						this.setRemove(true);
					}
					*/
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
	@Override
	public void update() {
		if(this.isDead) {
			this.changeHP(0);
			this.opacity -= this.getFrameTime() * (SHIELD_HIT_OPACITY) / SHIELD_DIE_FLASH_DURATION;
			if(this.opacity <= 0) {
				this.setRemove(true);
			}
		} else {
			if(this.opacity - SHIELD_NATURAL_OPACITY > SHIELD_OPACITY_THRESHOLD) { //from hit opacity to normal opacity
				this.opacity -= this.getFrameTime() * (SHIELD_HIT_OPACITY - SHIELD_NATURAL_OPACITY) / SHIELD_HIT_FLASH_DURATION;
			}
			if(SHIELD_NATURAL_OPACITY - this.opacity > SHIELD_OPACITY_THRESHOLD && this.hp > 0) { //from disabled opacity to normal opacity
				this.opacity += this.getFrameTime() * (SHIELD_NATURAL_OPACITY - SHIELD_DISABLED_OPACITY) / SHIELD_ENABLE_FLASH_DURATION;
			}
			if(this.hp == 0 && this.opacity >= SHIELD_DISABLED_OPACITY) { //from whatever to disabled opacity
				this.opacity -= this.getFrameTime() * (SHIELD_HIT_OPACITY - SHIELD_DISABLED_OPACITY) / SHIELD_DISABLE_FLASH_DURATION;
			}
			if(this.think) {
				this.thinkTimer -= this.getFrameTime();
				while(this.thinkTimer <= 0) {
					this.onThink();
					this.thinkTimer += this.thinkInterval;
				}
			}
			this.onUpdate();
			if(this.owner.getRemove()) {
				this.setRemove(true);
			}
		}
	}
	public void onUpdate() {
		
	}
	@Override
	public void draw(Graphics g) {
		if(this.owner != null) {
			this.changeLoc(this.owner.getLoc());
		}
		if(this.getImage() != null){
			Image endPic = this.getImage().getFlippedCopy(this.owner.direction == GameMap.ID_LEFT, false);
			endPic = endPic.getScaledCopy((float) this.getSize());
			endPic.setAlpha((float) this.opacity);
			float width = endPic.getWidth();
			float height = endPic.getHeight();
			int hflipvflip = 0;
			if(this.owner.direction == GameMap.ID_LEFT) {
				hflipvflip += 2;
			}
			g.drawImage(endPic, (float) this.getLoc().x - width/2, (float) this.getLoc().y - height/2 - this.getDrawHeight() + this.drawOffset);
			if(StateGame.isServer)
			this.getMap().addToDrawInfo(GameMap.getDrawDataI(this.getImagePath(), this.getLoc().x -width/2, this.getLoc().y - height/2 - this.getDrawHeight() + this.drawOffset, width, height, 0, 255, 255, 255, 255 * (float)this.opacity, hflipvflip));
		}
		if(this.drawHP) {
			this.hpText.changeLoc(Point.add(this.getLoc(), new Point(-200, HP_Y_OFFSET + this.drawOffset)));
			this.hpText.setText("" + (int)this.getHP());
		}
	}
	public void onThink() {
		
	}
	public void onHitByProjectile(Projectile p) {
		
	}
	public void onOwnerDamaged(double damage) {
		
	}
	public void setThinkInterval(float interval) {
		this.think = true;
		this.thinkTimer = interval;
		this.thinkInterval = interval;
	}
}
