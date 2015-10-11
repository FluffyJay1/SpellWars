package projectiles;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;

import mechanic.GameElement;

public class Laser extends Projectile {
	
	public static double LASER_DAMAGE = 10;
	
	GameElement tower;
	GameElement monster;
	int delay;
	
	public Laser(GameElement tower, GameElement monster){
		this.tower = tower;
		this.monster = monster;
		delay = 50;
	}
	
	@Override
	public void update(){
		if(delay < 0){
			setRemove(true);
			monster.doDamage(LASER_DAMAGE);
		}
		else{
			delay--;
		}
	}
	
	@Override
	public void draw(Graphics g){
		if(delay > 0){
			return;
		}
		g.setColor(Color.yellow);
		g.drawLine((float)tower.getX(),(float) tower.getY(),(float) monster.getX(),(float) monster.getY());
	}
	
}
