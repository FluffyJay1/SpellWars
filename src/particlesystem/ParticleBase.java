package particlesystem;
import mechanic.GameMap;
import mechanic.Point;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ParticleBase {
	private GameMap map;
	private Image pic;
	private Point loc;
	private Point vel; //I DON'T CARE I'M REPRESENTING VELOCITY AS A POINT
	private float drag; //BETWEEN 0 AND 100
	private float orientation; //in degrees
	private float rVel; //ROTATIONAL VELOCITY
	private float maxLifetime; //Used for alphadecay, which basically causes the particle to fade out over time
	private float lifetime; //IN SECONDS
	private boolean alphaDecay;
	private float scale;
	private float startScale;
	private float endScale;
	public float fps = 300;
	
	private boolean remove;

	
	public ParticleBase() {
		this(new Point());
	}
	public ParticleBase(Point loc){
		this(loc, new Point());
	}
	public ParticleBase(Point loc, Point vel){
		this(loc, vel, 0, 0, 0, 1, null, false, 1, 1); //MAGIC NUMBER ALERT
	}
	public ParticleBase(Point loc, Point vel, float drag, float orientation, 
			float rVel, float lifetime, String pic, boolean alphaDecay, float startScale, float endScale){
		this.loc = loc;
		this.vel = vel;
		try {
			this.pic = new Image(pic);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.lifetime = lifetime;
		this.maxLifetime = lifetime;
		this.drag = drag;
		this.orientation = orientation;
		this.rVel = rVel;
		this.alphaDecay = alphaDecay;
		this.remove = false;
		this.scale = startScale;
		this.startScale = startScale;
		this.endScale = endScale;
	}
	public float getMaxLifetime(){
		return maxLifetime;
	}
	public float getLifetime(){
		return lifetime;
	}
	public void draw(Graphics g) {
		Image endPic = pic.getScaledCopy(scale);
		endPic.setRotation(orientation);
		float width = endPic.getWidth();
		float height = endPic.getHeight();
		if(alphaDecay)
		{
			endPic.setAlpha(lifetime/maxLifetime); //GOES TO 0
		}
		g.drawImage(endPic, (float) loc.getX() - width/2, (float) loc.getY() - height/2);
	}
	public void move(){ //basically update
		lifetime -= 1/this.fps; //lifetime decay
		vel.changeX(vel.getX() * (1 - drag/this.fps)); //drag, which causes the particle to slow down over time (or speed up)
		vel.changeY(vel.getY() * (1 - drag/this.fps)); //motion per frame is based on fps, which means theoretically it will move at the same speed at 30 fps and at 300 fps
		loc.addX(vel.getX()/this.fps); //velocity
		loc.addY(vel.getY()/this.fps);
		orientation += rVel/this.fps; //rotational velocity
		scale = startScale + (1 - lifetime/maxLifetime) * (endScale - startScale);
		/*
		while(this.orientation > 360) { //not actually radians wtf
			this.orientation -= 360;
			System.out.println("changing orientation yo");
		}
		while(this.orientation < 0) {
			this.orientation += 360;
			System.out.println("changing orientation yo");
		}
		*/
		if(lifetime <= 0)
		{
			remove = true; //if particle has lived long enough
		}
	}
	public boolean getRemove() {
		return remove;
	}
	public void passFPS(float fps) {
		this.fps = fps; //simply just passes FPS to the particle so the particle can use that information to calculate how fast to go
	}
}
