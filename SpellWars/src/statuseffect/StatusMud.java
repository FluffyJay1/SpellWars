package statuseffect;

import org.newdawn.slick.Color;

public class StatusMud extends StatusEffect {
	public StatusMud(float speedModifier) {
		super(null, StackingProperty.UNSTACKABLE_REPLACE, "mud", -1, 1);
		this.setMoveSpeedModifier(speedModifier);
		this.setColorModifier(new Color(200, 200, 130));
	}
}
