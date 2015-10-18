package towers;

import java.util.ArrayList;

import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;
import monsters.Monster;
import particlesystem.ParticleEmitter;
import particlesystem.emitterTypes;
import projectiles.Laser;

public class DefaultTower extends Tower{
	
	public DefaultTower(double x, double y, GameMap map) {
		this(x, y, map, 300, 1, 100);
	}
	public DefaultTower(double x, double y, GameMap map, float attackRange, float baseAttackTime, float baseAttackSpeed){
		super(x, y, map, attackRange, baseAttackTime, baseAttackSpeed);
		this.setImage("res/fatbarrelturret.JPG");
	}
	
	/*
	@Override
	public void update(){
		for(GameElement e : map.getElements()){
			if(e instanceof Monster){
				map.addProjectile(new Laser(this, e));
				
			}
		}
	}
	*/
	
	/*
	@Override
	public void draw(Graphics g){
		g.drawImage(this.getImage().getScaledCopy((float) 0.5),(float)this.getX(), (float)this.getY());
	}*/
	
}
