package spell;

import mechanic.Panel;
import shield.AegisShield;
import shield.RechargingShield;
import shield.ReflectShield;
import unit.Unit;

public class AegisBarrier extends Spell {
	public static final float DURATION = 50;
	public static final int BLOCK_AMOUNT = 75;
	public static final float DELAY = 0.4f;
	public static final float REGEN_PER_SEC = 2;
	float timer;
	public AegisBarrier(Unit owner) {
		super(owner, 0, 0, "Aegis Barrier", "Give yourself a barrier, dispel debuffs, and you regenerate health as long as the barrier is alive", "res/spell/aegisbarrier.png", true);
		this.timer = DELAY;
	}
	@Override
	public void onActivate() {
		AegisShield a = new AegisShield(this.owner, BLOCK_AMOUNT, REGEN_PER_SEC, DURATION);
		this.getMap().addGameElement(a);
		this.owner.purgeDebuffs();
	}
	@Override
	public void onSpellUpdate() {
		this.timer -= this.getFrameTime();
		if(this.timer <= 0) {
			this.finishSpell();
		}
	}
}
