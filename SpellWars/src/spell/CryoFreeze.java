package spell;

import shield.CryoFreezeShield;
import unit.Unit;

public class CryoFreeze extends Spell {
	public static final float DURATION = 4;
	public static final float HEALING = 75;
	public CryoFreeze(Unit owner) {
		super(owner, 0, 0, "Cryo-Freeze", "Stun yourself, but also make yourself immune to damage and heal yourself", "res/particle_genericBlue.png", false);
	}
	@Override
	public void onActivate() {
		CryoFreezeShield shield = new CryoFreezeShield(this.owner, HEALING/DURATION, DURATION);
		this.getMap().addGameElement(shield);
		this.owner.stun(DURATION);
	}
}
