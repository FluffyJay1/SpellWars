package ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import mechanic.Game;
import mechanic.GameMap;
import mechanic.Point;
import spell.Spell;
import states.StateGame;

public class FieldEffectDisplay extends UIElement {
	public static final int ACTIVE_ICON_SIZE = 100;
	public static final int INACTIVE_ICON_SIZE = 50;
	public static final float EFFECT_SPACING = 100;
	public static final Color RED_TEAM = new Color(255, 120, 120);
	public static final Color BLUE_TEAM = new Color(120, 120, 255);
	public static final Color INACTIVE_ICON_COLOR = new Color(120, 120, 120);
	static Image borderImage;
	public FieldEffectDisplay(UI ui) {
		super(ui, new Point(Game.WINDOW_WIDTH/2, 128));
		borderImage = Game.getImage("res/ui/gridSelect.png");
	}
	@Override
	public void draw(Graphics g) {
		for(int i = 0; i < this.getMap().getFieldEffects().size(); i++) {
			Color c = Color.white;
			if(this.getMap().hasFieldEffects()) {
				if(this.getMap().getFieldEffects().get(i).getOwner().teamID == GameMap.ID_LEFT) {
					c = RED_TEAM;
				} else {
					c = BLUE_TEAM;
				}
			}
			Point drawLoc = new Point(this.getFinalLoc().x, this.getFinalLoc().y + i * EFFECT_SPACING);
			if(i == 0) {
				g.drawImage(this.getMap().getFieldEffects().get(i).getImage().getScaledCopy(ACTIVE_ICON_SIZE, ACTIVE_ICON_SIZE), (float)(drawLoc.x - ACTIVE_ICON_SIZE/2), (float)(drawLoc.y - ACTIVE_ICON_SIZE/2));
				if(StateGame.isServer)
				this.getUI().getMap().addToDrawInfo(GameMap.getDrawDataI(this.getMap().getFieldEffects().get(i).getImagePath(), drawLoc.x - ACTIVE_ICON_SIZE/2, drawLoc.y - ACTIVE_ICON_SIZE/2, ACTIVE_ICON_SIZE, ACTIVE_ICON_SIZE, 0, 255, 255, 255, 255, 0));
				g.drawImage(borderImage.getScaledCopy(ACTIVE_ICON_SIZE, ACTIVE_ICON_SIZE), (float)(drawLoc.x - ACTIVE_ICON_SIZE/2), (float)(drawLoc.y - ACTIVE_ICON_SIZE/2), c);
				if(StateGame.isServer)
				this.getUI().getMap().addToDrawInfo(GameMap.getDrawDataI("res/ui/gridSelect.png", drawLoc.x - ACTIVE_ICON_SIZE/2, drawLoc.y - ACTIVE_ICON_SIZE/2, ACTIVE_ICON_SIZE, ACTIVE_ICON_SIZE, 0, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha(), 0));
			} else {
				g.drawImage(this.getMap().getFieldEffects().get(i).getImage().getScaledCopy(INACTIVE_ICON_SIZE, INACTIVE_ICON_SIZE), (float)(drawLoc.x - INACTIVE_ICON_SIZE/2), (float)(drawLoc.y - INACTIVE_ICON_SIZE/2), INACTIVE_ICON_COLOR);
				if(StateGame.isServer)
				this.getUI().getMap().addToDrawInfo(GameMap.getDrawDataI(this.getMap().getFieldEffects().get(i).getImagePath(), drawLoc.x - INACTIVE_ICON_SIZE/2, drawLoc.y - INACTIVE_ICON_SIZE/2, INACTIVE_ICON_SIZE, INACTIVE_ICON_SIZE, 0, INACTIVE_ICON_COLOR.getRed(), INACTIVE_ICON_COLOR.getGreen(), INACTIVE_ICON_COLOR.getBlue(), INACTIVE_ICON_COLOR.getAlpha(), 0));
				g.drawImage(borderImage.getScaledCopy(INACTIVE_ICON_SIZE, INACTIVE_ICON_SIZE), (float)(drawLoc.x - INACTIVE_ICON_SIZE/2), (float)(drawLoc.y - INACTIVE_ICON_SIZE/2), c);
				if(StateGame.isServer)
				this.getUI().getMap().addToDrawInfo(GameMap.getDrawDataI("res/ui/gridSelect.png", drawLoc.x - INACTIVE_ICON_SIZE/2, drawLoc.y - INACTIVE_ICON_SIZE/2, INACTIVE_ICON_SIZE, INACTIVE_ICON_SIZE, 0, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha(), 0));
			}
		}		
	}
}
