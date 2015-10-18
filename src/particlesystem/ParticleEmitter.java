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
			if(emitterType == emitterTypes.POINT_RADIAL){
				if(isInstant == false) {
					emitPointRadial();
				} else {
					for(int i = 0; i < emissionRate; i++) {
						emitPointRadial();
					}
				}
			}
			if(emitterType == emitterTypes.POINT_DIRECTION){
				if(isInstant == false) {
					emitPointDirection();
				} else {
					for(int i = 0; i < emissionRate; i++) {
						emitPointDirection();
					}
				}
			}
			if(emitterType == emitterTypes.CIRCLE_RADIAL){
				if(isInstant == false) {
					emitCircleRadial();
				} else {
					for(int i = 0; i < emissionRate; i++) {
						emitCircleRadial();
					}
				}
			}
			if(emitterType == emitterTypes.CIRCLE_RANDOM){
				if(isInstant == false) {
					emitCircleRandom();
				} else {
					for(int i = 0; i < emissionRate; i++) {
						emitCircleRandom();
					}
				}
			}
			if(emitterType == emitterTypes.LINE_RADIAL){
				if(isInstant == false) {
					emitLineRadial();
				} else {
					for(int i = 0; i < emissionRate; i++) {
						emitLineRadial();
					}
				}
			}
			if(emitterType == emitterTypes.LINE_RANDOM){
				if(isInstant == false) {
					emitLineRandom();
				} else {
					for(int i = 0; i < emissionRate; i++) {
						emitLineRandom();
					}
				}
			}
			emissionTimer = emissionInterval;
		}
		emissionTimer -= this.getFrameTime();
		emitterLifetime -= this.getFrameTime();
		if(emitterLifetime <= 0) {
			this.setRemove(true);
		}
	}
	
	private void emitPointRadial(){
		Point tempVel = new Point();
		Point tempLoc = new Point(this.getLoc().getX(), this.getLoc().getY()); //for some reason whenever i change temploc the emitter's loc gets changed as well

		float randomRadian = (float)(Math.random() * 2 * Math.PI); //random angle
		float randomSpeed = (float) (this.minLaunchSpeed + Math.random() * (this.maxLaunchSpeed - this.minLaunchSpeed)); //interpolates between min and max launch speed
		tempVel.changeX((double) Math.cos((double) randomRadian) * randomSpeed); //translate from circle coordinates to x and y
		tempVel.changeY((double) -Math.sin((double) randomRadian) * randomSpeed); //NEGATED BECAUSE WRONG DIRECTION
		
		float randomStartScale = (this.particleMinStartScale + (float)Math.random() * (this.particleMaxStartScale - this.particleMinStartScale));
		float randomEndScale = (this.particleMinEndScale + (float)Math.random() * (this.particleMaxEndScale - this.particleMinEndScale));
		
		float randomRVel = (float) (this.particleMinRVel + Math.random() * (this.particleMaxRVel - this.particleMinRVel)); //interpolates between min and max rvel
		float randomLifetime = (float) (this.particleMinLifetime + Math.random() * (this.particleMaxLifetime - this.particleMinLifetime)); //interpolates between min and max lifetime
		
		ParticleBase particle = new ParticleBase(tempLoc, tempVel, this.particleDrag, (float)Math.toDegrees(randomRadian), 
				randomRVel, randomLifetime, this.particlePic, this.particleAlphaDecay, randomStartScale, randomEndScale);
		map.addParticle(particle); //adds the particle
	}
	private void emitPointDirection(){
		Point tempVel = new Point();
		Point tempLoc = new Point(this.getLoc().getX(), this.getLoc().getY()); //for some reason whenever i change temploc the emitter's loc gets changed as well

		float randomRadian = (float) ((Math.random() - 0.5f) * Math.toRadians(this.kv2)); //SPREAD
		randomRadian += Math.toRadians(this.kv1); //KV1 IS DIRECTION
		float randomSpeed = (float) (this.minLaunchSpeed + Math.random() * (this.maxLaunchSpeed - this.minLaunchSpeed)); //interpolates between min and max launch speed
		tempVel.changeX((double) Math.cos((double) randomRadian) * randomSpeed); //translate from circle coordinates to x and y
		tempVel.changeY((double) -Math.sin((double) randomRadian) * randomSpeed); //NEGATED BECAUSE WRONG DIRECTION
		
		float randomStartScale = (this.particleMinStartScale + (float)Math.random() * (this.particleMaxStartScale - this.particleMinStartScale));
		float randomEndScale = (this.particleMinEndScale + (float)Math.random() * (this.particleMaxEndScale - this.particleMinEndScale));
		
		float randomRVel = (float) (this.particleMinRVel + Math.random() * (this.particleMaxRVel - this.particleMinRVel)); //interpolates between min and max rvel
		float randomLifetime = (float) (this.particleMinLifetime + Math.random() * (this.particleMaxLifetime - this.particleMinLifetime)); //interpolates between min and max lifetime
		
		ParticleBase particle = new ParticleBase(tempLoc, tempVel, this.particleDrag, (float)Math.toDegrees(randomRadian), 
				randomRVel, randomLifetime, this.particlePic, this.particleAlphaDecay, randomStartScale, randomEndScale);
		map.addParticle(particle); //adds the particle
	}
	private void emitCircleRadial(){
		Point tempVel = new Point();
		Point tempLoc = new Point(this.getLoc().getX(), this.getLoc().getY()); //for some reason whenever i change temploc the emitter's loc gets changed as well

		float randomRadian = (float)(Math.random() * 2 * Math.PI); //random angle
		float randomSpeed = (float) (this.minLaunchSpeed + Math.random() * (this.maxLaunchSpeed - this.minLaunchSpeed)); //interpolates between min and max launch speed
		tempVel.changeX((double) Math.cos((double) randomRadian) * randomSpeed); //translate from circle coordinates to x and y
		tempVel.changeY((double) -Math.sin((double) randomRadian) * randomSpeed);
		
		float lowerboundsquared = (this.kv1/this.kv2) * (this.kv1/this.kv2); //as a ratio of max radius
		float distributedRandom = (float) Math.sqrt((lowerboundsquared + Math.random() * (1 - lowerboundsquared))); //Distributes the random evenly, since normal random would result in particles bunching up near the center
		float randomRadius = distributedRandom * this.kv2; //distributed random (which accounts for lower bound now) gets multiplied by the max radius to get an evenly distributed radius
		
		tempLoc.addX((double) Math.cos((double) randomRadian) * randomRadius);
		tempLoc.addY((double) -Math.sin((double) randomRadian) * randomRadius);
		
		float randomStartScale = (this.particleMinStartScale + (float)Math.random() * (this.particleMaxStartScale - this.particleMinStartScale));
		float randomEndScale = (this.particleMinEndScale + (float)Math.random() * (this.particleMaxEndScale - this.particleMinEndScale));
		
		float randomRVel = (float) (this.particleMinRVel + Math.random() * (this.particleMaxRVel - this.particleMinRVel)); //interpolates between min and max rvel
		float randomLifetime = (float) (this.particleMinLifetime + Math.random() * (this.particleMaxLifetime - this.particleMinLifetime)); //interpolates between min and max lifetime
		ParticleBase particle = new ParticleBase(tempLoc, tempVel, this.particleDrag, (float)Math.toDegrees(randomRadian), 
				randomRVel, randomLifetime, this.particlePic, this.particleAlphaDecay, randomStartScale, randomEndScale);
		map.addParticle(particle); //adds the particle
	}
	private void emitCircleRandom(){
		Point tempVel = new Point();
		Point tempLoc = new Point(this.getLoc().getX(), this.getLoc().getY()); //for some reason whenever i change temploc the emitter's loc gets changed as well

		float randomRadian = (float)(Math.random() * 2 * Math.PI); //random angle
		float randomRadian2 = (float)(Math.random() * 2 * Math.PI); //random angle for placement
		float randomSpeed = (float) (this.minLaunchSpeed + Math.random() * (this.maxLaunchSpeed - this.minLaunchSpeed)); //interpolates between min and max launch speed
		tempVel.changeX((double) Math.cos((double) randomRadian) * randomSpeed); //translate from circle coordinates to x and y
		tempVel.changeY((double) -Math.sin((double) randomRadian) * randomSpeed);
		
		float lowerboundsquared = (this.kv1/this.kv2) * (this.kv1/this.kv2); //as a ratio of max radius
		float distributedRandom = (float) Math.sqrt((lowerboundsquared + Math.random() * (1 - lowerboundsquared))); //Distributes the random evenly, since normal random would result in particles bunching up near the center
		float randomRadius = distributedRandom * this.kv2; //distributed random (which accounts for lower bound now) gets multiplied by the max radius to get an evenly distributed radius
		
		tempLoc.addX((double) Math.cos((double) randomRadian2) * randomRadius);
		tempLoc.addY((double) -Math.sin((double) randomRadian2) * randomRadius);
		
		float randomStartScale = (this.particleMinStartScale + (float)Math.random() * (this.particleMaxStartScale - this.particleMinStartScale));
		float randomEndScale = (this.particleMinEndScale + (float)Math.random() * (this.particleMaxEndScale - this.particleMinEndScale));
		
		float randomRVel = (float) (this.particleMinRVel + Math.random() * (this.particleMaxRVel - this.particleMinRVel)); //interpolates between min and max rvel
		float randomLifetime = (float) (this.particleMinLifetime + Math.random() * (this.particleMaxLifetime - this.particleMinLifetime)); //interpolates between min and max lifetime
		ParticleBase particle = new ParticleBase(tempLoc, tempVel, this.particleDrag, (float)Math.toDegrees(randomRadian), 
				randomRVel, randomLifetime, this.particlePic, this.particleAlphaDecay, randomStartScale, randomEndScale);
		map.addParticle(particle); //adds the particle
	}
	private void emitLineRadial() {
		if(this.kv3 == 0 && this.kv4 == 0){
			Point tempLoc = new Point();
			Point tempVel = new Point();
			
			double randomRatio = Math.random(); //between 0 and 1 what point on the line to spawn at
			
			tempLoc.changeX(this.getLoc().getX() + randomRatio * (this.kv1 - this.getLoc().getX()));
			tempLoc.changeY(this.getLoc().getY() + randomRatio * (this.kv2 - this.getLoc().getY()));
			float normalAngle = (float) (Math.atan((this.kv2 - this.getLoc().getY()) //inverse tan of dy over dx
												/(this.kv1 - this.getLoc().getX())) + Math.PI/2);
			
			if(Math.random() < 0.5) { //one half goes one way, other half goes other way
				normalAngle -= Math.PI;
			}
			float randomSpeed = (float) (this.minLaunchSpeed + Math.random() * (this.maxLaunchSpeed - this.minLaunchSpeed)); //interpolates between min and max launch speed
			tempVel.changeX((double) Math.cos((double) normalAngle) * randomSpeed);
			tempVel.changeY((double) Math.sin((double) normalAngle) * randomSpeed);
			
			float randomStartScale = (this.particleMinStartScale + (float)Math.random() * (this.particleMaxStartScale - this.particleMinStartScale));
			float randomEndScale = (this.particleMinEndScale + (float)Math.random() * (this.particleMaxEndScale - this.particleMinEndScale));
			
			float randomRVel = (float) (this.particleMinRVel + Math.random() * (this.particleMaxRVel - this.particleMinRVel)); //interpolates between min and max rvel
			float randomLifetime = (float) (this.particleMinLifetime + Math.random() * (this.particleMaxLifetime - this.particleMinLifetime)); //interpolates between min and max lifetime
			ParticleBase particle = new ParticleBase(tempLoc, tempVel, this.particleDrag, (float)Math.toDegrees(normalAngle), 
					randomRVel, randomLifetime, this.particlePic, this.particleAlphaDecay, randomStartScale, randomEndScale);
			map.addParticle(particle); //adds the particle
		} else { //IF A WIDTH IS SPECIFIED
			double circleSpawnWeight = this.kv4 * this.kv4 * Math.PI; //the weight of probablility to spawn in a circle end
			double lineSpawnWeight = 2 * this.kv4 * Point.getDistance(this.getLoc(), new Point(this.kv1, this.kv2));
			double endCircleSpawnWeight = circleSpawnWeight / (circleSpawnWeight + lineSpawnWeight);
			
			Point tempLoc = new Point();
			Point tempVel = new Point();
			////////////OH BOY ANGLES////////////////////////
			float spawnAngle;
			float normalAngle = (float) (Math.atan((this.kv2 - this.getLoc().getY()) //inverse tan of dy over dx
										/(this.kv1 - this.getLoc().getX())) + Math.PI/2);
			if(Math.random() <= endCircleSpawnWeight) { //SPAWNING IN CIRCLE
				spawnAngle = (float) ((Math.random() - 0.5f) * Math.PI); //half circle
				if(Math.random() < 0.5) { //half chance to spawn at emitter
					tempLoc = new Point(this.getLoc().getX(), this.getLoc().getY()); //SETTING POSITION
					
					if(this.getLoc().getX() > this.kv1) { //If the emitter is to the right of the end point
						spawnAngle += normalAngle - Math.PI/2;
					} else { //to the left of emitter
						spawnAngle += normalAngle + Math.PI/2;
					}
				} else { //spawning at other point
					tempLoc = new Point(this.kv1, this.kv2); //SETTING POSITION
					
					if(this.getLoc().getX() > this.kv1) { //If the emitter is to the right of the end point
						spawnAngle += normalAngle + Math.PI/2;
					} else { //to the left of emitter
						spawnAngle += normalAngle - Math.PI/2;
					}
				}
			} else { //SPAWNING ON THE LINE
				double randomRatio = Math.random();
				tempLoc.changeX(this.getLoc().getX() + randomRatio * (this.kv1 - this.getLoc().getX()));
				tempLoc.changeY(this.getLoc().getY() + randomRatio * (this.kv2 - this.getLoc().getY()));
				spawnAngle = normalAngle;
				if(Math.random() < 0.5) { //one half goes one way, other half goes other way
					spawnAngle -= Math.PI;
				}
			}
			//MOVING ON TO POSITION
			float randomRadius = this.kv3 + (float)Math.random() * (this.kv4 - this.kv3);
			tempLoc.addX((double) Math.cos((double) spawnAngle) * randomRadius);
			tempLoc.addY((double) Math.sin((double) spawnAngle) * randomRadius);
			
			float randomSpeed = (float) (this.minLaunchSpeed + Math.random() * (this.maxLaunchSpeed - this.minLaunchSpeed)); //interpolates between min and max launch speed
			tempVel.changeX((double) Math.cos((double) spawnAngle) * randomSpeed); //translate from circle coordinates to x and y
			tempVel.changeY((double) Math.sin((double) spawnAngle) * randomSpeed);
			
			float randomStartScale = (this.particleMinStartScale + (float)Math.random() * (this.particleMaxStartScale - this.particleMinStartScale));
			float randomEndScale = (this.particleMinEndScale + (float)Math.random() * (this.particleMaxEndScale - this.particleMinEndScale));
			
			float randomRVel = (float) (this.particleMinRVel + Math.random() * (this.particleMaxRVel - this.particleMinRVel)); //interpolates between min and max rvel
			float randomLifetime = (float) (this.particleMinLifetime + Math.random() * (this.particleMaxLifetime - this.particleMinLifetime)); //interpolates between min and max lifetime
			
			ParticleBase particle = new ParticleBase(tempLoc, tempVel, this.particleDrag, (float)Math.toDegrees(spawnAngle), 
					randomRVel, randomLifetime, this.particlePic, this.particleAlphaDecay, randomStartScale, randomEndScale);
			map.addParticle(particle); //adds the particle
		}
	}
	private void emitLineRandom() {
		if(this.kv3 == 0 && this.kv4 == 0){
			Point tempLoc = new Point();
			Point tempVel = new Point();
			
			double randomRatio = Math.random(); //between 0 and 1 what point on the line to spawn at
			
			tempLoc.changeX(this.getLoc().getX() + randomRatio * (this.kv1 - this.getLoc().getX()));
			tempLoc.changeY(this.getLoc().getY() + randomRatio * (this.kv2 - this.getLoc().getY()));
			float randomAngle = (float) (Math.random() * Math.PI * 2);
			float randomSpeed = (float) (this.minLaunchSpeed + Math.random() * (this.maxLaunchSpeed - this.minLaunchSpeed)); //interpolates between min and max launch speed
			tempVel.changeX((double) Math.cos((double) randomAngle) * randomSpeed);
			tempVel.changeY((double) Math.sin((double) randomAngle) * randomSpeed);
			
			float randomStartScale = (this.particleMinStartScale + (float)Math.random() * (this.particleMaxStartScale - this.particleMinStartScale));
			float randomEndScale = (this.particleMinEndScale + (float)Math.random() * (this.particleMaxEndScale - this.particleMinEndScale));
			
			float randomRVel = (float) (this.particleMinRVel + Math.random() * (this.particleMaxRVel - this.particleMinRVel)); //interpolates between min and max rvel
			float randomLifetime = (float) (this.particleMinLifetime + Math.random() * (this.particleMaxLifetime - this.particleMinLifetime)); //interpolates between min and max lifetime
			ParticleBase particle = new ParticleBase(tempLoc, tempVel, this.particleDrag, (float)Math.toDegrees(randomAngle), 
					randomRVel, randomLifetime, this.particlePic, this.particleAlphaDecay, randomStartScale, randomEndScale);
			map.addParticle(particle); //adds the particle
		} else { //IF A WIDTH IS SPECIFIED
			double circleSpawnWeight = this.kv4 * this.kv4 * Math.PI; //the weight of probablility to spawn in a circle end
			double lineSpawnWeight = 2 * this.kv4 * Point.getDistance(this.getLoc(), new Point(this.kv1, this.kv2));
			double endCircleSpawnWeight = circleSpawnWeight / (circleSpawnWeight + lineSpawnWeight);
			
			Point tempLoc = new Point();
			Point tempVel = new Point();
			////////////OH BOY ANGLES////////////////////////
			float spawnAngle;
			float normalAngle = (float) (Math.atan((this.kv2 - this.getLoc().getY()) //inverse tan of dy over dx
										/(this.kv1 - this.getLoc().getX())) + Math.PI/2);
			if(Math.random() <= endCircleSpawnWeight) { //SPAWNING IN CIRCLE
				spawnAngle = (float) ((Math.random() - 0.5f) * Math.PI); //half circle
				if(Math.random() < 0.5) { //half chance to spawn at emitter
					tempLoc = new Point(this.getLoc().getX(), this.getLoc().getY()); //SETTING POSITION
					
					if(this.getLoc().getX() > this.kv1) { //If the emitter is to the right of the end point
						spawnAngle += normalAngle - Math.PI/2;
					} else { //to the left of emitter
						spawnAngle += normalAngle + Math.PI/2;
					}
				} else { //spawning at other point
					tempLoc = new Point(this.kv1, this.kv2); //SETTING POSITION
					
					if(this.getLoc().getX() > this.kv1) { //If the emitter is to the right of the end point
						spawnAngle += normalAngle + Math.PI/2;
					} else { //to the left of emitter
						spawnAngle += normalAngle - Math.PI/2;
					}
				}
			} else { //SPAWNING ON THE LINE
				double randomRatio = Math.random();
				tempLoc.changeX(this.getLoc().getX() + randomRatio * (this.kv1 - this.getLoc().getX()));
				tempLoc.changeY(this.getLoc().getY() + randomRatio * (this.kv2 - this.getLoc().getY()));
				spawnAngle = normalAngle;
				if(Math.random() < 0.5) { //one half goes one way, other half goes other way
					spawnAngle -= Math.PI;
				}
			}
			//MOVING ON TO POSITION
			float randomRadius = this.kv3 + (float)Math.random() * (this.kv4 - this.kv3);
			tempLoc.addX((double) Math.cos((double) spawnAngle) * randomRadius); //spawning a random distance away from the line
			tempLoc.addY((double) Math.sin((double) spawnAngle) * randomRadius);
			
			float randomMoveDirection = (float) (Math.random() * Math.PI * 2);
			float randomSpeed = (float) (this.minLaunchSpeed + Math.random() * (this.maxLaunchSpeed - this.minLaunchSpeed)); //interpolates between min and max launch speed
			tempVel.changeX((double) Math.cos((double) randomMoveDirection) * randomSpeed); //translate from circle coordinates to x and y
			tempVel.changeY((double) Math.sin((double) randomMoveDirection) * randomSpeed);
			
			float randomStartScale = (this.particleMinStartScale + (float)Math.random() * (this.particleMaxStartScale - this.particleMinStartScale));
			float randomEndScale = (this.particleMinEndScale + (float)Math.random() * (this.particleMaxEndScale - this.particleMinEndScale));
			
			float randomRVel = (float) (this.particleMinRVel + Math.random() * (this.particleMaxRVel - this.particleMinRVel)); //interpolates between min and max rvel
			float randomLifetime = (float) (this.particleMinLifetime + Math.random() * (this.particleMaxLifetime - this.particleMinLifetime)); //interpolates between min and max lifetime
			
			ParticleBase particle = new ParticleBase(tempLoc, tempVel, this.particleDrag, (float)Math.toDegrees(randomMoveDirection), 
					randomRVel, randomLifetime, this.particlePic, this.particleAlphaDecay, randomStartScale, randomEndScale);
			map.addParticle(particle); //adds the particle
		}
	}
}
