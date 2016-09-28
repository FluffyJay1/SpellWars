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
import spell.Blizzard;
import spell.BouncingOrb;
import spell.DamageAmp;
import spell.ForgeSpirit;
import spell.ForgeSpiritFire;
import spell.HellRain;
import spell.LavaToss;
import spell.MudToss;
import spell.MysteryBox;
import spell.Omnislash;
import spell.PistolShot;
import spell.RechargingBarrier;
import spell.ReflectBarrier;
import spell.Regenerate;
import spell.Spell;
import spell.Stun;
import spell.TestFireball;
import spell.TimeBomb;
import spell.TimeDilation;
import spell.TrumpWall;
import spell.VacuumCannon;
import spell.WindCannon;
import states.StateGame;
import unit.Player;
import unit.Unit;

public class SpellSelector extends UIElement {
	public static final float WIDTH = 800; //originally 800x400
	public static final float HEIGHT = 400;
	public static final Color SPELL_BACKGROUND_COLOR = new Color(40, 40, 80);
	public static final Point SPELL_SELECTOR_DIMENSIONS = new Point(4, 2); //originally 4,2
	public static final Point SPELLS_DRAWLOC_TOPLEFT = new Point(64, 94); //originally 64,94
	public static final Point SPELLS_DRAWLOC_BOTTOMRIGHT = new Point(256, 158); //originally 256,158
	public static final Point HIGHLIGHTED_SPELL_DRAWLOC = new Point(20, 180); //originally 20, 180
	public static final Point HIGHLIGHTED_SPELL_DIMENSIONS = new Point(200, 200);
	public static final Point SPELLS_SELECTED_DRAWLOC_TOP = new Point(352, 94);
	public static final Point SPELLS_SELECTED_DRAWLOC_TOP_BATTLE = new Point(64, 94);
	public static final float SPELLS_SELECTED_DRAW_HEIGHT = 262; //originally 198
	public static final Point SPELL_ICON_DIMENSIONS = new Point(48, 48); //originally 48x48
	public static final Point SPELL_NAME_LOC = new Point(400, 36); //originally 400, 36
	public static final Point SPELL_DESCRIPTION_LOC = new Point(412, 128); //originally 412, 128
	public static final Point SPELL_SELECT_HELP_LOC = new Point(400, 350); //originally 400, 350
	public static final int MAX_SELECTED_SPELLS = 4;
	public static final String SELECTOR_IMAGEPATH = "res/ui/gridSelect.png";
	public static final float SPELL_CAST_HELP_OFFSET = 10;
	char style;
	UIBox box;
	Point selectorLoc;
	Image selectorImage;
	ArrayList<Spell> availableSpells;
	ArrayList<Spell> selectedSpells; 
	Player player;
	boolean pickingPhase;
	
	boolean hasCastFirstSpell;
	int prevNumSelectedSpells;
	
	Text spellName;
	Text spellDescription;
	Text spellSelectHelp;
	Text spellCastHelp;
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
		this.spellSelectHelp = new Text(ui, Point.add(SPELL_SELECT_HELP_LOC, this.box.getTopLeftLoc()), 386, 12, 14, 13, 15, new Color(230, 230, 240), "", TextFormat.LEFT_JUSTIFIED);
		this.spellSelectHelp.setUseOutline(true);
		this.addChild(spellSelectHelp);
		this.spellCastHelp = new Text(ui, new Point(SPELLS_SELECTED_DRAWLOC_TOP_BATTLE.x + SPELL_CAST_HELP_OFFSET, SPELLS_SELECTED_DRAWLOC_TOP_BATTLE.y), 386, 12, 14, 13, 15, new Color(230, 230, 240), "", TextFormat.LEFT_JUSTIFIED);
		this.addChild(spellCastHelp);
		if(style == GameMap.ID_LEFT) {
			this.spellSelectHelp.setText("WASD to move, E to select, Q to deselect, T to finish selecting");
		} else if(style == GameMap.ID_RIGHT) {
			this.spellSelectHelp.setText("arrows to move, (.) to select, (/) to deselect, L to finish selecting");
		}
		
