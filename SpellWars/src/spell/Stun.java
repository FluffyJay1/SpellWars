package spell;

import mechanic.Point;
import projectile.StunGrenade;
import unit.Unit;

public class Stun extends Spell {
	public static final int DAMAGE = 10;
	public static final float MAX_STUN_DURATION = 4f;
	public static final float MIN_STUN_DURATION = 3f;
	public Stun(Unit owner) {
		super(owner, 0.2f, 0.1f, "Stun Grenade", "Throws a grenade at the enemy, dealing minor damage and stunning on impact", "res/particle_genericBlue.png", false);
	}
	@Override
	public void onActivate() {
		StunGrenade snade = new StunGrenade((int)(DAMAGE * this.owner.finalDamageModifier), MAX_STUN_DURATION, MIN_STUN_DURATION, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
		this.getMap().addGameElement(snade);
		this.finishSpell();
	}
}
