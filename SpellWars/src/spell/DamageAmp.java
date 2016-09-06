package spell;

import statuseffect.StatusDamageAmplification;
import unit.Unit;

public class DamageAmp extends Spell {
	public static final float DAMAGE_MODIFIER = 2;
	public static final float DURATION = 10;
	public DamageAmp(Unit owner) {
		super(owner, 0, 0, "Damage Amplification", "Doubles the damage you deal for a short period", "res/spell/damageamp.png", false);
	}
	@Override
	public void onActivate() {
		StatusDamageAmplification status = new StatusDamageAmplification(this.owner, DAMAGE_MODIFIER, DURATION, 1);
		this.owner.addStatusEffect(status);
	}
}
