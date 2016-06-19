package projectile;

import mechanic.GameMap;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import particlesystem.Trail;
import unit.Unit;

public class Grenade extends Projectile {
	int distance;
	public static final double ACCELERATION = 981;
	public static final double FLASH_INTERVAL = 0.32;
	public static final double IMPORTANT_FLASH_DURATION = 0.35;
	public static final double CONSTANT_FLASH_DURATION = 0.2;
	//height is calculated based on ax^2 + bx + c
	//float a;
	float b;
	float c;
	float duration; //set upon construction, used to calculate how high the grenade is
	public float timeElapsed; //time elapsed since start
	public Point startLoc;
	public Point endLoc;
	
	int damage;
	boolean hasHitApex;
	public boolean flashDestinationPanel;
	public Grenade(int damage, double duration, int distance, float initialHeight, float endHeight, int direction, Point gridLoc, String imagePath, int teamID) {
		super(0, distance/duration, direction, gridLoc, imagePath, teamID, false, true, true);
		this.distance = distance;
		this.duration = (float)duration;
		this.startLoc = Point.clone(gridLoc);
		this.endLoc = Point.add(Point.scale(GameMap.getFuturePoint(new Point(), (char)direction), distance), gridLoc);
		//this.a = (float) -(Math.pow((distance - initialHeight/distance), 2)/(4 * maxHeight - initialHeight)); //this is wrong 
		this.b = (float) (ACCELERATION * duration - (initialHeight - endHeight)/duration);
		this.c = initialHeight;
		this.timeElapsed = 0;
		this.flashPanel = false;
		this.flashDestinationPanel = true;
		this.drawShadow = true;
		this.damage = damage;
		this.hasHitApex = false;
	}
	public Grenade(int damage, double duration, Point endDisplacement, float initialHeight, float endHeight, Point gridLoc, String imagePath, int teamID) {
		super(0, 1/duration, endDisplacement, gridLoc, imagePath, teamID, false, true, true);
		this.distance = (int) Point.getDistance(new Point(), endDisplacement);
		this.duration = (float)duration;
		this.startLoc = Point.clone(gridLoc);
		this.endLoc = Point.add(gridLoc, endDisplacement);
		this.b = (float) (ACCELERATION * duration - (initialHeight - endHeight)/duration);
		this.c = initialHeight;
		this.timeElapsed = 0;
		this.flashPanel = false;
		this.flashDestinationPanel = true;
		this.drawShadow = true;
		this.damage = damage;
		this.hasHitApex = false;
	}
	@Override
	public void flash() {
		if(this.flashDestinationPanel && this.getMap().pointIsInGrid(this.endLoc) && (this.duration - this.timeElapsed <= CONSTANT_FLASH_DURATION || this.timeElapsed % (FLASH_INTERVAL * (1 - this.timeElapsed/this.duration/2)) < (1 - this.timeElapsed/this.duration/2) * FLASH_INTERVAL/2)) {
			this.getMap().getPanelAt(endLoc).panelFlash();
		}
		if(this.duration - this.timeElapsed <= IMPORTANT_FLASH_DURATION) {
			if(this.getMap().pointIsInGrid(endLoc)) {
				this.getMap().getPanelAt(endLoc).panelFlashImportant();
			}
		}
	}
	@Override
	public void onProjectileUpdate() {
		this.timeElapsed += this.getFrameTime() * this.getFinalSpeed() / this.getSpeed();
		this.setDrawHeight(this.getHeightAt(this.timeElapsed));
		//if(this.distance * (1 - this.timeElapsed/this.duration) < 0.5) { //if it is on the last panel
		if(this.timeElapsed >= this.duration) {
			if(this.getMap().pointIsInGrid(this.endLoc)) {
				Unit target = this.getMap().getPanelAt(this.endLoc).unitStandingOnPanel;
				if(target != null && target.teamID != this.teamID && !this.getRemove()){
					target.doDamage(this.damage, false, this);
					this.onGrenadeHitTarget(target);
				}
				if(this.timeElapsed >= this.duration) {
					this.onGrenadeLanded();
					this.setRemove(true);
				}
			}
		}
		if(!this.hasHitApex && this.timeElapsed >= ACCELERATION/b/2) {
			this.hasHitApex = true;
		}
		/*
		System.out.println("frametime IS: " + this.getFrameTime());
		System.out.println(this.getHeightAt(this.timeElapsed));
		*/
	}
	public void onGrenadeLanded() {
		//String i = "res/particle_genericYellow.png";
		ParticleEmitter pe = new ParticleEmitter(this.getLoc(), EmitterTypes.POINT_RADIAL, GameMap.particle_explosion, true, //point/parent, emitter type, image path, alphaDecay
				0.4f, 0.6f, //particle start scale
				1.2f, 1.7f, //particle end scale
				6.5f, //drag
				-700, 700, //rotational velocity
				0.1f, 0.45f, //min and max lifetime
				200, 1000, //min and max launch speed
				0, 3, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
				0, 0, 0, 0); //keyvalues
		this.getMap().addParticleEmitter(pe);
	}
	public void onGrenadeHitTarget(Unit target) {
		
	}
	public float getHeightAt(float time) {
		float x = time;
		return (float) (-ACCELERATION*x*x + b*x + c);
	}
}
