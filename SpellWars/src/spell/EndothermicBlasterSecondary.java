package spell;

import projectile.MeiFrostProjectile;
import projectile.Projectile;
import unit.Unit;

public class EndothermicBlasterSecondary extends Spell {
	public static final double DAMAGE = 50;
	public static final float SPEED = 12;
	public EndothermicBlasterSecondary(Unit owner) {
		super(owner, 0.75f, 0.15f, "Endothermic Blast Icicle", "Shoots Mei's gun that deals a lot of damage", "res/projectile/icicle.png", false);
	}
	@Override
	public void onActivate() {
		Projectile p = new Projectile(DAMAGE * this.owner.finalDamageOutputModifier, SPEED, this.owner.direction, this.owner.gridLoc, "res/projectile/icicle.png", this.owner.teamID, true, true, true);
		this.getMap().addGameElement(p);
	}
}
