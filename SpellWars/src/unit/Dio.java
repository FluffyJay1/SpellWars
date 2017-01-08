package unit;

import mechanic.GameMap;
import mechanic.Panel;
import mechanic.Point;
import spell.KnifeThrow;
import spell.KnifeVolley;
import spell.VampiricSlash;
import spell.ZaWarudo;
import ui.SpellSelector;

public class Dio extends Unit {
	public static final double HP = 300;
	public static final double HP_PER_LEVEL = 75;
	public static final double SPEED = 1.2;
	public static final double SPEED_PER_LEVEL = 0.10;
	public static final int MOVES_PER_FIRE = 9;
	public static final int MOVES_VARIATION = 5;
	public static final float FIRE_FOLLOW_CHANCE = 0.5f;
	public static final float COOLDOWN_MULT_PER_LEVEL = 0.99f;
	public static final float KNIFE_THROW_COOLDOWN = 7f;
	public static final float KNIFE_THROW_COOLDOWN_ZA_WARUDO = 2.5f;
	public static final float KNIFE_THROW_CHANCE = 0.4f;
	public static final float KNIFE_THROW_CHANCE_ZA_WARUDO = 0.9f;
	public static final float KNIFE_VOLLEY_COOLDOWN = 40;
	public static final float KNIFE_VOLLEY_CHANCE = 0.1f;
	public static final float KNIFE_VOLLEY_CHANCE_ZA_WARUDO = 1;
	public static final float VAMPIRIC_SLASH_CHANCE = 0.25f;
	public static final float VAMPIRIC_SLASH_COOLDOWN = 25;
	public static final float ZA_WARUDO_COOLDOWN = 50;
	public static final float ZA_WARUDO_CHANCE = 0.4f;
	public static final float ZA_WARUDO_FOLLOW_CHANCE = 0.6f;
	int moves;
	Unit unitToTrack;
	int level;
	float knifeThrowTimer;
	float knifeVolleyTimer;
	float vampiricSlashTimer;
	float zaWarudoTimer;
	public Dio(Point gridLoc, int teamID, int level) {
		super(HP + HP_PER_LEVEL * level, HP + HP_PER_LEVEL * level, SPEED + SPEED_PER_LEVEL * level, GameMap.getOppositeDirection((char)teamID), gridLoc, "res/unit/dio.png", teamID);
		this.level = level;
		this.setSize(0.25);
		this.setDrawHeight(90);
		this.setThinkIntervalWithMove(true);
		this.refreshMoves();
		this.knifeThrowTimer = 0;
		this.knifeVolleyTimer = KNIFE_VOLLEY_COOLDOWN * (float)Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level);
		this.vampiricSlashTimer = 0;
		this.zaWarudoTimer = ZA_WARUDO_COOLDOWN * (float)Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level);
	}
	@Override
	public void onThink() {
		this.trackUnit();
		this.moves--;
		if(!(this.getMap().getActiveFieldEffect() instanceof ZaWarudo)) {
			if(this.knifeThrowTimer <= 0 && Math.random() < KNIFE_THROW_CHANCE) {
				this.aimAtTrackedUnit();
				this.castSpell(new KnifeThrow(this));
				this.knifeThrowTimer = (float)(KNIFE_THROW_COOLDOWN * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level));
			}
			else if(this.knifeVolleyTimer <= 0 && Math.random() < KNIFE_VOLLEY_CHANCE) {
				this.aimAtTrackedUnit();
				this.castSpell(new KnifeVolley(this));
				this.knifeVolleyTimer = (float)(KNIFE_VOLLEY_COOLDOWN * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level));
			}
			else if(this.vampiricSlashTimer <= 0 && Math.random() < VAMPIRIC_SLASH_CHANCE && !this.isCasting) {
				if(this.unitToTrack != null) {
					for(int i = 1; i < this.getMap().getGridDimensions().x; i++) {
						Point p = Point.add(this.unitToTrack.gridLoc, GameMap.getFuturePoint(new Point(), (char)this.direction, -i));
						if(!this.getMap().pointIsInGrid(p)) {
							break;
						}
						if(this.canMoveToLoc(p)) {
							this.moveTo(p, true);
							break;
						}
					}
				}
				this.castSpell(new VampiricSlash(this));
				this.vampiricSlashTimer= (float)(VAMPIRIC_SLASH_COOLDOWN * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level));
			}
			else if(this.zaWarudoTimer <= 0 && Math.random() < ZA_WARUDO_CHANCE) {
				this.aimAtTrackedUnit();
				this.castSpell(new ZaWarudo(this));
				this.zaWarudoTimer = (float)(ZA_WARUDO_COOLDOWN * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level)) + ZaWarudo.STOP_TIME;
				this.knifeThrowTimer = 0;
			}
		} else {
			if(this.knifeThrowTimer <= 0 && Math.random() < KNIFE_THROW_CHANCE_ZA_WARUDO) {
				this.aimAtTrackedUnit();
				this.castSpell(new KnifeThrow(this));
				this.knifeThrowTimer = (float)(KNIFE_THROW_COOLDOWN_ZA_WARUDO * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level));
			}
			else if(this.knifeVolleyTimer <= 0 && Math.random() < KNIFE_VOLLEY_CHANCE_ZA_WARUDO) {
				this.aimAtTrackedUnit();
				this.castSpell(new KnifeVolley(this));
				this.knifeVolleyTimer = (float)(KNIFE_VOLLEY_COOLDOWN * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level));
			}
			else if(Math.random() < ZA_WARUDO_FOLLOW_CHANCE) {
				this.aimAtTrackedUnit();
				this.putMoveCooldown();
			}
		}
		if(this.moves <= 0) {
			if(Math.random() < FIRE_FOLLOW_CHANCE) {
				this.aimAtTrackedUnit();
			}
			this.castSpell(SpellSelector.getRandomSpell(this));
			this.refreshMoves();
		} else {
			this.moveRandom4(true, false, true, true, true, false);
		}
	}
	@Override
	public void onUpdate() {
		if(this.knifeThrowTimer > 0) {
			this.knifeThrowTimer -= this.getFrameTime();
		}
		if(this.knifeVolleyTimer > 0) {
			this.knifeVolleyTimer -= this.getFrameTime();
		}
		if(this.vampiricSlashTimer > 0) {
			this.vampiricSlashTimer -= this.getFrameTime();
		}
		if(this.zaWarudoTimer > 0) {
			this.zaWarudoTimer -= this.getFrameTime();
		}
	}
	public void refreshMoves() {
		this.moves = MOVES_PER_FIRE + Point.roundToNearestInteger((Math.random() - 0.5) * 2 * MOVES_VARIATION);
	}
	public void trackUnit() {
		if(this.unitToTrack == null || this.unitToTrack.getRemove()) {
			for(Panel p : this.getMap().getPanelsOfTeam(GameMap.getOppositeDirection((char)this.teamID))) {
				if(p.unitStandingOnPanel != null && p.unitStandingOnPanel.isImportant) {
					this.unitToTrack = p.unitStandingOnPanel;
					break;
				}
			}
			if(this.unitToTrack == null || this.unitToTrack.getRemove()) { //if it can't find an important unit, it finds a random unit
				for(Panel p : this.getMap().getPanelsOfTeam(GameMap.getOppositeDirection((char)this.teamID))) {
					if(p.unitStandingOnPanel != null) {
						this.unitToTrack = p.unitStandingOnPanel;
						break;
					}
				}
			}
		}
	}
	public void aimAtTrackedUnit() {
		if(this.unitToTrack != null) {
			if(this.unitToTrack.gridLoc.y < this.gridLoc.y) { //move up
				if(this.canMoveToLoc(Point.add(this.gridLoc, new Point(0, -1)))) {
					this.move(GameMap.ID_UP, true, false, true, true, true, false);
				} else if(this.canMoveToLoc(Point.add(this.gridLoc, new Point(-1, -1)))) { //look into the future, see if there's obstruction
					this.move(GameMap.ID_LEFT, true, false, true, true, true, false); //move off to the side
				} else if(this.canMoveToLoc(Point.add(this.gridLoc, new Point(1, -1)))) {
					this.move(GameMap.ID_RIGHT, true, false, true, true, true, false);
				} else {
					this.moveRandom4(true, false, true, true, true, false);
				}
			} else if(this.unitToTrack.gridLoc.y > this.gridLoc.y) { //move down
				if(this.canMoveToLoc(Point.add(this.gridLoc, new Point(0, 1)))) {
					this.move(GameMap.ID_DOWN, true, false, true, true, true, false);
				} else if(this.canMoveToLoc(Point.add(this.gridLoc, new Point(-1, 1)))) { //look into the future, see if there's obstruction
					this.move(GameMap.ID_LEFT, true, false, true, true, true, false); //move off to the side
				} else if(this.canMoveToLoc(Point.add(this.gridLoc, new Point(1, 1)))) {
					this.move(GameMap.ID_RIGHT, true, false, true, true, true, false);
				} else {
					this.moveRandom4(true, false, true, true, true, false);
				}
			}
		}
	}
}
