package spell;

import java.util.ArrayList;

import mechanic.GameMap;
import mechanic.Point;
import projectile.LuckyStarGrenadeBig;
import projectile.LuckyStarGrenadeSmall;
import projectile.Projectile;
import unit.Unit;

public class HopesAndDreams extends Spell {
	ArrayList<Point> availableLandingPoints;
	public static final float DAMAGE = 25;
	public static final float AREA_DAMAGE = 25;
	public static final int NUM_STRIKES = 12;
	public static final float STRIKE_INTERVAL = 0.1f;
	boolean hasLaunchedAtUnits;
	float timer;
	int strikes;
	Projectile last;
	public HopesAndDreams(Unit owner) {
		super(owner, 0, 0, "Hopes and Dreams", "Unleash a barrage of stars and save the world", "res/spell/hopesanddreams.png", true);
	}
	@Override
	public void onActivate() {
		last = new LuckyStarGrenadeBig((int)(DAMAGE * this.owner.finalDamageModifier), (int)(AREA_DAMAGE * this.owner.finalDamageModifier), this.owner.direction, this.owner.gridLoc, this.owner.teamID);
		this.hasLaunchedAtUnits = false;
		this.timer = 0;
		this.strikes = 0;
		this.setThinkInterval(STRIKE_INTERVAL);
		this.availableLandingPoints = this.getMap().getPanelPointsOfTeam(GameMap.getOppositeDirection((char)this.owner.teamID));
	}
	@Override
	public void onThink() {
		if(this.strikes < NUM_STRIKES) {
			this.launchShower();
			this.strikes++;
		} else if(!hasLaunchedAtUnits) {
			for(Unit u : this.getMap().getUnitsOfTeam(GameMap.getOppositeDirection((char)this.owner.teamID))) {
				this.launch(u.gridLoc);
			}
			this.hasLaunchedAtUnits = true;
		} else {
			this.getMap().addGameElement(last);
		}
		if(last.getRemove()) {
			this.finishSpell();
		}
	}
	private void launch(Point destination) {
		Point spawnPoint = Point.add(destination, Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.teamID), 4));
		int distance = 4;
		while(!this.getMap().pointIsInGrid(spawnPoint)) {
			distance--;
			spawnPoint = Point.add(destination, Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.teamID), distance));
		}
		Projectile p = new LuckyStarGrenadeSmall((int)(DAMAGE * this.owner.finalDamageModifier), distance, this.owner.direction, spawnPoint, this.owner.teamID);
		this.getMap().addGameElement(p);
	}
	private void launchShower() {
		Point randomPoint = this.availableLandingPoints.get((int)(Math.random() * (float)this.availableLandingPoints.size()));
		Point spawnPoint = Point.add(randomPoint, Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.teamID), 4));
		int distance = 4;
		while(!this.getMap().pointIsInGrid(spawnPoint)) {
			distance--;
			spawnPoint = Point.add(randomPoint, Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.teamID), distance));
		}
		Projectile p = new LuckyStarGrenadeSmall((int)(DAMAGE * this.owner.finalDamageModifier), distance, this.owner.direction, spawnPoint, this.owner.teamID);
		this.getMap().addGameElement(p);
	}
}
