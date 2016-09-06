package unit;

import java.util.ArrayList;

import mechanic.GameMap;
import mechanic.Panel;
import mechanic.Point;
import spell.AreaGrab;
import spell.BouncingOrb;
import spell.CryoFreeze;
import spell.EndothermicBlasterPrimary;
import spell.EndothermicBlasterSecondary;
import spell.IceWall;
import spell.MeiBlizzard;
import spell.Stun;
import spell.TestFireball;
import spell.TimeBombDetonate;
import statuseffect.StatusFrostMei;
import ui.SpellSelector;

public class Mei extends Unit {
	public static final double HP = 250;
	public static final double HP_PER_LEVEL = 25;
	public static final double SPEED = 1.4;
	public static final double SPEED_PER_LEVEL = 0.22;
	public static final double SPEED_STATE2 = 3.2;
	public static final double SPEED_STATE2_PER_LEVEL = 0.45;
	public static final float COOLDOWN_MULT_PER_LEVEL = 0.975f;
	public static final float NORMAL_SPELL_COOLDOWN = 3.75f;
	public static final float NORMAL_SPELL_CHANCE = 0.24f;
	public static final float AREA_GRAB_COOLDOWN = 55;
	public static final float AREA_GRAB_CHANCE = 0.15f;
	public static final float CRYOFREEZE_HP_RATIO_THRESHOLD = 0.4f;
	public static final float CRYOFREEZE_CHANCE = 0.4f;
	public static final float CRYOFREEZE_COOLDOWN = 40;
	public static final float ICEWALL_COOLDOWN = 20;
	public static final float ICEWALL_CHANCE = 0.25f;
	public static final float BLIZZARD_COOLDOWN = 55;
	public static final float BLIZZARD_CHANCE = 0.95f;
	public static final float STATE2_COOLDOWN = 10;
	public static final float STATE2_CHANCE = 0.4f;
	public static final float STATE2_DURATION = 12;
	public static final float STATE2_PRIMARY_COOLDOWN = 1.5f;
	public static final float STATE2_ICICLE_COOLDOWN = 3.25f;
	public static final float STATE2_ICICLE_CHANCE_PER_STACK = 0.016f;
	public static final float STATE2_ICICLE_BASE_CHANCE = 0.05f;
	public static final float STATE2_ICICLE_BONUS_CHANCE_FROZEN = 0.46f;
	Unit unitToTrack;
	float normalSpellTimer;
	float areaGrabTimer;
	float cryofreezeTimer;
	float icewallTimer;
	float blizzardTimer = 0;
	float state2Timer;
	float state2primarytimer;
	float state2icicletimer;
	int level;
	int state; //1 is normal state, 2 is targeting the enemy with endothermic blaster state
	public Mei(Point gridLoc, int teamID, int level) {
		super(HP + HP_PER_LEVEL * level, HP + HP_PER_LEVEL * level, SPEED + SPEED_PER_LEVEL * level, GameMap.getOppositeDirection((char)teamID), gridLoc, "res/unit/mei.png", teamID);
		this.level = level;
		this.setSize(1);
		this.setDrawHeight(90);
		this.setThinkIntervalWithMove(true);
		this.isImportant = true;
		this.normalSpellTimer = 0;
		this.areaGrabTimer = AREA_GRAB_COOLDOWN;
		this.cryofreezeTimer = 0;
		this.icewallTimer = (float)(ICEWALL_COOLDOWN * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level));
		this.blizzardTimer = (float)(BLIZZARD_COOLDOWN * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level));
		this.state2Timer = 0;
		this.state2primarytimer = (float)(STATE2_PRIMARY_COOLDOWN * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level));
		this.state2icicletimer = (float)(STATE2_ICICLE_COOLDOWN * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level));
		this.state = 1;
	}
	@Override
	public void onThink() {
		this.trackUnit();
		if(this.state == 1) {
			this.moveRandom4(false, false, true, true, true);
			if(this.normalSpellTimer <= 0 && Math.random() < NORMAL_SPELL_CHANCE) {
				this.castSpell(SpellSelector.getRandomSpell(this));
				this.normalSpellTimer = (float)(NORMAL_SPELL_COOLDOWN * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level));
				return;
			} else if(this.areaGrabTimer <= 0 && Math.random() < AREA_GRAB_CHANCE) {
				this.castSpell(new AreaGrab(this));
				this.areaGrabTimer = (float)(AREA_GRAB_COOLDOWN * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level));
				return;
			} else if(this.cryofreezeTimer <= 0 && this.getHP()/this.getMaxHP() <= CRYOFREEZE_HP_RATIO_THRESHOLD && Math.random() < CRYOFREEZE_CHANCE) {
				this.castSpell(new CryoFreeze(this));
				this.cryofreezeTimer = (float)(CRYOFREEZE_COOLDOWN * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level));
				return;
			} else if(this.icewallTimer <= 0 && Math.random() < ICEWALL_CHANCE) {
				this.castSpell(new IceWall(this));
				this.icewallTimer = (float)(ICEWALL_COOLDOWN * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level));
				return;
			} else if(this.state2Timer <= 0 && Math.random() < STATE2_CHANCE) {
				this.state = 2;
				this.setSpeed(SPEED_STATE2 + SPEED_STATE2_PER_LEVEL * this.level);
				this.state2Timer = STATE2_COOLDOWN + STATE2_DURATION;
				return;
			}
		} else if(this.state == 2) {
			if(this.state2Timer < STATE2_COOLDOWN) {
				this.state = 1;
				this.setSpeed(SPEED + SPEED_PER_LEVEL * level);
				return;
			} else {
				if(this.unitToTrack != null && !this.unitToTrack.getRemove()) {
					this.aimAtTrackedUnit();
					if(this.state2icicletimer <= 0 
							&& ((this.unitToTrack.getStatusEffectCount(StatusFrostMei.ID) < StatusFrostMei.NUM_STACKS_TO_STUN && Math.random() < this.unitToTrack.getStatusEffectCount(StatusFrostMei.ID) * STATE2_ICICLE_CHANCE_PER_STACK + STATE2_ICICLE_BASE_CHANCE)
							|| (this.unitToTrack.getStatusEffectCount(StatusFrostMei.ID) >= StatusFrostMei.NUM_STACKS_TO_STUN && Math.random() < this.unitToTrack.getStatusEffectCount(StatusFrostMei.ID) * STATE2_ICICLE_CHANCE_PER_STACK + STATE2_ICICLE_BASE_CHANCE + STATE2_ICICLE_BONUS_CHANCE_FROZEN))
							&& Math.abs(this.unitToTrack.gridLoc.y - this.gridLoc.y) <= 0) {
						this.castSpell(new EndothermicBlasterSecondary(this));
						this.state2icicletimer = (float)(STATE2_ICICLE_COOLDOWN * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level));
						return;
					} else if(this.blizzardTimer <= 0 && Math.random() < BLIZZARD_CHANCE) {
						this.castSpell(new MeiBlizzard(this));
						this.blizzardTimer = (float)(BLIZZARD_COOLDOWN * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level));
						return;
					} else if (this.state2primarytimer <= 0) {
						this.castSpell(new EndothermicBlasterPrimary(this));
						this.state2primarytimer = (float)(STATE2_PRIMARY_COOLDOWN * Math.pow(COOLDOWN_MULT_PER_LEVEL, this.level));
					}
				}
			}
		}
	}
	@Override
	public void onUpdate(){
		if(this.normalSpellTimer > 0) {
			this.normalSpellTimer -= this.getFrameTime();
		}
		if(this.areaGrabTimer > 0) {
			this.areaGrabTimer -= this.getFrameTime();
		}
		if(this.cryofreezeTimer > 0) {
			this.cryofreezeTimer -= this.getFrameTime();
		}
		if(this.icewallTimer > 0) {
			this.icewallTimer -= this.getFrameTime();
		}
		if(this.blizzardTimer > 0) {
			this.blizzardTimer -= this.getFrameTime();
		}
		if(this.state2Timer > 0) {
			this.state2Timer -= this.getFrameTime();
		}
		if(this.state2primarytimer > 0) {
			this.state2primarytimer -= this.getFrameTime();
		}
		if(this.state2icicletimer > 0) {
			this.state2icicletimer -= this.getFrameTime();
		}
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
					this.move(GameMap.ID_UP, false, false, true, true, true);
				} else if(this.canMoveToLoc(Point.add(this.gridLoc, new Point(-1, -1)))) { //look into the future, see if there's obstruction
					this.move(GameMap.ID_LEFT, false, false, true, true, true); //move off to the side
				} else if(this.canMoveToLoc(Point.add(this.gridLoc, new Point(1, -1)))) {
					this.move(GameMap.ID_RIGHT, false, false, true, true, true);
				} else {
					this.moveRandom4(false, false, true, true, true);
				}
			} else if(this.unitToTrack.gridLoc.y > this.gridLoc.y) { //move down
				if(this.canMoveToLoc(Point.add(this.gridLoc, new Point(0, 1)))) {
					this.move(GameMap.ID_DOWN, false, false, true, true, true);
				} else if(this.canMoveToLoc(Point.add(this.gridLoc, new Point(-1, 1)))) { //look into the future, see if there's obstruction
					this.move(GameMap.ID_LEFT, false, false, true, true, true); //move off to the side
				} else if(this.canMoveToLoc(Point.add(this.gridLoc, new Point(1, 1)))) {
					this.move(GameMap.ID_RIGHT, false, false, true, true, true);
				} else {
					this.moveRandom4(false, false, true, true, true);
				}
			}
		}
	}
}
