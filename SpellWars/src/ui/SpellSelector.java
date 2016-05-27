package ui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import mechanic.GameMap;
import mechanic.Point;
import projectile.StunGrenade;
import spell.AreaGrab;
import spell.BouncingOrb;
import spell.ForgeSpirit;
import spell.HellRain;
import spell.Spell;
import spell.Stun;
import spell.TestFireball;
import spell.TimeBomb;
import unit.Player;

public class SpellSelector extends UIElement {
	public static final float WIDTH = 800;
	public static final float HEIGHT = 400;
	public static final Color SPELL_BACKGROUND_COLOR = new Color(40, 40, 80);
	public static final Point SPELL_SELECTOR_DIMENSIONS = new Point(4, 2);
	public static final Point SPELLS_DRAWLOC_TOPLEFT = new Point(64, 94);
	public static final Point SPELLS_DRAWLOC_BOTTOMRIGHT = new Point(256, 158);
	public static final Point SPELLS_SELECTED_DRAWLOC_TOP = new Point(352, 94);
	public static final Point SPELLS_SELECTED_DRAWLOC_TOP_BATTLE = new Point(64, 94);
	public static final float SPELLS_SELECTED_DRAW_HEIGHT = 198;
	public static final Point SPELL_ICON_DIMENSIONS = new Point(48, 48);
	public static final Point SPELL_NAME_LOC = new Point(400, 36);
	public static final Point SPELL_DESCRIPTION_LOC = new Point(412, 128);
	public static final int MAX_SELECTED_SPELLS = 4;
	public static final String SELECTOR_IMAGEPATH = "res/ui/gridSelect.png";
	char style;
	UIBox box;
	Point selectorLoc;
	Image selectorImage;
	ArrayList<Spell> availableSpells;
	ArrayList<Spell> selectedSpells; 
	Player player;
	boolean pickingPhase;
	Text spellName;
	Text spellDescription;
	public SpellSelector(UI ui, Point loc, char style, Player player) {
		super(ui, loc);
		this.setIsFront(true);
		this.availableSpells = new ArrayList<Spell>();
		this.selectedSpells = new ArrayList<Spell>();
		this.selectorLoc = new Point();
		this.style = style;
		UIBoxOrigin originStyle = null;
		if(style == GameMap.ID_LEFT) {
			originStyle = UIBoxOrigin.TOP_LEFT;
		} else if(style == GameMap.ID_RIGHT) {
			originStyle = UIBoxOrigin.TOP_RIGHT;
		}
		try {
			selectorImage = new Image("res/ui/gridSelect.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.box = new UIBox(ui, new Point(), WIDTH, HEIGHT, true, originStyle);
		this.addChild(box);
		this.player = player;
		this.refillSpells();
		this.player.setSpells(this.selectedSpells);
		//this.setPickingPhase(false);
		this.spellName = new Text(ui, Point.add(SPELL_NAME_LOC, this.box.getTopLeftLoc()), 400, 22, 40, 28, 42, new Color(255, 255, 235), "asd", TextFormat.LEFT_JUSTIFIED);
		this.spellName.setUseOutline(true);
		this.addChild(spellName);
		this.spellDescription = new Text(ui, Point.add(SPELL_DESCRIPTION_LOC, this.box.getTopLeftLoc()), 386, 10, 18, 16, 24, new Color(230, 230, 240), "asdf", TextFormat.LEFT_JUSTIFIED);
		this.spellDescription.setUseOutline(true);
		this.addChild(spellDescription);
	}
	public void updateText() {
		if(this.pickingPhase && this.getIndexAtLoc(this.selectorLoc) < this.availableSpells.size()) {
			Spell spell = this.availableSpells.get(this.getIndexAtLoc(this.selectorLoc));
			if(spell != null) {
				this.spellName.setText(spell.getName().toUpperCase());
				this.spellDescription.setText(spell.getDescription().toUpperCase());
			}
		} else {
			this.spellName.setText("");
			this.spellDescription.setText("");
		}
	}
	public void moveSelector(Point p) {
		this.selectorLoc.add(p);
		if(this.selectorLoc.x < 0) {
			this.selectorLoc.x = SPELL_SELECTOR_DIMENSIONS.x - 1;
		} else if(this.selectorLoc.x > SPELL_SELECTOR_DIMENSIONS.x - 1) {
			this.selectorLoc.x = 0;
		}
		if(this.selectorLoc.y < 0) {
			this.selectorLoc.y = SPELL_SELECTOR_DIMENSIONS.y - 1;
		} else if(this.selectorLoc.y > SPELL_SELECTOR_DIMENSIONS.y - 1) {
			this.selectorLoc.y = 0;
		}
		this.updateText();
	}
	public void setPickingPhase(boolean pickingPhase) {
		this.pickingPhase = pickingPhase;
		if(!pickingPhase){
			this.box.setImage("res/blank.png");
			this.box.setEdgeColor(new Color(0, 0, 0, 0));
			this.spellName.setText("");
			this.spellDescription.setText("");
		} else {
			this.box.setImage("res/ui/uibox_texture.png");
			this.box.setEdgeColor(new Color(125, 125, 200, 200));
			this.selectorLoc = new Point();
			this.refillSpells();
			this.updateText();
		}
	}
	public boolean getIsReady() {
		return !this.pickingPhase;
	}
	public void selectCurrentSpell() {
		if(this.pickingPhase) {
			this.selectSpell(this.getIndexAtLoc(this.selectorLoc));
			this.updateText();
		}
	}
	public void deselectSpell() {
		if(this.pickingPhase) {
			if(this.selectedSpells.size() >= 1) {
				int index = this.selectedSpells.size()-1;
				this.availableSpells.add(this.selectedSpells.get(index));
				this.selectedSpells.remove(index);
			}
		}
	}
	public void removeSelectedSpells() {
		this.selectedSpells.clear();
	}
	public void selectSpell(int index) {
		if(this.pickingPhase && this.selectedSpells.size() < MAX_SELECTED_SPELLS) {
			if(index < this.availableSpells.size()) {
				this.selectedSpells.add(this.availableSpells.get(index));
				this.availableSpells.remove(index);
			}
		}
	}
	/*
	 *USELESS FUNCTION 
	 */
	public void sendSpells() {
		this.player.setSpells(this.selectedSpells);
	} 
	public int getIndexAtLoc(Point p) {
		return (int) (p.y * SPELL_SELECTOR_DIMENSIONS.x + p.x);
	}
	public Point getLocAtIndex(int index) {
		return new Point(index%4, (int)(index/4));
	}
	public void refillSpells() {
		for(int i = this.availableSpells.size(); i < SPELL_SELECTOR_DIMENSIONS.x * SPELL_SELECTOR_DIMENSIONS.y; i++) {
			this.availableSpells.add(this.getRandomSpell());
		}
	}
	public Spell getRandomSpell() {
		int numSpells = 7;
		int num = (int)(Math.pow(Math.random(), 0.8) * numSpells); //weighted so that spells under a bigger num get picked more
		switch(num) {
		case 0:
			return new AreaGrab(this.player);
		case 1:
			return new TimeBomb(this.player);
		case 2:
			return new ForgeSpirit(this.player);
		case 3:
			return new Stun(this.player);
		case 4:
			return new HellRain(this.player);
		case 5:
			return new BouncingOrb(this.player);
		case 6:
			return new TestFireball(this.player);
		default:
			return new TestFireball(this.player);
		}
	}
	@Override
	public void draw(Graphics g) {
		//for the available spells, spacing between each icon
		float horizontalSpacing = (float)((SPELLS_DRAWLOC_BOTTOMRIGHT.x - SPELLS_DRAWLOC_TOPLEFT.x) /  (SPELL_SELECTOR_DIMENSIONS.x - 1));
		float verticalSpacing = (float)((SPELLS_DRAWLOC_BOTTOMRIGHT.y - SPELLS_DRAWLOC_TOPLEFT.y) / (SPELL_SELECTOR_DIMENSIONS.y - 1));
		if(this.pickingPhase) {
			for(int i = 0; i < SPELL_SELECTOR_DIMENSIONS.x * SPELL_SELECTOR_DIMENSIONS.y ; i++) {
				//the point to draw the spell icon
				Point spellDrawLoc = Point.add(Point.add(SPELLS_DRAWLOC_TOPLEFT, Point.add(this.getLoc(), this.box.getTopLeftLoc())), new Point(horizontalSpacing * this.getLocAtIndex(i).x, verticalSpacing * this.getLocAtIndex(i).y));
				//fills in a background for the spell icon
				g.setColor(SPELL_BACKGROUND_COLOR);
				Rectangle rect = new Rectangle((float)(spellDrawLoc.x - SPELL_ICON_DIMENSIONS.x), (float)(spellDrawLoc.y - SPELL_ICON_DIMENSIONS.y), (float)SPELL_ICON_DIMENSIONS.x, (float)SPELL_ICON_DIMENSIONS.y);
				g.fill(rect);
				if(i < this.availableSpells.size()) {
					//draws a spell icon if there is a spell
					g.drawImage(this.availableSpells.get(i).getImage().getScaledCopy((int)SPELL_ICON_DIMENSIONS.x, (int)SPELL_ICON_DIMENSIONS.y), (float)(spellDrawLoc.x - SPELL_ICON_DIMENSIONS.x), (float)(spellDrawLoc.y - SPELL_ICON_DIMENSIONS.y));
				}
			}
		}
		for(int i = 0; i < MAX_SELECTED_SPELLS; i++){
			Point localDrawLoc = new Point();
			if(this.pickingPhase) {
				localDrawLoc = Point.clone(SPELLS_SELECTED_DRAWLOC_TOP);
			} else {
				localDrawLoc = Point.clone(SPELLS_SELECTED_DRAWLOC_TOP_BATTLE);
				if(this.style == GameMap.ID_RIGHT) {
					localDrawLoc.x = -localDrawLoc.x;
				}
			}
			Point spellDrawLoc = Point.add(Point.add(localDrawLoc, Point.add(this.getLoc(), this.box.getTopLeftLoc())), new Point(0, SPELLS_SELECTED_DRAW_HEIGHT * i/(MAX_SELECTED_SPELLS-1)));
			if(this.style == GameMap.ID_RIGHT && !this.pickingPhase) {
				spellDrawLoc = Point.add(Point.add(localDrawLoc, Point.add(this.getLoc(), this.box.getTopRightLoc())), new Point(0, SPELLS_SELECTED_DRAW_HEIGHT * i/(MAX_SELECTED_SPELLS-1)));
			}
			g.setColor(SPELL_BACKGROUND_COLOR);
			Rectangle rect = new Rectangle((float)(spellDrawLoc.x - SPELL_ICON_DIMENSIONS.x), (float)(spellDrawLoc.y - SPELL_ICON_DIMENSIONS.y), (float)SPELL_ICON_DIMENSIONS.x, (float)SPELL_ICON_DIMENSIONS.y);
			g.fill(rect);
			if(i < this.selectedSpells.size()) {
				//draws a spell icon if there is a spell
				g.drawImage(this.selectedSpells.get(i).getImage().getScaledCopy((int)SPELL_ICON_DIMENSIONS.x, (int)SPELL_ICON_DIMENSIONS.y), (float)(spellDrawLoc.x - SPELL_ICON_DIMENSIONS.x), (float)(spellDrawLoc.y - SPELL_ICON_DIMENSIONS.y));
			}
		}
		if(this.pickingPhase) {
			Point selectorDrawLoc = Point.add(Point.add(SPELLS_DRAWLOC_TOPLEFT, Point.add(this.getLoc(), this.box.getTopLeftLoc())), new Point(horizontalSpacing * selectorLoc.x, verticalSpacing * selectorLoc.y));
			if(this.selectedSpells.size() == MAX_SELECTED_SPELLS || this.getIndexAtLoc(this.selectorLoc) >= this.availableSpells.size()) {
				g.drawImage(selectorImage.getScaledCopy((int)SPELL_ICON_DIMENSIONS.x, (int)SPELL_ICON_DIMENSIONS.y), (float)(selectorDrawLoc.x - SPELL_ICON_DIMENSIONS.x), (float)(selectorDrawLoc.y - SPELL_ICON_DIMENSIONS.y), Color.gray);
			} else {
				g.drawImage(selectorImage.getScaledCopy((int)SPELL_ICON_DIMENSIONS.x, (int)SPELL_ICON_DIMENSIONS.y), (float)(selectorDrawLoc.x - SPELL_ICON_DIMENSIONS.x), (float)(selectorDrawLoc.y - SPELL_ICON_DIMENSIONS.y));
			}
		}
	}
}
