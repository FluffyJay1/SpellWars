package ui;

import mechanic.Point;
/*
 * DESCRIPTION
 * 
 * a button to start the game
 */
public class StartButton extends UIElement {
	boolean mouseIsInside;
	public boolean isPressed;
	UIElement test1, test2, test3, test4;
	public StartButton(UI ui, Point loc) {
		super(ui, loc);
		this.setImage("res/StartButton.png");
		isPressed = false;
		test1 = new StartSparkle(this.getUI(), new Point(132, -66), "res/particle_genericGreen.png");
		test1.setParent(this);
		test2 = new StartSparkle(this.getUI(), new Point(132, 66), "res/particle_genericGreen.png");
		test2.setParent(this);
		test3 = new StartSparkle(this.getUI(), new Point(-132, -66), "res/particle_genericGreen.png");
		test3.setParent(this);
		test4 = new StartSparkle(this.getUI(), new Point(-132, 66), "res/particle_genericGreen.png");
		test4.setParent(this);
	}
	@Override
	public void onClick() {
		if(this.pointIsInHitbox(this.getUI().getMouseLoc())) {
			System.out.println("BUTTON HAS BEEN PRESSED");
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
	}
}
