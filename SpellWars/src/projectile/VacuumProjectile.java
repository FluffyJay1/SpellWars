package projectile;

import mechanic.GameMap;
import mechanic.Point;
import unit.Unit;

public class VacuumProjectile extends Projectile {
	public static final float STUN_DURATION = 0.4f;
	public VacuumProjectile(int damage, double speed, int direction, Point gridLoc, int teamID) {
		super(damage, speed, direction, gridLoc, "res/projectile/vacuum.png", teamID, false, true, true);
		this.setDrawHeight(32);
	}
	@Override
	public void onTargetHit(Unit target) {
		target.move(GameMap.getOppositeDirection((char)this.direction), false, false, false, false, true, true);
		target.stun(STUN_DURATION);
	}
}
