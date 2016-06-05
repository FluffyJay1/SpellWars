package ui;

import org.newdawn.slick.Color;

import mechanic.PlayerType;
import mechanic.Point;

public class PlayerTypeSelector extends UIBox {
	public static final float WIDTH = 400;
	public static final float HEIGHT = 300;
	public static final Point MARGIN = new Point(4, 5);
	int index;
	public int level;
	Text text;
	public PlayerTypeSelector(UI ui, Point loc) {
		super(ui, loc, WIDTH, HEIGHT, true, UIBoxOrigin.CENTER);
		this.index = 0;
		this.level = 1;
		this.text = new Text(ui, Point.add(new Point(-WIDTH/2, -HEIGHT/2), MARGIN), (int)(WIDTH - MARGIN.x * 2), 12, 18, 14, 22, Color.white, "player", TextFormat.CENTER_JUSTIFIED);
		this.text.setIsFront(true);
		this.addChild(text);
		ui.addUIElement(text);
		//this.setImage("res/blank.png");
	}
	public void changeLevel(int i) {
		if(this.index != 0) {
			this.level += i;
			if(this.level < 1) {
				this.level = 1;
			}
		}
	}
	public void changeIndex(int i) {
		this.index += i;
		if(this.index >= PlayerType.values().length) {
			this.index = index % PlayerType.values().length;
		} else if(this.index < 0) {
			this.index = PlayerType.values().length + index % PlayerType.values().length;
		}
	}
	public PlayerType getPlayerType() {
		return PlayerType.values()[index];
	}
	@Override
	public void update() {
		this.text.text = "";
		for(int i = 0; i < PlayerType.values().length; i++) {
			if(this.index == i) {
				this.text.text += PlayerType.values()[i].toString().toUpperCase();
				if(this.index != 0) {
					this.text.text += " " + this.level;
				}
			} else {
				this.text.text += PlayerType.values()[i].toString().toLowerCase();
			}
			if(i != PlayerType.values().length - 1) {
				this.text.text += "|";
			}
		}
	}
}
