package ui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import mechanic.Game;
import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;
import states.StateGame;

public class UIElement {
	/*
	 * Notes:
	 * UI elements are created in a hierarchy system
	 * (e.g. for an options menu, a scrollbar's parent might be the menu window and it's children might include the actual bar)
	 * (Also the when the scrollbar gets moved, it tells its parents (the window) to scroll, and the parent (the window) then causes its children (options) to move)
	 * Removing a parent removes its children
	 * Children are positioned relative to their parents (their origin is their parent's loc + origin)
	 * When an element is clicked, it can choose to relay that information to its parent and the parent decides what to do
	 * (e.g. when the upgrade button is pressed in the upgrade menu, the menu tells the map to upgrade the tower)
	 */
	Point origin; //normally the parent, else it should be (0, 0)
	Point loc;
	UIElement parent;
	GameElement elementToRemoveWith;
	ArrayList<UIElement> children = new ArrayList<UIElement>();
	ArrayList<UIElement> childrenAddBuffer = new ArrayList<UIElement>();
	ArrayList<UIElement> childrenRemoveBuffer = new ArrayList<UIElement>();
	
	private boolean isFront;
	private Image pic;
	private String imagepath;
	public boolean sendDrawInfo;
	private Color color;
	private boolean fixedHitbox;
	private float width; //for hitbox (clicking)
	private float height; //for hitbox 
	private float alpha;
	private boolean remove;
	private boolean removeNextFrame;
	private double frametime;
	private GameMap map;
	private UI ui;
	private double size;
	double orientation;
	
