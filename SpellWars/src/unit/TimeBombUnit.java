package unit;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import mechanic.GameMap;
import mechanic.Point;
import spell.TimeBombDetonate;
import ui.Text;
import ui.TextFormat;

public class TimeBombUnit extends Unit {
	public static final int HP = 25;
	public static final double SPEED = 1;
	public static final int DETONATION_TIME = 5;
	int ticksLeft;
	Text timerText;
	public TimeBombUnit(Point gridLoc, int teamID) {
		super(HP, HP, SPEED, GameMap.getOppositeDirection((char)teamID), gridLoc, "res/unit/timebomb.png", teamID);
		if(teamID == GameMap.ID_LEFT) {
			this.setDrawColor(new Color(255, 127, 127));
		} else {
			this.setDrawColor(new Color(127, 127, 255));
		}
		this.ticksLeft = DETONATION_TIME;
		this.setDrawHeight(40);
		this.setThinkIntervalWithMove(true);
		this.ignoreTeam = true;
	}
	@Override
	public void onUnitSetMap() {
		this.timerText = new Text(this.getMap().getUI(), Point.add(this.getLoc(), new Point(-200, -105)), 400, 26, 40, 28, 48, Color.yellow, "" + this.ticksLeft, TextFormat.CENTER_JUSTIFIED);
		this.timerText.setUseOutline(true);
		this.timerText.setElementToRemoveWith(this);
		this.getMap().getUI().addUIElement(timerText);
	}
	@Override
	public void onThink() {
		this.ticksLeft--;
		if(this.ticksLeft <= 0) {
			this.castSpell(new TimeBombDetonate(this));
		}
	}
	@Override
	public void drawSpecialEffects(Graphics g) {
		this.timerText.setText("" + this.ticksLeft);
		this.timerText.changeLoc(Point.add(this.getLoc(), new Point(-200, -105)));
	}
}
