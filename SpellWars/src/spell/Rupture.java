package spell;

import mechanic.Point;
import projectile.RuptureProjectile;
import unit.Unit;

public class Rupture extends Spell {
	public static final double DAMAGE = 10;
	public static final double SPEED = 20;
	public static final float DURATION_PRIMARY = 15;
	public static final float DURATION_SECONDARY = 10;
	public Rupture(Unit owner) {
		super(owner, 0.05f, 0.2f, "Rupture", "Causes the enemy to take damage whenever they move", "res/spell/rupture.png", false);
	}
	@Override
	public void onActivate() {
		RuptureProjectile p = new RuptureProjectile(DAMAGE * this.owner.finalDamageOutputModifier, DURATION_PRIMARY, SPEED, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
		this.getMap().addGameElement(p);
		p = new RuptureProjectile(DAMAGE * this.owner.finalDamageOutputModifier, DURATION_SECONDARY, SPEED, this.owner.direction, Point.add(this.owner.gridLoc, new Point(0, 1)), this.owner.teamID);
		this.getMap().addGameElement(p);
		p = new RuptureProjectile(DAMAGE * this.owner.finalDamageOutputModifier, DURATION_SECONDARY, SPEED, this.owner.direction, Point.add(this.owner.gridLoc, new Point(0, -1)), this.owner.teamID);
		this.getMap().addGameElement(p);
	}
}
