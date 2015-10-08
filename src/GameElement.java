
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
	DamageType[] types = new DamageType[3]; // not sure how to avoid magic number
	int[] resistances; // not sure how to combine with above array
	int cost;
	
	public void move() {
		
	}
}
