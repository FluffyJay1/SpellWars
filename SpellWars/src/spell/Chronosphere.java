package spell;

import java.util.ArrayList;

import mechanic.GameMap;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import statuseffect.StackingProperty;
import statuseffect.StatusEffect;
import unit.Unit;

public class Chronosphere extends Spell {
	public static final float INTERVAL = 0.125f;
	public static final float DURATION = 6f;
	public static final float DAMAGE_PER_SECOND = 2.5f;
	public static final float DAMAGE_AMP = 2;
	int ticks;
	Point chronoLoc;
	ArrayList<Point> points;
	public Chronosphere(Unit owner) {
		super(owner, 0.1f, 0.4f, "Chronosphere", "Creates a bubble in spacetime 4 squares ahead, stunning all unfortunate units in this bubble, and doubles their damage taken", "res/spell/chronosphere.png", true);
	}
	@Override
	public void onActivate() {
		this.chronoLoc = GameMap.getFuturePoint(owner.gridLoc, (char)owner.direction, 4);
		this.map.unpauseAll();
		this.ticks = 0;
		this.points = Point.proximity8(chronoLoc);
		this.points.add(chronoLoc);
		this.onThink();
		this.setThinkInterval(INTERVAL);
	}
	@Override
	public void onThink() {
		this.ticks++;
		for(Point p : this.points) {
			if(this.map.pointIsInGrid(p)) {
				Unit target = this.map.getPanelAt(p).unitStandingOnPanel;
				if(target != null) {
					StatusEffect s = new StatusEffect(target, StackingProperty.UNSTACKABLE_REPLACE, "chronosphere amp", INTERVAL * 2, false, false, 1);
					s.setDamageInputModifier(DAMAGE_AMP);
					target.addStatusEffect(s);
					target.updateFinalModifiers();
					target.stun(INTERVAL);
					target.doDamage(INTERVAL * DAMAGE_PER_SECOND);
				}
				ParticleEmitter pe = new ParticleEmitter(this.getMap().gridToPosition(p), EmitterTypes.CIRCLE_DIRECTION, "res/particle_genericBlue.png", false, //point/parent, emitter type, image path, alphaDecay
						1.5f, 2.5f, //particle start scale
						0.0f, 0.0f, //particle end scale
						4.5f, //drag
						0, 0, //rotational velocity
						INTERVAL * 3, INTERVAL * 3, //min and max lifetime
						10, 400, //min and max launch speed
						0, 2, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
						0, (float)this.getMap().getSizeOfPanel().y/3, 90, 20); //keyvalues
				this.getMap().addParticleEmitter(pe);
			}
		}
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.getMap().gridToPosition(this.chronoLoc), new Point(0, -80)), EmitterTypes.POINT_DIRECTION, "res/spell/chronosphere_effect.png", false, //point/parent, emitter type, image path, alphaDecay
				2.75f, 2.75f, //particle start scale
				2.75f, 2.75f, //particle end scale
				0f, //drag
				0, 0, //rotational velocity
				INTERVAL * 1, INTERVAL * 1, //min and max lifetime
				0, 0, //min and max launch speed
				0, 1, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
				0, 3, 0, 0); //keyvalues
		this.getMap().addParticleEmitter(pe);
		if(this.ticks >= DURATION/INTERVAL) {
			this.finishSpell();
		}
	}
}
