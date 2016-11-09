package spell;

import mechanic.GameMap;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import projectile.Projectile;
import projectile.ShotgunBlastProjectile;
import unit.Unit;

public class ShotgunBlast extends Spell {
	static final Point[] MOVEVEC = {new Point(1, 0),
									new Point(1, 2.0/3),
									new Point(1, -2.0/3),
									new Point(1, 1.0/5),
									new Point(1, -1.0/5)};
	public static final double DAMAGE = 20;
	public static final double DAMAGE_REDUCTION_PER_THINK = 5;
	public static final double SPEED = 18;
	public ShotgunBlast(Unit owner) {
		super(owner, 0, 0.5f, "Shotgun Blast", "Shoots multiple bullets in a spread, bullets deal less damage the farther they go", "res/spell/shotgunblast.png", false);
	}
	public void onActivate() {
		for(Point vec : MOVEVEC) {
			Point moveVec = Point.clone(vec);
			if(this.owner.direction == GameMap.ID_LEFT) {
				moveVec.x *= -1;
			}
			Projectile p = new ShotgunBlastProjectile(DAMAGE * this.owner.finalDamageModifier, SPEED / Point.getDistance(new Point(), moveVec), moveVec, this.owner.gridLoc, this.owner.teamID, DAMAGE_REDUCTION_PER_THINK);
			this.getMap().addGameElement(p);
		}
		float direction = 0;
		if(this.owner.direction == GameMap.ID_LEFT) {
			direction = 180;
		}
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.owner.getLoc(), new Point(0, -28)), EmitterTypes.POINT_DIRECTION, "res/particle_genericWhite.png", false, //point/parent, emitter type, image path, alphaDecay
				6.5f, 2.5f, //particle start scale
				0.0f, 0.0f, //particle end scale
				16.5f, //drag
				0, 0, //rotational velocity
				0.1f, 0.45f, //min and max lifetime
				200, 12200, //min and max launch speed
				0, 18, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
				direction, 40, 0, 0); //keyvalues
		this.getMap().addParticleEmitter(pe);
	}
}
