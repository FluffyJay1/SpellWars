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
	static Image icon;
	static boolean imageLoaded = false;
	//float damageChange;
	public StatusFrost(GameElement owner, float speedModifier, float damagePerSecond, float duration, float damageChange, int level) {
		super(owner, StackingProperty.STACKABLE_INDEPENDENT, ID, duration, level);
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
		//this.damageChange = damageReduction;
		if(this.getOwner() instanceof Projectile) {
			this.setAttackDamageModifier(damageChange);
		}
		//this.setDamageType(DAMAGE_TYPE);
	}
	
	@Override
	public void onInterval() {
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.POINT_RADIAL, "res/particle_genericBlue.png", true, /*point, emitter type, image path, alphaDecay*/
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
			ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.POINT_RADIAL, "res/particle_genericBlue.png", true, /*point, emitter type, image path, alphaDecay*/
					1.5f, 1.5f, /*particle start scale*/
					4.0f, 8.0f, /*particle end scale*/
					3.5f, /*drag*/
					0, 0, /*rotational velocity*/
					0.4f, 0.8f, /*min and max lifetime*/
					0, 350, /*min and max launch speed*/
					0, 6, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
					0, 0, 0, 0); /*keyvalues*/
			getOwner().getMap().addParticleEmitter(pe);
		}
	}
}
