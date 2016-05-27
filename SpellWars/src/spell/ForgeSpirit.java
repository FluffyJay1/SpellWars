package spell;

import mechanic.GameMap;
import unit.ForgeSpiritUnit;
import unit.Unit;

public class ForgeSpirit extends Spell {
	public static final float DELAY = 0.4f;
	float timer;
	int numSpawned;
	int numToSpawn;
	public ForgeSpirit(Unit owner) {
		super(owner, 0, 0, "Summon Forge Spirit", "Summon a unit that launches artillery at the enemy", "res/plane.png", true);
		this.timer = DELAY;
		this.numSpawned = 0;
		this.numToSpawn = 1;
	}
	public ForgeSpirit(Unit owner, int num) {
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
				Unit unit = new ForgeSpiritUnit(GameMap.getFuturePoint(this.owner.gridLoc, GameMap.getOppositeDirection((char)this.owner.teamID)), this.owner.teamID);
				this.owner.getMap().addUnit(unit);
				unit.setPause(true);
				numSpawned++;
			}
		}
	}
}
