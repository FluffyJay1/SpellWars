package spell;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import mechanic.Animation;
import mechanic.GameMap;
import mechanic.Panel;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import states.StateGame;
import unit.Unit;

public class VampiricSlash extends Spell {	
	public static final float ANIMATION_DURATION = 0.3f;
	public static final float SLASH_DELAY = 0.4f;
	public static final float DRAW_HEIGHT = 0;
	public static final Color COLOR = new Color(255, 100, 100);
	int length;
	int height;
	double damage;
	static Image animation;
	float timer;
	float slashDelay;
	float animationDuration;
	boolean flipVertical;
	ArrayList<Panel> affectedPanels;
	public VampiricSlash(Unit owner, int length, int height, double damage, float slashDelay, float animationDuration) {
		super(owner, slashDelay, animationDuration, "Vampiric Slash", "Slash an area in front of you and steal health from all units you hit", "res/spell/vampiricslash.png", false);
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
	public VampiricSlash(Unit owner, int length, int height, double damage) {
		this(owner, length, height, damage, SLASH_DELAY, ANIMATION_DURATION);
	}
	public VampiricSlash(Unit owner) {
		this(owner, 3, 1, 25);
	}
	@Override
	public void onActivate() {
		for(int y = -height; y <= height; y++) {
			for(int x = 1; x <= length; x++) {
				Point slashLoc = Point.add(this.owner.gridLoc, Point.add(new Point(0, y), Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.direction), x)));
				if(this.getMap().pointIsInGrid(slashLoc) && this.getMap().getPanelAt(slashLoc).unitStandingOnPanel != null && this.getMap().getPanelAt(slashLoc).unitStandingOnPanel.teamID != this.owner.teamID) {
					Unit target = this.getMap().getPanelAt(slashLoc).unitStandingOnPanel;
					target.doDamage(this.damage * this.owner.finalDamageModifier);
					this.owner.doDamage(-this.damage * this.owner.finalDamageModifier);
					ParticleEmitter pe = new ParticleEmitter(Point.add(this.owner.getLoc(), new Point(0, -this.owner.getDrawHeight())), EmitterTypes.CIRCLE_DIRECTION, "res/particle_heal.png", true, /*point, emitter type, image path, alphaDecay*/
							0.4f, 0.6f, /*particle start scale*/
							0.4f, 0.6f, /*particle end scale*/
							12.5f, /*drag*/
							0, 0, /*rotational velocity*/
							0.6f, 0.8f, /*min and max lifetime*/
							1000, 1250, /*min and max launch speed*/
							0, 3, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
							0, 50, 90, 0); /*keyvalues*/
					this.getMap().addParticleEmitter(pe);
					float direction = (float)Point.getDirectionDeg(Point.add(target.getLoc(), new Point(0, -target.getDrawHeight())), Point.add(this.owner.getLoc(), new Point(0, -this.owner.getDrawHeight())));
					float distance = (float)Point.getDistance(Point.add(target.getLoc(), new Point(0, -target.getDrawHeight())), Point.add(this.owner.getLoc(), new Point(0, -this.owner.getDrawHeight())));
					pe = new ParticleEmitter(Point.add(target.getLoc(), new Point(0, -target.getDrawHeight())), EmitterTypes.CIRCLE_DIRECTION, "res/particle_genericRed.png", true, /*point, emitter type, image path, alphaDecay*/
							7.2f, 7.2f, /*particle start scale*/
							2.4f, 2.4f, /*particle end scale*/
							0.5f, /*drag*/
							0, 0, /*rotational velocity*/
							ANIMATION_DURATION, ANIMATION_DURATION, /*min and max lifetime*/
							distance / ANIMATION_DURATION, distance / ANIMATION_DURATION, /*min and max launch speed*/
							0, 3, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
							0, 50, direction, 0); /*keyvalues*/
					this.getMap().addParticleEmitter(pe);
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
		g.drawImage(animation, x, y, x2, y2, srcx, srcy, srcx2, srcy2, COLOR);
		if(StateGame.isServer)
		this.getMap().addToDrawInfo(GameMap.getDrawDataA("res/spell/slashanimation.png", x, y, x2 - x, y2 - y, srcx, srcy, srcx2, srcy2, COLOR.getRed(), COLOR.getGreen(), COLOR.getBlue(), COLOR.getAlpha()));
	}
	@Override
	public void onSpellUpdate() {
		this.timer += this.getFrameTime();
		if(this.timer >= this.animationDuration) {
			this.finishSpell();
		}
	}
}
