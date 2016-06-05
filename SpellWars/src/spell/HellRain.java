package spell;

import mechanic.Panel;
import mechanic.Point;
import projectile.CrackGrenade;
import projectile.Grenade;
import projectile.Projectile;

import java.util.ArrayList;

import mechanic.GameMap;
import unit.Unit;

public class HellRain extends Spell {
	public static final float DURATION = 4.5f;
	public static final int NUM_STRIKES = 32;
	public static final int DAMAGE = 10;
	public static final float CHANCE_TO_CRACK = 0.05f;
	float timer;
	int shotsFired;
	ArrayList<Panel> affectedPanels;
	public HellRain(Unit owner) {
		super(owner, 0, DURATION, "Hell Rain", "Rain hell on the enemy, and also crack some of their panels", "res/x.png", false, true);
		this.timer = DURATION;
		this.setThinkInterval(DURATION/NUM_STRIKES);
		this.shotsFired = 0;
	}
	@Override
	public void onSetMap() {
		this.affectedPanels = this.getMap().getPanelsOfTeam(this.owner.direction);
	}
	@Override
	public void onThink(){
		while(this.shotsFired < (1 - (this.timer/DURATION)) * NUM_STRIKES) {
			int randomIndex = (int)(Math.random()*this.affectedPanels.size() - 0.0000001);
			Point loc = this.affectedPanels.get(randomIndex).getLoc();
			Projectile projectile = new CrackGrenade(DAMAGE, CHANCE_TO_CRACK, 1.75, Point.subtract(loc, this.owner.gridLoc), 80, 10, this.owner.gridLoc, "res/particle_genericYellow.png", this.owner.teamID);
			projectile.setImageScale(2);
			this.map.addGameElement(projectile);
			this.shotsFired++;
			if(shotsFired >= NUM_STRIKES) {
				this.finishSpell();
				break;
			}
		}
	}
	@Override
	public void onSpellUpdate() {
		this.timer -= this.getFrameTime();
		if(this.owner.getRemove()) {
			this.finishSpell();
		}
	}
}
