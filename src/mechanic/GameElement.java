
package mechanic;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GameElement {

	private double hp;
	private double maxHP;
	private double speed;
	private double orientation; // in degrees
	private Point loc;
	private double size; // as a ratio (scale thing)
	private double dmg;
	private DamageType type;
	private double reload; // in frames
	private double aoe; // in pixels
	private double range;
	private GameElement target;
	static DamageType[] types = DamageType.values();
	static double[] resistances = new double[types.length];
	private double cost;
	private Image pic;
	private boolean remove;
	
	public GameElement() {
		this(new Point());
	}
	
	public GameElement(Point loc) {
		this(1, 1, 0, 0, loc, 0, null, 0, null, 0, 0, 0, null, 0);
	}
	
	/* INCOMPLETE */ public GameElement(double hp, double maxHP, double speed, double orientation, Point loc, double size, Image pic, double dmg, DamageType type, double reload, 
			double aoe, double range, GameElement target, double cost) {
		this.changeHP(hp);
		this.changeMaxHP(maxHP);
		this.changeSpeed(speed);
		this.changeOrientation(orientation);
		this.changeLoc(loc);
		this.remove = false;
	}
	
	/**
	 * Access instance variable hp
	 * 
	 * @return the value of instance variable hp
	 */
	public double getHP() {
		return this.hp;
	}
	
	/**
	 * Modify instance variable hp
	 * 
	 * If hp < 0, sets hp to 0
	 * If hp > maxHP, sets hp to maxHP
	 * 
	 * @param hp	The new value of hp
	 */
	public void changeHP(double hp) {
		if (hp < 0) {
			this.hp = 0;
			remove = true;
		} else if (hp > this.maxHP) {
			this.hp = this.maxHP;
		} else {
			this.hp = hp;
		}
	}
	
	/**
	 * Access instance variable maxHP
	 * 
	 * @return	The value of maxHP
	 */
	public double getMaxHP() {
		return this.maxHP;
	}
	
	/**
	 * Modify instance variable maxHP
	 * 
	 * Also calls changeHP to maintain same health percentage
	 * 
	 * @param maxHP
	 */
	public void changeMaxHP(double maxHP) {
		double ratio = this.hp / this.maxHP;
		
		this.maxHP = maxHP;
		
		this.changeHP(this.hp * ratio);
	}
	
	/**
	 * Access instance variable speed
	 * 
	 * @return	The value of speed
	 */
	public double getSpeed() {
		return this.speed;
	}
	
	/**
	 * Modify instance variable speed
	 * 
	 * If speed is negative, speed will be set as 0
	 * 
	 * @param speed	The new value of speed
	 */
	public void changeSpeed(double speed) {
		if (speed < 0) { // Speed cannot be negative!
			this.speed = 0;
		} else {
			this.speed = speed;
		}
	}
	
	/**
	 * Access instance variable orientation
	 * 
	 * @return	The value of orientation
	 */
	public double getOrientation() {
		return this.orientation;
	}
	
	/**
	 * Modify instance variable orientation
	 * 
	 * If orientation is greater than 2pi or less than 0, orientation will be converted to equivalent value between 0 and 2pi before being set
	 * 
	 * @param orientation	The new value of orientation
	 */
	public void changeOrientation(double orientation) {
		while (orientation > 2 * Math.PI) {
			orientation -= 2 * Math.PI;
		}
		while (orientation < 0) {
			orientation += 2 * Math.PI;
		}
		
		this.orientation = orientation;
	}
	
	/**
	 * Access instance variable loc
	 * 
	 * @return	The location of the GameElement
	 */
	public Point getLoc() {
		return this.loc;
	}
	
	/**
	 * Modify instance variable loc
	 * 
	 * @param loc	The new location of the GameElement
	 */
	public void changeLoc(Point loc) {
		this.loc = loc;
	}
	
	/**
	 * 
	 * 
	 */
	public void move() {
		
	}
	
	public Image getImage(){
		return this.pic;
	}
	
	public double getX(){
		return this.loc.x;
	}
	
	public double getY(){
		return this.loc.y;
	}
	
	public void update(){
		
	}
	
	public void setImage(String path){
		try {
			this.pic = new Image(path);
		} catch (SlickException e) {
			System.out.println("Unable to load image");
			e.printStackTrace();
		}
	}
	
	public boolean getRemove(){
		return remove;
	}
	
	public void setRemove(boolean state){
		remove = state;
	}
	
	public void doDamage(double damage){
		this.changeHP(hp - damage);
	}
	
	public void draw(Graphics g){
		g.drawImage(pic, (float) loc.x, (float) loc.y);
	}
}
