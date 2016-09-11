package unit;

import mechanic.GameMap;
import mechanic.Point;
import spell.BouncingOrb;
import spell.Stun;
import spell.TestFireball;
import spell.TimeBombDetonate;
import ui.SpellSelector;

public class Boss extends Unit {
	public static final double HP = 400;
	public static final double HP_PER_LEVEL = 50;
	public static final double SPEED = 1.2;
	public static final double SPEED_PER_LEVEL = 0.21;
	public static final int MOVES_PER_FIRE = 6;
	public static final int MOVES_VARIATION = 3;
	int moves;
	int level;
	public Boss(Point gridLoc, int teamID, int level) {
		super(HP + HP_PER_LEVEL * level, HP + HP_PER_LEVEL * level, SPEED + SPEED_PER_LEVEL * level, GameMap.getOppositeDirection((char)teamID), gridLoc, "res/plane.png", teamID);
		this.level = level;
		this.refreshMoves();
		this.setSize(1.4);
		this.setDrawHeight(60);
		this.setThinkIntervalWithMove(true);
		this.isImportant = true;
	}
	@Override
	public void onThink() {
		this.moves--;
		this.moveRandom4(false, false, true, true, true, false);
		if(this.moves <= 0) {
			this.castSpell(SpellSelector.getRandomSpell(this));
			this.refreshMoves();
		}
	}
	public void refreshMoves() {
		this.moves = MOVES_PER_FIRE + Point.roundToNearestInteger((Math.random() - 0.5) * 2 * MOVES_VARIATION);
	}
}
