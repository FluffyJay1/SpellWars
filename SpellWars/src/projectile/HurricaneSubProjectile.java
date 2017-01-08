package projectile;

import mechanic.GameMap;
import mechanic.Point;
import unit.Unit;

public class HurricaneSubProjectile extends Projectile {
	public static final float STUN_DURATION = 0.4f;
	public HurricaneSubProjectile(double damage, double speed, int direction, Point gridLoc, int teamID) {
		super(damage, speed, direction, gridLoc, "res/projectile/hurricane.png", teamID, false, true, true);
		this.setDrawHeight(32);
	}
	@Override
	public void onTargetHit(Unit target) {
		target.move(GameMap.getOppositeDirection((char)this.direction), false, false, false, false, true, true);
		target.stun(STUN_DURATION);
	}
}
