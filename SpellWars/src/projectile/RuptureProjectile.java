package projectile;

import mechanic.Point;
import statuseffect.StatusRupture;
import unit.Unit;

public class RuptureProjectile extends Projectile {
	float duration;
	public RuptureProjectile(double damage, float duration, double speed, int direction, Point gridLoc, int teamID) {
		super(damage, speed, direction, gridLoc, "res/particle_blood.png", teamID, true, true, false);
		this.duration = duration;
		this.setDrawHeight(40);
		this.imageScale = 8;
	}
	@Override
	public void onTargetHit(Unit target) {
		StatusRupture s = new StatusRupture(target, this.duration);
		target.addStatusEffect(s);
	}

}
