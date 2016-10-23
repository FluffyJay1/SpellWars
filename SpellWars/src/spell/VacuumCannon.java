package spell;

import mechanic.Point;
import projectile.VacuumProjectile;
import unit.Unit;

public class VacuumCannon extends Spell {
	public static final int DAMAGE = 5;
	public static final int NUM_SHOTS = 10;
	public static final float DURATION = 0.9f;
	public static final double SPEED = 15;
	int shotsFired;
	public VacuumCannon(Unit owner) {
		super(owner, 0, DURATION, "Vacuum Cannon", "Blow the enemy towards you, stun them, and deal a bunch of damage", "res/spell/vacuumcannon.png", false);
		this.setThinkInterval(DURATION/NUM_SHOTS);
	}
	@Override
	public void onSetMap() {
		VacuumProjectile projectile = new VacuumProjectile((int)(DAMAGE * this.owner.finalDamageModifier), SPEED, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
		this.getMap().addGameElement(projectile);
		this.shotsFired = 1;
	}
	@Override
	public void onThink() {
		//int random = (int)(Math.random() * 3) - 1;
		int random = (shotsFired + 1) % 3 - 1;
		VacuumProjectile projectile = new VacuumProjectile((int)(DAMAGE * this.owner.finalDamageModifier), SPEED, this.owner.direction, Point.add(this.owner.gridLoc, new Point(0, random)), this.owner.teamID);
		this.getMap().addGameElement(projectile);
		this.shotsFired++;
		if(this.shotsFired >= NUM_SHOTS) {
			this.finishSpell();
		}
	}
}
