package spell;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import mechanic.Animation;
import mechanic.GameMap;
import mechanic.Panel;
import mechanic.Point;
import unit.Unit;

public class Slash extends Spell {	
	public static final float ANIMATION_DURATION = 0.15f;
	public static final float SLASH_DELAY = 0.2f;
	public static final float DRAW_HEIGHT = 0;
	int length;
	int height;
	double damage;
	static Image animation;
	float timer;
	float slashDelay;
	float animationDuration;
	boolean flipVertical;
	ArrayList<Panel> affectedPanels;
	public Slash(Unit owner, int length, int height, double damage, float slashDelay, float animationDuration) {
		super(owner, slashDelay, animationDuration, "Slash", "Slash an area in front of you", "res/spell/slash.png", false);
		this.length = length;
		this.height = height;
		this.damage = damage;
		this.slashDelay = slashDelay;
		this.animationDuration = animationDuration;
		if(animation == null) {
			try {
				animation = new Image("res/spell/slashanimation.png");
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.flipVertical = Math.random() > 0.5;
		this.affectedPanels = new ArrayList<Panel>();
	}
	public Slash(Unit owner, int length, int height, double damage) {
		this(owner, length, height, damage, SLASH_DELAY, ANIMATION_DURATION);
	}
	public Slash(Unit owner) {
		this(owner, 2, 1, 50);
	}
	@Override
	public void onActivate() {
		for(int y = -height; y <= height; y++) {
			for(int x = 1; x <= length; x++) {
				Point slashLoc = Point.add(this.owner.gridLoc, Point.add(new Point(0, y), Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.direction), x)));
				if(this.getMap().pointIsInGrid(slashLoc) && this.getMap().getPanelAt(slashLoc).unitStandingOnPanel != null && this.getMap().getPanelAt(slashLoc).unitStandingOnPanel.teamID != this.owner.teamID) {
					this.getMap().getPanelAt(slashLoc).unitStandingOnPanel.doDamage(this.damage * this.owner.finalDamageModifier);
				}
			}
		}
		this.timer = 0;
	}
	@Override
	public void onStartCast() {
		for(int y = -height; y <= height; y++) {
			for(int x = 1; x <= length; x++) {
				Point slashLoc = Point.add(this.owner.gridLoc, Point.add(new Point(0, y), Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.direction), x)));
				if(this.getMap().pointIsInGrid(slashLoc)) {
					this.affectedPanels.add(this.getMap().getPanelAt(slashLoc));
				}
			}
		}
		for(Panel p : this.affectedPanels) {
			p.panelFlash(this.slashDelay);
		}
	}
	@Override
	public void drawSpecialEffects(Graphics g) {
		Point[] corners = this.getMap().getCornerPositionsOfGridPoints(Point.add(this.owner.gridLoc, Point.add(new Point(0, -this.height), Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.direction), 1))), 
				Point.add(this.owner.gridLoc, Point.add(new Point(0, this.height), Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.direction), this.length))));
		float x = (float)corners[0].x;
		float y = (float)corners[0].y - DRAW_HEIGHT;
		float x2 = (float)corners[1].x;
		float y2 = (float)corners[1].y - DRAW_HEIGHT;
		//7 total frames, each frame is 96x96, goes from left to right, bottom to top
		int currFrame = (int)(7 * (this.timer/this.animationDuration)); //goes from 0 to 6
		float srcx = 80 + ((currFrame + 1) % 3) * 96;
		float srcx2 = 96 + ((currFrame + 1) % 3) * 96;
		float srcy = 24 + (int)((8 - (currFrame + 1))/3) * 96;
		float srcy2 = 85 + (int)((8 - (currFrame + 1))/3) * 96;
		if(this.owner.direction == GameMap.ID_LEFT) {
			float temp = srcx;
			srcx = srcx2;
			srcx2 = temp;
		}
		if(this.flipVertical) {
			float temp = srcy;
			srcy = srcy2;
			srcy2 = temp;
		}
		g.drawImage(animation, x, y, x2, y2, srcx, srcy, srcx2, srcy2);
	}
	@Override
	public void onSpellUpdate() {
		this.timer += this.getFrameTime();
		if(this.timer >= this.animationDuration) {
			this.finishSpell();
		}
	}
}
