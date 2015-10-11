package monsters;

import mechanic.Point;

public class DefaultMonster extends Monster{
	public static String imagePath = "res/turret_monster pics.png";
	
	public DefaultMonster(double x, double y){
		super();
		changeLoc(new Point(x,y));
		setImage(imagePath);
	}
}
