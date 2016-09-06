package projectile;

import mechanic.Point;
import statuseffect.StatusFrostMei;
import unit.Unit;

public class MeiFrostProjectile extends Projectile {
	public static final float SPEED = 20;
	public MeiFrostProjectile(double damage, int direction, Point gridLoc, int teamID) {
		super(damage, SPEED, direction, gridLoc, "res/particle_genericBlue.png", teamID, true, true, true);
		this.setImageScale(4);
	}
	@Override
	public void onTargetHit(Unit target) {
		StatusFrostMei frost = new StatusFrostMei(target, 1);
		target.addStatusEffect(frost);
	}
}
