package statuseffect;

import mechanic.GameMap;
import mechanic.Panel;
import mechanic.Point;
import unit.Unit;

public class PanelAura {
	GameMap map;
	Panel panel;
	Unit unitAffected;
	StatusEffect effect;
	StatusEffect effectOnUnit;
	public PanelAura(GameMap map, Panel panel) {
		this.map = map;
		this.panel = panel;
		//this.effect = effect;
		//this.effect.setPermanent(true);
	}
	public void update() {
		if(this.unitNoLongerAffected()) {
			if(this.unitAffected != null) {
				this.effectOnUnit.setRemove(true);
			}
			this.unitAffected = null;
			if(this.panel.unitStandingOnPanel != null) { // TIME TO FIND A NEW OWNER
				if(this.effect != null) {
					this.unitAffected = this.panel.unitStandingOnPanel;
					this.effectOnUnit = this.effect.clone();
					this.unitAffected.addStatusEffect(this.effectOnUnit);
				}
			}
		}
	}
	public void setEffect(StatusEffect effect) {
		if(this.effectOnUnit != null) {
			this.effectOnUnit.setRemove(true);
		}
		this.effect = effect;
		if(effect != null) {
			this.effect.setPermanent(true);
			this.effectOnUnit = this.effect.clone();
			if(!this.unitNoLongerAffected()) {
				this.unitAffected.addStatusEffect(this.effectOnUnit);
			}
		} else {
			if(this.effectOnUnit != null) {
				this.effectOnUnit.setRemove(true);
			}
		}
	}
	public boolean unitNoLongerAffected() {
		return (this.unitAffected == null || this.unitAffected.getRemove() || !Point.equals(this.unitAffected.gridLoc, this.panel.getLoc()));
	}
}
