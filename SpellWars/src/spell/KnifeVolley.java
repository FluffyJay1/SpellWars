package spell;

import mechanic.Point;
import projectile.KnifeProjectile;
import projectile.WindProjectile;
import unit.Unit;

public class KnifeVolley extends Spell {
	public static final int DAMAGE = 10;
	public static final int NUM_SHOTS = 13;
	public static final float DURATION = 0.6f;
	int shotsFired;
	public KnifeVolley(Unit owner) {
		super(owner, 0.3f, DURATION, "Knife Volley", "Throw a huge volley of knives, but with reduced initial damage", "res/spell/knifevolley.png", false);
	}
	@Override
	public void onActivate() {
		this.map.addGameElement(new KnifeProjectile(DAMAGE * this.owner.finalDamageModifier, this.owner.direction, this.owner.gridLoc, this.owner.teamID));
		this.shotsFired = 1;
		this.setThinkInterval(DURATION/NUM_SHOTS);
	}
	@Override
	public void onThink() {
		int random = (shotsFired + 1) % 3 - 1;
		this.map.addGameElement(new KnifeProjectile(DAMAGE * this.owner.finalDamageModifier, this.owner.direction, Point.add(this.owner.gridLoc, new Point(0, random)), this.owner.teamID));
		this.shotsFired++;
		if(this.shotsFired >= NUM_SHOTS) {
			this.finishSpell();
		}
	}
}
