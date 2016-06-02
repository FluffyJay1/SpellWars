package statuseffect;

import particlesystem.ParticleEmitter;
import particlesystem.EmitterTypes;
import projectile.Projectile;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


import mechanic.GameElement;


public class StatusFrost extends StatusEffect {
	/*----
	 * STATUS EFFECT: Frost
	 * ---
	 * AFFECTS: Monsters
	 * DAMAGETYPE: Magic
	 * STACKING: No
	 * ID: frost
	 * 
	 * --DESCRIPTION--
	 * Slows and deals damage, once per sec starting on creation
	 * 
	 * --FIXED ATTRIBUTES--
	 * Damage interval: 1
	 * 
	 * --MODIFIED ATTRIBUTES--
	 * speedModifier
	 * damagePerSecond
	 * duration
	 */
	public static final String ID = "frost"; //for stacking
	//public static final DamageType DAMAGE_TYPE = DamageType.MAGIC;
	static Image icon;
	static boolean imageLoaded = false;
	public StatusFrost(GameElement owner, float speedModifier, float damagePerSecond, float duration, int level) {
		super(owner, StackingProperty.UNSTACKABLE_REPLACE, ID, duration, level);
		if(imageLoaded == false) {
			try {
				icon = new Image("res/statuseffect/icon_frost.png");
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.setIcon(icon);
		this.damagePerInterval = damagePerSecond;
		this.interval = 1;
		this.setMoveSpeedModifier(speedModifier);
		//this.setDamageType(DAMAGE_TYPE);
	}
	
	@Override
	public void onInterval() {
		String i = "res/particle_genericBlue.png";
		ParticleEmitter pe = new ParticleEmitter(this.getOwner().getLoc(), EmitterTypes.POINT_RADIAL, i, true, /*point, emitter type, image path, alphaDecay*/
				1.0f, 1.0f, /*particle start scale*/
				2.5f, 2.5f, /*particle end scale*/
				2.0f, /*drag*/
				0, 0, /*rotational velocity*/
				0.5f, 1.5f, /*min and max lifetime*/
				25, 60, /*min and max launch speed*/
				0, 2, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
				0, 0, 0, 0); /*keyvalues*/
		this.getOwner().getMap().addParticleEmitter(pe);
		/*
		if(this.owner.getRemove() == false){
			for(GameElement e : this.owner.getMap().findElementsInArea(owner.getLoc(), 1250)) {
				if(e instanceof Monster) {
					this.owner.getMap().addProjectile(new Projectile(this.owner, e, this.damagePerInterval, DamageType.IMPACT, 150, "res/particle_genericBlue.png"));
				}
			}
		} */
	}
	@Override
	public void onCreate() {
		if(this.getOwner() != null) {
			String i = "res/particle_genericBlue.png";
			ParticleEmitter pe = new ParticleEmitter(this.getOwner().getLoc(), EmitterTypes.POINT_RADIAL, i, true, /*point, emitter type, image path, alphaDecay*/
					1.5f, 1.5f, /*particle start scale*/
					4.0f, 8.0f, /*particle end scale*/
					3.5f, /*drag*/
					0, 0, /*rotational velocity*/
					0.4f, 0.8f, /*min and max lifetime*/
					0, 150, /*min and max launch speed*/
					0, 4, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
					0, 0, 0, 0); /*keyvalues*/
			getOwner().getMap().addParticleEmitter(pe);
		}
	}
}
