package spell;

import java.util.ArrayList;

import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import projectile.DebuffTransferProjectile;
import statuseffect.StatusEffect;
import unit.Unit;

public class DebuffTransfer extends Spell {
	public static final double DAMAGE = 30;
	public static final double DAMAGE_PER_DEBUFF = 10;
	public static final double SPEED = 26;
	public static final double SPEED_PER_DEBUFF = 4;
	public DebuffTransfer(Unit owner) {
		super(owner, 0.2f, 0.4f, "Debuff Transfer", "Removes your debuffs, puts them on a projectile, and tranfers those debuffs to all enemies it hits", "res/spell/debufftransfer.png", false);
	}
	@Override
	public void onStartCast() {
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.CIRCLE_RADIAL, "res/projectile/debufftransfer.png", true, /*point, emitter type, image path, alphaDecay*/
				0.0f, 0.0f, /*particle start scale*/
				1.0f, 1.0f, /*particle end scale*/
				8.5f, /*drag*/
				0, 0, /*rotational velocity*/
				0.4f, 0.4f, /*min and max lifetime*/
				-4350, -4350, /*min and max launch speed*/
				0, 20, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
				400, 400, 0, 0); /*keyvalues*/
		getOwner().getMap().addParticleEmitter(pe);
	}
	@Override
	public void onActivate() {
		ArrayList<StatusEffect> effects = new ArrayList<StatusEffect>();
		for(StatusEffect e : this.owner.getPurgableDebuffs()) {
			effects.add(e.clone());
		}
		int debuffs = this.owner.purgeDebuffs();
		DebuffTransferProjectile p = new DebuffTransferProjectile((DAMAGE + DAMAGE_PER_DEBUFF * debuffs) * this.owner.finalDamageOutputModifier, SPEED + SPEED_PER_DEBUFF * debuffs, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
		p.setEffects(effects);
		this.getMap().addGameElement(p);
	}
}
