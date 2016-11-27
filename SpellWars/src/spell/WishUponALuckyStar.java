package spell;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import mechanic.GameMap;
import mechanic.Point;
import projectile.LuckyStarGrenadeBig;
import projectile.LuckyStarGrenadeSmall;
import projectile.Projectile;
import unit.Unit;

public class WishUponALuckyStar extends Spell {
	public static final double DAMAGE = 50;
	public static final double AREA_DAMAGE = 25;
	public static final float STANDARD_WEIGHT = 6;
	public static final float SMALL_SHOWER_WEIGHT = 3;
	public static final float BIG_SHOWER_WEIGHT = 2;
	public static final float BIG_STAR_WEIGHT = 1;
	float timer;
	static final ArrayList<Point> SHOWER_POINTS = Point.getIntegerPointsInCircle(2);
	static final float SMALL_SHOWER_DURATION = 0.4f;
	static final int SMALL_SHOWER_COUNT = 3;
	static final float BIG_SHOWER_DURATION = 0.5f;
	static final int BIG_SHOWER_COUNT = 5;
	int type;
	int extraStarsReleased;
	ArrayList<Point> availableLandingPoints;
	public WishUponALuckyStar(Unit owner) {
		super(owner, 0.1f, BIG_SHOWER_DURATION, "Wish upon a lucky star", "A star lands 4 panels ahead, penetrating and obliterating all shields and barriers, more things may happen depending on your luck", "res/projectile/luckystar.png", false);
	}
	@Override
	public void onActivate() {
		this.timer = 0;
		this.type = 0;
		this.extraStarsReleased = 0;
		this.availableLandingPoints = new ArrayList<Point>();
		double random = Math.random() * (STANDARD_WEIGHT + SMALL_SHOWER_WEIGHT + BIG_SHOWER_WEIGHT + BIG_STAR_WEIGHT);
		if(random < STANDARD_WEIGHT) {
			Projectile p = new LuckyStarGrenadeSmall(DAMAGE * this.owner.finalDamageOutputModifier, 4, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
			this.getMap().addGameElement(p);
			this.finishSpell();
		} else if(random < STANDARD_WEIGHT + SMALL_SHOWER_WEIGHT) {
			Projectile p = new LuckyStarGrenadeSmall(DAMAGE * this.owner.finalDamageOutputModifier, 4, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
			this.getMap().addGameElement(p);
			this.type = 1;
			this.getAvailableLandingPoints();
		} else if(random < STANDARD_WEIGHT + SMALL_SHOWER_WEIGHT + BIG_SHOWER_WEIGHT) {
			Projectile p = new LuckyStarGrenadeSmall(DAMAGE * this.owner.finalDamageOutputModifier, 4, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
			this.getMap().addGameElement(p);
			this.type = 2;
			this.getAvailableLandingPoints();
		} else {
			Projectile p = new LuckyStarGrenadeBig(DAMAGE * this.owner.finalDamageOutputModifier, AREA_DAMAGE * this.owner.finalDamageOutputModifier, this.owner.direction, this.owner.gridLoc, this.owner.teamID);
			this.getMap().addGameElement(p);
			this.finishSpell();
		}
	}
	@Override
	public void onSpellUpdate() {
		this.timer += this.getFrameTime();
		if(type == 1) {
			while(this.extraStarsReleased < (int)(this.timer * (float)SMALL_SHOWER_COUNT / SMALL_SHOWER_DURATION)) {
				this.launchShower();
				this.extraStarsReleased++;
				if(this.extraStarsReleased == SMALL_SHOWER_COUNT) {
					this.finishSpell();
					break;
				}
			}
		} else if(type == 2) {
			while(this.extraStarsReleased < (int)(this.timer * (float)BIG_SHOWER_COUNT / BIG_SHOWER_DURATION)) {
				this.launchShower();
				this.extraStarsReleased++;
				if(this.extraStarsReleased == BIG_SHOWER_COUNT) {
					this.finishSpell();
					break;
				}
			}
		}
	}
	private void getAvailableLandingPoints() {
		for(Point p : SHOWER_POINTS) {
			Point testPoint = Point.add(p, Point.add(this.owner.gridLoc, Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.direction), 4)));
			if(this.getMap().pointIsInGrid(testPoint) && this.getMap().getPanelAt(testPoint).teamID != this.owner.teamID) {
				this.availableLandingPoints.add(testPoint);
			}
		}
	}
	private void launchShower() {
		Point randomPoint;
		if(this.availableLandingPoints.size() == 0) {
			randomPoint = this.getMap().getPanelsOfTeam((char)this.owner.direction).get((int)(Math.random() * (float)this.getMap().getPanelsOfTeam((char)this.owner.direction).size())).getLoc();
		} else {
			randomPoint = this.availableLandingPoints.get((int)(Math.random() * (float)this.availableLandingPoints.size()));
		}
		Point spawnPoint = Point.add(randomPoint, Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.teamID), 4));
		int distance = 4;
		while(!this.getMap().pointIsInGrid(spawnPoint)) {
			distance--;
			spawnPoint = Point.add(randomPoint, Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.teamID), distance));
		}
		Projectile p = new LuckyStarGrenadeSmall(DAMAGE * this.owner.finalDamageOutputModifier, distance, this.owner.direction, spawnPoint, this.owner.teamID);
		this.getMap().addGameElement(p);
	}
}
