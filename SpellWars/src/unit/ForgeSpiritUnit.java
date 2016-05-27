package unit;

import mechanic.GameMap;
import mechanic.Point;
import spell.AreaGrab;
import spell.ForgeSpiritFire;
import spell.TestFireball;

public class ForgeSpiritUnit extends Unit {
	public static final double HP = 80;
	public static final double SPEED = 1.25;
	public static final int MOVES_PER_FIRE = 4;
	int moves;
	public ForgeSpiritUnit(Point gridLoc, int teamID) {
		super(HP, HP, SPEED, GameMap.getOppositeDirection((char)teamID), gridLoc, "res/plane.png", teamID);
		this.setThinkIntervalWithMove(true);
		this.setDrawHeight(42);
		this.moves = MOVES_PER_FIRE;
	}
	@Override
	public void onThink() {
		this.moves--;
		if(this.moves == 0) {
			this.castSpell(new ForgeSpiritFire(this));
			this.moves += MOVES_PER_FIRE;
		} else {
			this.moveRandom4(false, true, false, true);
		}
	}
	/*
	@Override
	public void onUpdate() {
		if(this.moves == MOVES_PER_FIRE){
			this.castSpell(new ForgeSpiritFire(this));
		}
	}
	*/
	/*
	@Override
	public void onDeath() {
		this.castSpell(new TestFireball(this));
	}
	*/
}