		this.hasCastFirstSpell = false;
		this.prevNumSelectedSpells = 0;
	}
	@Override
	public void update() {
		if(!this.hasCastFirstSpell && this.prevNumSelectedSpells > this.selectedSpells.size() && this.prevNumSelectedSpells != 0) {
			this.spellCastHelp.setRemove(true);
			this.hasCastFirstSpell = true;
		} else {
			this.prevNumSelectedSpells = this.selectedSpells.size();
		}
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
		this.pickingPhase = !(!pickingPhase || this.player.getRemove());
		if(!pickingPhase || this.player.getRemove()){
			this.box.setImage("res/blank.png");
			this.box.setEdgeColor(new Color(0, 0, 0, 0));
			this.spellName.setText("");
			this.spellDescription.setText("");
			this.spellSelectHelp.setRemove(true);
			if(!this.player.getRemove() && this.selectedSpells.size() > 0) {
				if(this.style == GameMap.ID_LEFT) {
					this.spellCastHelp.setText("press (E) to cast!");
				} else {
					this.spellCastHelp.changeLoc(new Point(-SPELLS_SELECTED_DRAWLOC_TOP_BATTLE.x - 384 -SPELL_CAST_HELP_OFFSET, SPELLS_SELECTED_DRAWLOC_TOP_BATTLE.y));
					this.spellCastHelp.format = TextFormat.RIGHT_JUSTIFIED;
					this.spellCastHelp.setText("press (.) to cast!");
				}
			}
		} else {
			this.box.setImage("res/ui/uibox_texture.png");
			this.box.setEdgeColor(new Color(125, 125, 200, 200));
			this.selectorLoc = new Point();
			if(this.spellSelectHelp.getRemove()) {
				this.getUI().addUIElement(this.spellSelectHelp);
			}
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
			this.updateText();
		}
		this.prevNumSelectedSpells = this.selectedSpells.size();
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
		return new Point(index%SPELL_SELECTOR_DIMENSIONS.x, (int)(index/SPELL_SELECTOR_DIMENSIONS.x));
	}
	public void refillSpells() {
		for(int i = this.availableSpells.size(); i < SPELL_SELECTOR_DIMENSIONS.x * SPELL_SELECTOR_DIMENSIONS.y; i++) {
			this.availableSpells.add(getRandomSpell(this.player));
		}
	}
	public static Spell getRandomSpell(Unit unit) {
		Spell[] spells = {new TrumpWall(unit),
				new ReflectBarrier(unit),
				new AreaGrab(unit),
				new RechargingBarrier(unit),
				new TimeBomb(unit),
				new ForgeSpirit(unit),
				new HellRain(unit),
				new Stun(unit),
				new BouncingOrb(unit),
				new WindCannon(unit),
				new TestFireball(unit),
				new PistolShot(unit),
				new Blizzard(unit),
				new Regenerate(unit),
				new DamageAmp(unit),
				new Omnislash(unit),
				new TimeDilation(unit),
				new VacuumCannon(unit),
				new MysteryBox(unit),
				new MudToss(unit),
				new LavaToss(unit)
		};
		double[] weights = {0.45, //TRUMP WALL
				0.45, //reflect barrier
				0.55, //area grab
				0.5, //recharging barrier
				0.7, //time bomb
				1.2, //forge spirit
				0.7, //hell rain
				1.0, //stun
				1.3, //bouncing orb
				1.0, //wind cannon
				1.65, //firebreath
				1.8, //pistol shot
				0.45, //blizzard
				0.65, //regenerate
				0.5, //damage amp
				0.4, //omnislash
				0.45, //time dilation
				0.75, //vacuum cannon
				0.4, //mystery box
				0.4, //mud grenade
				0.35 //lava grenade
		};
		if(spells.length != weights.length) {
			System.out.println("WARNING: SPELLS AND WEIGHTS MISMATCH");
			System.out.println("size of spells: " + spells.length + ". size of weights: " + weights.length);
		}
		double totalWeight = 0;
		for(int i = 0; i < weights.length; i++) {
			totalWeight += weights[i];
		}
		double random = Math.random() * totalWeight;
		double runningTotal = 0;
		for(int i = 0; i < weights.length; i++) {
			if(random < weights[i] + runningTotal) {
				return spells[i];
			} else {
				runningTotal += weights[i];
			}
		}
		return new TestFireball(unit);
		/* OLD SYSTEM
		int num = (int)(Math.pow(Math.random(), 0.7) * numSpells); //weighted so that spells under a bigger num get picked more
		switch(num) {
		case 0:
			return new TrumpWall(unit);
		case 1:
			return new ReflectBarrier(unit);
		case 2:
			return new AreaGrab(unit);
		case 3:
			return new RechargingBarrier(unit);
		case 4:
			return new TimeBomb(unit);
		case 5:
			return new ForgeSpirit(unit);
		case 6:
			return new HellRain(unit);
		case 7:
			return new Stun(unit);
		case 8:
			return new BouncingOrb(unit);
		case 9:
			return new WindCannon(unit);
		case 10:
			return new TestFireball(unit);
		case 11:
			return new PistolShot(unit);
		default:
			return new TestFireball(unit);
		}
		*/
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
				if(StateGame.isServer)
				this.getMap().addToDrawInfo(GameMap.getDrawDataR(spellDrawLoc.x - SPELL_ICON_DIMENSIONS.x, spellDrawLoc.y - SPELL_ICON_DIMENSIONS.y, SPELL_ICON_DIMENSIONS.x, SPELL_ICON_DIMENSIONS.y, SPELL_BACKGROUND_COLOR.getRedByte(), SPELL_BACKGROUND_COLOR.getGreenByte(), SPELL_BACKGROUND_COLOR.getBlueByte(), SPELL_BACKGROUND_COLOR.getAlphaByte()));
				if(i < this.availableSpells.size()) {
					//draws a spell icon if there is a spell
					g.drawImage(this.availableSpells.get(i).getImage().getScaledCopy((int)SPELL_ICON_DIMENSIONS.x, (int)SPELL_ICON_DIMENSIONS.y), (float)(spellDrawLoc.x - SPELL_ICON_DIMENSIONS.x), (float)(spellDrawLoc.y - SPELL_ICON_DIMENSIONS.y));
					if(StateGame.isServer)
					this.getMap().addToDrawInfo(GameMap.getDrawDataI(this.availableSpells.get(i).getImagePath(), spellDrawLoc.x - SPELL_ICON_DIMENSIONS.x, spellDrawLoc.y - SPELL_ICON_DIMENSIONS.y, SPELL_ICON_DIMENSIONS.x, SPELL_ICON_DIMENSIONS.y, 0, 255, 255, 255, 255, 0));
				}
			}
			if(this.getIndexAtLoc(this.selectorLoc) < this.availableSpells.size()) {
				Spell spell = this.availableSpells.get(this.getIndexAtLoc(this.selectorLoc));
				if(spell != null) {
					g.setColor(SPELL_BACKGROUND_COLOR); //background
					Rectangle rect = new Rectangle((float)(HIGHLIGHTED_SPELL_DRAWLOC.x + this.getLoc().x + this.box.getTopLeftLoc().x), (float)(HIGHLIGHTED_SPELL_DRAWLOC.y + this.getLoc().y + this.box.getTopLeftLoc().y), (int)(HIGHLIGHTED_SPELL_DIMENSIONS.x), (int)(HIGHLIGHTED_SPELL_DIMENSIONS.y));
					g.fill(rect);
					if(StateGame.isServer)
					this.getMap().addToDrawInfo(GameMap.getDrawDataR(HIGHLIGHTED_SPELL_DRAWLOC.x + this.getLoc().x + this.box.getTopLeftLoc().x, HIGHLIGHTED_SPELL_DRAWLOC.y + this.getLoc().y + this.box.getTopLeftLoc().y, HIGHLIGHTED_SPELL_DIMENSIONS.x, HIGHLIGHTED_SPELL_DIMENSIONS.y, SPELL_BACKGROUND_COLOR.getRedByte(), SPELL_BACKGROUND_COLOR.getGreenByte(), SPELL_BACKGROUND_COLOR.getBlueByte(), SPELL_BACKGROUND_COLOR.getAlphaByte()));
					g.drawImage(spell.getImage().getScaledCopy((int)(HIGHLIGHTED_SPELL_DIMENSIONS.x), (int)(HIGHLIGHTED_SPELL_DIMENSIONS.y)), (float)(HIGHLIGHTED_SPELL_DRAWLOC.x + this.getLoc().x + this.box.getTopLeftLoc().x), (float)(HIGHLIGHTED_SPELL_DRAWLOC.y + this.getLoc().y + this.box.getTopLeftLoc().y));
					if(StateGame.isServer)
					this.getMap().addToDrawInfo(GameMap.getDrawDataI(spell.getImagePath(), HIGHLIGHTED_SPELL_DRAWLOC.x + this.getLoc().x + this.box.getTopLeftLoc().x, HIGHLIGHTED_SPELL_DRAWLOC.y + this.getLoc().y + this.box.getTopLeftLoc().y, HIGHLIGHTED_SPELL_DIMENSIONS.x, HIGHLIGHTED_SPELL_DIMENSIONS.y, 0, 255, 255, 255, 255, 0));
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
					localDrawLoc.x = -localDrawLoc.x + SPELL_ICON_DIMENSIONS.x;
				}
			}
			Point spellDrawLoc = Point.add(Point.add(localDrawLoc, Point.add(this.getLoc(), this.box.getTopLeftLoc())), new Point(0, SPELLS_SELECTED_DRAW_HEIGHT * i/(MAX_SELECTED_SPELLS-1)));
			if(this.style == GameMap.ID_RIGHT && !this.pickingPhase) {
				spellDrawLoc = Point.add(Point.add(localDrawLoc, Point.add(this.getLoc(), this.box.getTopRightLoc())), new Point(0, SPELLS_SELECTED_DRAW_HEIGHT * i/(MAX_SELECTED_SPELLS-1)));
			}
			g.setColor(SPELL_BACKGROUND_COLOR);
			Rectangle rect = new Rectangle((float)(spellDrawLoc.x - SPELL_ICON_DIMENSIONS.x), (float)(spellDrawLoc.y - SPELL_ICON_DIMENSIONS.y), (float)SPELL_ICON_DIMENSIONS.x, (float)SPELL_ICON_DIMENSIONS.y);
			g.fill(rect);
			if(StateGame.isServer)
			this.getMap().addToDrawInfo(GameMap.getDrawDataR(spellDrawLoc.x - SPELL_ICON_DIMENSIONS.x, spellDrawLoc.y - SPELL_ICON_DIMENSIONS.y, SPELL_ICON_DIMENSIONS.x, SPELL_ICON_DIMENSIONS.y, SPELL_BACKGROUND_COLOR.getRedByte(), SPELL_BACKGROUND_COLOR.getGreenByte(), SPELL_BACKGROUND_COLOR.getBlueByte(), SPELL_BACKGROUND_COLOR.getAlphaByte()));
			if(i < this.selectedSpells.size()) {
				//draws a spell icon if there is a spell
				g.drawImage(this.selectedSpells.get(i).getImage().getScaledCopy((int)SPELL_ICON_DIMENSIONS.x, (int)SPELL_ICON_DIMENSIONS.y), (float)(spellDrawLoc.x - SPELL_ICON_DIMENSIONS.x), (float)(spellDrawLoc.y - SPELL_ICON_DIMENSIONS.y));
				if(StateGame.isServer)
				this.getMap().addToDrawInfo(GameMap.getDrawDataI(this.selectedSpells.get(i).getImagePath(), spellDrawLoc.x - SPELL_ICON_DIMENSIONS.x, spellDrawLoc.y - SPELL_ICON_DIMENSIONS.y, SPELL_ICON_DIMENSIONS.x, SPELL_ICON_DIMENSIONS.y, 0, 255, 255, 255, 255, 0));
				if(i == 0 && !this.pickingPhase) {
					g.drawImage(selectorImage.getScaledCopy((int)SPELL_ICON_DIMENSIONS.x, (int)SPELL_ICON_DIMENSIONS.y), (float)(spellDrawLoc.x - SPELL_ICON_DIMENSIONS.x), (float)(spellDrawLoc.y - SPELL_ICON_DIMENSIONS.y));
					if(StateGame.isServer)
					this.getMap().addToDrawInfo(GameMap.getDrawDataI(SELECTOR_IMAGEPATH, spellDrawLoc.x - SPELL_ICON_DIMENSIONS.x, spellDrawLoc.y - SPELL_ICON_DIMENSIONS.y, SPELL_ICON_DIMENSIONS.x, SPELL_ICON_DIMENSIONS.y, 0, 255, 255, 255, 255, 0));
				}
			}
		}
		if(this.pickingPhase) {
			Point selectorDrawLoc = Point.add(Point.add(SPELLS_DRAWLOC_TOPLEFT, Point.add(this.getLoc(), this.box.getTopLeftLoc())), new Point(horizontalSpacing * selectorLoc.x, verticalSpacing * selectorLoc.y));
			if(this.selectedSpells.size() == MAX_SELECTED_SPELLS || this.getIndexAtLoc(this.selectorLoc) >= this.availableSpells.size()) {
				g.drawImage(selectorImage.getScaledCopy((int)SPELL_ICON_DIMENSIONS.x, (int)SPELL_ICON_DIMENSIONS.y), (float)(selectorDrawLoc.x - SPELL_ICON_DIMENSIONS.x), (float)(selectorDrawLoc.y - SPELL_ICON_DIMENSIONS.y), Color.gray);
				if(StateGame.isServer)
				this.getMap().addToDrawInfo(GameMap.getDrawDataI(SELECTOR_IMAGEPATH, selectorDrawLoc.x - SPELL_ICON_DIMENSIONS.x, selectorDrawLoc.y - SPELL_ICON_DIMENSIONS.y, SPELL_ICON_DIMENSIONS.x, SPELL_ICON_DIMENSIONS.y, 0, Color.gray.getRedByte(), Color.gray.getGreenByte(), Color.gray.getBlueByte(), Color.gray.getAlphaByte(), 0));
			} else {
				g.drawImage(selectorImage.getScaledCopy((int)SPELL_ICON_DIMENSIONS.x, (int)SPELL_ICON_DIMENSIONS.y), (float)(selectorDrawLoc.x - SPELL_ICON_DIMENSIONS.x), (float)(selectorDrawLoc.y - SPELL_ICON_DIMENSIONS.y));
				if(StateGame.isServer)
				this.getMap().addToDrawInfo(GameMap.getDrawDataI(SELECTOR_IMAGEPATH, selectorDrawLoc.x - SPELL_ICON_DIMENSIONS.x, selectorDrawLoc.y - SPELL_ICON_DIMENSIONS.y, SPELL_ICON_DIMENSIONS.x, SPELL_ICON_DIMENSIONS.y, 0, 255, 255, 255, 255, 0));
			}
		}
	}
}
