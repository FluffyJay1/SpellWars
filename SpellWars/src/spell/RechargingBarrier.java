package spell;

import mechanic.Panel;
import shield.RechargingShield;
import shield.ReflectShield;
import unit.Unit;

public class RechargingBarrier extends Spell {
	public static final float DURATION = 30;
	public static final int BLOCK_AMOUNT = 50;
	public static final float DELAY = 0.4f;
	public static final float REGEN_PER_SEC = 6;
	public static final float DAMAGE_COOLDOWN_TIME = 7f;
	float timer;
	public RechargingBarrier(Unit owner) {
		super(owner, 0, 0, "Recharging Barrier", "Gives all of your units a barrier that recharges after not taking damage for a while", "res/spell/rechargingbarrier.png", true);
		this.timer = DELAY;
	}
	@Override
	public void onActivate() {
		for(Panel p : this.getMap().getPanels()) {
			if(p.unitStandingOnPanel != null && p.unitStandingOnPanel.teamID == this.owner.teamID) {
				RechargingShield shield = new RechargingShield(p.unitStandingOnPanel, 0, BLOCK_AMOUNT, DURATION, REGEN_PER_SEC, DAMAGE_COOLDOWN_TIME);
				shield.setPause(true);
				this.getMap().addGameElement(shield);
			}
		}
	}
	@Override
	public void onSpellUpdate() {
		this.timer -= this.getFrameTime();
		if(this.timer <= 0) {
			this.finishSpell();
		}
	}
}
