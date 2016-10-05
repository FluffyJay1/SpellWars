package spell;

import mechanic.Panel;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import projectile.BrimstoneGrenade;
import projectile.CrackGrenade;
import projectile.Grenade;
import projectile.Projectile;

import java.util.ArrayList;

import mechanic.GameMap;
import unit.Unit;

public class FireAndBrimstone extends Spell {
	public static final float DURATION = 5.0f;
	public static final int NUM_STRIKES = 12;
	public static final int DAMAGE = 5;
	public static final float DAMAGE_PER_SECOND = 2;
	public static final int DAMAGE_DURATION = 20;
	public static final float AIR_TIME = 1.00f;
	public static final float INITIAL_HEIGHT = 1700;
	public static final float FINAL_HEIGHT = 10;
	float timer;
	int shotsFired;
	ArrayList<Panel> affectedPanels;
	public FireAndBrimstone(Unit owner) {
		super(owner, 0, DURATION, "Fire and Brimstone", "Pretty much just win lol", "res/spell/fireandbrimstone.png", false, true);
		this.timer = DURATION;
		this.setThinkInterval(DURATION/NUM_STRIKES);
		this.shotsFired = 0;
	}
	@Override
	public void onSetMap() {
		this.affectedPanels = this.getMap().getPanelsOfTeam(this.owner.direction);
	}
	@Override
	public void onActivate() {
		this.fire();
	}
	@Override
	public void onThink(){
		while(this.shotsFired < (1 - (this.timer/DURATION)) * NUM_STRIKES) {
			this.fire();
			ParticleEmitter pe = new ParticleEmitter(this.owner.getLoc(), EmitterTypes.LINE_RADIAL, "res/particle_genericRed.png", false, //point/parent, emitter type, image path, alphaDecay
					2.8f, 3.0f, //particle start scale
					0, 0, //particle end scale
					10.5f, //drag
					0, 0, //rotational velocity
					0.4f, 0.65f, //min and max lifetime
					80, 420, //min and max launch speed
					0, 15, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
					(float)this.owner.getLoc().x, (float)this.owner.getLoc().y - INITIAL_HEIGHT, 0, 0); //keyvalues
			this.getMap().addParticleEmitter(pe);
			if(shotsFired >= NUM_STRIKES) {
				this.finishSpell();
				break;
			}
		}
	}
	public void fire() {
		int randomIndex = (int)(Math.random()*this.affectedPanels.size() - 0.0000001);
		Point loc = this.affectedPanels.get(randomIndex).getLoc();
		Projectile projectile = new BrimstoneGrenade((int)(DAMAGE * this.owner.finalDamageModifier), AIR_TIME, DAMAGE_PER_SECOND, DAMAGE_DURATION, Point.subtract(loc, this.owner.gridLoc), INITIAL_HEIGHT, FINAL_HEIGHT, this.owner.gridLoc, "res/particle_genericYellow.png", this.owner.teamID);
		projectile.setImageScale(0.75f);
		this.map.addGameElement(projectile);
		this.shotsFired++;
	}
	@Override
	public void onSpellUpdate() {
		this.timer -= this.getFrameTime();
		if(this.owner.getRemove()) {
			this.finishSpell();
		}
	}
}
