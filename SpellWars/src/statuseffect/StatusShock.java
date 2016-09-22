package statuseffect;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import mechanic.GameElement;

public class StatusShock extends StatusEffect {
	public static final String ID = "shock";
	static Image icon;
	static boolean imageLoaded = false;
	public StatusShock(GameElement owner, float duration, float impactResistanceModifier, int level) {
		super(owner, StackingProperty.UNSTACKABLE_REPLACE, ID, duration, level);
		//this.setResistanceModifier(DamageType.IMPACT, impactResistanceModifier);
		if(imageLoaded == false) {
			try {
				icon = new Image("res/statuseffect/icon_shock.png");
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//this.setIcon(icon);
	}
}
