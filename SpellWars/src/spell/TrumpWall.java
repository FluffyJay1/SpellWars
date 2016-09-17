package spell;

import java.util.ArrayList;

import mechanic.GameMap;
import mechanic.Panel;
import mechanic.PanelState;
import mechanic.Point;
import unit.ForgeSpiritUnit;
import unit.TrumpWallUnit;
import unit.Unit;

public class TrumpWall extends Spell {
	public static final float DELAY = 0.8f;
	float timer;
	int numSpawned;
	ArrayList<Point> affectedPoints;
	public TrumpWall(Unit owner) {
		super(owner, 0, 0, "Trump's wall", "Builds a wall that keeps mexicans out", "res/spell/trumpwall.png", true);
		this.timer = DELAY;
		this.numSpawned = 0;
		this.affectedPoints = new ArrayList<Point>();
	}
	@Override
	public void onActivate() {
		for(int row = 0; row < this.getMap().getGridDimensions().y; row++) {
			int column = 0;
			if(this.owner.teamID == GameMap.ID_LEFT) {
				for(column = (int)this.getMap().getGridDimensions().x - 2; column > 0; column--) {
					Panel panel = this.getMap().getPanelAt(new Point(column, row));
					if(panel.teamID == GameMap.ID_LEFT) {
						if(panel.unitStandingOnPanel == null || panel.unitStandingOnPanel instanceof TrumpWallUnit) {
							this.affectedPoints.add(new Point(column, row));
						}
						/*
						if(panel.unitStandingOnPanel == null) {
							//panel.setTeamID(GameMap.ID_LEFT);
							this.affectedPanels.add(panel);
						} else {
							panel.unitStandingOnPanel.doDamage(DAMAGE);
						}
						*/
						break;
					}
				}
			} else {
				for(column = 1; column < this.getMap().getGridDimensions().x - 1; column++) { 
					Panel panel = this.getMap().getPanelAt(new Point(column, row));
					if(panel.teamID == GameMap.ID_RIGHT) {
						if(panel.unitStandingOnPanel == null || panel.unitStandingOnPanel instanceof TrumpWallUnit) {
							this.affectedPoints.add(new Point(column, row));
						}
						/*
						if(panel.unitStandingOnPanel == null) {
							//panel.setTeamID(GameMap.ID_RIGHT);
							this.affectedPanels.add(panel);
						} else {
							panel.unitStandingOnPanel.doDamage(DAMAGE);
						}
						*/
						break;
					}
				}
			}
		}
		if(this.affectedPoints.size() == 0) {
			this.finishSpell();
		}
	}
	@Override
	public void onSpellUpdate() {
		this.timer -= this.getFrameTime();
		if(timer < 0) {
			this.finishSpell();
		} else {
			while(this.numSpawned < (1 - this.timer/DELAY) * this.affectedPoints.size()) {
				this.getMap().getPanelAt(this.affectedPoints.get(numSpawned)).clearState();
				if(this.getMap().getPanelAt(this.affectedPoints.get(numSpawned)).unitStandingOnPanel == null) {
					Unit unit = new TrumpWallUnit(this.affectedPoints.get(numSpawned), this.owner.teamID);
					this.owner.getMap().addUnit(unit);
					unit.setPause(true);
				} else if(this.getMap().getPanelAt(this.affectedPoints.get(numSpawned)).unitStandingOnPanel instanceof TrumpWallUnit) {
					this.getMap().getPanelAt(this.affectedPoints.get(numSpawned)).unitStandingOnPanel.changeHP(TrumpWallUnit.HP);
				}
				numSpawned++;
			}
		}
	}
}
