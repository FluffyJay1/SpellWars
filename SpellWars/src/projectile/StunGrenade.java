package projectile;

import mechanic.GameMap;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;

public class StunGrenade extends Grenade {
	public static final float AIR_DURATION = 0.7f;
	public static final Point[] AFFECTED_POINTS = {new Point(), new Point(1, 0), new Point(0, 1), new Point(0, -1), new Point(-1, 0)};
	float maxStunDuration;
	float minStunDuration;
	public StunGrenade(int damage, float maxStunDuration, float minStunDuration, int direction, Point gridLoc, int teamID) {
		super(damage, AIR_DURATION, 4, 70, 10, direction, gridLoc, "res/particle_genericBlue.png", teamID);
		this.setImageScale(3);
		this.maxStunDuration = maxStunDuration;
		this.minStunDuration = minStunDuration;
	}
	@Override
	public void flash() {
		if(this.flashDestinationPanel && this.getMap().pointIsInGrid(this.endLoc) && (this.duration - this.timeElapsed <= CONSTANT_FLASH_DURATION || this.timeElapsed % (FLASH_INTERVAL * (1 - this.timeElapsed/this.duration/2)) < (1 - this.timeElapsed/this.duration/2) * FLASH_INTERVAL/2)) {
			for(Point p : AFFECTED_POINTS) {
				Point f = Point.add(p, this.endLoc);
				if(this.getMap().pointIsInGrid(f)) {
					this.getMap().getPanelAt(f).panelFlash();
				}
			}
		}
		if(this.duration - this.timeElapsed <= IMPORTANT_FLASH_DURATION) {
			if(this.getMap().pointIsInGrid(endLoc)) {
				this.getMap().getPanelAt(endLoc).panelFlashImportant();
			}
		}
	}
	@Override
	public void onGrenadeLanded() {
		for(Point p : AFFECTED_POINTS) {
			Point f = Point.add(p, this.endLoc);
			if(this.getMap().pointIsInGrid(f)) {
				if(Point.equals(p, new Point())) {
					if(this.getMap().getPanelAt(f).unitStandingOnPanel != null && this.getMap().getPanelAt(f).unitStandingOnPanel.teamID != this.teamID) {
						this.getMap().getPanelAt(f).unitStandingOnPanel.stun(maxStunDuration);
					}
					ParticleEmitter pe = new ParticleEmitter(this.getMap().gridToPosition(f), EmitterTypes.POINT_DIRECTION, "res/particle_genericBlue.png", false, //point/parent, emitter type, image path, alphaDecay
							3.0f, 1.5f, //particle start scale
							0.0f, 0.0f, //particle end scale
							4.5f, //drag
							0, 0, //rotational velocity
							2.8f, 4.1f, //min and max lifetime
							-100, 700, //min and max launch speed
							0, 18, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
							90, 60, 0, 0); //keyvalues
					this.getMap().addParticleEmitter(pe);
				} else {
					if(this.getMap().getPanelAt(f).unitStandingOnPanel != null && this.getMap().getPanelAt(f).unitStandingOnPanel.teamID != this.teamID) {
						this.getMap().getPanelAt(f).unitStandingOnPanel.stun(minStunDuration);
						this.getMap().getPanelAt(f).unitStandingOnPanel.doDamage(this.grenadeDamage);
					}
					ParticleEmitter pe = new ParticleEmitter(this.getMap().gridToPosition(f), EmitterTypes.POINT_DIRECTION, "res/particle_genericBlue.png", false, //point/parent, emitter type, image path, alphaDecay
							2.5f, 1.5f, //particle start scale
							0.0f, 0.0f, //particle end scale
							4.5f, //drag
							0, 0, //rotational velocity
							1.6f, 2.8f, //min and max lifetime
							-100, 500, //min and max launch speed
							0, 12, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
							90, 60, 0, 0); //keyvalues
					this.getMap().addParticleEmitter(pe);
				}
			}
		}
	}
}
