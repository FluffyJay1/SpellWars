

import java.awt.Point;

import org.newdawn.slick.Image;

public class GameElement {
	private int hp;
	private int maxhp;
	private double speed;
	private double orientation; // in degrees
	private Point loc;
	private int size; // in pixels
	private int dmg;
	private DamageType type;
	private int reload; // in frames
	private int aoe; // in pixels
	private Monster target;
	static DamageType[] types = DamageType.values();
	private int[] resistances = new int[types.length];
	private int cost;
	private Image pic;
	
	public int getHP() {
		return this.hp;
	}
	
	public void changeHP(int hp) {
		if (hp < 0) {
			this.hp = 0;
		}
	}
	
	public int getMaxHP() {
		return this.hp;
	}
	
	public void changeMaxHP(int maxHP) {
		if(maxHP >= 0){
			this.maxhp = maxHP;
		}
	}
	
	public double getX(){
		return this.loc.getX();
	}
	
	public double getY(){
		return this.loc.getY();
	}
	
	public void move() {
		
	}
	
	public Image getImage(){
		return this.pic;
	}
	
	public double getOrientation(){
		return this.orientation;
	}
}
