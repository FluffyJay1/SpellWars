package shield;

import unit.Unit;

public class CryoFreezeShield extends Shield {
	float timer;
	public CryoFreezeShield(Unit owner, float healPerSec, float duration) {
		super(owner, 9999, 9999, "res/shield/cryofreeze.png");
		this.drawHP = false;
		this.setDrawHeight(owner.getDrawHeight() + 20);
		this.setThinkInterval(1/healPerSec);
		this.timer = duration;
	}
	@Override
	public void onThink() {
		this.owner.doDamage(-1);
		this.owner.stun(this.thinkInterval);
	}
	@Override
	public void onUpdate() {
		this.timer -= this.getFrameTime();
		if(this.timer <= 0) {
			this.isDead = true;
		}
	}
}
