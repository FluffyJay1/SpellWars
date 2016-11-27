package statuseffect;

import particlesystem.ParticleEmitter;
import particlesystem.EmitterTypes;
import projectile.Projectile;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;


public class StatusFrost extends StatusEffect {
	public static final String ID = "frost"; //for stacking
	//public static final DamageType DAMAGE_TYPE = DamageType.MAGIC;
	//float damageChange;
	public StatusFrost(GameElement owner, float speedModifier, float damagePerSecond, float duration, float damageChange, int level) {
		super(owner, StackingProperty.STACKABLE_INDEPENDENT, ID, duration, false, true, level);
		this.setIcon("res/statuseffect/icon_frost.png");
		this.damagePerInterval = damagePerSecond;
		this.interval = 1;
		this.setMoveSpeedModifier(speedModifier);
		//this.damageChange = damageReduction;
		if(this.getOwner() instanceof Projectile) {
			this.setAttackDamageModifier(damageChange);
		}
		//this.setDamageType(DAMAGE_TYPE);
	}
	@Override
	public StatusEffect clone() {
		StatusEffect effect = new StatusFrost(this.getOwner(), 0, 0, 0, 0, 0);
		StatusEffect.copyFromTo(this, effect);
		return effect;
	}
	@Override
	public void onInterval() {
		
	}
	@Override
	public void onCreate() {
		if(this.getOwner() != null) {
			ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.POINT_RADIAL, "res/particle_genericBlue.png", true, /*point, emitter type, image path, alphaDecay*/
					1.5f, 1.5f, /*particle start scale*/
					4.0f, 8.0f, /*particle end scale*/
					3.5f, /*drag*/
					0, 0, /*rotational velocity*/
					0.4f, 0.8f, /*min and max lifetime*/
					0, 350, /*min and max launch speed*/
					0, 1, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
					0, 0, 0, 0); /*keyvalues*/
			getOwner().getMap().addParticleEmitter(pe);
		}
	}
}
