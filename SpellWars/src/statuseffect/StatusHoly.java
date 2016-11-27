package statuseffect;

import org.newdawn.slick.Color;

public class StatusHoly extends StatusEffect {
	public StatusHoly(float damageInputModifier, float healInputModifier) {
		super(null, StackingProperty.UNSTACKABLE_REPLACE, "holy", -1, true, false, 1);
		this.setDamageInputModifier(damageInputModifier);
		this.setHealInputModifier(healInputModifier);
		this.setColorModifier(new Color(255, 255, 240));
	}
	@Override
	public StatusEffect clone() {
		StatusEffect effect = new StatusHoly(1, 1);
		StatusEffect.copyFromTo(this, effect);
		return effect;
	}
}
