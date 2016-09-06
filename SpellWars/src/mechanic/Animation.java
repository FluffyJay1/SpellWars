package mechanic;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Animation {
	float timer;
	float animateDuration;
	int numFramesWidth;
	int numFramesHeight;
	int frameWidth;
	int frameHeight;
	Image animation;
	boolean pause;
	boolean loop;
	Point loc;
	public Animation(Image animation, int numFramesWidth, int numFramesHeight, float animateDuration, boolean loop, boolean pause) {
		this.animation = animation;
		this.numFramesWidth = numFramesWidth;
		this.numFramesHeight = numFramesHeight;
		this.frameWidth = animation.getWidth()/numFramesWidth;
		this.frameHeight = animation.getHeight()/numFramesHeight;
		this.animateDuration = animateDuration;
		this.loop = loop;
		this.pause = pause;
	}
	public void changeLoc(Point loc) {
		this.loc = loc;
	}
	public void setPause(boolean pause) {
		this.pause = pause;
	}
	public void update(float frametime) {
		if(!this.pause) {
			this.timer += frametime;
		}
	}
	public void draw(Graphics g) {
		float x = (float) (this.loc.x - this.frameWidth/2);
		float y = (float) (this.loc.y - this.frameHeight/2);
		float x2 = (float) (this.loc.x + this.frameWidth/2);
		float y2 = (float) (this.loc.y + this.frameHeight/2);
		int currFrame = (int)(this.numFramesWidth * this.numFramesHeight * (this.timer/this.animateDuration));
		if(this.loop) {
			currFrame = (int)(this.numFramesWidth * this.numFramesHeight * (this.timer % this.animateDuration) / this.animateDuration);
		}
		float srcx = (currFrame % this.numFramesWidth) * this.frameWidth;
		float srcx2 = this.frameWidth + (currFrame % this.numFramesWidth) * this.frameWidth;
		float srcy = (int)(currFrame/this.numFramesWidth) * this.frameHeight;
		float srcy2 = this.frameHeight + (int)(currFrame/this.numFramesWidth) * this.frameHeight;
		g.drawImage(this.animation, x, y, x2, y2, srcx, srcy, srcx2, srcy2);
	}
}
