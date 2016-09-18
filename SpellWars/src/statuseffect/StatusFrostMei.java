package statuseffect;

import particlesystem.ParticleEmitter;
import particlesystem.EmitterTypes;
import projectile.Projectile;
import unit.Unit;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;


public class StatusFrostMei extends StatusEffect {
	/*----
	 * 5 instances and target is stunned
	 */
	public static final String ID = "meifrost"; //for stacking
	public static final int NUM_STACKS_TO_STUN = 20;
	public static final float SPEED_MODIFIER = 0.88f;
	public static final float DURATION = 9;
	public static final float CHECK_INTERVAL = 0.25f;
	public static final double EMIT_CHANCE = 0.25;
	//public static final DamageType DAMAGE_TYPE = DamageType.MAGIC;
	static Image icon;
	static boolean imageLoaded = false;
	//float damageChange;
	public StatusFrostMei(GameElement owner, int level) {
		super(owner, StackingProperty.STACKABLE_INDEPENDENT, ID, DURATION, level);
		if(imageLoaded == false) {
			try {
				icon = new Image("res/statuseffect/icon_frost.png");
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.setIcon(icon);
		this.damagePerInterval = 0;
		this.interval = CHECK_INTERVAL;
		this.setMoveSpeedModifier(SPEED_MODIFIER);
	}
	
	@Override
	public void onInterval() {
		this.setColorModifier(new Color(180, 180, 255));
		if(Math.random() < EMIT_CHANCE) {
			ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.POINT_RADIAL, "res/particle_genericBlue.png", true, /*point, emitter type, image path, alphaDecay*/
					1.0f, 1.0f, /*particle start scale*/
					2.5f, 2.5f, /*particle end scale*/
					2.0f, /*drag*/
					0, 0, /*rotational velocity*/
					0.5f, 1.5f, /*min and max lifetime*/
					25, 25 + 10 * this.getOwner().getStatusEffectCount(ID), /*min and max launch speed*/
					0, 1, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
					0, 0, 0, 0); /*keyvalues*/
			this.getOwner().getMap().addParticleEmitter(pe);
		}
		if(this.getOwner().getStatusEffectCount(ID) >= NUM_STACKS_TO_STUN && this.getOwner() instanceof Unit) {
			((Unit)this.getOwner()).stun(this.interval);
			this.setColorModifier(new Color(100, 100, 255));
			ParticleEmitter pe2 = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.CIRCLE_RADIAL, "res/particle_genericBlue.png", true, /*point, emitter type, image path, alphaDecay*/
					1.0f, 1.0f, /*particle start scale*/
					2.5f, 2.5f, /*particle end scale*/
					2.0f, /*drag*/
					0, 0, /*rotational velocity*/
					0.5f, 1.5f, /*min and max lifetime*/
					-25, -25 - 10 * this.getOwner().getStatusEffectCount(ID), /*min and max launch speed*/
					0, 1, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
					80, 80, 0, 0); /*keyvalues*/
			this.getOwner().getMap().addParticleEmitter(pe2);
		}
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
