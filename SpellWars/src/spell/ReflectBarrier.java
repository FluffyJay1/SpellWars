package spell;

import mechanic.Panel;
import shield.ReflectShield;
import unit.Unit;

public class ReflectBarrier extends Spell {
	public static final float DURATION = 12;
	public static final int BLOCK_AMOUNT = 50;
	public static final float DELAY = 0.4f;
	float timer;
	public ReflectBarrier(Unit owner) {
		super(owner, 0, 0, "Reflect Barrier", "Gives all of your units a temporary barrier that blocks some damage and reflects projectiles back to the enemy", "res/spell/reflectbarrier.png", true);
		this.timer = DELAY;
	}
	@Override
	public void onActivate() {
		for(Panel p : this.getMap().getPanels()) {
			if(p.unitStandingOnPanel != null && p.unitStandingOnPanel.teamID == this.owner.teamID) {
				ReflectShield shield = new ReflectShield(p.unitStandingOnPanel, DURATION, BLOCK_AMOUNT);
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
