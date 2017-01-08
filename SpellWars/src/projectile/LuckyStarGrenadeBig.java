package projectile;

import java.util.ArrayList;

import mechanic.PanelState;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import shield.Shield;

public class LuckyStarGrenadeBig extends Grenade {
	public static final float AIR_DURATION = 0.8f;
	public static final float START_HEIGHT = 1600;
	public static final float END_HEIGHT = 60;
	public static final ArrayList<Point> AFFECTED_POINTS = Point.getIntegerPointsInCircle(1.5);
	double areaDamage;
	public LuckyStarGrenadeBig(double damage, double areaDamage, int direction, Point gridLoc, int teamID) {
		super(damage, AIR_DURATION, 4, START_HEIGHT, END_HEIGHT, direction, gridLoc, "res/projectile/luckystar.png", teamID);
		this.areaDamage = areaDamage;
		this.drawShadow = false;
		this.setImageScale(2.5f);
		this.setShieldBehavior(Shield.SHIELD_IGNORE);
	}
	@Override
	public void flash() {
		if(this.flashDestinationPanel && this.getMap().pointIsInGrid(this.endLoc) && (this.duration - this.timeElapsed <= CONSTANT_FLASH_DURATION || this.timeElapsed % (FLASH_INTERVAL * (1 - this.timeElapsed/this.duration/2)) < (1 - this.timeElapsed/this.duration/2) * FLASH_INTERVAL/2)) {
			for(Point p : AFFECTED_POINTS) {
				Point f = Point.add(p, this.endLoc);
				if(this.getMap().pointIsInGrid(f)) {
					this.getMap().getPanelAt(f).panelFlash();
				}
			}
		}
		if(this.duration - this.timeElapsed <= IMPORTANT_FLASH_DURATION) {
			if(this.getMap().pointIsInGrid(endLoc)) {
				this.getMap().getPanelAt(endLoc).panelFlashImportant();
			}
		}
	}
	@Override
	public void onGrenadeLanded() {
		for(Point p : AFFECTED_POINTS) {
			Point f = Point.add(p, this.endLoc);
			if(this.getMap().pointIsInGrid(f)) {
				this.getMap().getPanelAt(f).crackLight();
				if(this.getMap().getPanelAt(f).unitStandingOnPanel != null && this.getMap().getPanelAt(f).unitStandingOnPanel.teamID != this.teamID) {
					this.getMap().getPanelAt(f).unitStandingOnPanel.doDamage(this.areaDamage, true, this.shieldbehavior, null);
					for(Shield s : this.getMap().getPanelAt(f).unitStandingOnPanel.getShields()) {
						s.isDead = true;
					}
				}
				ParticleEmitter pe = new ParticleEmitter(this.getMap().gridToPosition(f), EmitterTypes.CIRCLE_DIRECTION, "res/projectile/luckystar.png", false, //point/parent, emitter type, image path, alphaDecay
						0.4f, 0.6f, //particle start scale
						0.0f, 0.0f, //particle end scale
						4.5f, //drag
						-200, 200, //rotational velocity
						0.3f, 0.5f, //min and max lifetime
						300, 500, //min and max launch speed
						0, 8, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
						0, (float)this.getMap().getSizeOfPanel().y/2, 90, 20); //keyvalues
				this.getMap().addParticleEmitter(pe);
			}
		}
	}
}
