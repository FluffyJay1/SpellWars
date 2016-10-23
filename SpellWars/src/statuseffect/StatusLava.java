package statuseffect;

import org.newdawn.slick.Color;

public class StatusLava extends StatusEffect {
	public StatusLava(float damagePerSecond) {
		super(null, StackingProperty.UNSTACKABLE_REPLACE, "lava", -1, 1);
		this.setColorModifier(new Color(240, 170, 170));
		this.interval = 1/damagePerSecond;
		this.damagePerInterval = 1;
	}
}
