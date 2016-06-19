package shield;

import mechanic.GameMap;
import mechanic.Point;
import projectile.Projectile;
import unit.Unit;

public class ReflectShield extends Shield {
	float timer;
	public ReflectShield(Unit owner, float duration, int blockAmount) {
		super(owner, blockAmount, blockAmount, "res/shield/reflectshield.png");
		this.drawHP = true;
		this.setDrawHeight(owner.getDrawHeight());
		this.timer = duration;
	}
	@Override
	public void onUpdate() {
		this.timer -= this.getFrameTime();
		if(this.timer <= 0) {
			this.isDead = true;
		}
	}
	@Override
	public void onHitByProjectile(Projectile p) {
		p.teamID = this.owner.teamID;
		p.direction = GameMap.getOppositeDirection((char)p.direction);
		p.vel = Point.scale(p.vel, -1);
		p.setRemove(false);
	}
}
