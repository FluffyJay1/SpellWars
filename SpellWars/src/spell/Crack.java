package spell;

import mechanic.Point;
import projectile.CrackGrenade;
import projectile.StunGrenade;
import unit.Unit;

public class Crack extends Spell {
	public static final double DAMAGE = 15;
	public Crack(Unit owner) {
		super(owner, 0.2f, 0.1f, "Crack Grenade", "Throws a grenade at the enemy, dealing minor damage cracking panels around it", "res/spell/crack.png", false);
	}
	@Override
	public void onActivate() {
		CrackGrenade snade = new CrackGrenade(DAMAGE * this.owner.finalDamageOutputModifier, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
		this.getMap().addGameElement(snade);
		this.finishSpell();
	}
}
