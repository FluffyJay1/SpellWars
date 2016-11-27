package spell;

import statuseffect.StatusDamageAmplification;
import unit.Unit;

public class DamageAmp extends Spell {
	public static final float DAMAGE_MODIFIER = 1.5f;
	public static final float DURATION = 15;
	public DamageAmp(Unit owner) {
		super(owner, 0, 0, "Damage Amplification", "Increases the damage you deal by 1.5x for a short period", "res/spell/damageamp.png", false);
	}
	@Override
	public void onActivate() {
		StatusDamageAmplification status = new StatusDamageAmplification(this.owner, DAMAGE_MODIFIER, DURATION, 1);
		this.owner.addStatusEffect(status);
	}
}
