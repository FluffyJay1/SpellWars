package ui;

import mechanic.Point;
/*
 * DESCRIPTION
 * 
 * a decorative sparkle that emits when the start button is hovered over
 */
public class StartSparkle extends UIElement {
	Point destination;
	public StartSparkle(UI ui, Point loc, String imagePath) {
		super(ui, loc);
		this.destination = loc;
		this.setImage(imagePath);
	}
	@Override
	public void onClick() {
		if(this.pointIsInHitbox(this.getUI().getMouseLoc())) {
			System.out.println("BUTTON HAS BEEN PRESSED");
		}
	}
	@Override
	public void update() {
		float speed = (float)Point.getDistance(this.loc, destination)/2;
		if(this.isReasonableDistanceAwayFrom(destination, speed) == false) {
			this.moveTowardPoint(destination, speed);
		}
	}
}
