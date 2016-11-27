package statuseffect;

import mechanic.GameElement;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;

public class StatusRegenerate extends StatusEffect {
	int hpHealed;
	int hpToHeal;
	public StatusRegenerate(GameElement owner, float duration, int hp) {
		super(owner, StackingProperty.STACKABLE_INDEPENDENT, "regenerate", duration, true, true, 1);
		this.setIcon("res/spell/regenerate.png");
		this.hpToHeal = hp;
		this.interval = duration/hp;
		this.damagePerInterval = 0;
		this.hpHealed = 0;
	}
	@Override
	public void onInterval() {
		this.getOwner().doHeal(1);
		this.hpHealed++;
		/*
		if(this.hpHealed >= hpToHeal) {
			this.finishSpell();
		}
		*/
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.CIRCLE_DIRECTION, "res/particle_heal.png", true, /*point, emitter type, image path, alphaDecay*/
				0.5f, 1, /*particle start scale*/
				0.5f, 1, /*particle end scale*/
				3.5f, /*drag*/
				0, 0, /*rotational velocity*/
				0.5f, 0.6f, /*min and max lifetime*/
				0, 350, /*min and max launch speed*/
				0, 1, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
				0, 50, 90, 0); /*keyvalues*/
		this.getOwner().getMap().addParticleEmitter(pe);
	}
}
