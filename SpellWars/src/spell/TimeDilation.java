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
import states.StateGame;
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
	float tickTimer;
	public TimeDilation(Unit owner) {
		super(owner, 0, 0, "Time Dilation", "Periodically speeds up allied units and projectiles and slows enemy units and projectiles", "res/spell/timedilation.png", true);
		if(clock == null) {
			try {
				clock = new Image("res/circle256x256.png");
				clockHand = new Image("res/clockhand.png");
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.registerFieldEffect("time dilation");
		timer = 0;
		tickTimer = 0;
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
				0.3f, 0.3f, /*min and max lifetime*/
				0, 0, /*min and max launch speed*/
				NUM_TICKS * INTERVAL, 1/INTERVAL, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
				0, 0, 0, 0); /*keyvalues*/
		this.getMap().addParticleEmitter(pe);
		pe.emit();
	}
	@Override
	public void onThink() {
		for(Unit u : this.getMap().getUnits()) {
			if(u.teamID == this.owner.teamID) {
				u.addStatusEffect(new StatusTimeDilation(u, ALLIED_SPEED_MULT, INTERVAL, 1));
			} else {
				u.addStatusEffect(new StatusTimeDilation(u, ENEMY_SPEED_MULT, INTERVAL, 1));
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
		this.tickTimer = 0;
		if(this.ticks >= NUM_TICKS) {
			this.finishSpell();
		}
	}
	@Override
	public void onSpellUpdate() {
		this.timer += this.getFrameTime();
		this.tickTimer += this.getFrameTime();
		for(Unit u : this.getMap().getUnits()) {
			if(!u.hasStatusEffect(StatusTimeDilation.ID)) {
				if(u.teamID == this.owner.teamID) {
					u.addStatusEffect(new StatusTimeDilation(u, ALLIED_SPEED_MULT * (float)Math.pow(StatusTimeDilation.SPEED_DECAY_PER_INTERVAL, (tickTimer/StatusTimeDilation.UPDATE_INTERVAL)), INTERVAL - tickTimer, 1));
				} else {
					u.addStatusEffect(new StatusTimeDilation(u, ENEMY_SPEED_MULT * (float)Math.pow(StatusTimeDilation.SPEED_DECAY_PER_INTERVAL, (tickTimer/StatusTimeDilation.UPDATE_INTERVAL)), INTERVAL - tickTimer, 1));
				}
			}
		}
		for(Projectile p : this.getMap().getProjectiles()) {
			if(!p.hasStatusEffect(StatusTimeDilation.ID)) {
				if(p.teamID == this.owner.teamID) {
					p.addStatusEffect(new StatusTimeDilation(p, ALLIED_SPEED_MULT * (float)Math.pow(StatusTimeDilation.SPEED_DECAY_PER_INTERVAL, (tickTimer/StatusTimeDilation.UPDATE_INTERVAL)), INTERVAL - tickTimer, 1));
				} else {
					p.addStatusEffect(new StatusTimeDilation(p, ENEMY_SPEED_MULT * (float)Math.pow(StatusTimeDilation.SPEED_DECAY_PER_INTERVAL, (tickTimer/StatusTimeDilation.UPDATE_INTERVAL)), INTERVAL - tickTimer, 1));
				}
			}
		}
	}
	@Override
	public void drawSpecialEffects(Graphics g) {
		Point drawLoc = Point.add(new Point(Game.WINDOW_WIDTH/2, 200), Point.scale(GameMap.getFuturePoint(new Point(), (char)owner.teamID), 256));
		Color clockColor = new Color(255, 200, 200, 128); //red
		if(owner.teamID == GameMap.ID_RIGHT) {
			clockColor = new Color(200, 200, 255, 128);
		}
		g.drawImage(clock, (float)(drawLoc.x - clock.getWidth()/2), (float)(drawLoc.y - clock.getHeight()/2), clockColor);
		if(StateGame.isServer)
		this.getMap().addToDrawInfo(GameMap.getDrawDataI("res/circle256x256.png", drawLoc.x - clock.getWidth()/2, drawLoc.y - clock.getHeight()/2, clock.getWidth(), clock.getHeight(), 0, clockColor.getRed(), clockColor.getGreen(), clockColor.getBlue(), clockColor.getAlpha(), 0));
		for(double rad = -Math.PI/2; rad < Math.PI*3/2; rad += Math.PI*2/(NUM_TICKS - 1)) {
			g.drawImage(GameMap.particle_genericWhite, (float)(Math.cos(rad) * 120 + drawLoc.x - GameMap.particle_genericWhite.getWidth()/2), (float)(Math.sin(rad) * 120 + drawLoc.y - GameMap.particle_genericWhite.getHeight()/2));
			if(StateGame.isServer)
			this.getMap().addToDrawInfo(GameMap.getDrawDataI("res/particle_genericWhite.png", Math.cos(rad) * 120 + drawLoc.x - GameMap.particle_genericWhite.getWidth()/2, Math.sin(rad) * 120 + drawLoc.y - GameMap.particle_genericWhite.getHeight()/2, GameMap.particle_genericWhite.getWidth(), GameMap.particle_genericWhite.getHeight(), 0, 255, 255, 255, 255, 0));
		}
		Image temphand = clockHand.copy();
		temphand.rotate(360 * this.timer/((NUM_TICKS - 1) * INTERVAL));
		g.drawImage(temphand, (float)(drawLoc.x - temphand.getWidth()/2), (float)(drawLoc.y - temphand.getHeight()/2));
		if(StateGame.isServer)
		this.getMap().addToDrawInfo(GameMap.getDrawDataI("res/clockhand.png", drawLoc.x - clockHand.getWidth()/2, drawLoc.y - clockHand.getHeight()/2, clockHand.getWidth(), clockHand.getHeight(), 360 * this.timer/((NUM_TICKS - 1) * INTERVAL), 255, 255, 255, 255, 0));
	}
}