	double temporaryDuration;
	double temporaryTimer;
	boolean isTemporary;
	public UIElement() {
		this(null, new Point());
	}
	public UIElement(UI ui) {
		this(ui, new Point());
	}
	public UIElement(UI ui, float width, float height, Point loc) {
		this.setSize(1);
		this.color = Color.white;
		this.remove = false;
		this.removeNextFrame = false;
		this.ui = ui;
		this.orientation = 0;
		this.fixedHitbox = true;
		this.width = width;
		this.height = height;
		this.loc = loc;
		this.origin = new Point();
		this.alpha = 1;
		this.sendDrawInfo = true;
		this.isTemporary = false;
	}
	public UIElement(UI ui, Point loc) {
		this.setSize(1);
		this.color = Color.white;
		this.remove = false;
		this.removeNextFrame = false;
		this.ui = ui;
		this.orientation = 0;
		this.fixedHitbox = false;
		this.origin = new Point();
		this.loc = loc;
		this.alpha = 1;
		this.sendDrawInfo = true;
		this.isTemporary = false;
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
	 * Access instance variable origin
	 * 
	 * @return	The origin of the GameElement
	 */
	public Point getOrigin() {
		return this.origin;
	}
	
	/**
	 * Modify instance variable origin
	 * 
	 * @param loc	The new origin of the GameElement
	 */
	public void changeOrigin(Point loc) {
		this.origin = loc;
	}
	/**
	 * Gets the final position of the UIElement, taking into account
	 * its origin and its local location
	 * 
	 * @return The final location on the screen
	 */
	public Point getFinalLoc() {
		Point finalLoc = Point.clone(this.origin);
		finalLoc.add(this.loc);
		return finalLoc;
	}
	public void moveTowardPoint(Point loc, float speed) {
		Point vec = Point.getVector(this.loc, loc);
		vec.scale(speed * this.frametime);
		this.loc.add(vec);
	}
	public boolean isReasonableDistanceAwayFrom(Point loc, float speed) {
		return Point.getDistance(this.getLoc(), loc) < speed * this.getFrameTime();
	}
	public Image getImage(){
		return this.pic;
	}
	public void update(){
		
	}
	public void updateDuration() {
		if(this.isTemporary) {
			this.temporaryTimer -= this.getFrameTime();
			if(this.temporaryTimer <= 0) {
				this.setRemove(true);
			}
		}
	}
	public void setImage(String path){
		if(Game.images.containsKey(path)) {
			this.pic = Game.images.get(path).copy();
		} else {
			try {
				this.pic = new Image(path);
				Game.images.put(path, this.pic.copy());
			} catch (SlickException e) {
				System.out.println("Unable to load: " + path);
				e.printStackTrace();
			} finally {
				System.out.println("loaded into memory: " + path);
			}
		}
		this.imagepath = path;
	}
	/*
	public void setImage(Image pic) {
		this.pic = pic.copy();
	}
	*/
	public void setElementToRemoveWith(GameElement e) {
		this.elementToRemoveWith = e;
	}
	public boolean getRemove(){
		return this.remove;
	}
	
	public void setRemove(boolean state){
		this.remove = state;
		for(UIElement u : this.getChildren()) {
			u.setRemove(state);
			/*
			if(state) {
				u.removeParent(); //Severs all connections
			}
			*/
		}
		/*
		if(state) {
			this.removeParent(); //If it is getting removed, it severs connections with its parent
		}
		*/
	}
	public void setRemoveNextFrame(boolean state){
		this.removeNextFrame = state;
	}
	public boolean getRemoveNextFrame() {
		return this.removeNextFrame;
	}
	public void setDuration(double duration) {
		this.temporaryDuration = duration;
		this.temporaryTimer = duration;
		this.isTemporary = true;
	}
	public void draw(Graphics g){
		if(this.getImage() != null){
			Image endPic = this.pic.getScaledCopy((float) getSize());
			endPic.rotate(-(float)orientation);
			float width = endPic.getWidth();
			float height = endPic.getHeight();
			endPic.setAlpha(this.alpha);
			g.drawImage(endPic, (float) this.getFinalLoc().getX() - width/2, (float) this.getFinalLoc().getY() - height/2, this.color);
			if(this.fixedHitbox == false) {
				this.width = width;
				this.height = height;
			}
			if(this.getMap() != null) {
				if(StateGame.isServer && this.sendDrawInfo)
				this.getMap().addToDrawInfo(GameMap.getDrawDataI(this.imagepath, this.getFinalLoc().getX() - width/2, this.getFinalLoc().getY() - height/2, width, height, 0, this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.color.getAlpha(), 0));
			}
		}
	}
	public String getImagePath() {
		return this.imagepath;
	}
	public double getFrameTime() {
		return frametime;
	}
	public void passFrameTime(float frametime) {
		this.frametime = frametime;
	}
	public GameMap getMap() {
		return this.map;
	}
	public void setMap(GameMap map) {
		this.map = map;
		this.onSetMap();
	}
	public void onSetMap() {
		
	}
	public UI getUI() {
		return this.ui;
	}
	public void setUI(UI ui) {
		this.ui = ui;
		this.onSetUI();
	}
	public void onSetUI() {
		
	}
	public void onClick() {
		
	}
	public void onAddedToUI() {
		
	}
	//A way of sending messages to others, and controlling what they do upon receiving what messages
	public void passMessage(String action) {
		
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getWidth() {
		if(this.fixedHitbox) {
			return this.width;
		}
		Image endPic = this.pic.getScaledCopy((float) getSize());
		endPic.rotate(-(float)orientation);
		return endPic.getWidth();
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getHeight(){
		if(this.fixedHitbox) {
			return this.height;
		}
		Image endPic = this.pic.getScaledCopy((float) getSize());
		endPic.rotate(-(float)orientation);
		return endPic.getHeight();
	}
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	public float getAlpha() {
		return this.alpha;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public Color getColor(){
		return this.color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public void setIsFront(boolean isFront) {
		this.isFront = isFront;
	}
	public boolean isFront() {
		return this.isFront;
	}
	public void fitOnScreen(){
		float height = this.getHeight();
		float width = this.getWidth();
		if(this.getFinalLoc().getX() - width/2 < 0) {
			this.loc = new Point(width/2 - this.getOrigin().getX(), this.loc.getY());
		} 
		if(this.getFinalLoc().getX() + width/2 > Game.WINDOW_WIDTH) {
			this.loc = new Point(Game.WINDOW_WIDTH - width/2 - this.getOrigin().getX(), this.loc.getY());
		}
		if(this.getFinalLoc().getY() - height/2 < 0) {
			this.loc = new Point(this.loc.getX(), height/2 - this.getOrigin().getY());
		}
		if(this.getFinalLoc().getY() + height/2 > Game.WINDOW_HEIGHT) {
			this.loc = new Point(this.loc.getX(), Game.WINDOW_HEIGHT - height/2 - this.getOrigin().getY());
		}
	}
	public boolean pointIsInHitbox(Point loc) {
		return loc.getX() >= this.getFinalLoc().getX() - this.width/2
				&& loc.getX() <= this.getFinalLoc().getX() + this.width/2
				&& loc.getY() >= this.getFinalLoc().getY() - this.height/2
				&& loc.getY() <= this.getFinalLoc().getY() + this.height/2;
	}
	//PARENTING////////////////////////////////////////
	/*
	 * Notes on parenting:
	 * When creating a UI element that needs children, the children should be initialized under the declaration of the UI element itself
	 * 	and their parents should be set
	 * When changing the remove boolean of an element, all of its children get set the same state
	 * A child has 1 parent max
	 * We should NEVER USE the remove parent / remove child functions!
	 */
	public UIElement getParent() {
		return this.parent;
	}
	public ArrayList<UIElement> getChildren() {
		ArrayList<UIElement> allchildren = new ArrayList<UIElement>();
		allchildren.addAll(this.children);
		allchildren.addAll(this.childrenAddBuffer);
		allchildren.removeAll(this.childrenRemoveBuffer);
		return allchildren;
		
	}
	public void setParent(UIElement parent) { //should only be run once per thing
		this.parent = parent;
		this.origin = this.parent.getFinalLoc();
		if(parent.children.contains(this) == false && parent.childrenAddBuffer.contains(this) == false) {
			parent.childrenAddBuffer.add(this);
		}
	}
	public void addChild(UIElement child) {
		child.setParent(this);
	}
	/**
	 * Removes the parent of the UIElement, handles
	 * the removing of the connection between parent
	 * and child by removing itself from the parent's
	 * children and by removing its parent object
	 */
	public void removeParent() {
		if(this.parent != null) {
			this.origin = Point.clone(this.parent.getFinalLoc()); //Stops the following of the origin
			this.parent.childrenRemoveBuffer.add(this);
		}
		this.parent = null;
	}
	public void updateRelationships() {
		//this.origin = this.parent.getFinalLoc();
		/*
		for(UIElement u : this.children) {
			if(u.getRemove()) {
				this.childrenRemoveBuffer.add(u);
			}
		}
		*/
		if(this.parent != null) {
			if(this.parent.getRemove()) {
				this.setRemove(true);
			}
			this.origin = this.parent.getFinalLoc();
		}
		this.children.addAll(this.childrenAddBuffer);
		this.children.removeAll(this.childrenRemoveBuffer);
		this.childrenAddBuffer.clear();
		this.childrenRemoveBuffer.clear();
	}
	/**
	 * Removes a child of an UIElement
	 * 
	 * @param child The child to remove
	 */
	public void removeChild(UIElement child) {
		child.removeParent();
	}
	public void removeChildren() {
		for(UIElement u : this.getChildren()) {
			u.removeParent();
		}
	}
}
