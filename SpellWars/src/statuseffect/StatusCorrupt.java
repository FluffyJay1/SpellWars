package statuseffect;

import org.newdawn.slick.Color;

import mechanic.GameElement;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import spell.CorruptBerserk;
import unit.Unit;

public class StatusCorrupt extends StatusEffect {
	public static final float DURATION = 250;
	public static final float INTERVAL = 5;
	public static final float DAMAGE = 1;
	public static final float DAMAGE_CORRUPT = 5;
	public static final float INTERVAL_CORRUPT = 1;
	public static final float DURATION_CORRUPT = 10;
	static final Color DEBUFF_COLOR = new Color(255, 200, 255);
	static final Color FLASH_COLOR = new Color(120, 80, 120);
	static final float FLASH_INTERVAL = 2;
	static final float FLASH_DURATION = 0.1f;
	float flashTimer;
	public StatusCorrupt(GameElement owner) {
		super(owner, StackingProperty.STACKABLE_INDEPENDENT, "corrupt", DURATION, false, true, 1);
		this.setIcon("res/statuseffect/icon_corrupt.png");
		this.interval = INTERVAL;
		this.damagePerInterval = DAMAGE;
		this.setHealInputModifier((float)2/(float)3);
		this.flashTimer = 0;
		this.setColorModifier(FLASH_COLOR);
	}
	@Override
	public StatusEffect clone() {
		StatusEffect effect = new StatusCorrupt(this.getOwner());
		StatusEffect.copyFromTo(this, effect);
		return effect;
	}
	@Override
	public void onApplied() {
		if(this.getOwner().getStatusEffectCount("corrupt") == 3) {
			for(StatusEffect s : this.getOwner().getStatusEffects("corrupt")) {
				s.setDuration(DURATION_CORRUPT);
				s.damagePerInterval = DAMAGE_CORRUPT;
				s.interval = INTERVAL_CORRUPT;
				s.damagetimer = 0;
				s.isPurgable = false;
			}
			if(this.getOwner() instanceof Unit) {
				((Unit)this.getOwner()).castSpell(new CorruptBerserk(((Unit)this.getOwner())), true, true, true, true);
			}
		} else if(this.getOwner().getStatusEffectCount("corrupt") > 3) {
			this.setRemove(true);
		}
	}
	@Override
	public void onUpdate() {
		if(this.flashTimer < FLASH_DURATION) {
			this.setColorModifier(FLASH_COLOR);
		} else {
			this.setColorModifier(DEBUFF_COLOR);
		}
		if(this.flashTimer <= 0) {
			this.flashTimer += FLASH_INTERVAL;
		}
		this.flashTimer -= this.getOwner().getFrameTime();
	}
	@Override
	public void onInterval() {
		if(this.getOwner().getStatusEffectCount("corrupt") >= 2) {
			if(this.getOwner() instanceof Unit) {
				ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.CIRCLE_RADIAL, "res/particle_genericPurple.png", true, /*point, emitter type, image path, alphaDecay*/
						5.0f, 5.0f, /*particle start scale*/
						0.0f, 0.0f, /*particle end scale*/
						8.75f, /*drag*/
						0, 0, /*rotational velocity*/
						0.6f, 0.6f, /*min and max lifetime*/
						-2050, -2050, /*min and max launch speed*/
						0, 6, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
						200, 200, 0, 0); /*keyvalues*/
				this.getOwner().getMap().addParticleEmitter(pe);
				((Unit)this.getOwner()).moveRandom4(false, true, true, true, true, true);
			}
		}
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.POINT_RADIAL, "res/particle_genericPurple.png", true, /*point, emitter type, image path, alphaDecay*/
				3.0f, 3.0f, /*particle start scale*/
				5.0f, 5.0f, /*particle end scale*/
				8.75f, /*drag*/
				0, 0, /*rotational velocity*/
				0.6f, 0.6f, /*min and max lifetime*/
				950, 950, /*min and max launch speed*/
				0, 6, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
				0, 0, 0, 0); /*keyvalues*/
		this.getOwner().getMap().addParticleEmitter(pe);
	}
}
