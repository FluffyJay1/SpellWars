package projectile;

import mechanic.GameMap;
import mechanic.Point;
import unit.Unit;

public class HurricaneProjectile extends Projectile {
	public HurricaneProjectile(double damage, double speed, int direction, Point gridLoc, int teamID) {
		super(damage, speed, direction, gridLoc, "res/projectile/hurricane.png", teamID, false, true, true);
		this.setDrawHeight(32);
	}
	@Override
	public void onThink() {
		this.getMap().addGameElement(new HurricaneSubProjectile(this.damage, this.getSpeed(), GameMap.ID_DOWN, GameMap.getFuturePoint(this.getGridLoc(), GameMap.ID_DOWN), this.teamID));
		this.getMap().addGameElement(new HurricaneSubProjectile(this.damage, this.getSpeed(), GameMap.ID_UP, GameMap.getFuturePoint(this.getGridLoc(), GameMap.ID_UP), this.teamID));
	}
}
