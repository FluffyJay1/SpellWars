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
		this.getLoc().addX(100 * this.getFrameTime());
		if(this.getRemove()) {
			String i = "res/explosion.png";
			ParticleEmitter pe = new ParticleEmitter(this.getLoc(), emitterTypes.POINT_RADIAL, i, true, /*point, emitter type, image path, alphaDecay*/
					0.0f, 0.5f, /*particle start scale*/
					0.5f, 1.0f, /*particle end scale*/
					2.5f, /*drag*/
					-300, 300, /*rotational velocity*/
					0.5f, 1, /*min and max lifetime*/
					100, 300, /*min and max launch speed*/
					0, 6, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
					0, 0, 0, 0, map); /*keyvalues and map*/
			map.addParticleEmitter(pe);
		}
	}
}
