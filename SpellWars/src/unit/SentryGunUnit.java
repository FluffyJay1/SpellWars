package unit;

import mechanic.GameMap;
import mechanic.Panel;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import projectile.SentryGunProjectile;
import spell.SentryGunFire;
import spell.Spell;

public class SentryGunUnit extends Unit {
	public static final double HP = 80;
	public static final float DAMAGE = 5;
	public static final float FIRE_SPEED = 4;
	public static final float FIRE_DELAY = 0.5f;
	float fireDelayTimer;
	boolean enemyDetected;
	boolean isFiring;
	public SentryGunUnit(Point gridLoc, int teamID) {
		super(HP, HP, FIRE_SPEED, GameMap.getOppositeDirection((char)teamID), gridLoc, "res/unit/sentrygun.png", teamID);
		this.setDrawHeight(25);
		this.setSize(0.4);
		this.updatePausedByStuns = true;
		this.fireDelayTimer = FIRE_DELAY;
		this.enemyDetected = false;
		this.isFiring = false;
	}
	@Override
	public void onUpdate() {
		if(!this.enemyDetected) {
			if(this.detectEnemies()) {
				this.enemyDetected = true;
				this.fireDelayTimer = FIRE_DELAY;
				ParticleEmitter pe = new ParticleEmitter(this, EmitterTypes.POINT_DIRECTION, "res/particle_exclamationmark.png", true, //point/parent, emitter type, image path, alphaDecay
						1.0f, 1.0f, //particle start scale
						1.0f, 1.0f, //particle end scale
						2.5f, //drag
						0, 0, //rotational velocity
						FIRE_DELAY, FIRE_DELAY, //min and max lifetime
						400, 400, //min and max launch speed
						0, 1, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
						90, 0, 0, 0); //keyvalues
				this.getMap().addParticleEmitter(pe);
				this.castSpell(new Spell(this, FIRE_DELAY, 0, "windup", "dummy spell", "res/blank.png", false));
			}
		} else {
			if(this.fireDelayTimer > 0) {
				this.fireDelayTimer -= this.getFrameTime();
			} else if(!isFiring) {
				this.isFiring = true;
				this.setThinkIntervalWithMove(true);
				this.onThink();
			}
		}
	}
	public boolean detectEnemies() {
		for(Panel p : this.getMap().getPanelsInRow((int)this.gridLoc.getY())) {
			if(p.unitStandingOnPanel != null && p.unitStandingOnPanel.teamID != this.teamID) {
				if(this.direction == GameMap.ID_RIGHT) {
					if(p.getLoc().x > this.gridLoc.x) {
						return true;
					}
				} else { //other direction
					if(p.getLoc().x < this.gridLoc.x) {
						return true;
					}
				}
			}
		}
		return false;
	}
	@Override
	public void onThink() {
		if(this.detectEnemies()) {
			this.castSpell(new SentryGunFire(this, 1/(float)this.getFinalSpeed(), DAMAGE), false, true, false, true);
		} else {
			this.enemyDetected = false;
			this.isFiring = false;
			this.disableThink();
		}
		
	}
}
