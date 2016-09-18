package spell;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import mechanic.GameMap;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import ui.SpellSelector;
import unit.Player;
import unit.Unit;

public class MysteryBox extends Spell {
	public static final int NUM_SPELLS = 3;
	public static final float INTERVAL = 1;
	public static final float SPELL_DRAW_Y_OFFSET = -100;
	public static final int SPELL_DRAW_DIMENSIONS = 128;
	ArrayList<Spell> spells;
	float timer;
	static Image background;
	Image previousSpellImage;
	public MysteryBox(Unit owner) {
		super(owner, 0, 0, "Mystery Box", "Grants you up to 3 extra spells to cast", "res/spell/mysterybox.png", false);
		if(owner instanceof Player) {
			this.pauseWhenActivated = true;
		}
		spells = new ArrayList<Spell>();
		this.timer = 0;
		if(background == null) {
			try {
				background = new Image("res/spell/mysteryboxbackground.png");
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public void onSpellUpdate() {
		this.timer -= this.getFrameTime();
		if(this.timer <= 0) {
			if(this.spells.size() == 0 || ((Player)this.owner).getNumSpells() == SpellSelector.MAX_SELECTED_SPELLS) {
				this.spells.clear();
				this.finishSpell();
			} else {
				this.timer = INTERVAL;
				((Player)this.owner).addSpell(this.spells.get(0));
				this.previousSpellImage = this.spells.get(0).getImage();
				this.spells.remove(0);
				ParticleEmitter pe = new ParticleEmitter(new Point(this.owner.getLoc().x, this.owner.getLoc().y + SPELL_DRAW_Y_OFFSET), EmitterTypes.CIRCLE_RADIAL, "res/particle_genericYellow.png", true, //point/parent, emitter type, image path, alphaDecay
						6.5f, 9.0f, //particle start scale
						1.5f, 4.0f, //particle end scale
						20.0f, //drag
						0, 0, //rotational velocity
						0.2f, 0.5f, //min and max lifetime
						800, 2500, //min and max launch speed
						0, 15, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
						0, 64, 0, 0); //keyvalues
				this.getMap().addParticleEmitter(pe);
			}
		}
	}
	@Override
	public void onActivate() {
		if(this.owner instanceof Player) {
			for(int i = 0; i < NUM_SPELLS; i++) {
				spells.add(SpellSelector.getRandomSpell(this.owner));
			}
		} else {
			this.owner.castSpell(SpellSelector.getRandomSpell(this.owner));
			this.finishSpell();
		}
	}
	@Override
	public void drawSpecialEffects(Graphics g) {
		if(this.previousSpellImage != null) {
			g.drawImage(background, (float)(this.owner.getLoc().x - 74 * SPELL_DRAW_DIMENSIONS/128), (float)(this.owner.getLoc().y + SPELL_DRAW_Y_OFFSET - 98 * SPELL_DRAW_DIMENSIONS/128));
			Image i = this.previousSpellImage.getScaledCopy(SPELL_DRAW_DIMENSIONS, SPELL_DRAW_DIMENSIONS);
			g.drawImage(i, (float)(this.owner.getLoc().x - SPELL_DRAW_DIMENSIONS/2), (float)(this.owner.getLoc().y + SPELL_DRAW_Y_OFFSET - SPELL_DRAW_DIMENSIONS/2));
		}
	}
}
