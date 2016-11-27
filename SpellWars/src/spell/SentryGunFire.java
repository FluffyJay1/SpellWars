package spell;

import mechanic.GameMap;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import projectile.SentryGunProjectile;
import unit.Unit;

public class SentryGunFire extends Spell {
	float damage;
	public static final float PROJECTILE_SPEED = 28;
	public SentryGunFire(Unit owner, float fireInterval, float damage) {
		super(owner, 0, fireInterval, "Sentry Gun Fire", "Shoot a bullet that penetrates one person", "res/projectile/bullet.png", false);
		this.damage = damage;
	}
	@Override
	public void onActivate() {
		SentryGunProjectile p = new SentryGunProjectile(this.damage * this.owner.finalDamageOutputModifier, PROJECTILE_SPEED, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
		this.getMap().addGameElement(p);
		float direction = 0;
		if(this.owner.direction == GameMap.ID_LEFT) {
			direction = 180;
		}
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.owner.getLoc(), new Point(0, -60)), EmitterTypes.POINT_DIRECTION, "res/particle_explosion.png", false, //point/parent, emitter type, image path, alphaDecay
				0.1f, 0.25f, //particle start scale
				0.0f, 0.0f, //particle end scale
				11.5f, //drag
				0, 0, //rotational velocity
				0.1f, 0.25f, //min and max lifetime
				1000, 1400, //min and max launch speed
				0, 5, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
				direction, 15, 0, 0); //keyvalues
		this.getMap().addParticleEmitter(pe);
	}
}
