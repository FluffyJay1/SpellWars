package monsters;

import org.newdawn.slick.Color;

import particlesystem.ParticleEmitter;
import particlesystem.emitterTypes;
import mechanic.GameMap;
import mechanic.Point;

public class DefaultMonster extends Monster{
	public static String imagePath = "res/plane.png";
	public static int MAXHP = 500;
	private GameMap map;
	
	public DefaultMonster(double x, double y, GameMap map){
		super();
		changeLoc(new Point(x,y));
		setImage(imagePath);
		this.changeMaxHP(MAXHP);
		this.changeHP(MAXHP);
		this.map = map;
	}
	@Override
	public void update() {
		if(this.getRemove()) {
			String i = "res/explosion.png";
			ParticleEmitter pe = new ParticleEmitter(this.getLoc(), emitterTypes.POINT, i, Color.white, true, /*point, emitter type, image path, color, alphaDecay*/
					2.5f, /*drag*/
					-30, 30, /*rotational velocity*/
					0, 1, /*min and max lifetime*/
					10, 500, /*min and max launch speed*/
					0, 40, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
					0, 0, 0, 0, map); /*keyvalues and map*/
			map.addParticleEmitter(pe);
		}
	}
}
