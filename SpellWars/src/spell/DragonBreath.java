package spell;


import mechanic.GameMap;
import mechanic.Panel;
import mechanic.PanelState;
import mechanic.Point;
import projectile.BouncingOrbProjectile;
import projectile.Grenade;
import projectile.Projectile;
import unit.Unit;

public class DragonBreath extends Spell {
	Projectile projectile;
	float spellTimer;
	int shotsFired;
	final static float SHOT_INTERVAL = 0.075f;
	final static int NUM_SHOTS = 25;
	final static int DAMAGE = 5;
	public DragonBreath(Unit owner) {
		super(owner, 0.3f, 0, "Dragon's breath", "Shoot a whole lot of fire, and you can move while you shoot", "res/spell/dragonbreath.png", false, true);
		spellTimer = 1.2f;
		shotsFired = 0;
	}
	@Override
	public void onActivate() {
		this.onThink();
		this.setThinkInterval(SHOT_INTERVAL);
		//this.finishSpell();
	}
	@Override
	public void onThink() {
		for(int i = -1; i <= 1; i++) {
			projectile = new Projectile(DAMAGE * this.owner.finalDamageModifier, 16, GameMap.clampDirection((char) (GameMap.getOppositeDirection((char)this.owner.teamID))), Point.add(this.owner.gridLoc, new Point(0, i)), "res/particle_explosion.png", this.owner.teamID, true, true, true);
			projectile.setDrawHeight(10);
			this.map.addGameElement(projectile);
		}
		shotsFired++;
		//System.out.println("DAMAGE " + (int)(8.85 * this.spellTimer/0.7f));
		if(this.shotsFired >= NUM_SHOTS) {
			this.finishSpell();
		}
	}
	@Override
	public void onSpellUpdate() {
		/*
		if(this.owner.getRemove()) {
			this.finishSpell();
		}
		*/
		this.spellTimer -= this.getFrameTime();
		if(this.owner.getRemove()) {
			this.finishSpell();
		}
	}
}