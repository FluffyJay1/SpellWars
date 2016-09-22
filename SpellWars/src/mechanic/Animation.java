package mechanic;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import states.StateGame;

public class Animation {
	float timer;
	float animateDuration;
	int numFramesWidth;
	int numFramesHeight;
	int frameWidth;
	int frameHeight;
	float width;
	float height;
	Image animation;
	private String imagepath;
	boolean pause;
	boolean loop;
	Color color;
	public boolean hflip;
	public boolean vflip;
	Point loc;
	public Animation(String animationpath, int numFramesWidth, int numFramesHeight, float width, float height, float animateDuration, boolean loop, boolean pause) {
		this.setImage(animationpath);
		this.numFramesWidth = numFramesWidth;
		this.numFramesHeight = numFramesHeight;
		this.frameWidth = animation.getWidth()/numFramesWidth;
		this.frameHeight = animation.getHeight()/numFramesHeight;
		this.animateDuration = animateDuration;
		this.width = width;
		this.height = height;
		this.loop = loop;
		this.pause = pause;
		this.color = new Color(255, 255, 255, 255);
	}
	public void setImage(String path){
		if(Game.images.containsKey(path)) {
			this.animation = Game.images.get(path).copy();
		} else {
			try {
				this.animation = new Image(path);
				Game.images.put(path, this.animation.copy());
			} catch (SlickException e) {
				System.out.println("Unable to load: " + path);
				e.printStackTrace();
			} finally {
				System.out.println("loaded into memory: " + path);
			}
		}
		this.imagepath = path;
	}
	public String getImagePath() {
		return this.imagepath;
	}
	public void changeLoc(Point loc) {
		this.loc = loc;
	}
	public void setPause(boolean pause) {
		this.pause = pause;
	}
	public void update(float frametime) {
		if(this.timer < this.animateDuration) {
			if(!this.pause) {
				this.timer += frametime;
			}
		} else if(this.loop) {
			this.timer -= this.animateDuration;
		} else {
			this.timer = this.animateDuration;
		}
	}
	public void draw(Graphics g, GameMap map) {
		float x = (float) (this.loc.x - this.width/2);
		float y = (float) (this.loc.y - this.height/2);
		float x2 = (float) (this.loc.x + this.width/2);
		float y2 = (float) (this.loc.y + this.height/2);
		int currFrame = (int)(this.numFramesWidth * this.numFramesHeight * (this.timer/this.animateDuration));
		if(this.loop) {
			currFrame = (int)(this.numFramesWidth * this.numFramesHeight * (this.timer % this.animateDuration) / this.animateDuration);
		}
		float srcx = (currFrame % this.numFramesWidth) * this.frameWidth;
		float srcx2 = this.frameWidth + (currFrame % this.numFramesWidth) * this.frameWidth;
		if(this.hflip){
			float temp = srcx;
			srcx = srcx2;
			srcx2 = temp;
		}
		float srcy = (int)(currFrame/this.numFramesWidth) * this.frameHeight;
		float srcy2 = this.frameHeight + (int)(currFrame/this.numFramesWidth) * this.frameHeight;
		if(this.vflip){
			float temp = srcy;
			srcy = srcy2;
			srcy2 = temp;
		}
		g.drawImage(this.animation, x, y, x2, y2, srcx, srcy, srcx2, srcy2);
		if(StateGame.isServer)
		map.addToDrawInfo(GameMap.getDrawDataA(this.imagepath, x, y, this.width, this.height, srcx, srcy, srcx2, srcy2, this.color.getRedByte(), this.color.getGreenByte(), this.color.getBlueByte(), this.color.getAlphaByte()));
	}
}
