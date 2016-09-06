package spell;

import mechanic.Point;
import projectile.WindProjectile;
import unit.Unit;

public class WindCannon extends Spell {
	public static final int DAMAGE = 5;
	public static final int NUM_SHOTS = 9;
	public static final float DURATION = 0.6f;
	public static final double SPEED = 18;
	int shotsFired;
	public WindCannon(Unit owner) {
		super(owner, 0, DURATION, "Wind Cannon", "Blow the enemy away, stun them a bit, cancel the spell they're casting, and deal some damage", "res/spell/windcannon.png", false);
		this.setThinkInterval(DURATION/NUM_SHOTS);
	}
	@Override
	public void onSetMap() {
		WindProjectile projectile = new WindProjectile((int)(DAMAGE * this.owner.finalDamageModifier), SPEED, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
		this.getMap().addGameElement(projectile);
		this.shotsFired = 1;
	}
	@Override
	public void onThink() {
		int random = (int)(Math.random() * 3) - 1;
		WindProjectile projectile = new WindProjectile((int)(DAMAGE * this.owner.finalDamageModifier), SPEED, this.owner.direction, Point.add(this.owner.gridLoc, new Point(0, random)), this.owner.teamID);
		this.getMap().addGameElement(projectile);
		this.shotsFired++;
		if(this.shotsFired >= NUM_SHOTS) {
			this.finishSpell();
		}
	}
}
