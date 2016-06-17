package projectile;

import mechanic.GameMap;
import mechanic.Panel;
import mechanic.PanelState;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import unit.Unit;

public class CrackGrenade extends Grenade {
	float crackChance;
	public CrackGrenade(int damage, float crackChance, double duration, Point endDisplacement, float initialHeight, float endHeight, Point gridLoc, String imagePath, int teamID) {
		super(damage, duration, endDisplacement, initialHeight, endHeight, gridLoc, imagePath, teamID);
		this.crackChance = crackChance;
	}
	@Override
	public void onGrenadeLanded() {
		//String i = "res/particle_genericYellow.png";
		if(Math.random() < this.crackChance) {
			Panel panel = this.getMap().getPanelAt(this.endLoc);
			if(panel.getPanelState() != PanelState.HOLE) {
				panel.setPanelState(PanelState.CRACKED);
			}
			ParticleEmitter pe = new ParticleEmitter(this.getLoc(), EmitterTypes.POINT_DIRECTION, GameMap.particle_genericWhite, false, //point/parent, emitter type, image path, alphaDecay
					2.8f, 3.0f, //particle start scale
					0, 0, //particle end scale
					10.5f, //drag
					0, 0, //rotational velocity
					0.1f, 0.55f, //min and max lifetime
					200, 4200, //min and max launch speed
					0, 15, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
					90, 60, 0, 0); //keyvalues
			this.getMap().addParticleEmitter(pe);
		}
		ParticleEmitter pe = new ParticleEmitter(this.getLoc(), EmitterTypes.POINT_RADIAL, GameMap.particle_genericYellow, false, //point/parent, emitter type, image path, alphaDecay
				1.4f, 2.6f, //particle start scale
				0, 0, //particle end scale
				18.5f, //drag
				0, 0, //rotational velocity
				0.1f, 0.25f, //min and max lifetime
				200, 3200, //min and max launch speed
				0, 5, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
				0, 0, 0, 0); //keyvalues
		this.getMap().addParticleEmitter(pe);
	}
}
