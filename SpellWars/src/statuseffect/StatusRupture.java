package statuseffect;

import org.newdawn.slick.Color;

import mechanic.GameElement;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import unit.Unit;

public class StatusRupture extends StatusEffect {
	public static final String ID = "rupture";
	public static final double DAMAGE_PER_MOVE = 5;
	Point lastLoc;
	public StatusRupture(GameElement owner, float duration) {
		super(owner, StackingProperty.UNSTACKABLE_REFRESH_DURATION, ID, duration, false, true, 1);
		this.setIcon("res/statuseffect/icon_rupture.png");
		this.setColorModifier(new Color(255, 190, 190));
	}
	@Override
	public void onUpdate() {
		if(this.getOwner() instanceof Unit && !this.muteEffect) {
			if(this.lastLoc == null) {
				this.lastLoc = ((Unit)this.getOwner()).gridLoc;
			}
			if(!Point.equals(this.lastLoc, ((Unit)this.getOwner()).gridLoc)) {
				double distance = Point.getDistance(this.lastLoc, ((Unit)this.getOwner()).gridLoc);
				this.getOwner().doDamage(DAMAGE_PER_MOVE * distance);
				for(Point p : Point.getPointsBetween(this.getOwner().getMap().gridToPosition(this.lastLoc), this.getOwner().getMap().gridToPosition(((Unit)this.getOwner()).gridLoc))) {
					if(Math.random() < 0.1) {
						ParticleEmitter pe = new ParticleEmitter(Point.add(p, new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.CIRCLE_DIRECTION, "res/particle_blood.png", true, /*point, emitter type, image path, alphaDecay*/
								1.5f, 2.5f, /*particle start scale*/
								0.0f, 0.0f, /*particle end scale*/
								-3.75f, /*drag*/
								0, 0, /*rotational velocity*/
								0.3f, 0.6f, /*min and max lifetime*/
								40, 40, /*min and max launch speed*/
								0, 1, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
								0, 20, -90, 0); /*keyvalues*/
						this.getOwner().getMap().addParticleEmitter(pe);
					}
				}
				ParticleEmitter pe = new ParticleEmitter(Point.add(this.getOwner().getLoc(), new Point(0, -this.getOwner().getDrawHeight())), EmitterTypes.CIRCLE_DIRECTION, "res/particle_blood.png", true, /*point, emitter type, image path, alphaDecay*/
						1.5f, 2.5f, /*particle start scale*/
						0.0f, 0.0f, /*particle end scale*/
						-3.75f, /*drag*/
						0, 0, /*rotational velocity*/
						0.3f, 0.6f, /*min and max lifetime*/
						40, 40, /*min and max launch speed*/
						0, 3 * (float)distance, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
						0, 20, -90, 0); /*keyvalues*/
				this.getOwner().getMap().addParticleEmitter(pe);
				this.lastLoc = ((Unit)this.getOwner()).gridLoc;
				
			}
		}
	}
	@Override
	public StatusEffect clone() {
		StatusEffect effect = new StatusRupture(this.getOwner(), this.getDuration());
		StatusEffect.copyFromTo(this, effect);
		return effect;
	}
}
