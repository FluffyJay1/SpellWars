package projectile;

import java.util.ArrayList;

import mechanic.GameMap;
import mechanic.PanelState;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;

public class LavaGrenade extends Grenade {
	public static final float AIR_DURATION = 1.4f;
	public static final ArrayList<Point> AFFECTED_POINTS = Point.getIntegerPointsInCircle(1.5);
	public LavaGrenade(int damage, int direction, Point gridLoc, int teamID) {
		super(damage, AIR_DURATION, 4, 70, 10, direction, gridLoc, "res/projectile/lavagrenade.png", teamID);
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
				this.getMap().getPanelAt(f).setPanelState(PanelState.LAVA);
				ParticleEmitter pe = new ParticleEmitter(this.getMap().gridToPosition(f), EmitterTypes.CIRCLE_DIRECTION, GameMap.particle_genericYellow, false, //point/parent, emitter type, image path, alphaDecay
						8.5f, 3.5f, //particle start scale
						0.0f, 0.0f, //particle end scale
						4.5f, //drag
						0, 0, //rotational velocity
						0.3f, 0.5f, //min and max lifetime
						10, 1500, //min and max launch speed
						0, 12, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
						0, (float)this.getMap().getSizeOfPanel().y/2, 90, 20); //keyvalues
				this.getMap().addParticleEmitter(pe);
			}
		}
	}
}
