package ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.opengl.Texture;

import mechanic.Point;
/*
 * DESCRIPTION
 * 
 * acts as a little window (such as for menus or options)
 * and you can always find the topleft point
 */
public class UIBox extends UIElement {
	boolean hasEdge;
	Color edgeColor;
	Color bodyColor;
	UIBoxOrigin originMethod;
	Point topLeftLoc;
	float opacity;
	public UIBox(UI ui, Point loc, float width, float height, String texture, boolean hasEdge, UIBoxOrigin originMethod) {
		super(ui, width, height, loc);
		this.setBodyColor(new Color(160, 202, 255));
		this.setEdgeColor(new Color(50, 70, 100));
		this.setImage(texture);
		this.hasEdge = hasEdge;
		this.originMethod = originMethod;
		topLeftLoc = new Point();
	}
	public UIBox(UI ui, Point loc, float width, float height, boolean hasEdge, UIBoxOrigin originMethod) {
		this(ui, loc, width, height, "res/ui/uibox_texture.png", hasEdge, originMethod);
	}
	public UIBox(UI ui, Point loc, boolean hasEdge, UIBoxOrigin originMethod) {
		this(ui, loc, 0, 0, hasEdge, originMethod);
	}
	public UIBox(UI ui, Point loc, UIBoxOrigin originMethod) {
		this(ui, loc, true, originMethod);
	}
	@Override
	public void draw(Graphics g) {
		Point drawTopLeft = new Point();
		switch(originMethod) {
		case TOP_LEFT:
			drawTopLeft = this.getFinalLoc();
			break;
		case TOP_CENTER:
			drawTopLeft = new Point(this.getFinalLoc().getX() - this.getWidth()/2, this.getFinalLoc().getY());
			break;
		case TOP_RIGHT:
			drawTopLeft = new Point(this.getFinalLoc().getX() - this.getWidth(), this.getFinalLoc().getY());
			break;
		case CENTER_LEFT:
			drawTopLeft = new Point(this.getFinalLoc().getX(), this.getFinalLoc().getY() - this.getHeight()/2);
			break;
		case CENTER:
			drawTopLeft = new Point(this.getFinalLoc().getX() - this.getWidth()/2, this.getFinalLoc().getY() - this.getHeight()/2);
			break;
		case CENTER_RIGHT:
			drawTopLeft = new Point(this.getFinalLoc().getX() - this.getWidth(), this.getFinalLoc().getY() - this.getHeight()/2);
			break;
		case BOTTOM_LEFT:
			drawTopLeft = new Point(this.getFinalLoc().getX(), this.getFinalLoc().getY() - this.getHeight());
			break;
		case BOTTOM_CENTER:
			drawTopLeft = new Point(this.getFinalLoc().getX() - this.getWidth()/2, this.getFinalLoc().getY() - this.getHeight());
			break;
		case BOTTOM_RIGHT:
			drawTopLeft = new Point(this.getFinalLoc().getX() - this.getWidth(), this.getFinalLoc().getY() - this.getHeight());
			break;
		default:
			break;
		}
		//Shape rectangle = new Rectangle((float)drawTopLeft.getX(), (float)drawTopLeft.getY(), this.getWidth(), this.getHeight());
		Image texture = this.getImage().getScaledCopy((int)this.getWidth(), (int)this.getHeight());
		texture.setAlpha(this.getAlpha());
		g.drawImage(texture, (float)drawTopLeft.getX(), (float)drawTopLeft.getY(), this.bodyColor);
		//g.texture(rectangle, texture);
		if(this.hasEdge) {
			g.setColor(this.edgeColor);
			g.drawRect((float)drawTopLeft.getX(), (float)drawTopLeft.getY(), this.getWidth(), this.getHeight());
		}
	}
	public void setEdgeColor(Color color) {
		this.edgeColor = color;
	}
	public Color getEdgeColor() {
		return this.edgeColor;
	}
	public void setBodyColor(Color color) {
		this.bodyColor = color;
	}
	public Color getBodyColor(Color color) {
		return this.bodyColor;
	}
	public void setOriginMethod(UIBoxOrigin originMethod) {
		this.originMethod = originMethod;
	}
	public Point getTopLeftLoc() {
		switch(originMethod) {
		case TOP_LEFT:
			return Point.clone(this.getLoc());
		case TOP_CENTER:
			return new Point(this.getLoc().getX() - this.getWidth()/2, this.getLoc().getY());
		case TOP_RIGHT:
			return new Point(this.getLoc().getX() - this.getWidth(), this.getLoc().getY());
		case CENTER_LEFT:
			return new Point(this.getLoc().getX(), this.getLoc().getY() - this.getHeight()/2);
		case CENTER:
			return new Point(this.getLoc().getX() - this.getWidth()/2, this.getLoc().getY() - this.getHeight()/2);
		case CENTER_RIGHT:
			return new Point(this.getLoc().getX() - this.getWidth(), this.getLoc().getY() - this.getHeight()/2);
		case BOTTOM_LEFT:
			return new Point(this.getLoc().getX(), this.getLoc().getY() - this.getHeight());
		case BOTTOM_CENTER:
			return new Point(this.getLoc().getX() - this.getWidth()/2, this.getLoc().getY() - this.getHeight());
		case BOTTOM_RIGHT:
			return new Point(this.getLoc().getX() - this.getWidth(), this.getLoc().getY() - this.getHeight());
		default:
			return Point.clone(this.getLoc());
		}
	}
	public Point getTopCenterLoc() {
		return Point.add(this.getTopLeftLoc(), new Point(this.getWidth()/2, 0));
	}
	public Point getTopRightLoc() {
		return Point.add(this.getTopLeftLoc(), new Point(this.getWidth(), 0));
	}
	public Point getCenterLeftLoc() {
		return Point.add(this.getTopLeftLoc(), new Point(0, this.getHeight()/2));
	}
	public Point getCenterLoc() {
		return Point.add(this.getTopLeftLoc(), new Point(this.getWidth()/2, this.getHeight()/2));
	}
	public Point getCenterRightLoc() {
		return Point.add(this.getTopLeftLoc(), new Point(this.getWidth(), this.getHeight()/2));
	}
	public Point getBottomLeftLoc() {
		return Point.add(this.getTopLeftLoc(), new Point(0, this.getHeight()));
	}
	public Point getBottomCenterLoc() {
		return Point.add(this.getTopLeftLoc(), new Point(this.getWidth()/2, this.getHeight()));
	}
	public Point getBottomRightLoc() {
		return Point.add(this.getTopLeftLoc(), new Point(this.getWidth(), this.getHeight()));
	}
	@Override
	public void fitOnScreen(){
		if(Point.add(this.getOrigin(), this.getCenterLoc()).getX() - this.getWidth()/2 < 0) {
			this.loc.add(new Point(-(Point.add(this.getOrigin(), this.getCenterLoc()).getX() - this.getWidth()/2), 0));
		} 
		if(Point.add(this.getOrigin(), this.getCenterLoc()).getX() + this.getWidth()/2 > 800) { //TODO CHANGE THIS IF WINDOW DIMENSIONS CHANGE
			this.loc.add(new Point(800 - (Point.add(this.getOrigin(), this.getCenterLoc()).getX() + this.getWidth()/2), 0));
		}
		if(Point.add(this.getOrigin(), this.getCenterLoc()).getY() - this.getHeight()/2 < 0) {
			this.loc.add(new Point(0, -(Point.add(this.getOrigin(), this.getCenterLoc()).getY() - this.getHeight()/2)));
		}
		if(Point.add(this.getOrigin(), this.getCenterLoc()).getY() + this.getHeight()/2 > 800) { //TODO CHANGE THIS IF WINDOW DIMENSIONS CHANGE
			this.loc.add(new Point(0, 800 - (Point.add(this.getOrigin(), this.getCenterLoc()).getY() + this.getHeight()/2)));
		}
	}
	@Override
	public boolean pointIsInHitbox(Point loc) {
		return loc.getX() >= Point.add(this.getOrigin(), this.getCenterLoc()).getX() - this.getWidth()/2
				&& loc.getX() <= Point.add(this.getOrigin(), this.getCenterLoc()).getX() + this.getWidth()/2
				&& loc.getY() >= Point.add(this.getOrigin(), this.getCenterLoc()).getY() - this.getHeight()/2
				&& loc.getY() <= Point.add(this.getOrigin(), this.getCenterLoc()).getY() + this.getHeight()/2;
	}
}
