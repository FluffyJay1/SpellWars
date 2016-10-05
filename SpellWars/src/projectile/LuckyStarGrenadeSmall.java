package projectile;

import mechanic.PanelState;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;

public class LuckyStarGrenadeSmall extends Grenade {
	public static final float AIR_DURATION = 0.2f;
	public static final float START_HEIGHT = 1200;
	public static final float END_HEIGHT = 40;
	public LuckyStarGrenadeSmall(int damage, int distance, int direction, Point gridLoc, int teamID) {
		super(damage, AIR_DURATION, distance, START_HEIGHT, END_HEIGHT, direction, gridLoc, "res/projectile/luckystar.png", teamID);
		this.drawShadow = false;
	}
	@Override
	public void onGrenadeLanded() {
		this.getMap().getPanelAt(this.endLoc).crackLight();
		ParticleEmitter pe = new ParticleEmitter(this.getMap().gridToPosition(this.endLoc), EmitterTypes.CIRCLE_DIRECTION, "res/projectile/luckystar.png", false, //point/parent, emitter type, image path, alphaDecay
				0.3f, 0.5f, //particle start scale
				0.0f, 0.0f, //particle end scale
				4.5f, //drag
				-200, 200, //rotational velocity
				0.3f, 0.5f, //min and max lifetime
				300, 1000, //min and max launch speed
				0, 4, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
				0, (float)this.getMap().getSizeOfPanel().y/2, 90, 20); //keyvalues
		this.getMap().addParticleEmitter(pe);
	}
}
