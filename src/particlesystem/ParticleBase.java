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
	private Color col;
	private Point loc;
	private Point vel; //I DON'T CARE I'M REPRESENTING VELOCITY AS A POINT
	private float drag; //BETWEEN 0 AND 100
	private float orientation; //in degrees
	private float rVel; //ROTATIONAL VELOCITY
	private float maxLifetime; //Used for alphadecay, which basically causes the particle to fade out over time
	private float lifetime; //IN SECONDS
	private boolean alphaDecay;
	public float fps = 1000;
	
	private boolean remove;

	
	public ParticleBase() {
		this(new Point());
	}
	public ParticleBase(Point loc){
		this(loc, new Point());
	}
	public ParticleBase(Point loc, Point vel){
		this(loc, vel, 0, 0, 0, 1, null, null, false); //MAGIC NUMBER ALERT
	}
	public ParticleBase(Point loc, Point vel, float drag, float orientation, 
			float rVel, float lifetime, String pic, Color col, boolean alphaDecay){
		this.loc = loc;
		this.vel = vel;
		try {
			this.pic = new Image(pic);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.col = col;
		this.lifetime = lifetime;
		this.maxLifetime = lifetime;
		this.drag = drag;
		this.orientation = orientation;
		this.rVel = rVel;
		this.alphaDecay = alphaDecay;
		this.remove = false;
	}
	public float getMaxLifetime(){
		return maxLifetime;
	}
	public float getLifetime(){
		return lifetime;
	}
	public void draw(Graphics g) {
		float width = this.pic.getWidth();
		float height = this.pic.getHeight();
		pic.setRotation(orientation);
		g.drawImage(pic, (float) loc.getX() - width/2, (float) loc.getY() - height/2, col);
	}
	public void move(){ //basically update
		lifetime -= 1/fps; //lifetime decay
		vel.changeX(vel.getX() * (1 - drag/fps)); //drag, which causes the particle to slow down over time (or speed up)
		vel.changeY(vel.getY() * (1 - drag/fps)); //motion per frame is based on fps, which means theoretically it will move at the same speed at 30 fps and at 300 fps
		loc.addX(vel.getX()/fps); //velocity
		loc.addY(vel.getY()/fps);
		orientation += rVel/fps; //rotational velocity
		while(orientation > 360) { //not actually radians wtf
			orientation -= 360;
		}
		while(orientation < 0) {
			orientation += 360;
		}
		if(alphaDecay)
		{
			pic.setAlpha(lifetime/maxLifetime); //GOES TO 0
		}
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
