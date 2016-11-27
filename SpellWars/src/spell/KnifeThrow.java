package spell;

import projectile.KnifeProjectile;
import unit.Unit;

public class KnifeThrow extends Spell {
	public static final float DAMAGE = 40;
	public KnifeThrow(Unit owner) {
		super(owner, 0.25f, 0.1f, "Knife Throw", "Throws a knife, causing an enemy to bleed and be slown a little, Multiple stacks of bleeding have increased effectiveness", "res/spell/knife.png", false);
	}
	@Override
	public void onActivate() {
		this.map.addGameElement(new KnifeProjectile(DAMAGE * this.owner.finalDamageOutputModifier, this.owner.direction, this.owner.gridLoc, this.owner.teamID));
		this.finishSpell();
	}
}
