package spell;

import mechanic.GameMap;
import unit.TimeBombUnit;
import unit.Unit;

public class TimeBomb extends Spell {
	public static final float DELAY = 0.4f;
	float timer;
	public TimeBomb(Unit owner) {
		super(owner, 0, 0, "Summon Time Bomb", "Summons a time bomb that explodes after a while if it isn't destroyed prematurely", "res/unit/timebomb.png", true) ;
		this.timer = DELAY;
	}
	public void onActivate() {
		this.owner.getMap().addUnit(new TimeBombUnit(GameMap.getFuturePoint(this.owner.gridLoc, (char)this.owner.direction), this.owner.teamID), false);
	}
	public void onSpellUpdate() {
		this.timer -= this.getFrameTime();
		if(timer <= 0){
			this.finishSpell();
		}
	}
}
