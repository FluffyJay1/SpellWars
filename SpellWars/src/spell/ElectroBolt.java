package spell;

import mechanic.Point;
import projectile.ElectroBoltProjectile;
import unit.Unit;

public class ElectroBolt extends Spell {
	public static final double DAMAGE = 30;
	public static final float STUN_DURATION = 1.5f;
	public static final float SPEED = 0.05f;
	public ElectroBolt(Unit owner) {
		super(owner, 0.1f, 0.1f, "Electro Bolt", "Shoot a bolt of electricity that stuns, purges buffs, and spreads in 4 directions when it hits an enemy, the extra bolts also spread", "res/spell/electrobolt.png", false);
	}
	@Override
	public void onActivate() {
		this.getMap().addGameElement(new ElectroBoltProjectile(DAMAGE * this.owner.finalDamageOutputModifier, SPEED, this.owner.direction, this.owner.gridLoc, this.owner.teamID, STUN_DURATION));
		this.finishSpell();
	}
}
