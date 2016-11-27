package spell;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import mechanic.GameMap;
import mechanic.Panel;
import mechanic.PanelState;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import states.StateGame;
import unit.Unit;

public class Sanctuary extends Spell {
	public static final float CLEAR_DURATION = 1.2f;
	public static final float FINAL_DURATION = 0.4f;
	float timer;
	ArrayList<Panel> panels;
	int panelsSet;
	public Sanctuary(Unit owner){
		super(owner, 0, 0, "Sanctuary", "Changes all your panels to holy panels, decreasing damage taken and increasing healing taken", "res/spell/sanctuary.png", true);
	}
	@Override
	public void onActivate() {
		this.timer = 0;
		this.panels = this.getMap().getPanelsOfTeam(this.owner.teamID);
		this.panelsSet = 0;
	}
	@Override
	public void onSpellUpdate() {
		this.timer += this.getFrameTime();
		while(this.timer/CLEAR_DURATION > (float)panelsSet/(float)panels.size() && this.panelsSet < this.panels.size()) {
			this.panels.get(panelsSet).setPanelState(PanelState.HOLY);
			ParticleEmitter pe = new ParticleEmitter(this.getMap().gridToPosition(this.panels.get(panelsSet).getLoc()), EmitterTypes.CIRCLE_DIRECTION, "res/particle_genericWhite.png", true, //point/parent, emitter type, image path, alphaDecay
					1.5f, 2.5f, //particle start scale
					4.0f, 8.0f, //particle end scale
					4.5f, //drag
					0, 0, //rotational velocity
					0.2f, 0.4f, //min and max lifetime
					10, 1800, //min and max launch speed
					0, 10, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
					0, (float)this.getMap().getSizeOfPanel().y/2, 90, 20); //keyvalues
			this.getMap().addParticleEmitter(pe);
			panelsSet++;
		}
		if(this.panelsSet == this.panels.size() && this.timer > CLEAR_DURATION + FINAL_DURATION) {
			this.finishSpell();
		}
	}
	/*
	@Override
	public void drawSpecialEffects(Graphics g) {
		if(!this.panelsSet) {
			if(this.timer % FLASH_INTERVAL > FLASH_INTERVAL/2 && this.timer < FLASH_DURATION) {
				for(Panel p : this.panels) {
					g.drawImage(Panel.normal.getScaledCopy((int)this.map.getSizeOfPanel().x,(int)this.map.getSizeOfPanel().y), (float)(this.map.gridToPosition(p.getLoc()).x - this.map.getSizeOfPanel().x/2),(float)(this.map.gridToPosition(p.getLoc()).y - this.map.getSizeOfPanel().y/2));
					if(StateGame.isServer)
					this.map.addToDrawInfo(GameMap.getDrawDataFN("res/panel/normal.png", this.map.gridToPosition(p.getLoc()).x - this.map.getSizeOfPanel().x/2, this.map.gridToPosition(p.getLoc()).y - this.map.getSizeOfPanel().y/2, this.map.getSizeOfPanel().x, this.map.getSizeOfPanel().y, 0, 255, 255, 255, 255, 0));
				}
			}
		}
	}
	*/
}
