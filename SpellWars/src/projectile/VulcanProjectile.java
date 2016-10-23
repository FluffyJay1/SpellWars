package projectile;

import mechanic.GameMap;
import mechanic.Panel;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import unit.Unit;

public class VulcanProjectile extends Projectile {
	double bonusDamage;
	float stunDuration;
	public VulcanProjectile(double damage, double bonusDamage, float stunDuration, double speed, int direction, Point gridLoc, int teamID) {
		super(damage, speed, direction, gridLoc, "res/projectile/bullet.png", teamID, true, true, true);
		this.setDrawHeight(20 + (float)(Math.random() - 0.5) * 30);
		this.setImageScale(0.75f);
		this.setFlashPanel(false);
		this.bonusDamage = bonusDamage;
		this.stunDuration = stunDuration;
	}
	@Override
	public void onTargetHit(Unit target) {
		if(target.isCasting) {
			target.doDamage(this.bonusDamage);
			target.stun(this.stunDuration);
			ParticleEmitter pe = new ParticleEmitter(target.getLoc(), EmitterTypes.POINT_DIRECTION, "res/particle_crit.png", true, //point/parent, emitter type, image path, alphaDecay
					1.0f, 1.0f, //particle start scale
					1.0f, 1.0f, //particle end scale
					8.5f, //drag
					0, 0, //rotational velocity
					1.0f, 1.0f, //min and max lifetime
					900, 900, //min and max launch speed
					0, 1, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
					90, 0, 0, 0); //keyvalues
			this.getMap().addParticleEmitter(pe);
		}
		float direction = 0;
		if(this.direction == GameMap.ID_LEFT) {
			direction = 180;
		}
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.getLoc(), new Point(0, -this.getDrawHeight())), EmitterTypes.POINT_DIRECTION, "res/particle_genericYellow.png", false, //point/parent, emitter type, image path, alphaDecay
				3.5f, 1.5f, //particle start scale
				0.0f, 0.0f, //particle end scale
				13.5f, //drag
				0, 0, //rotational velocity
				0.05f, 0.15f, //min and max lifetime
				200, 12200, //min and max launch speed
				0, 4, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
				direction, 10, 0, 0); //keyvalues
		this.getMap().addParticleEmitter(pe);
	}
}
