package spell;

import mechanic.GameMap;
import mechanic.Panel;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import projectile.Projectile;
import statuseffect.StatusFrost;
import unit.Unit;

public class Blizzard extends Spell {
	public static final float SPEED_MODIFIER = 0.75f;
	public static final float DAMAGE_PER_SECOND = 0.25f;
	public static final float DAMAGE_MULT_PER_INSTANCE = 0.75f;
	public static final float DEBUFF_DURATION = 1;
	public static final int APPLY_TICKS = 40;
	public static final float APPLY_INTERVAL = 0.25f;
	int appliedTicks;
	public Blizzard(Unit owner) {
		super(owner, 0, 0, "Blizzard", "Gradually slows all enemy units and projectiles on the map, deals slight damage over time, and reduces some projectile's damage", "res/statuseffect/icon_frost.png", true);
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
		for(Panel p : this.getMap().getPanels()) {
			if(p.unitStandingOnPanel != null && p.unitStandingOnPanel.teamID != this.owner.teamID) {
				p.unitStandingOnPanel.addStatusEffect(new StatusFrost(p.unitStandingOnPanel, SPEED_MODIFIER, DAMAGE_PER_SECOND, DEBUFF_DURATION, DAMAGE_MULT_PER_INSTANCE, 1));
			}
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
				p.addStatusEffect(new StatusFrost(p, SPEED_MODIFIER, DAMAGE_PER_SECOND, DEBUFF_DURATION, DAMAGE_MULT_PER_INSTANCE, 1));
			}
		}
		this.appliedTicks++;
		if(this.appliedTicks >= APPLY_TICKS) {
			this.finishSpell();
		}
	}
}
