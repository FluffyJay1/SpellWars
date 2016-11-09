package projectile;

import mechanic.Point;

public class ShotgunBlastProjectile extends Projectile {
	double damageDecrease;
	public ShotgunBlastProjectile(double damage, double speed, Point moveVec, Point gridLoc, int teamID, double damageDecrease) {
		super(damage + damageDecrease, speed, moveVec, gridLoc, "res/particle_genericYellow.png", teamID, true, true, true);
		this.setDrawHeight(45);
		this.drawShadow = true;
		this.setImageScale(3f);
		this.setFlashPanel(false);
		this.damageDecrease = damageDecrease;
	}
	@Override
	public void onThink() {
		this.changeDamage(-damageDecrease);
		if(this.getDamage() <= 0) {
			this.setRemove(true);
		}
	}
}
