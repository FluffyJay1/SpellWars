
import java.awt.Point;

public class GameElement {
	int hp;
	int maxhp;
	double speed;
	double orientation; // in degrees
	Point loc;
	int size; // in pixels
	// pic;
	int dmg;
	DamageType type;
	int reload; // in frames
	int aoe; // in pixels
	Monster target;
	static DamageType[] types = DamageType.values();
	int[] resistances = new int[types.length];
	int cost;
	
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
		
	}
	
	public void move() {
		
	}
}
