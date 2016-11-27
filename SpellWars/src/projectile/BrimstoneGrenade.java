package projectile;

import org.newdawn.slick.Graphics;

import mechanic.GameMap;
import mechanic.Panel;
import mechanic.PanelState;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import particlesystem.Trail;
import statuseffect.StatusFire;
import unit.Unit;

public class BrimstoneGrenade extends Grenade {
	Trail trail;
	float fireDamage;
	float fireDuration;
	public BrimstoneGrenade(double damage, double duration, float fireDamage, float fireDuration, Point endDisplacement, float initialHeight, float endHeight, Point gridLoc, String imagePath, int teamID) {
		super(damage, duration, endDisplacement, initialHeight, endHeight, gridLoc, imagePath, teamID);
		this.setImage("res/projectile/lavagrenade.png");
		this.fireDuration = fireDuration;
		this.fireDamage = fireDamage;
	}
	@Override
	public void onProjectileSetMap() {
		this.trail = new Trail(this, "res/trail_fire.png", 48, 0, 0.18f, 64, false, 0);
		//this.trail.changeLoc(Point.add(this.getLoc(), new Point(0, -this.getDrawHeight())));
		this.trail.resetTrail();
		this.getMap().addGameElement(this.trail);
	}
	@Override
	public void onGrenadeUpdate(){
		//this.trail.changeLoc(Point.add(this.getLoc(), new Point(0, -this.getDrawHeight())));
	}
	@Override
	public void onGrenadeLanded() {
		//String i = "res/particle_genericYellow.png";
		Panel panel = this.getMap().getPanelAt(this.endLoc);
		panel.setPanelState(PanelState.LAVA);
		ParticleEmitter pe = new ParticleEmitter(this.getMap().gridToPosition(this.endLoc), EmitterTypes.CIRCLE_DIRECTION, "res/particle_genericYellow.png", false, //point/parent, emitter type, image path, alphaDecay
				8.5f, 3.5f, //particle start scale
				0.0f, 0.0f, //particle end scale
				4.5f, //drag
				0, 0, //rotational velocity
				0.3f, 0.5f, //min and max lifetime
				10, 1500, //min and max launch speed
				0, 12, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
				0, (float)this.getMap().getSizeOfPanel().y/2, 90, 20); //keyvalues
		this.getMap().addParticleEmitter(pe);
	}
	@Override
	public void onGrenadeTargetHit(Unit target) {
		target.addStatusEffect(new StatusFire(target, this.fireDamage, this.fireDuration));
	}
	@Override
	public void onDraw() {
		this.trail.changeLoc(Point.add(this.getLoc(), new Point(0, -this.getDrawHeight())));
	}
}
