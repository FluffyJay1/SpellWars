package spell;

import java.util.ArrayList;

import mechanic.Point;
import projectile.Grenade;
import projectile.Projectile;
import unit.Unit;

public class TimeBombDetonate extends Spell {
	public static final double DAMAGE = 75;
	/*
	public static final Point[] AFFECTED_POINTS = {new Point(0,4),
			new Point(-1,3), new Point(0,3), new Point(1,3),
			new Point(-2,2), new Point(-1,2), new Point(0,2), new Point(1,2), new Point(2,2),
			new Point(-3,1), new Point(-2,1), new Point(-1,1), new Point(0,1), new Point(1,1), new Point(2,1), new Point(3,1),
			new Point(-4,0), new Point(-3,0), new Point(-2,0), new Point(-1,0), new Point(), new Point(1,0), new Point(2,0), new Point(3,0), new Point(4,0),
			new Point(-3,-1), new Point(-2,-1), new Point(-1,-1), new Point(0,-1), new Point(1,-1), new Point(2,-1), new Point(3,-1),
			new Point(-2,-2), new Point(-1,-2), new Point(0,-2), new Point(1,-2), new Point(2,-2),
			new Point(-1,-3), new Point(0,-3), new Point(1,-3),
			new Point(0,-4)};
			*/
	public static final ArrayList<Point> AFFECTED_POINTS = Point.getIntegerPointsInCircle(3.5);
	float timer;
	public TimeBombDetonate(Unit owner) {
		super(owner, 0, 0, "Suicide Bomb", "Kill yourself, but for the greater good", "res/particle_explosion.png", true);
		this.timer = 2;
	}
	@Override
	public void onActivate() {
		for(Point p : AFFECTED_POINTS) {
			//Point f = Point.add(p, this.owner.gridLoc);
			Projectile projectile = new Grenade(DAMAGE * this.owner.finalDamageOutputModifier, 1.25, p, 10, 10, this.owner.gridLoc, "res/particle_genericYellow.png", this.owner.teamID);
			projectile.setImageScale(2);
			this.map.addGameElement(projectile);
		}
	}
	@Override
	public void onSpellUpdate() {
		this.timer -= this.getFrameTime();
		if(timer <= 0.75){
			this.owner.changeHP(0);
		}
		if(timer <= 0) {
			this.finishSpell();
		}
	}
}
