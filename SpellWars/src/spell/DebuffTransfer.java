package spell;

import java.util.ArrayList;

import mechanic.Point;
import projectile.DebuffTransferProjectile;
import statuseffect.StatusEffect;
import unit.Unit;

public class DebuffTransfer extends Spell {
	public static final double DAMAGE = 30;
	public static final double DAMAGE_PER_DEBUFF = 10;
	public static final double SPEED = 22;
	public static final double SPEED_PER_DEBUFF = 4;
	public DebuffTransfer(Unit owner) {
		super(owner, 0, 0.2f, "Debuff Transfer", "Removes your debuffs, puts them on a projectile, and tranfers those debuffs to all enemies it hits", "res/spell/debufftransfer.png", false);
	}
	@Override
	public void onActivate() {
		ArrayList<StatusEffect> effects = new ArrayList<StatusEffect>();
		for(StatusEffect e : this.owner.getPurgableDebuffs()) {
			effects.add(e.clone());
		}
		int debuffs = this.owner.purgeDebuffs();
		DebuffTransferProjectile p = new DebuffTransferProjectile((DAMAGE + DAMAGE_PER_DEBUFF * debuffs) * this.owner.finalDamageOutputModifier, SPEED + SPEED_PER_DEBUFF * debuffs, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
		p.setEffects(effects);
		this.getMap().addGameElement(p);
	}
}
