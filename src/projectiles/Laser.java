package projectiles;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;

import mechanic.GameElement;

public class Laser extends Projectile {
	
	float damage;
	
	GameElement tower;
	GameElement monster;
	int delay;
	
	public Laser(GameElement tower, GameElement monster, float damage){
		this.tower = tower;
		this.monster = monster;
		this.delay = 150;
		this.damage = damage;
		monster.doDamage(this.damage);
	}
	
	@Override
	public void update(){
		if(this.delay < 0){
			this.setRemove(true);
		}
		else{
			this.delay--;
		}
	}
	
	@Override
	public void draw(Graphics g){
		if(delay < 0){
			return;
		}
		g.setColor(Color.yellow);
		g.drawLine((float)tower.getX(),(float) tower.getY(),(float) monster.getX(),(float) monster.getY());
	}
	
}
