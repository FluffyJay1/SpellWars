package statuseffect;

import org.newdawn.slick.Color;

public class StatusMud extends StatusEffect {
	public StatusMud(float speedModifier) {
		super(null, StackingProperty.UNSTACKABLE_REPLACE, "mud", -1, false, false, 1);
		this.setMoveSpeedModifier(speedModifier);
		this.setColorModifier(new Color(200, 200, 130));
	}
	@Override
	public StatusEffect clone() {
		StatusEffect effect = new StatusMud(1);
		StatusEffect.copyFromTo(this, effect);
		return effect;
	}
}
