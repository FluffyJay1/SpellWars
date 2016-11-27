package statuseffect;

import particlesystem.ParticleEmitter;
import particlesystem.EmitterTypes;
import projectile.Projectile;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;


public class StatusZaWarudo extends StatusEffect {
	public static final String ID = "zawarudo"; //for stacking
	public static final float UPDATE_INTERVAL = 0.05f;
	public static final float SPEED_DECAY_PER_INTERVAL = 0.5f;
	public static final int TICKS_UNTIL_STOP = 4;
	int ticks;
	public StatusZaWarudo(GameElement owner, float duration) {
		super(owner, StackingProperty.UNSTACKABLE_REPLACE, ID, duration, false, false, 1);
		this.interval = UPDATE_INTERVAL;
		//this.damageChange = damageReduction;
		//this.setDamageType(DAMAGE_TYPE);
		this.setColorModifier(new Color(200, 240, 250));
		this.ticks = 0;
	}
	@Override
	public StatusEffect clone() {
		StatusEffect effect = new StatusZaWarudo(this.getOwner(), 0);
		StatusEffect.copyFromTo(this, effect);
		return effect;
	}
	
	@Override
	public void onInterval() {
		if(ticks < TICKS_UNTIL_STOP) {
			this.setMoveSpeedModifier(this.getMoveSpeedModifier() * SPEED_DECAY_PER_INTERVAL);
			this.ticks++;
		} else {
			this.setMoveSpeedModifier(0);
		}
	}
	@Override
	public void onCreate() {
		
	}
}
