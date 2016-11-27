package spell;

import projectile.MeiFrostProjectile;
import unit.Unit;

public class EndothermicBlasterPrimary extends Spell {
	public static final double DAMAGE = 2;
	public static final int MIN_SHOTS = 3;
	public static final int MAX_SHOTS = 6;
	public static final float SHOT_INTERVAL = 0.1f;
	int numShots;
	public EndothermicBlasterPrimary(Unit owner) {
		super(owner, 0f, 0.1f, "Endothermic Blast", "Shoots Mei's gun that slows", "res/particle_genericBlue.png", false);
		this.setThinkInterval(SHOT_INTERVAL);
		this.numShots = (int)(MIN_SHOTS + Math.random() * (MAX_SHOTS + 1 - MIN_SHOTS));
	}
	@Override
	public void onActivate() {
		this.onThink();
	}
	public void onThink() {
		MeiFrostProjectile p = new MeiFrostProjectile(DAMAGE * this.owner.finalDamageOutputModifier, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
		this.getMap().addGameElement(p);
		this.numShots--;
		if(this.numShots <= 0) {
			this.finishSpell();
		}
	}
}
