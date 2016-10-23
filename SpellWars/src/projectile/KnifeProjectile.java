package projectile;

import mechanic.GameMap;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import statuseffect.StatusBleed;
import unit.Unit;

public class KnifeProjectile extends Projectile {
	public static final double SPEED = 18;
	public KnifeProjectile(double damage, int direction, Point gridLoc, int teamID) {
		super(damage, SPEED, direction, gridLoc, "res/projectile/knife.png", teamID, true, true, true);
		this.setDrawHeight(40);
	}
	@Override
	public void onTargetHit(Unit target) {
		target.addStatusEffect(new StatusBleed(target));
		float direction = 0;
		if(this.direction == GameMap.ID_LEFT) {
			direction = 180;
		}
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.getLoc(), new Point(0, -40)), EmitterTypes.POINT_DIRECTION, "res/particle_blood.png", true, //point/parent, emitter type, image path, alphaDecay
				2.5f, 1.5f, //particle start scale
				3.0f, 4.0f, //particle end scale
				1.5f, //drag
				-30, 30, //rotational velocity
				0.1f, 0.3f, //min and max lifetime
				100, 900, //min and max launch speed
				0, 4, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
				direction, 40, 0, 0); //keyvalues
		this.getMap().addParticleEmitter(pe);
	}
}
