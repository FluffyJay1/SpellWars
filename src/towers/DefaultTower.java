package towers;

import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;
import monsters.Monster;

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
			}
		}
	}
	/*
	@Override
	public void draw(Graphics g){
		g.drawImage(this.getImage().getScaledCopy((float) 0.5),(float)this.getX(), (float)this.getY());
	}*/
	
}
