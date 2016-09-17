package spell;

import mechanic.Point;
import projectile.Grenade;
import projectile.LavaGrenade;
import projectile.MudGrenade;
import unit.Unit;

public class LavaToss extends Spell {
	public static final int DAMAGE = 15;
	public LavaToss(Unit owner) {
		super(owner, 0.2f, 0.3f, "Lava grenade", "Throws a grenade at the enemy, dealing minor damage to the target it lands on and changes the area around it to lava that damages the enemy for as long as it lasts", "res/projectile/lavagrenade.png", false);
	}
	@Override
	public void onActivate() {
		Grenade g = new LavaGrenade((int)(DAMAGE * this.owner.finalDamageModifier), this.owner.direction, this.owner.gridLoc, this.owner.teamID);
		this.getMap().addGameElement(g);
		this.finishSpell();
	}
}
