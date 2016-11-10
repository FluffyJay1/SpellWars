package shield;

import mechanic.GameMap;
import mechanic.Point;
import projectile.Grenade;
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
		//p.vel.x *= -1;
		p.setRemove(false);
		if(p instanceof Grenade) {
			((Grenade)p).timeElapsed = 0;
			Point temploc = Point.clone(((Grenade)p).startLoc);
			((Grenade)p).startLoc = Point.clone(((Grenade)p).endLoc);
			((Grenade)p).endLoc = temploc;
			//because a grenade does this after it moves, we have to move backwards twice
			if(p.useMoveVec) {
				p.move(p.vel, false, false);
			} else {
				p.move(p.direction, false, false);
			}
		}
		if(p.useMoveVec) {
			p.move(p.vel, false, false);
		} else {
			p.move(p.direction, false, false);
		}
	}
}
