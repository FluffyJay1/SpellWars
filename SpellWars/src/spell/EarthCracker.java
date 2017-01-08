package spell;

import projectile.EarthCrackerProjectile;
import unit.Unit;

public class EarthCracker extends Spell {
	public static final double DAMAGE = 40;
	public static final double SPEED = 22;
	public EarthCracker(Unit owner) {
		super(owner, 0.05f, 0.1f, "Earth Cracker", "Crack panels in a line and damage units", "res/spell/earthcracker.png", false);
	}
	@Override
	public void onActivate() {
		EarthCrackerProjectile p = new EarthCrackerProjectile(DAMAGE, SPEED, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
		this.getMap().addGameElement(p);
	}
}
