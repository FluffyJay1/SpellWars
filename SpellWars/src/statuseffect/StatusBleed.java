package statuseffect;

import mechanic.GameElement;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;

public class StatusBleed extends StatusEffect {
	public static final String ID = "bleed"; //for stacking
	public static final float DURATION = 10;
	public static final float DAMAGE_PER_SECOND = 1;
	public static final float SPEED_MODIFIER = 0.95f;
	public StatusBleed(GameElement owner) {
		super(owner, StackingProperty.STACKABLE_INDEPENDENT, ID, DURATION, false, true, 1);
		this.setMoveSpeedModifier(SPEED_MODIFIER);
		this.setIcon("res/statuseffect/icon_bleed.png");
		this.damagePerInterval = DAMAGE_PER_SECOND;
		this.interval = 1;
	}
	@Override
	public StatusEffect clone() {
		StatusEffect effect = new StatusBleed(this.getOwner());
		StatusEffect.copyFromTo(this, effect);
		return effect;
	}
	@Override
	public void onInterval() {
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.CIRCLE_DIRECTION, "res/particle_blood.png", true, /*point, emitter type, image path, alphaDecay*/
				1.5f, 2.5f, /*particle start scale*/
				0.0f, 0.0f, /*particle end scale*/
				-3.75f, /*drag*/
				0, 0, /*rotational velocity*/
				0.3f, 0.6f, /*min and max lifetime*/
				40, 40, /*min and max launch speed*/
				0, 3, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
				0, 20, -90, 0); /*keyvalues*/
		getOwner().getMap().addParticleEmitter(pe);
	}
	@Override
	public void onExpired() {
		for(StatusEffect e : this.getOwner().getStatusEffects(ID)) {
			e.setDuration(DURATION);
		}
	}
}
