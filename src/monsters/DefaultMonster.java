package monsters;

import mechanic.Point;

public class DefaultMonster extends Monster{
	public static String imagePath = "res/plane.png";
	public static int MAXHP = 500;
	
	public DefaultMonster(double x, double y){
		super();
		changeLoc(new Point(x,y));
		setImage(imagePath);
		this.changeMaxHP(MAXHP);
	}
}
