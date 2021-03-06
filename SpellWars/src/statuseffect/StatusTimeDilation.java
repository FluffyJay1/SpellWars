package statuseffect;

import particlesystem.ParticleEmitter;
import particlesystem.EmitterTypes;
import projectile.Projectile;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;


public class StatusTimeDilation extends StatusEffect {
	public static final String ID = "timedilation"; //for stacking
	public static final float UPDATE_INTERVAL = 0.25f;
	public static final float SPEED_DECAY_PER_INTERVAL = 0.82f;
	//public static final DamageType DAMAGE_TYPE = DamageType.MAGIC;
	//float damageChange;
	public StatusTimeDilation(GameElement owner, float speedModifier, float duration, int level) {
		super(owner, StackingProperty.UNSTACKABLE_REPLACE, ID, duration, speedModifier >= 1, true, level);
		this.setIcon("res/statuseffect/icon_timedilation.png");
		this.interval = UPDATE_INTERVAL;
		this.setMoveSpeedModifier(speedModifier);
		//this.damageChange = damageReduction;
		//this.setDamageType(DAMAGE_TYPE);
		if(this.getOwner() != null) {
			if(this.isBuff) {
				this.setColorModifier(new Color(200, 220, 255));
				ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.POINT_RADIAL, "res/particle_timedilationgood.png", true, /*point, emitter type, image path, alphaDecay*/
						0.5f, 1.5f, /*particle start scale*/
						3.0f, 5.0f, /*particle end scale*/
						11.5f, /*drag*/
						0, 0, /*rotational velocity*/
						0.4f, 0.8f, /*min and max lifetime*/
						0, 2250, /*min and max launch speed*/
						0, 5, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
						0, 0, 0, 0); /*keyvalues*/
				getOwner().getMap().addParticleEmitter(pe);
			} else {
				this.setColorModifier(new Color(255, 150, 230));
				ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.POINT_RADIAL, "res/particle_timedilationbad.png", true, /*point, emitter type, image path, alphaDecay*/
						0.5f, 1.5f, /*particle start scale*/
						2.0f, 5.0f, /*particle end scale*/
						6.5f, /*drag*/
						0, 0, /*rotational velocity*/
						0.4f, 0.8f, /*min and max lifetime*/
						0, 1050, /*min and max launch speed*/
						0, 5, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
						0, 0, 0, 0); /*keyvalues*/
				getOwner().getMap().addParticleEmitter(pe);
			}
		}
	}
	@Override
	public StatusEffect clone() {
		StatusEffect effect = new StatusTimeDilation(this.getOwner(), 0, 0, 0);
		StatusEffect.copyFromTo(this, effect);
		return effect;
	}
	@Override
	public void onInterval() {
		if(this.isBuff) {
			ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.POINT_RADIAL, "res/particle_timedilationgood.png", true, /*point, emitter type, image path, alphaDecay*/
					0.5f * (this.getMoveSpeedModifier() - 1), 1.0f, /*particle start scale*/
					0.5f, 0.5f, /*particle end scale*/
					22.0f, /*drag*/
					-50, 50, /*rotational velocity*/
					0.4f, 0.6f, /*min and max lifetime*/
					325, 760, /*min and max launch speed*/
					0, 3, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
					0, 0, 0, 0); /*keyvalues*/
			this.getOwner().getMap().addParticleEmitter(pe);
		} else {
			ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.CIRCLE_RADIAL, "res/particle_timedilationbad.png", true, /*point, emitter type, image path, alphaDecay*/
					0.4f, 0.6f, /*particle start scale*/
					0.2f, 0.2f, /*particle end scale*/
					2.0f, /*drag*/
					-50, 50, /*rotational velocity*/
					1.0f, 1.2f, /*min and max lifetime*/
					-25, -100, /*min and max launch speed*/
					0, 2, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
					30, 50/(float)Math.pow(this.getMoveSpeedModifier(), 0.25), 0, 0); /*keyvalues*/
			this.getOwner().getMap().addParticleEmitter(pe);
		}
		this.setMoveSpeedModifier((this.getMoveSpeedModifier() - 1) * SPEED_DECAY_PER_INTERVAL + 1);
	}
	@Override
	public void onCreate() {
		
	}
}
