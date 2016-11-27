package statuseffect;

import particlesystem.ParticleEmitter;
import particlesystem.EmitterTypes;
import projectile.Projectile;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;


public class StatusFire extends StatusEffect {
	public static final String ID = "fire"; //for stacking
	//public static final DamageType DAMAGE_TYPE = DamageType.MAGIC;
	//float damageChange;
	public StatusFire(GameElement owner, float damagePerSecond, float duration) {
		super(owner, StackingProperty.UNSTACKABLE_REFRESH_DURATION, ID, duration, false, true, 1);
		this.setIcon("res/statuseffect/icon_fire.png");
		this.damagePerInterval = 1;
		this.interval = 1/damagePerSecond;
		//this.damageChange = damageReduction;
		//this.setDamageType(DAMAGE_TYPE);
	}
	@Override
	public StatusEffect clone() {
		StatusEffect effect = new StatusFire(this.getOwner(), 0, 0);
		StatusEffect.copyFromTo(this, effect);
		return effect;
	}
	@Override
	public void onInterval() {
		if(this.getOwner() != null) {
			ParticleEmitter pe = new ParticleEmitter(this.getOwner(), EmitterTypes.CIRCLE_DIRECTION, "res/particle_explosion.png", true, /*point, emitter type, image path, alphaDecay*/
					0.1f, 0.25f, /*particle start scale*/
					0.4f, 0.8f, /*particle end scale*/
					3.75f, /*drag*/
					0, 0, /*rotational velocity*/
					0.2f, 0.4f, /*min and max lifetime*/
					400, 800, /*min and max launch speed*/
					this.interval, 8, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
					0, 60, 90, 0); /*keyvalues*/
			getOwner().getMap().addParticleEmitter(pe);
		}
	}
	@Override
	public void onCreate() {
		
	}
}
