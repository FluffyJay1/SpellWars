package unit;

import mechanic.GameMap;
import mechanic.Point;

public class TrumpWallUnit extends Unit {
	public static final int HP = 50;
	public static final double SPEED = 1;
	public static final int DAMAGE_PER_TICK = 1;
	public TrumpWallUnit(Point gridLoc, int teamID) {
		super(HP, HP, SPEED, GameMap.getOppositeDirection((char)teamID), gridLoc, "res/spell/trumpwall.png", teamID);
		this.setDrawHeight(32);
		this.setSize(0.75);
		this.setThinkInterval((float)(1/SPEED));
	}
	@Override
	public void onThink() {
		if(this.unitHasControl()) { //for za warudo pause
			this.changeHP(this.getHP() - DAMAGE_PER_TICK);
		}
	}
}
