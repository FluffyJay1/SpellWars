package spell;

import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import statuseffect.StackingProperty;
import statuseffect.StatusCorrupt;
import ui.SpellSelector;
import unit.Unit;

public class Berserk extends Spell {
	public static final float DURATION = 10;
	public static final float SPEED_MULTIPLIER = 2;
	public static final float SPELL_INTERVAL = 1f;
	float spellTimer;
	float timer;
	public Berserk(Unit owner) {
		super(owner, 0, 0, "Berserk", "Go berserk, losing control of your unit but it uses random spells at a great rate, also becomes immune to slows", "res/spell/berserk.png", true);
		this.disableUnitControl = true;
	}
	@Override
	public void onActivate() {
		this.getMap().unpauseAll();
		this.timer = 0;
		this.spellTimer = 0;
		this.setThinkInterval((float)(1/(this.owner.getSpeed() * SPEED_MULTIPLIER)));
	}
	@Override
	public void onThink() {
		if(this.owner.getRemove()) {
			this.finishSpell();
		}
		this.owner.moveRandom4(false, false, true, true, true, true);
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.CIRCLE_DIRECTION, "res/particle_genericRed.png", true, /*point, emitter type, image path, alphaDecay*/
				2.5f, 3.5f, /*particle start scale*/
				0.0f, 0.0f, /*particle end scale*/
				8.75f, /*drag*/
				0, 0, /*rotational velocity*/
				0.3f, 0.6f, /*min and max lifetime*/
				150, 950, /*min and max launch speed*/
				0, 4, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
				0, 50, 90, 45); /*keyvalues*/
		getOwner().getMap().addParticleEmitter(pe);
	}
	@Override
	public void onSpellUpdate() {
		if(this.spellTimer >= SPELL_INTERVAL) {
			Spell spell = SpellSelector.getRandomSpell(this.owner);
			while(spell instanceof Berserk) {
				spell = SpellSelector.getRandomSpell(this.owner);
			}
			if(this.owner.castSpell(spell, false, false, false, true)) {
				this.spellTimer -= SPELL_INTERVAL;
			}
		} else {
			this.spellTimer += this.getFrameTime();
		}
		this.timer += this.getFrameTime();
		if(this.timer >= DURATION) {
			this.finishSpell();
			ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.CIRCLE_RADIAL, "res/particle_genericWhite.png", true, /*point, emitter type, image path, alphaDecay*/
					10.0f, 10.0f, /*particle start scale*/
					10.0f, 10.0f, /*particle end scale*/
					8.75f, /*drag*/
					0, 0, /*rotational velocity*/
					0.6f, 0.6f, /*min and max lifetime*/
					-3950, -3950, /*min and max launch speed*/
					0, 24, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
					400, 400, 0, 0); /*keyvalues*/
			getOwner().getMap().addParticleEmitter(pe);
		}
	}
}
