package spell;

import mechanic.GameMap;
import projectile.BouncingOrbProjectile;
import projectile.Projectile;
import unit.Unit;

public class BouncingOrb extends Spell {
	public static final float DAMAGE = 10;
	public static final float SPEED = 10;
	public static final int NUM_SQUARES = 20;
	//float timer;
	public BouncingOrb(Unit owner) {
		super(owner, 0.05f, 0.25f, "Bouncing Orb", "Shoots an orb that bounces in the enemy territory, which is able to hit the same unit multuple times",
				"res/betterTower.png", false);
		//this.timer = (NUM_SQUARES + 0.5f)/SPEED;
	}
	@Override
	public void onActivate() {
		Projectile projectile = new BouncingOrbProjectile(DAMAGE, SPEED, GameMap.getFuturePoint(this.owner.gridLoc, GameMap.getOppositeDirection((char)this.owner.teamID)), this.owner.teamID, NUM_SQUARES);
		this.getMap().addGameElement(projectile);
		/*
		projectile = new BouncingOrbProjectile(DAMAGE, SPEED, this.owner.gridLoc, this.owner.teamID, NUM_SQUARES);
		projectile.vel.y = 0;
		this.getMap().addGameElement(projectile);
		projectile = new BouncingOrbProjectile(DAMAGE, SPEED, this.owner.gridLoc, this.owner.teamID, NUM_SQUARES);
		projectile.vel.y = -1;
		this.getMap().addGameElement(projectile);
		*/
		this.finishSpell();
	}
	@Override
	public void onSpellUpdate() {
		/*
		this.timer -= this.getFrameTime();
		if(timer <= 0) {
			this.finishSpell();
		}
		*/
	}
}
