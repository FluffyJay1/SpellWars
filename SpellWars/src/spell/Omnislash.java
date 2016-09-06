package spell;

import java.util.ArrayList;

import mechanic.GameMap;
import mechanic.Panel;
import mechanic.Point;
import unit.Unit;

public class Omnislash extends Spell {
	public static final float SLASH_INTERVAL = 0.4f;
	public static final int LENGTH = 2;
	public static final int HEIGHT = 1;
	public static final double DAMAGE = 25;
	public static final float DELAY = 0.2f;
	public static final float ANIMATION_DURATION = 0.15f;
	public static final int LENGTH_FINAL = 3;
	public static final int HEIGHT_FINAL = 2;
	public static final double DAMAGE_FINAL = 30;
	public static final float DELAY_FINAL = 0.6f;
	public static final float ANIMATION_DURATION_FINAL = 0.3f;
	//float timer;
	Point originalLoc;
	ArrayList<Point> teleportPoints;
	boolean ignoreTeam;
	boolean ignoreHoles;
	boolean ownerthink;
	boolean hasSlashedFinal;
	public Omnislash(Unit owner) {
		super(owner, 0, 0, "Omnislash", "A very powerful spell, teleport and slash at an enemy in every row, then slash in a huge area", "res/spell/slash.png", true);
		this.teleportPoints = new ArrayList<Point>();
		this.hasSlashedFinal = false;
	}
	@Override
	public void onActivate() {
		this.originalLoc = Point.clone(this.owner.gridLoc);
		this.ignoreHoles = this.owner.ignoreHoles;
		this.ignoreTeam = this.owner.ignoreTeam;
		this.ownerthink = this.owner.getThink();
		for(int row = 0; row < this.getMap().getGridDimensions().y; row++) {
			int column = 0;
			if(this.owner.teamID == GameMap.ID_LEFT) {
				for(column = 1; column < this.getMap().getGridDimensions().x; column++) {
					Panel panel = this.getMap().getPanelAt(new Point(column, row));
					if(panel.unitStandingOnPanel != null && panel.unitStandingOnPanel.teamID != this.owner.teamID) {
						this.teleportPoints.add(new Point(column - 1, row));
						break;
					}
				}
			} else {
				for(column = (int)this.getMap().getGridDimensions().x - 1; column >= 0; column--) {
					Panel panel = this.getMap().getPanelAt(new Point(column, row));
					if(panel.unitStandingOnPanel != null && panel.unitStandingOnPanel.teamID != this.owner.teamID) {
						this.teleportPoints.add(new Point(column + 1, row));
						break;
					}
				}
			}
		}
		this.setThinkInterval(SLASH_INTERVAL);
		if(this.teleportPoints.size() == 0) {
			this.finishSpell();
		}
		this.onThink();
	}
	@Override
	public void onThink() {
		if(this.owner.getRemove()) {
			this.finishSpell();
		}
		if(this.teleportPoints.size() == 0 && Point.equals(this.owner.gridLoc, this.originalLoc) && !this.owner.isCasting && this.hasSlashedFinal) {
			this.owner.ignoreTeam = this.ignoreTeam;
			this.owner.ignoreHoles = this.ignoreHoles;
			if(this.ownerthink) {
				this.owner.enableThink();
			}
			this.finishSpell();
		} else if(this.teleportPoints.size() == 0 && !this.owner.isCasting) {
			this.owner.moveTo(originalLoc, true);
			this.owner.castSpell(new Slash(this.owner, LENGTH_FINAL, HEIGHT_FINAL, DAMAGE_FINAL, DELAY_FINAL, ANIMATION_DURATION_FINAL), true, true, true);
			this.hasSlashedFinal = true;
		} else if (this.teleportPoints.size() != 0) {
			this.owner.ignoreHoles = true;
			this.owner.ignoreTeam = true;
			if(this.owner.canMoveToLoc(this.teleportPoints.get(0)) || Point.equals(this.teleportPoints.get(0), this.owner.gridLoc)) {
				this.owner.moveTo(this.teleportPoints.get(0), false);
			} else {
				for(Point p : Point.proximity8(this.teleportPoints.get(0))) {
					if(this.owner.canMoveToLoc(p)) {
						this.owner.moveTo(p, false);
						break;
					}
				}
			}
			//this.owner.setPause(false);
			this.owner.disableThink();
			this.owner.castSpell(new Slash(this.owner, LENGTH, HEIGHT, DAMAGE, DELAY, ANIMATION_DURATION), true, true, true);
			this.teleportPoints.remove(0);
		}
	}
}
