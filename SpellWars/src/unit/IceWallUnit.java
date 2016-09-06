package unit;

import mechanic.GameMap;
import mechanic.Point;

public class IceWallUnit extends Unit {
	public static final int HP = 200;
	public static final double SPEED = 1;
	public static final int DAMAGE_PER_TICK = 1;
	public IceWallUnit(Point gridLoc, int teamID, float duration) {
		super(HP, HP, SPEED, GameMap.getOppositeDirection((char)teamID), gridLoc, "res/shield/cryofreeze.png", teamID);
		this.setDrawHeight(32);
		this.setSize(0.75);
		this.setThinkInterval(duration);
		this.ignoreTeam = true;
	}
	@Override
	public void onThink() {
		this.changeHP(0);
	}
}
