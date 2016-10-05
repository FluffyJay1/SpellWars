package spell;

import mechanic.GameMap;
import mechanic.Panel;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import projectile.Projectile;
import statuseffect.StatusFrost;
import statuseffect.StatusFrostMei;
import unit.Unit;

public class MeiBlizzard extends Spell {
	public static final int APPLY_TICKS = 10;
	public static final float APPLY_INTERVAL = 0.5f;
	public static final double DAMAGE_PER_TICK = 2;
	int appliedTicks;
	public MeiBlizzard(Unit owner) {
		super(owner, 0, 0, "Dong Zhu, Bu xu Zou", "FREEZE DON'T MOVE", "res/statuseffect/icon_frost.png", true);
		this.setThinkInterval(APPLY_INTERVAL);
		this.appliedTicks = 0;
	}
	@Override
	public void onActivate() {
		this.onThink();
		this.getMap().unpauseAll();
	}
	@Override
	public void onThink() {
		for(Unit u : this.getMap().getUnitsOfTeam(GameMap.getOppositeDirection((char)this.owner.teamID))) {
			u.addStatusEffect(new StatusFrostMei(u, 1));
			u.doDamage(DAMAGE_PER_TICK * this.owner.finalDamageModifier);
		}
		for(Panel p : this.getMap().getPanels()) {
			if(p.teamID != this.owner.teamID) {
				ParticleEmitter pe = new ParticleEmitter(this.getMap().gridToPosition(p.getLoc()), EmitterTypes.POINT_RADIAL, "res/particle_genericBlue.png", true, /*point, emitter type, image path, alphaDecay*/
						1.5f, 1.5f, /*particle start scale*/
						4.0f, 8.0f, /*particle end scale*/
						3.5f, /*drag*/
						0, 0, /*rotational velocity*/
						0.5f, 0.6f, /*min and max lifetime*/
						0, 350, /*min and max launch speed*/
						0, 1, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
						0, 0, 0, 0); /*keyvalues*/
				this.getMap().addParticleEmitter(pe);
			}
		}
		for(Projectile p : this.getMap().getProjectiles()) {
			if(p.teamID != this.owner.teamID) {
				p.addStatusEffect(new StatusFrostMei(p, 1));
			}
		}
		this.appliedTicks++;
		if(this.appliedTicks >= APPLY_TICKS) {
			this.finishSpell();
		}
	}
}
