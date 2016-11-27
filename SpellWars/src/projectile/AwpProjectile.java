package projectile;

import mechanic.GameMap;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import particlesystem.Trail;
import unit.Unit;

public class AwpProjectile extends Projectile {
	double penetrationModifier;
	public Trail trail;
	public AwpProjectile(double damage, double penetrationModifier, double speed, int direction, Point gridLoc, int teamID) {
		super(damage, speed, direction, gridLoc, "res/projectile/bullet.png", teamID, false, true, true);
		this.penetrationModifier = penetrationModifier;
		this.setFlashPanel(false);
		this.setDrawHeight(32);
		this.setImageScale(2);
		this.setPenetrateShields(true);
	}
	@Override
	public void onProjectileSetMap() {
		this.trail = new Trail(this, "res/trail_fire.png", 48, 0, 0.4f, 64, false, 0);
		//this.trail.changeLoc(Point.add(this.getLoc(), new Point(0, -this.getDrawHeight())));
		this.trail.resetTrail();
		this.getMap().addGameElement(this.trail);
	}
	@Override
	public void onTargetHit(Unit target) {
		this.setDamage(this.getDamage() * penetrationModifier);
		float direction = 0;
		if(this.direction == GameMap.ID_RIGHT) {
			direction = 180;
		}
		ParticleEmitter pe = new ParticleEmitter(Point.add(target.getLoc(), new Point(0, -28)), EmitterTypes.POINT_DIRECTION, "res/particle_genericWhite.png", false, //point/parent, emitter type, image path, alphaDecay
				4.5f, 1.5f, //particle start scale
				0.0f, 0.0f, //particle end scale
				12.5f, //drag
				0, 0, //rotational velocity
				0.3f, 0.5f, //min and max lifetime
				200, 4000, //min and max launch speed
				0, 6, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
				direction, 90, 0, 0); //keyvalues
		this.getMap().addParticleEmitter(pe);
	}
}
