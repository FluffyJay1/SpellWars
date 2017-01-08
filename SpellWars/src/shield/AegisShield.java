package shield;

import org.newdawn.slick.Color;

import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import unit.Unit;

public class AegisShield extends Shield {
	float timer;
	public AegisShield(Unit owner, double hp, float regenPerSec, float duration) {
		super(owner, hp, hp, "res/shield/aegisshield.png");
		this.setThinkInterval(1/regenPerSec);
		this.timer = duration;
		this.drawHP = true;
		this.setDrawHeight(owner.getDrawHeight());
		this.HPTextColor = new Color(80, 255, 0);
	}
	@Override
	public void onThink() {
		this.owner.doHeal(1);
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.owner.getLoc(), new Point(0, -this.owner.getDrawHeight())), EmitterTypes.CIRCLE_DIRECTION, "res/particle_heal.png", true, /*point, emitter type, image path, alphaDecay*/
				0.4f, 0.6f, /*particle start scale*/
				0.4f, 0.6f, /*particle end scale*/
				12.5f, /*drag*/
				0, 0, /*rotational velocity*/
				0.6f, 0.8f, /*min and max lifetime*/
				1000, 1250, /*min and max launch speed*/
				0, 1, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
				0, 50, 90, 0); /*keyvalues*/
		this.getMap().addParticleEmitter(pe);
	}
	@Override
	public void onUpdate() {
		this.timer -= this.getFrameTime();
		if(this.timer <= 0) {
			this.isDead = true;
		}
	}
}
