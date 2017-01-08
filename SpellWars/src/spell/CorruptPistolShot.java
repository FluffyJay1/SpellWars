package spell;

import mechanic.GameMap;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import projectile.Projectile;
import unit.Unit;

public class CorruptPistolShot extends Spell {
	public static final double DAMAGE = 150;
	public static final int SPEED = 72;
	public CorruptPistolShot(Unit owner) {
		super(owner, 0, 0, "Corrupt Pistol Shot", "P|O|W|E|R", "res/spell/corruptpistolshot.png", false);
	}
	@Override
	public void onActivate() {
		Projectile projectile = new Projectile(DAMAGE * this.owner.finalDamageOutputModifier, SPEED, GameMap.clampDirection((char) (GameMap.getOppositeDirection((char)this.owner.teamID))), this.owner.gridLoc, "res/particle_genericPurple.png", this.owner.teamID, true, true, true);
		this.map.addGameElement(projectile);
		projectile.setDrawHeight(40);
		projectile.setImageScale(8);
		projectile.drawShadow = true;
		float direction = 0;
		if(this.owner.direction == GameMap.ID_LEFT) {
			direction = 180;
		}
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.owner.getLoc(), new Point(0, -28)), EmitterTypes.POINT_DIRECTION, "res/particle_genericPurple.png", false, //point/parent, emitter type, image path, alphaDecay
				6.5f, 1.5f, //particle start scale
				0.0f, 0.0f, //particle end scale
				13.5f, //drag
				0, 0, //rotational velocity
				0.1f, 0.45f, //min and max lifetime
				200, 12200, //min and max launch speed
				0, 16, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
				direction, 30, 0, 0); //keyvalues
		this.getMap().addParticleEmitter(pe);
		this.finishSpell();
		this.corrupt();
	}
}
