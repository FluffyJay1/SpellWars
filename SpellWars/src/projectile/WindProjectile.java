package projectile;

import mechanic.Point;
import unit.Unit;

public class WindProjectile extends Projectile {
	public static final float STUN_DURATION = 0.4f;
	public WindProjectile(int damage, double speed, int direction, Point gridLoc, int teamID) {
		super(damage, speed, direction, gridLoc, "res/projectile/wind.png", teamID, true, true, true);
		this.setDrawHeight(32);
	}
	@Override
	public void onTargetHit(Unit target) {
		target.move(this.direction, false, false, false, false, true);
		target.stun(STUN_DURATION);
		target.interruptCast();
	}
}
