package ui;

import mechanic.Point;

public class MultiplayerButton extends UIElement {
	boolean mouseIsInside;
	public boolean isPressed;
	UIElement test1, test2, test3, test4;
	public HostServerButton hostServerButton;
	public ConnectToServerButton connectToServerButton;
	public MultiplayerButton(UI ui, Point loc) {
		super(ui, loc);
		this.setImage("res/MultiplayerButton.png");
		isPressed = false;
		test1 = new StartSparkle(this.getUI(), new Point(132, -66), "res/particle_heal.png");
		test1.setParent(this);
		test2 = new StartSparkle(this.getUI(), new Point(132, 66), "res/particle_heal.png");
		test2.setParent(this);
		test3 = new StartSparkle(this.getUI(), new Point(-132, -66), "res/particle_heal.png");
		test3.setParent(this);
		test4 = new StartSparkle(this.getUI(), new Point(-132, 66), "res/particle_heal.png");
		test4.setParent(this);
		hostServerButton = new HostServerButton(ui, new Point(0, -66));
		hostServerButton.setParent(this);
		connectToServerButton = new ConnectToServerButton(ui, new Point(0, 66));
		connectToServerButton.setParent(this);
	}
	@Override
	public void onClick() {
		if(this.pointIsInHitbox(this.getUI().getMouseLoc())) {
			System.out.println("MULTIPLAYER BUTTON HAS BEEN PRESSED");
			isPressed = true;
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
				this.setSize(2.5);
				this.setAlpha(0);
				for(UIElement u : this.getChildren()) {
					//u.setRemove(false); already done in ui
					u.changeLoc(new Point(0,0));
					this.getUI().addUIElement(u);
				}
			}
		} else {
			this.setSize(1);
			this.setAlpha(1);
			for(UIElement u : this.getChildren()) {
				u.setRemove(true);
			}
		}
	}
}
