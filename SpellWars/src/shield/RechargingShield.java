package shield;

import org.newdawn.slick.Color;

import mechanic.GameMap;
import mechanic.Point;
import projectile.Projectile;
import unit.Unit;

public class RechargingShield extends Shield {
	float timer;
	float rechargePerSec;
	float damageCooldown;
	float damageCooldownTimer;
	public RechargingShield(Unit owner, int initialAmount, int finalAmount, float duration, float rechargePerSec, float damageCooldown) {
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
	}
	@Override
	public void onUpdate() {
		this.timer -= this.getFrameTime();
		if(this.timer <= 0) {
			this.isDead = true;
		}
		if(this.damageCooldownTimer > 0) {
			this.damageCooldownTimer -= this.getFrameTime();
			if(this.damageCooldownTimer <= 0) {
				this.onThink();
				this.setThinkInterval(1/rechargePerSec);
			}
		}
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
