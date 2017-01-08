package projectile;

import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import unit.Unit;

public class EarthCrackerProjectile extends Projectile {
	boolean firstThink;
	public EarthCrackerProjectile(double damage, double speed, int direction, Point gridLoc, int teamID) {
		super(damage, speed, direction, gridLoc, "res/projectile/earthcracker.png", teamID, false, true, false);
		this.setDrawHeight(32);
		this.firstThink = true;
	}
	@Override
	public void onThink() {
		if(!firstThink)
		if(this.getMap().pointIsInGrid(this.getGridLoc()))
		this.getMap().getPanelAt(this.getGridLoc()).crackLight();
		ParticleEmitter pe = new ParticleEmitter(this.getMap().gridToPosition(this.getGridLoc()), EmitterTypes.CIRCLE_DIRECTION, "res/particle_genericWhite.png", false, //point/parent, emitter type, image path, alphaDecay
				4.5f, 6.5f, //particle start scale
				0.0f, 0.0f, //particle end scale
				8.5f, //drag
				0, 0, //rotational velocity
				0.25f, 0.4f, //min and max lifetime
				1200, 1500, //min and max launch speed
				0, 6, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
				0, (float)this.getMap().getSizeOfPanel().y/2, 90, 20); //keyvalues
		this.getMap().addParticleEmitter(pe);
		this.firstThink = false;
	}
}
