package projectile;

import java.util.ArrayList;

import mechanic.Point;
import statuseffect.StatusEffect;
import unit.Unit;

public class DebuffTransferProjectile extends Projectile {
	ArrayList<StatusEffect> effects;
	public DebuffTransferProjectile(double damage, double speed, int direction, Point gridLoc, int teamID) {
		super(damage, speed, direction, gridLoc, "res/projectile/debufftransfer.png", teamID, false, true, true);
		this.setDrawHeight(72);
	}
	public void setEffects(ArrayList<StatusEffect> effects) {
		this.effects = effects;
	}
	@Override
	public void onTargetHit(Unit target) {
		for(StatusEffect effect : this.effects) {
			target.addStatusEffect(effect.clone());
		}
	}
}
