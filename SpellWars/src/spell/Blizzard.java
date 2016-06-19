package spell;

import mechanic.Panel;
import projectile.Projectile;
import statuseffect.StatusFrost;
import unit.Unit;

public class Blizzard extends Spell {
	public static final float SPEED_MODIFIER = 0.25f;
	public static final float DAMAGE_PER_SECOND = 1;
	public static final float DURATION = 10;
	public Blizzard(Unit owner) {
		super(owner, 0, 0, "Blizzard", "Greatly slows all enemy units and projectiles on the map, deals slight damage over time, and reduces some projectile's damage", "res/statuseffect/icon_frost.png", true);
	}
	@Override
	public void onActivate() {
		for(Panel p : this.getMap().getPanels()) {
			if(p.unitStandingOnPanel != null && p.unitStandingOnPanel.teamID != this.owner.teamID) {
				p.unitStandingOnPanel.addStatusEffect(new StatusFrost(p.unitStandingOnPanel, SPEED_MODIFIER, DAMAGE_PER_SECOND, DURATION, 1));
			}
		}
		for(Projectile p : this.getMap().getProjectiles()) {
			if(p.teamID != this.owner.teamID) {
				p.addStatusEffect(new StatusFrost(p, SPEED_MODIFIER, DAMAGE_PER_SECOND, DURATION, 1));
			}
		}
		this.finishSpell();
	}
}
