package spell;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import mechanic.GameMap;
import mechanic.Panel;
import mechanic.Point;
import unit.Unit;

public class AreaGrab extends Spell {
	public static final float ANIMATION_HEIGHT = 800;
	public static final float ANIMATION_TIME = 1.0f;
	float timer;
	public static final float DAMAGE = 10;
	public static final float STUN_DURATION = 1;
	ArrayList<Panel> affectedPanels;
	static Image redOrb;
	static Image blueOrb;
	boolean areaStolen;
	public AreaGrab(Unit owner) {
		super(owner, 0, 0, "Area Grab", "Steals a column of area from the enemy, and stunning units that get in the way", "res/particle_genericRed.png", true);
		this.timer = ANIMATION_TIME;
		this.affectedPanels = new ArrayList<Panel>();
		if(redOrb == null) {
			try {
				redOrb = new Image("res/particle_genericRed.png");
				blueOrb = new Image("res/particle_genericBlue.png");
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.areaStolen = false;
	}
	@Override
	public void onActivate() {
		for(int row = 0; row < this.getMap().getGridDimensions().y; row++) {
			int column = 0;
			if(this.owner.teamID == GameMap.ID_LEFT) {
				for(column = 1; column < this.getMap().getGridDimensions().x - 1; column++) {
					Panel panel = this.getMap().getPanelAt(new Point(column, row));
					if(panel.teamID != GameMap.ID_LEFT) {
						this.affectedPanels.add(panel);
						/*
						if(panel.unitStandingOnPanel == null) {
							//panel.setTeamID(GameMap.ID_LEFT);
							this.affectedPanels.add(panel);
						} else {
							panel.unitStandingOnPanel.doDamage(DAMAGE);
						}
						*/
						break;
					}
				}
			} else {
				for(column = (int)this.getMap().getGridDimensions().x - 2; column > 0; column--) {
					Panel panel = this.getMap().getPanelAt(new Point(column, row));
					if(panel.teamID != GameMap.ID_RIGHT) {
						this.affectedPanels.add(panel);
						/*
						if(panel.unitStandingOnPanel == null) {
							//panel.setTeamID(GameMap.ID_RIGHT);
							this.affectedPanels.add(panel);
						} else {
							panel.unitStandingOnPanel.doDamage(DAMAGE);
						}
						*/
						break;
					}
				}
			}
		}
		if(this.affectedPanels.size() == 0) {
			this.finishSpell();
		}
	}
	@Override
	public void onSpellUpdate() {
		this.timer -= this.getFrameTime();
		/*
		if(this.timer % 0.2 > 0.1 && this.timer > 0.6) {
			for(Panel p : this.affectedPanels) {
				p.setTeamID(GameMap.getOppositeDirection((char)this.owner.teamID));
			}
		} else {
			for(Panel p : this.affectedPanels) {
				p.setTeamID(this.owner.teamID);
			}
		}
		if(this.timer <= 0.6) {
			for(Panel p : this.affectedPanels) {
				p.setTeamID(this.owner.teamID);
			}
		}
		*/
		if(this.timer <= 0.5) {
			if(!this.areaStolen) {
				for(Panel p : this.affectedPanels) {
					if(p.unitStandingOnPanel == null || p.unitStandingOnPanel.teamID == this.owner.teamID) {
						p.setTeamID(this.owner.teamID);
					} else {
						p.unitStandingOnPanel.doDamage(DAMAGE);
						p.unitStandingOnPanel.stun(STUN_DURATION);
					}
				}
				this.areaStolen = true;
			}
		}
		if(this.timer <= 0) {
			this.affectedPanels.clear();
			this.finishSpell();
		}
	}
	@Override
	public void drawSpecialEffects(Graphics g) {
		if(this.timer > 0.5) {
			for(Panel p : this.affectedPanels) {
				Point drawLoc = Point.add(this.map.gridToPosition(p.getLoc()), new Point(0, -ANIMATION_HEIGHT * (this.timer - 0.5)/(ANIMATION_TIME - 0.5)));
				if(this.owner.teamID == GameMap.ID_LEFT) {
					g.drawImage(redOrb.getScaledCopy(8), (float)drawLoc.x - redOrb.getWidth() * 4, (float)drawLoc.y - redOrb.getWidth()/2);
				} else {
					g.drawImage(blueOrb.getScaledCopy(8), (float)drawLoc.x - redOrb.getWidth() * 4, (float)drawLoc.y - redOrb.getWidth()/2);
				}
			}
		}
		if(this.timer > 0.4 && this.timer < 0.5/* && this.timer % 0.1 > 0.05*/) {
			for(Panel p : this.affectedPanels) {
				Point drawLoc = Point.add(this.map.gridToPosition(p.getLoc()), Point.scale(this.map.getSizeOfPanel(), -0.5));
				Rectangle rect = new Rectangle((float)drawLoc.x, (float)drawLoc.y, (float)this.map.getSizeOfPanel().x, (float)this.map.getSizeOfPanel().y);
				g.setColor(Color.gray);
				g.fill(rect);
			}
		}
	}
}
