package unit;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import mechanic.GameMap;
import mechanic.Point;
import spell.BouncingOrb;
import spell.ForgeSpirit;
import spell.Spell;
import spell.TestFireball;
import spell.TimeBombDetonate;

public class Player extends Unit {
	ArrayList<Spell> spells = new ArrayList<Spell>();
	public Player(double hp, double speed, char teamID, Point loc) {
		super(hp, hp, speed, GameMap.getOppositeDirection(teamID) /*opposite direction*/, loc, "res/idgaf.png", teamID);
		this.setDrawHeight(20);
		this.drawMoveCooldown = true;
		this.drawSpellCasting = true;
		this.HPTextColor = Color.black;
	}
	public void setSpells(ArrayList<Spell> spells) {
		this.spells = spells;
	}
	public void castNextAvailableSpell() {
		if(!this.isCasting && this.stunTimer <= 0 && this.spells.size() > 0 && !this.isPaused() && !this.getRemove()) {
			this.castSpell(this.spells.get(0));
			this.spells.remove(0); 
		}
	}
	@Override 
	public void onDeath() {
		this.interruptCast();
		this.castSpell(new TimeBombDetonate(this), true, true);
	}
	/*
	@Override
	public void draw(Graphics g){
		if(this.getImage() != null){
			Image endPic = this.getImage().getFlippedCopy(this.direction == GameMap.ID_LEFT, false);
			float width = endPic.getWidth();
			float height = endPic.getHeight();
			Color col = Color.white;
			
			if(this.moveCooldown > 0) {
				col = new Color(205, 205, 235);
			}
			if(this.isCasting) {
				col = new Color(220, 130, 40);
			}
			g.drawImage(endPic, (float) this.getLoc().x - width/2, (float) this.getLoc().y - height/2 - this.getDrawHeight(), col);
		}
		g.setColor(Color.black);
		//g.setFont(font);
		g.drawString("" + (int)this.getHP(), (float) this.getLoc().x - g.getFont().getWidth("" + (int)this.getHP())/2, (float) this.getLoc().y + HP_Y_OFFSET);
	}
	*/
}
