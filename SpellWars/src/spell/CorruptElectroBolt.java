package spell;

import mechanic.Point;
import projectile.CorruptElectroBoltProjectile;
import unit.Unit;

public class CorruptElectroBolt extends Spell {
	public static final double DAMAGE = 40;
	public static final float STUN_DURATION = 2.5f;
	public static final float SPEED = 0.05f;
	public CorruptElectroBolt(Unit owner) {
		super(owner, 0.1f, 0.1f, "Corrupt Electro Bolt", "P|O|W|E|R", "res/spell/corruptelectrobolt.png", false);
	}
	@Override
	public void onActivate() {
		this.getMap().addGameElement(new CorruptElectroBoltProjectile(DAMAGE * this.owner.finalDamageOutputModifier, SPEED, this.owner.direction, this.owner.gridLoc, this.owner.teamID, STUN_DURATION));
		this.finishSpell();
		this.corrupt();
	}
}
