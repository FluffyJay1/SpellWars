package shield;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import mechanic.Animation;
import mechanic.GameMap;
import mechanic.Point;
import projectile.Projectile;
import unit.Unit;

public class RechargingShield extends Shield {
	float timer;
	float rechargePerSec;
	float damageCooldown;
	float damageCooldownTimer;
	Animation rechargeAnimation;
	public RechargingShield(Unit owner, double initialAmount, double finalAmount, float duration, float rechargePerSec, float damageCooldown) {
		super(owner, initialAmount, finalAmount, "res/shield/rechargingshield.png");
		this.drawHP = true;
		this.setDrawHeight(owner.getDrawHeight());
		this.timer = duration;
		this.HPTextColor = new Color(0, 120, 255);
		this.rechargePerSec = rechargePerSec;
		if(this.rechargePerSec > 0) {
			this.setThinkInterval(1/rechargePerSec);
		}
		this.damageCooldownTimer = 0;
		this.damageCooldown = damageCooldown;
		this.removeOnKill = false;
		this.rechargeAnimation = new Animation("res/shield/rechargingshield_animation.png", 9, 1, 192, 384, 0.5f, false, false);
		this.rechargeAnimation.changeLoc(Point.add(this.getLoc(), new Point(0, -this.owner.getDrawHeight())));
	}
	@Override
	public void onUpdate() {
		this.rechargeAnimation.update((float)this.getFrameTime());
		this.rechargeAnimation.changeLoc(Point.add(this.getLoc(), new Point(0, -this.owner.getDrawHeight())));
		this.timer -= this.getFrameTime();
		if(this.timer <= 0) {
			this.isDead = true;
		}
		if(this.damageCooldownTimer > 0) {
			this.damageCooldownTimer -= this.getFrameTime();
			if(this.damageCooldownTimer <= 0) {
				this.rechargeAnimation.resetTimer();
				this.onThink();
				this.setThinkInterval(1/rechargePerSec);
			}
		}
	}
	@Override
	public void drawSpecialEffects(Graphics g) {
		this.rechargeAnimation.draw(g, this.getMap());
	}
	@Override
	public void onThink() {
		if(this.damageCooldownTimer <= 0) {
			this.doDamage(-1);
		}
	}
	@Override
	public void onDamaged(double damage) {
		this.damageCooldownTimer = this.damageCooldown;
	}
	@Override
	public void onOwnerDamaged(double damage) {
		this.damageCooldownTimer = this.damageCooldown;
	}
}
