package projectile;

import java.util.ArrayList;

import mechanic.GameMap;
import mechanic.PanelState;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;

public class MudGrenade extends Grenade {
	public static final float AIR_DURATION = 0.9f;
	public static final ArrayList<Point> AFFECTED_POINTS = Point.getIntegerPointsInCircle(1.5);
	public MudGrenade(int damage, int direction, Point gridLoc, int teamID) {
		super(damage, AIR_DURATION, 4, 70, 10, direction, gridLoc, "res/projectile/mudgrenade.png", teamID);
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
				this.getMap().getPanelAt(f).setPanelState(PanelState.MUD);
			}
		}
	}
}
