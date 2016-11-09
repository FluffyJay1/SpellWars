package spell;

import mechanic.GameMap;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import projectile.VulcanProjectile;
import projectile.WindProjectile;
import unit.Unit;

public class Vulcan extends Spell {
	public static final int DAMAGE = 10;
	public static final int BONUS_DAMAGE = 5;
	public static final float STUN_DURATION = 1f;
	public static final int NUM_SHOTS = 5;
	public static final float DURATION = 0.5f;
	public static final double SPEED = 28;
	int shotsFired;
	public Vulcan(Unit owner) {
		super(owner, 0, DURATION, "Vulcan", "Shoot a rapid-fire volley of bullets, and if the enemy is casting, it deals extra damage and stuns", "res/spell/vulcan.png", false, true);
	}
	@Override
	public void onActivate() {
		this.shotsFired = 0;
		this.setThinkInterval(DURATION/NUM_SHOTS);
		this.onThink();
	}
	@Override
	public void onThink() {
		VulcanProjectile projectile = new VulcanProjectile(DAMAGE * this.owner.finalDamageModifier, BONUS_DAMAGE * this.owner.finalDamageModifier, STUN_DURATION, SPEED, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
		this.getMap().addGameElement(projectile);
		this.shotsFired++;
		float direction = 0;
		if(this.owner.direction == GameMap.ID_LEFT) {
			direction = 180;
		}
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.owner.getLoc(), new Point(0, -20)), EmitterTypes.POINT_DIRECTION, "res/particle_explosion.png", true, //point/parent, emitter type, image path, alphaDecay
				0.2f, 0.35f, //particle start scale
				0.0f, 0.0f, //particle end scale
				11.5f, //drag
				0, 0, //rotational velocity
				0.1f, 0.25f, //min and max lifetime
				1000, 3400, //min and max launch speed
				0, 5, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
				direction, 25, 0, 0); //keyvalues
		this.getMap().addParticleEmitter(pe);
		if(this.shotsFired >= NUM_SHOTS) {
			this.finishSpell();
		}
	}
}
