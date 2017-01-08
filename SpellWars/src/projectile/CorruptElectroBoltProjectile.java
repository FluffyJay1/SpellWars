package projectile;

import mechanic.GameMap;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import unit.Unit;

public class CorruptElectroBoltProjectile extends Projectile {
	public static final float STUN_DURATION_MULTIPLIER = 0.9f;
	public static final float DAMAGE_REDUCTION = 10;
	public static final double SPEED_BOOST_PER_SECOND = 15;
	float stunDuration;
	double initialSpeed;
	public CorruptElectroBoltProjectile(double damage, double speed, int direction, Point gridLoc, int teamID, float stunDuration) {
		super(damage, speed, direction, gridLoc, "res/projectile/corruptelectricity.png", teamID, true, true, true);
		this.setDrawHeight(30);
		this.setImageScale((float)this.getFinalDamage()/16 + 1.5f);
		this.stunDuration = stunDuration;
		this.initialSpeed = speed;
	}
	@Override
	public void onProjectileUpdate() {
		this.setImageScale((float)this.getFinalDamage()/16 + 1.5f);
		this.setSpeed(this.getSpeed() + SPEED_BOOST_PER_SECOND * this.getFrameTime() * this.getFinalSpeed()/this.getSpeed());
	}
	@Override
	public void onThink() {
		this.changeOrientation(this.getOrientation() + 90);
		if(!this.getMap().pointIsInGrid(GameMap.getFuturePoint(this.getGridLoc(), (char)this.direction)) && this.getDamage() > DAMAGE_REDUCTION) {
			this.setRemove(true);
			for(int direction = 0; direction < 8; direction++) {
				if(this.getMap().pointIsInGrid(GameMap.getFuturePoint(this.getGridLoc(), (char)direction))) {
					CorruptElectroBoltProjectile p = new CorruptElectroBoltProjectile(this.getDamage() - DAMAGE_REDUCTION, this.initialSpeed, direction, this.getGridLoc(), this.teamID, this.stunDuration * STUN_DURATION_MULTIPLIER);
					this.getMap().addGameElement(p, true);
				}
			}
		}
	}
	@Override
	public void onTargetHit(Unit target) {
		target.stun(this.stunDuration);
		target.purgeBuffs();
		ParticleEmitter pe = new ParticleEmitter(Point.add(target.getLoc(), new Point(0, -50)), EmitterTypes.CIRCLE_DIRECTION, "res/particle_genericPurple.png", false, //point/parent, emitter type, image path, alphaDecay
				2.5f, 3.5f, //particle start scale
				0.0f, 0.0f, //particle end scale
				1.5f, //drag
				0, 0, //rotational velocity
				this.stunDuration/2, this.stunDuration, //min and max lifetime
				-75, 75, //min and max launch speed
				0, 6, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
				0, (float)this.getMap().getSizeOfPanel().x/3, 90, 20); //keyvalues
		this.getMap().addParticleEmitter(pe);
		if(this.getDamage() > DAMAGE_REDUCTION) {
			for(int direction = 0; direction < 8; direction++) {
				if(this.getMap().pointIsInGrid(GameMap.getFuturePoint(this.getGridLoc(), (char)direction))) {
					CorruptElectroBoltProjectile p = new CorruptElectroBoltProjectile(this.getDamage() - DAMAGE_REDUCTION, this.initialSpeed, direction, target.gridLoc, this.teamID, this.stunDuration * STUN_DURATION_MULTIPLIER);
					p.addToUnitsHit(target);
					this.getMap().addGameElement(p, true);
				}
			}
		}
	}
}
