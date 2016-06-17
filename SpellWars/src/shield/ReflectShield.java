package shield;

import mechanic.GameMap;
import mechanic.Point;
import projectile.Projectile;
import unit.Unit;

public class ReflectShield extends Shield {
	float timer;
	public ReflectShield(Unit owner, float duration) {
		super(owner, 1, 1, "res/shield/reflectshield.png");
		this.drawHP = false;
		this.setDrawHeight(owner.getDrawHeight());
		this.timer = duration;
	}
	@Override
	public void onUpdate() {
		this.timer -= this.getFrameTime();
		if(this.timer <= 0) {
			this.setRemove(true);
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
