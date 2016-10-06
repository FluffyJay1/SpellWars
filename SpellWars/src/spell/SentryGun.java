package spell;

import mechanic.GameMap;
import unit.ForgeSpiritUnit;
import unit.SentryGunUnit;
import unit.Unit;

public class SentryGun extends Spell {
	public static final float DELAY = 0.4f;
	float timer;
	int numSpawned;
	int numToSpawn;
	public SentryGun(Unit owner) {
		super(owner, 0, 0, "Summon Sentry Gun", "Summon an immobile sentry gun that shoots at the enemy. Bullets from this sentry can penetrate 1 panel behind the unit it hits", "res/unit/sentrygun.png", true);
		this.timer = DELAY;
		this.numSpawned = 0;
		this.numToSpawn = 1;
	}
	public SentryGun(Unit owner, int num) {
		this(owner);
		this.numToSpawn = num;
	}
	@Override
	public void onActivate() {
		/*
		for(int i = 0; i < 8; i++) {
			this.owner.getMap().addUnit(new ForgeSpiritUnit(GameMap.getFuturePoint(this.owner.gridLoc, GameMap.getOppositeDirection((char)this.owner.teamID)), this.owner.teamID));
		}
		*/
		//this.finishSpell();
	}
	@Override
	public void onSpellUpdate() {
		this.timer -= this.getFrameTime();
		if(timer < 0) {
			this.finishSpell();
		} else {
			while(this.numSpawned < (1 - this.timer/DELAY) * this.numToSpawn) {
				Unit unit = new SentryGunUnit(GameMap.getFuturePoint(this.owner.gridLoc, GameMap.getOppositeDirection((char)this.owner.teamID)), this.owner.teamID);
				this.owner.getMap().addUnit(unit);
				unit.setPause(true);
				numSpawned++;
			}
		}
	}
}
