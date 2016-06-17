package shield;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;
import projectile.Projectile;
import ui.Text;
import ui.TextFormat;
import unit.Unit;

public class Shield extends GameElement {
	private static final double HP_Y_OFFSET = -100;

	Unit owner;
	
	boolean think;
	float thinkInterval;
	float thinkTimer;
	
	public Color HPTextColor;
	float drawOffset; //when layering multiple shields on top of each other
	boolean drawHP;
	
	public Shield(Unit owner, double hp, double maxhp, String imagePath) {
		super(hp, maxhp, 0, 0, owner.getLoc(), 1, 0, imagePath);
		this.owner = owner;
		this.think = false;
		this.thinkInterval = -1;
		this.thinkTimer = 0;
		this.owner.addShield(this);
		this.HPTextColor = new Color(0, 0, 255);
		this.drawHP = true;
	}
	public void setDrawOffset(float y) {
		this.drawOffset = y;
	}
	@Override
	public void update() {
		this.changeLoc(this.owner.getLoc());
		if(this.think) {
			this.thinkTimer -= this.getFrameTime();
			while(this.thinkTimer <= 0) {
				this.onThink();
				this.thinkTimer += this.thinkInterval;
			}
		}
		this.onUpdate();
	}
	public void onUpdate() {
		
	}
	@Override
	public void draw(Graphics g) {
		if(this.getImage() != null){
			Image endPic = this.getImage().getFlippedCopy(this.owner.direction == GameMap.ID_LEFT, false);
			endPic = endPic.getScaledCopy((float) this.getSize());
			float width = endPic.getWidth();
			float height = endPic.getHeight();
			g.drawImage(endPic, (float) this.getLoc().x - width/2, (float) this.getLoc().y - height/2 - this.getDrawHeight() + this.drawOffset);
		}
		if(this.drawHP) {
			Text text = new Text(this.getMap().getUI(), Point.add(this.getLoc(), new Point(-200, HP_Y_OFFSET + this.drawOffset)), 400, 10, 16, 12, 20, Color.white, "" + (int)this.getHP(), TextFormat.CENTER_JUSTIFIED);
			text.setUseOutline(true);
			text.setOutlineColor(this.HPTextColor);
			text.setRemoveNextFrame(true);
			this.getMap().getUI().addUIElement(text);
		}
	}
	public void onThink() {
		
	}
	public void onHitByProjectile(Projectile p) {
		
	}
	public void setThinkInterval(float interval) {
		this.think = true;
		this.thinkTimer = interval;
		this.thinkInterval = interval;
	}
}
