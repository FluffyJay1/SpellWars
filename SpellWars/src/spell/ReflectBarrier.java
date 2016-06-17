package spell;

import mechanic.Panel;
import shield.ReflectShield;
import unit.Unit;

public class ReflectBarrier extends Spell {
	public static final float DURATION = 10;
	public static final float DELAY = 0.4f;
	float timer;
	public ReflectBarrier(Unit owner) {
		super(owner, 0, 0, "Reflect Barrier", "Gives all of your units a temporary barrier that blocks one instance of damage and reflects it back to the enemy if it's a ground projectile", "res/spell/reflectbarrier.png", true);
		this.timer = DELAY;
	}
	@Override
	public void onActivate() {
		for(Panel p : this.getMap().getPanels()) {
			if(p.unitStandingOnPanel != null && p.unitStandingOnPanel.teamID == this.owner.teamID) {
				this.getMap().addGameElement(new ReflectShield(p.unitStandingOnPanel, DURATION));
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
