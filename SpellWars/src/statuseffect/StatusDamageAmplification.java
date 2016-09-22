package statuseffect;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import projectile.Projectile;
import unit.Unit;

public class StatusDamageAmplification extends StatusEffect {
	public static final String ID = "damageamp";
	public StatusDamageAmplification(GameElement owner, float damageChange, float duration, int level) {
		super(owner, StackingProperty.UNSTACKABLE_REFRESH_DURATION, ID, duration, level);
		this.setIcon("res/spell/damageamp.png");
		this.interval = 0.1f;
		if(this.getOwner() instanceof Unit) {
			this.setAttackDamageModifier(damageChange);
		}
	}
	@Override
	public void onInterval() {
		if(this.getOwner() != null && this.getOwner() instanceof Unit) {
			String image = "res/particle_genericRed.png";
			if(((Unit)this.getOwner()).teamID == GameMap.ID_RIGHT) {
				image = "res/particle_genericBlue.png";
			}
			ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.CIRCLE_DIRECTION, image, true, /*point, emitter type, image path, alphaDecay*/
					1.5f, 2.5f, /*particle start scale*/
					0.0f, 0.0f, /*particle end scale*/
					3.5f, /*drag*/
					0, 0, /*rotational velocity*/
					0.4f, 0.8f, /*min and max lifetime*/
					350, 350, /*min and max launch speed*/
					0, 2, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
					0, 50, 90, 0); /*keyvalues*/
			getOwner().getMap().addParticleEmitter(pe);
		}
	}
}
