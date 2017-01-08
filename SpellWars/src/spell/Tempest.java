package spell;

import java.util.ArrayList;

import mechanic.GameMap;
import mechanic.Point;
import projectile.HurricaneProjectile;
import projectile.VacuumProjectile;
import projectile.WindProjectile;
import unit.Unit;

public class Tempest extends Spell {
	public static final double DAMAGE = 5;
	public static final float DURATION = 10;
	public static final float NUM_SHOTS = 60;
	public static final float WIND_WEIGHT = 1;
	public static final float VACUUM_WEIGHT = 1.2f;
	public static final float HURRICANE_WEIGHT = 0.3f;
	public static final double SPEED = 15;
	int x;
	int shotsFired;
	public Tempest(Unit owner) {
		super(owner, 0.2f, 0.2f, "Tempest", "Blow the enemy all over the place", "res/spell/tempest.png", true);
		this.registerFieldEffect("tempest");
		this.x = 0;
	}
	@Override
	public void onActivate() {
		this.getMap().unpauseAll();
		this.x = 0;
		if(this.owner.teamID == GameMap.ID_RIGHT) {
			this.x = (int)this.getMap().getGridDimensions().x - 1;
		}
		this.shotsFired = 0;
		this.setThinkInterval(DURATION/NUM_SHOTS);
		this.onThink();
	}
	@Override
	public void onThink() {
		float random = (float)Math.random() * (WIND_WEIGHT + VACUUM_WEIGHT + HURRICANE_WEIGHT);
		int randomY = (int)(Math.random() * this.getMap().getGridDimensions().y);
		//int randomY = this.shotsFired % 4;
		if(random <= WIND_WEIGHT) {
			this.getMap().addGameElement(new WindProjectile(DAMAGE * this.owner.finalDamageOutputModifier, SPEED, this.owner.direction, new Point(this.x, randomY), this.owner.teamID));
		} else if(random <= WIND_WEIGHT + VACUUM_WEIGHT) {
			this.getMap().addGameElement(new VacuumProjectile(DAMAGE * this.owner.finalDamageOutputModifier, SPEED, this.owner.direction, new Point(this.x, randomY), this.owner.teamID));
		} else {
			this.getMap().addGameElement(new HurricaneProjectile(DAMAGE * this.owner.finalDamageOutputModifier, SPEED, this.owner.direction, new Point(this.x, randomY), this.owner.teamID));
		}
		this.shotsFired++;
		if(this.shotsFired >= NUM_SHOTS) {
			this.finishSpell();
		}
	}
}
