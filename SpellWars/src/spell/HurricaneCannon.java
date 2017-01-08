package spell;

import mechanic.Point;
import projectile.HurricaneProjectile;
import projectile.WindProjectile;
import unit.Unit;

public class HurricaneCannon extends Spell {
	public static final double DAMAGE = 5;
	public static final int NUM_SHOTS = 3;
	public static final float DURATION = 0.6f;
	public static final double SPEED = 12;
	int shotsFired;
	public HurricaneCannon(Unit owner) {
		super(owner, 0, DURATION, "Hurricane Cannon", "Move the enemy to the same row that you are on", "res/spell/hurricanecannon.png", false);
		this.setThinkInterval(DURATION/NUM_SHOTS);
	}
	@Override
	public void onSetMap() {
		HurricaneProjectile projectile = new HurricaneProjectile(DAMAGE * this.owner.finalDamageOutputModifier, SPEED, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
		this.getMap().addGameElement(projectile);
		this.shotsFired = 1;
	}
	@Override
	public void onThink() {
		//int random = (int)(Math.random() * 3) - 1;
		//int random = (shotsFired + 1) % 3 - 1;
		int random = 0;
		HurricaneProjectile projectile = new HurricaneProjectile(DAMAGE * this.owner.finalDamageOutputModifier, SPEED, this.owner.direction, Point.add(this.owner.gridLoc, new Point(0, random)), this.owner.teamID);
		this.getMap().addGameElement(projectile);
		this.shotsFired++;
		if(this.shotsFired >= NUM_SHOTS) {
			this.finishSpell();
		}
	}
}
