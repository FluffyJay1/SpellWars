package spell;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import mechanic.Game;
import mechanic.GameMap;
import mechanic.Panel;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import projectile.Projectile;
import statuseffect.StatusTimeDilation;
import unit.Unit;

public class TimeDilation extends Spell {
	public static final float ALLIED_SPEED_MULT = 4;
	public static final float ENEMY_SPEED_MULT = 0.01f;
	public static final float NUM_TICKS = 5;
	public static final float INTERVAL = 2.5f;
	static Image clock;
	static Image clockHand;
	int ticks;
	float timer;
	public TimeDilation(Unit owner) {
		super(owner, 0, 0, "Time Dilation", "Periodically speeds up allied units and projectiles and slows enemy units and projectiles", "res/spell/timedilation.png", true);
		if(clock == null) {
			try {
				clock = new Image("res/circle256x256.png");
				clock.setAlpha(0.5f);
				clockHand = new Image("res/clockhand.png");
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		timer = 0;
	}
	@Override
	public void onActivate() {
		this.getMap().unpauseAll();
		this.setThinkInterval(INTERVAL);
		this.ticks = 0;
		this.onThink();
		ParticleEmitter pe = new ParticleEmitter(new Point(Game.WINDOW_WIDTH/2, Game.WINDOW_HEIGHT/2), EmitterTypes.POINT_DIRECTION, "res/spell/timedilation.png", true, /*point, emitter type, image path, alphaDecay*/
				20.0f, 20.0f, /*particle start scale*/
				25, 25, /*particle end scale*/
				0.0f, /*drag*/
				0, 0, /*rotational velocity*/
				0.5f, 0.5f, /*min and max lifetime*/
				0, 0, /*min and max launch speed*/
				NUM_TICKS * INTERVAL, 1/INTERVAL, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
				0, 0, 0, 0); /*keyvalues*/
		this.getMap().addParticleEmitter(pe);
		pe.emit();
	}
	@Override
	public void onThink() {
		for(Panel p : this.getMap().getPanels()) {
			if(p.unitStandingOnPanel != null) {
				if(p.unitStandingOnPanel.teamID == this.owner.teamID) {
					p.unitStandingOnPanel.addStatusEffect(new StatusTimeDilation(p.unitStandingOnPanel, ALLIED_SPEED_MULT, INTERVAL, 1));
				} else {
					p.unitStandingOnPanel.addStatusEffect(new StatusTimeDilation(p.unitStandingOnPanel, ENEMY_SPEED_MULT, INTERVAL, 1));
				}
			}
		}
		for(Projectile p : this.getMap().getProjectiles()) {
			if(p.teamID == this.owner.teamID) {
				p.addStatusEffect(new StatusTimeDilation(p, ALLIED_SPEED_MULT, INTERVAL, 1));
			} else {
				p.addStatusEffect(new StatusTimeDilation(p, ENEMY_SPEED_MULT, INTERVAL, 1));
			}
		}
		this.ticks++;
		if(this.ticks >= NUM_TICKS) {
			this.finishSpell();
		}
	}
	@Override
	public void onSpellUpdate() {
		this.timer += this.getFrameTime();
	}
	@Override
	public void drawSpecialEffects(Graphics g) {
		Point drawLoc = Point.add(new Point(Game.WINDOW_WIDTH/2, 200), Point.scale(GameMap.getFuturePoint(new Point(), (char)owner.teamID), 256));
		Color clockColor = new Color(255, 200, 200); //red
		if(owner.teamID == GameMap.ID_RIGHT) {
			clockColor = new Color(200, 200, 255);
		}
		g.drawImage(clock, (float)(drawLoc.x - clock.getWidth()/2), (float)(drawLoc.y - clock.getHeight()/2), clockColor);
		for(double rad = -Math.PI/2; rad < Math.PI*3/2; rad += Math.PI*2/(NUM_TICKS - 1)) {
			g.drawImage(GameMap.particle_genericBlue, (float)(Math.cos(rad) * 120 + drawLoc.x - GameMap.particle_genericBlue.getWidth()/2), (float)(Math.sin(rad) * 120 + drawLoc.y - GameMap.particle_genericBlue.getHeight()/2));
		}
		Image temphand = clockHand.copy();
		temphand.rotate(360 * this.timer/((NUM_TICKS - 1) * INTERVAL));
		g.drawImage(temphand, (float)(drawLoc.x - temphand.getWidth()/2), (float)(drawLoc.y - temphand.getHeight()/2));
	}
}
