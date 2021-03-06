package spell;


import mechanic.GameMap;
import mechanic.Panel;
import mechanic.PanelState;
import mechanic.Point;
import projectile.BouncingOrbProjectile;
import projectile.Grenade;
import projectile.Projectile;
import unit.Unit;

public class TestFireball extends Spell {
	Projectile projectile;
	float spellTimer;
	int shotsFired;
	final static double[] DAMAGE_FOR_SHOT = {7,6,5,4,4,3,3,2,2,2,2,1,1,1,1,1};
	public TestFireball(Unit owner) {
		super(owner, 0.3f, 1.3f, "Firebreath", "Shoots a bunch of fire, initially dealing 15-30 damage each and then dealing up to 45 extra damage total afterwards", "res/particle_explosion.png", false, true);
		spellTimer = 1.2f;
		shotsFired = 0;
	}
	@Override
	public void onActivate() {
		for(int i = -1; i <= 1; i++) {
			projectile = new Projectile((15 + (int)(Math.random() * 3.99) * 5) * this.owner.finalDamageOutputModifier, 16 - 4 * Math.abs(i), GameMap.clampDirection((char) (GameMap.getOppositeDirection((char)this.owner.teamID))), Point.add(this.owner.gridLoc, new Point(0, i)), "res/particle_explosion.png", this.owner.teamID, false, true, true);
			projectile.setDrawHeight(10);
			this.map.addGameElement(projectile);
			/*
			if(i == 0) {
				Panel p = this.map.getPanelAt(Point.add(Point.add(this.owner.gridLoc, Point.scale(GameMap.getFuturePoint(new Point(), GameMap.getOppositeDirection((char)this.owner.teamID)), 4)), new Point(0, i)));
				if(p != null) {
					p.crackLight();
				}
			}
			*/
		}
		this.setThinkInterval(0.075f);
		//this.finishSpell();
	}
	@Override
	public void onThink() {
		projectile = new Projectile(DAMAGE_FOR_SHOT[shotsFired] * this.owner.finalDamageOutputModifier, 16, GameMap.clampDirection((char) (GameMap.getOppositeDirection((char)this.owner.teamID))), this.owner.gridLoc, "res/particle_explosion.png", this.owner.teamID, true, true, true);
		projectile.setImageScale(this.spellTimer/1.2f + 0.1f);
		projectile.setDrawHeight(10);
		shotsFired++;
		//System.out.println("DAMAGE " + (int)(8.85 * this.spellTimer/0.7f));
		this.map.addGameElement(projectile);
		if(this.shotsFired >= 16) {
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