package particlesystem;

import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ParticleEmitter extends GameElement {
	private GameMap map;
	private String particlePic;
	private float particleDrag; //BETWEEN 0 AND 100
	private float particleMinRVel; //ROTATIONAL VELOCITY
	private float particleMaxRVel;
	private float particleMinLifetime;
	private float particleMaxLifetime;
	private float particleMinStartScale;
	private float particleMaxStartScale;
	private float particleMinEndScale;
	private float particleMaxEndScale;
	private boolean particleAlphaDecay;
	private float kv1, kv2, kv3, kv4;

	private emitterTypes emitterType;
	private float minLaunchSpeed;
	private float maxLaunchSpeed;
	private float emitterLifetime;
	private float emissionRate; //PARTICLES PER SECOND
	private float emissionInterval; //Calculated on creation
	private float emissionTimer;
	private boolean isInstant;
	
	//public float fps;
	
	public ParticleEmitter(Point position, emitterTypes emitterType, String particlePic, boolean particleAlphaDecay, //simple version without the mins and maxes
			float particleDrag, float particleRVel, float particleLifetime, float particleStartScale, float particleEndScale,
			float launchSpeed, float emitterLifetime, float emissionRate,
			float kv1, float kv2, float kv3, float kv4, GameMap map) {
		this(position, emitterType, particlePic, particleAlphaDecay,
				particleStartScale, particleStartScale,
				particleEndScale, particleEndScale,
				particleDrag,
				particleRVel, particleRVel,
				particleLifetime, particleLifetime,
				launchSpeed, launchSpeed,
				emitterLifetime, emissionRate,
				kv1, kv2, kv3, kv4, map);
	}
	public ParticleEmitter(Point position, emitterTypes emitterType, String particlePic, boolean particleAlphaDecay,/*Creates a particle emitter at .position, type is .emitterType, filepath of image is .particlePic, .particleAlphaDecay controls if particle fades out*/
			float particleMinStartScale, float particleMaxStartScale, /*how much to scale the image at the start*/
			float particleMinEndScale, float particleMaxEndScale, /*how much to scale the image at the end*/
			float particleDrag, /*How much the particle slows down, as fraction of speed to lose per sec*/
			float particleMinRVel, float particleMaxRVel, /*the minimum and maximum speeds the particle can spin at, in degrees per sec*/
			float particleMinLifetime, float particleMaxLifetime, /*the minimum and maximum seconds the particle can live for, in seconds*/
			float minLaunchSpeed, float maxLaunchSpeed, /*the minimum and maximum speeds the particle can start at, in pixels per second*/
			float emitterLifetime, float emissionRate, /*the lifetime of the emitter itself in seconds, and .emissionRate is how often the emitter emits particles, in particles per second, and if the lifetime of the emitter is 0, then the emission rate becomes the number of particles to emit at once*/
			float kv1, float kv2, float kv3, float kv4, GameMap map) { /*.kv1 to .kv4 are just for extra parameters to pass to the function based on its .emitterType, the .map is for adding particles*/
		this.getLoc().changeX(position.getX());
		this.getLoc().changeY(position.getY());
		this.emitterType = emitterType;
		this.particlePic = particlePic;
		this.particleDrag = particleDrag;
		this.particleMinRVel = particleMinRVel;
		this.particleMaxRVel = particleMaxRVel;
		this.particleMinLifetime = particleMinLifetime;
		this.particleMaxLifetime = particleMaxLifetime;
		this.particleMinStartScale = particleMinStartScale;
		this.particleMaxStartScale = particleMaxStartScale;
		this.particleMinEndScale = particleMinEndScale;
		this.particleMaxEndScale = particleMaxEndScale;
		this.minLaunchSpeed = minLaunchSpeed;
		this.maxLaunchSpeed = maxLaunchSpeed;
		this.emitterLifetime = emitterLifetime;
		this.emissionRate = emissionRate;
			if(emissionRate != 0) {this.emissionInterval = 1 / emissionRate;}
			else{this.emissionInterval = -1;}
			if(emitterLifetime == 0) {this.isInstant = true;}
			else{this.isInstant = false;}
		this.emissionTimer = 0;
		this.particleAlphaDecay = particleAlphaDecay;
		this.kv1 = kv1;
		this.kv2 = kv2;
		this.kv3 = kv3;
		this.kv4 = kv4;
		this.map = map;
	}
	
	@Override
	public void update(){
		if(emissionTimer <= 0) {
			if(emitterType == emitterTypes.POINT){
				if(isInstant == false) {
					emitPoint();
				} else {
					for(int i = 0; i < emissionRate; i++) {
						emitPoint();
					}
				}
			}
			if(emitterType == emitterTypes.CIRCLE_RANDOM){
				if(isInstant == false) {
					//emitCircleRandom();
				} else {
					for(int i = 0; i < emissionRate; i++) {
						//emitCircleRandom();
					}
				}
			}
			if(emitterType == emitterTypes.LINE_RANDOM){
				if(isInstant == false) {
					//emitLineRandom();
				} else {
					for(int i = 0; i < emissionRate; i++) {
						//emitLineRandom();
					}
				}
			}
			emissionTimer = emissionInterval;
		}
		emissionTimer -= 1/this.getFPS();
		emitterLifetime -= 1/this.getFPS();
		if(emitterLifetime <= 0) {
			this.setRemove(true);
		}
	}
	
	private void emitPoint(){
		Point tempVel = new Point();
		Point tempLoc = this.getLoc();

		float randomDegree = (float)(Math.random() * 360); //random angle
		float randomSpeed = (float) (this.minLaunchSpeed + Math.random() * (this.maxLaunchSpeed - this.minLaunchSpeed)); //interpolates between min and max launch speed
		tempVel.changeX((double) Math.cos((double) randomDegree) * randomSpeed); //translate from circle coordinates to x and y
		tempVel.changeY((double) Math.sin((double) randomDegree) * randomSpeed);
		
		float randomStartScale = (this.particleMinStartScale + (float)Math.random() * (this.particleMaxStartScale - this.particleMinStartScale));
		float randomEndScale = (this.particleMinEndScale + (float)Math.random() * (this.particleMaxEndScale - this.particleMinEndScale));
		
		float randomRVel = (float) (this.particleMinRVel + Math.random() * (this.particleMaxRVel - this.particleMinRVel)); //interpolates between min and max rvel
		float randomLifetime = (float) (this.particleMinLifetime + Math.random() * (this.particleMaxLifetime - this.particleMinLifetime)); //interpolates between min and max lifetime
		ParticleBase particle = new ParticleBase(new Point(tempLoc.getX(), tempLoc.getY()), tempVel, this.particleDrag, randomDegree, 
				randomRVel, randomLifetime, this.particlePic, this.particleAlphaDecay, randomStartScale, randomEndScale);
		map.addParticle(particle); //adds the particle
	}
}
