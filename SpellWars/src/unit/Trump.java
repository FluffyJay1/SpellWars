package unit;

import mechanic.GameMap;
import mechanic.Panel;
import mechanic.Point;
import spell.AreaGrab;
import spell.ForgeSpirit;
import spell.HellRain;
import spell.TimeBomb;
import spell.TrumpWall;
import ui.SpellSelector;

public class Trump extends Unit {
	public static final double HP = 325;
	public static final double HP_PER_LEVEL = 25;
	public static final double SPEED = 1.1;
	public static final double SPEED_PER_LEVEL = 0.15;
	public static final double SPEED_SPECIAL = 3.75;
	public static final double SPEED_SPECIAL_PER_LEVEL = 0.75;
	public static final int MOVES_PER_FIRE = 9;
	public static final int MOVES_VARIATION = 4;
	public static final float TRUMP_WALL_COOLDOWN = 40;
	public static final float SPECIAL_COOLDOWN = 9;
	public static final double TRUMP_WALL_MULTIPLIER_PER_LEVEL = 0.98;
	public static final double SPECIAL_COOLDOWN_MULTIPLIER_PER_LEVEL = 0.97;
	boolean isFirstMove;
	int moves;
	int level;
	int state; //0 = normal state; 1 = just cast area grab in the combo, 2 = trying to move left to plant the bomb, 3 = trying to back up for hell rain
	float trumpWallCooldown;
	float specialCooldown;
	public Trump(Point gridLoc, int teamID, int level) {
		super(HP + HP_PER_LEVEL * level, HP + HP_PER_LEVEL * level, SPEED + SPEED_PER_LEVEL * level, GameMap.getOppositeDirection((char)teamID), gridLoc, "res/unit/trump.png", teamID);
		this.level = level;
		this.refreshMoves();
		//this.setSize(1.4);
		this.setDrawHeight(100);
		this.setThinkIntervalWithMove(true);
		this.state = 0;
		this.isFirstMove = true;
		this.trumpWallCooldown = (float) (TRUMP_WALL_COOLDOWN * Math.pow(TRUMP_WALL_MULTIPLIER_PER_LEVEL, level));
		this.specialCooldown = (float)(SPECIAL_COOLDOWN * Math.pow(SPECIAL_COOLDOWN_MULTIPLIER_PER_LEVEL, level));
		this.isImportant = true;
	}
	@Override
	public void onThink() {
		this.moves--;
		if(this.isFirstMove) {
			this.castSpell(new AreaGrab(this));
			this.state = 1;
			this.isFirstMove = false;
		}
		if(this.state == 2) {
			this.move(this.direction, false, false, true, true, true, false);
			Point futureLoc = GameMap.getFuturePoint(this.gridLoc, (char)this.direction);
			if(!this.canMoveToLoc(futureLoc)) {
				this.setSpeed(SPEED + SPEED_PER_LEVEL * level);
				this.castSpell(new TimeBomb(this));
				this.state = 0;
			}
		} else if(this.state == 3) {
			this.move(GameMap.getOppositeDirection((char)this.direction), false, false, true, true, true, false);
			Point futureLoc = GameMap.getFuturePoint(this.gridLoc, GameMap.getOppositeDirection((char)this.direction));
			if(!this.canMoveToLoc(futureLoc)) {
				this.setSpeed(SPEED + SPEED_PER_LEVEL * level);
				this.castSpell(new HellRain(this));
				this.state = 0;
			}
		} else {
			this.moveRandom4(true, false, true, true, true, false);
		}
		if(Math.random() < 0.15 && this.state == 0 && this.trumpWallCooldown <= 0) { //TRUMPS WALL
			this.castSpell(new AreaGrab(this));
			this.state = 1;
			this.trumpWallCooldown = (float) (TRUMP_WALL_COOLDOWN * Math.pow(TRUMP_WALL_MULTIPLIER_PER_LEVEL, level));
		} 
		if(this.specialCooldown <= 0 && Math.random() < 0.1 && this.state == 0 && !this.isCasting) { //BOMB
			this.state = 2;
			this.setSpeed(SPEED_SPECIAL + SPEED_SPECIAL_PER_LEVEL * this.level);
			this.specialCooldown = (float)(SPECIAL_COOLDOWN * Math.pow(SPECIAL_COOLDOWN_MULTIPLIER_PER_LEVEL, level));
		} 
		if(this.specialCooldown <= 0 && Math.random() < 0.1 && this.state == 0 && !this.isCasting) { //HELL RAIN
			this.state = 3;
			this.setSpeed(SPEED_SPECIAL + SPEED_SPECIAL_PER_LEVEL * this.level);
			this.specialCooldown = (float)(SPECIAL_COOLDOWN * Math.pow(SPECIAL_COOLDOWN_MULTIPLIER_PER_LEVEL, level));
		} 
		if(this.moves <= 0 && this.state == 0) {
			this.castSpell(SpellSelector.getRandomSpell(this));
			this.refreshMoves();
		}
	}
	public void refreshMoves() {
		this.moves = MOVES_PER_FIRE + Point.roundToNearestInteger((Math.random() - 0.5) * 2 * MOVES_VARIATION);
	}
	@Override
	public void onUpdate() {
		if(this.trumpWallCooldown > 0) {
			this.trumpWallCooldown -= this.getFrameTime();
		}
		if(this.specialCooldown > 0) {
			this.specialCooldown -= this.getFrameTime();
		}
		if(this.state == 1) {
			boolean hasCast = this.castSpell(new TrumpWall(this));
			if(hasCast) {
				this.state = 0;
			}
		}
	}
}
