package towers;

import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;
import monsters.Monster;
import particlesystem.ParticleEmitter;
import particlesystem.emitterTypes;
import projectiles.Laser;

public class DefaultTower extends Tower{
	private GameMap map;
	public DefaultTower(double x, double y, GameMap map){
		super();
		this.changeLoc(new Point(x,y));
		this.setImage("res/fatbarrelturret.JPG");
		this.map = map;
	}
	
	@Override
	public void update(){
		for(GameElement e : map.getElements()){
			if(e instanceof Monster){
				map.addProjectile(new Laser(this, e));
				/*THINGS BELOW ARE COMPLETELY UNNECESSARY*/
				String i = "res/explosion.png";
				ParticleEmitter pe = new ParticleEmitter(this.getLoc(), emitterTypes.LINE_RADIAL, i, true, /*point, emitter type, image path, alphaDecay*/
						0.3f, 0.3f, /*particle start scale*/
						0.1f, 0.1f, /*particle end scale*/
						13.5f, /*drag*/
						0, 0, /*rotational velocity*/
						0.7f, 0.7f, /*min and max lifetime*/
						400, 400, /*min and max launch speed*/
						0, 1, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
						(float)e.getLoc().getX(), (float)e.getLoc().getY(), 0, 20, map); /*keyvalues and map*/
				map.addParticleEmitter(pe);
				/*THINGS ABOVE ARE COMPLETELY UNNECESSARY*/
			}
		}
	}
	/*
	@Override
	public void draw(Graphics g){
		g.drawImage(this.getImage().getScaledCopy((float) 0.5),(float)this.getX(), (float)this.getY());
	}*/
	
}
