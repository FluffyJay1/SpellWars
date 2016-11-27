package spell;

import mechanic.GameMap;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import statuseffect.StatusRegenerate;
import unit.Unit;

public class Regenerate extends Spell {
	public static final int HEAL_AMOUNT = 75;
	public static final float DURATION = 25;
	int hpHealed;
	public Regenerate(Unit owner) {
		super(owner, 0, 0, "Regenerate", "Regain some HP over time", "res/spell/regenerate.png", false);
	}
	@Override
	public void onActivate() {
		/*
		this.setThinkInterval(DURATION/HEAL_AMOUNT);
		this.hpHealed = 0;
		*/
		this.owner.addStatusEffect(new StatusRegenerate(this.owner, DURATION, HEAL_AMOUNT));
		this.finishSpell();
	}
	/*
	@Override
	public void onThink() {
		this.owner.doDamage(-1);
		this.hpHealed++;
		if(this.hpHealed >= HEAL_AMOUNT || this.owner.getRemove()) {
			this.finishSpell();
		}
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.owner.getLoc(), new Point(0, -this.owner.getDrawHeight())), EmitterTypes.CIRCLE_DIRECTION, "res/particle_heal.png", true, //point, emitter type, image path, alphaDecay
				0.5f, 1, //particle start scale
				0.5f, 1, //particle end scale
				3.5f, //drag
				0, 0, //rotational velocity
				0.5f, 0.6f, //min and max lifetime
				0, 350, //min and max launch speed
				0, 1, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)
				0, 50, 90, 0); //keyvalues
		this.getMap().addParticleEmitter(pe);
	}
	*/
}
