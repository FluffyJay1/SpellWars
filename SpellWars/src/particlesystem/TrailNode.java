package particlesystem;
import mechanic.Point;

public class TrailNode {
	float orientation;
	float startWidth;
	float endWidth;
	float lifeTime;
	float maxLifeTime;
	Point loc;
	Point leftControlPoint;
	Point rightControlPoint;
	Point velocity;
	boolean reverseAfter;
	boolean remove;
	
	public TrailNode(Point loc, float direction, float startWidth, float endWidth, float lifeTime, boolean reverseAfter, Point velocity) {
		this.loc = loc;
		this.orientation = direction;
		this.startWidth = startWidth;
		this.endWidth = endWidth;
		this.lifeTime = lifeTime;
		this.maxLifeTime = lifeTime;
		this.leftControlPoint = Point.getFuturePoint(this.loc, startWidth/2, Math.toRadians(this.orientation + 90));
		this.rightControlPoint = Point.getFuturePoint(this.loc, startWidth/2, Math.toRadians(this.orientation - 90));
		this.reverseAfter = reverseAfter;
		this.velocity = velocity;
	}
	public void update(float frameTime) {
		float width = this.endWidth + (this.lifeTime/this.maxLifeTime) * (this.startWidth - this.endWidth);
		if(reverseAfter) {
			width = this.endWidth + Math.abs(2 *(this.lifeTime/this.maxLifeTime) - 1) * (this.startWidth - this.endWidth);
		}
		this.leftControlPoint = Point.getFuturePoint(this.loc, width/2, Math.toRadians(this.orientation + 90));
		this.rightControlPoint = Point.getFuturePoint(this.loc, width/2, Math.toRadians(this.orientation - 90));
		this.lifeTime -= frameTime;
		if(lifeTime <= 0) {
			this.remove = true;
		}
		this.loc.add(Point.scale(this.velocity, frameTime));
	}
	public Point getLeftControlPoint() {
		return this.leftControlPoint;
	}
	public Point getRightControlPoint() {
		return this.rightControlPoint;
	}
}
