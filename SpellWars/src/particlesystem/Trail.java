package particlesystem;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import mechanic.Game;
import mechanic.GameElement;
import mechanic.Point;

public class Trail extends GameElement {
	Image trailPic;
	private String imagepath;
	float trailStartWidth, trailEndWidth;
	float nodeSpawnInterval;
	boolean reverseAfter;
	float distanceTraveledSinceLastNode; //pretty much useless
	float directionOfTravel;
	float directionOfTrail;
	float trailLifeTime;
	boolean runOnce; //used in running a function after its first update tick
	float randomSpeed;
	Point prevLoc;
	ArrayList<TrailNode> trailNodes; //LATEST NODES ARE IN THE BACK
	ArrayList<TrailNode> trailNodeRemoveBuffer;
	TrailNode headNode;
	public Trail(Point loc, String imagePath, float startWidth, float endWidth, float trailLifeTime, float nodeSpawnInterval, boolean reverseAfter, float randomSpeed) {
		super();
		this.setImage(imagePath);
		this.changeLoc(loc);
		this.trailStartWidth = startWidth;
		this.trailEndWidth = endWidth;
		this.nodeSpawnInterval = nodeSpawnInterval;
		this.reverseAfter = reverseAfter;
		this.prevLoc = Point.clone(this.getLoc());
		trailNodes = new ArrayList<TrailNode>();
		trailNodeRemoveBuffer  = new ArrayList<TrailNode>();
		this.distanceTraveledSinceLastNode = 0;
		this.directionOfTravel = 0;
		this.directionOfTrail = 0;
		this.trailLifeTime = trailLifeTime;
		this.headNode = new TrailNode(this.getLoc(), this.directionOfTravel, this.trailStartWidth, this.trailEndWidth, this.trailLifeTime, reverseAfter, new Point());
		this.runOnce = true;
		this.randomSpeed = randomSpeed;
	}
	public Trail(GameElement element, String imagePath, float startWidth, float endWidth, float trailLifeTime, float nodeSpawnInterval, boolean reverseAfter, float randomSpeed) {
		this(element.getLoc(), imagePath, startWidth, endWidth, trailLifeTime, nodeSpawnInterval, reverseAfter, randomSpeed);
		this.setParent(element);
	}
	public void setImage(String path){
		if(Game.images.containsKey(path)) {
			this.trailPic = Game.images.get(path).copy();
		} else {
			try {
				this.trailPic = new Image(path);
				Game.images.put(path, this.trailPic.copy());
			} catch (SlickException e) {
				System.out.println("Unable to load trail");
				e.printStackTrace();
			} finally {
				System.out.println("loaded into memory: " + path);
			}
		}
		this.imagepath = path;
	}
	public void resetTrail() {
		this.distanceTraveledSinceLastNode = 0;
		this.directionOfTravel = 0;
		this.directionOfTrail = 0;
		this.prevLoc = Point.clone(this.getLoc());
	}
	@Override
	public void update() {
		float distanceTraveled = (float) Point.getDistance(this.prevLoc, this.getLoc());
		this.distanceTraveledSinceLastNode += distanceTraveled;
		if(distanceTraveled > 0) {
			this.directionOfTravel = (float) Point.getDirectionDeg(this.prevLoc, this.getLoc());
			if(this.runOnce) {
				TrailNode t = new TrailNode(this.prevLoc, this.directionOfTravel, this.trailStartWidth, this.trailEndWidth, this.trailLifeTime, this.reverseAfter, 
						Point.scale(Point.getVector(1, Math.toRadians(this.directionOfTravel + 90)), (Math.random() - 0.5) * 2 * this.randomSpeed));
				this.trailNodes.add(t);
				this.runOnce = false;
			}
			this.directionOfTrail = (float) Point.getDirectionDeg(this.getLastTrailNodeLoc(), this.getLoc());
		}
		float distanceFromLastNode = (float)Point.getDistance(this.getLastTrailNodeLoc(), this.getLoc());
		
		this.headNode.update((float)this.getFrameTime());
		if(this.getParent() != null) {
			if(!this.getParent().getRemove()) {
				this.headNode.lifeTime = this.headNode.maxLifeTime;
			} 
		} else {
			this.headNode.lifeTime = this.headNode.maxLifeTime;
		}
		this.headNode.orientation = directionOfTrail;
		this.headNode.loc = Point.clone(this.getLoc());
		
		while(distanceFromLastNode >= nodeSpawnInterval && distanceTraveled > 0) {
			directionOfTrail = (float) Point.getDirectionDeg(this.getLastTrailNodeLoc(), this.getLoc());
			Point nextLoc = Point.getFuturePoint(this.getLastTrailNodeLoc(), nodeSpawnInterval, Math.toRadians(directionOfTrail));
			TrailNode t = new TrailNode(nextLoc, directionOfTrail, this.trailStartWidth, this.trailEndWidth, this.trailLifeTime, this.reverseAfter,
					Point.scale(Point.getVector(1, Math.toRadians(this.directionOfTrail + 90)), (Math.random() - 0.5) * 2 * this.randomSpeed));
			this.trailNodes.add(t);
			distanceFromLastNode -= nodeSpawnInterval;
		}
		this.prevLoc = Point.clone(this.getLoc());
		for(TrailNode t : this.trailNodes) {
			t.update((float)this.getFrameTime());
			if(t.remove) {
				this.trailNodeRemoveBuffer.add(t);
			}
		}
		this.trailNodes.removeAll(this.trailNodeRemoveBuffer);
		this.trailNodeRemoveBuffer.clear();
	}
	@Override
	public void draw(Graphics g){
		for(int i = 1; i < this.trailNodes.size(); i++) {
			TrailNode firstNode = this.trailNodes.get(i);
			TrailNode secondNode = this.trailNodes.get(i - 1);
			this.drawTrailSegment(firstNode, secondNode);
		}
		if(this.trailNodes.size() > 0) {
			this.drawTrailSegment(this.headNode, this.trailNodes.get(this.trailNodes.size() - 1));
		}
		/*
		for(TrailNode t : this.trailNodes) {
			drawNode(g, t);
		}
		drawNode(g, this.headNode);
		*/
	}
	public void drawTrailSegment(TrailNode firstNode, TrailNode secondNode) {
		Image tempPic = this.trailPic.copy();
		tempPic.drawWarped((float)firstNode.getRightControlPoint().getX(), (float)firstNode.getRightControlPoint().getY(), 
				(float)secondNode.getRightControlPoint().getX(), (float)secondNode.getRightControlPoint().getY(), 
				(float)secondNode.getLeftControlPoint().getX(), (float)secondNode.getLeftControlPoint().getY(), 
				(float)firstNode.getLeftControlPoint().getX(), (float)firstNode.getLeftControlPoint().getY());
	}
	public void drawNode(Graphics g, TrailNode t) { //A DEBUG FUNCTION
		g.drawOval((float)t.loc.getX() - 5, (float)t.loc.getY() - 5, 10, 10);
		g.drawOval((float)t.getLeftControlPoint().getX() - 5, (float)t.getLeftControlPoint().getY() - 5, 10, 10);
		g.drawOval((float)t.getRightControlPoint().getX() - 5, (float)t.getRightControlPoint().getY() - 5, 10, 10);
	}
	
	@Override
	public void updateRegardsToParent() {
		if(this.getParent() != null) {
			if(this.getParent().getRemove() && this.trailNodes.size() == 0) {
				this.setRemove(true);
			}
		}
	}
	
	private Point getLastTrailNodeLoc() {
		if(this.trailNodes.size() != 0) {
			return this.trailNodes.get(this.trailNodes.size() - 1).loc;
		} else {
			/*
			TrailNode t = new TrailNode(Point.getFuturePoint(this.getLoc(), 1, -this.directionOfTravel), this.directionOfTravel, this.trailStartWidth, this.trailEndWidth, this.trailLifeTime, this.alphaDecay);
			this.trailNodes.add(t);
			return this.getLastTrailNodeLoc();
			*/
			this.distanceTraveledSinceLastNode = this.nodeSpawnInterval;
			return Point.getFuturePoint(this.getLoc(), this.nodeSpawnInterval, this.directionOfTravel + 180);
		}
	}
}
