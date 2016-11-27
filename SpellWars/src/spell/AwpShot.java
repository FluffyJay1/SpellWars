package spell;

import mechanic.GameMap;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import projectile.AwpProjectile;
import unit.Unit;

public class AwpShot extends Spell {
	public static final double DAMAGE = 200;
	public static final double SPEED = 20;
	public static final double PENETRATION_MODIFIER = 0.5;
	AwpProjectile projectile;
	public AwpShot(Unit owner) {
		super(owner, 0, 0, "Awp Shot", "Shoot a bullet with deadly damage which penetrates shields and barriers, bullet pierces with reduced damage", "res/spell/awpshot.png", true);
	}
	@Override
	public void onActivate() {
		projectile = new AwpProjectile(DAMAGE * this.owner.finalDamageOutputModifier, PENETRATION_MODIFIER, SPEED, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
		this.getMap().addGameElement(projectile);
		float direction = 0;
		if(this.owner.direction == GameMap.ID_LEFT) {
			direction = 180;
		}
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.owner.getLoc(), new Point(0, -28)), EmitterTypes.POINT_DIRECTION, "res/particle_genericWhite.png", false, //point/parent, emitter type, image path, alphaDecay
				6.5f, 1.5f, //particle start scale
				0.0f, 0.0f, //particle end scale
				5.5f, //drag
				0, 0, //rotational velocity
				0.6f, 1.2f, //min and max lifetime
				200, 6000, //min and max launch speed
				0, 32, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
				direction, 50, 0, 0); //keyvalues
		this.getMap().addParticleEmitter(pe);
	}
	@Override
	public void onSpellUpdate() {
		if(projectile.trail.getRemove()) {
			this.finishSpell();
		}
	}
}
