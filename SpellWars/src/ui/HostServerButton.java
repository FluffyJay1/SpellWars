package ui;

import mechanic.Point;

public class HostServerButton extends UIElement {
	boolean mouseIsInside;
	public boolean isPressed;
	UIElement test1, test2, test3, test4;
	Point destination;
	public HostServerButton(UI ui, Point loc) {
		super(ui, loc);
		this.destination = loc;
		this.setImage("res/HostServer.png");
		isPressed = false;
		test1 = new StartSparkle(this.getUI(), new Point(132, -66), "res/particle_genericYellow.png");
		test1.setParent(this);
		test2 = new StartSparkle(this.getUI(), new Point(132, 66), "res/particle_genericYellow.png");
		test2.setParent(this);
		test3 = new StartSparkle(this.getUI(), new Point(-132, -66), "res/particle_genericYellow.png");
		test3.setParent(this);
		test4 = new StartSparkle(this.getUI(), new Point(-132, 66), "res/particle_genericYellow.png");
		test4.setParent(this);
	}
	@Override
	public void onClick() {
		if(this.pointIsInHitbox(this.getUI().getMouseLoc())) {
			System.out.println("HOST SERVER BUTTON HAS BEEN PRESSED");
			isPressed = true;
			this.setRemove(true);
		}
	}
	@Override
	public void update() {
		if(this.pointIsInHitbox(this.getUI().getMouseLoc())) {
			mouseIsInside = true;
		} else {
			mouseIsInside = false;
		}
		//test.changeOrigin(this.getUI().getMouseLoc());
		if(mouseIsInside) {
			if(test1.getRemove()) {
				this.setSize(1.1);
				for(UIElement u : this.getChildren()) {
					//u.setRemove(false); already done in ui
					u.changeLoc(new Point(0,0));
					this.getUI().addUIElement(u);
				}
			}
		} else {
			this.setSize(1);
			for(UIElement u : this.getChildren()) {
				u.setRemove(true);
			}
		}
		float speed = (float)Point.getDistance(this.loc, destination)/2;
		if(this.isReasonableDistanceAwayFrom(destination, speed) == false) {
			this.moveTowardPoint(destination, speed);
		}
	}
}
