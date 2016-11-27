package spell;

import org.newdawn.slick.Color;

import mechanic.Game;
import mechanic.GameElement;
import mechanic.Point;
import projectile.Projectile;
import shield.Shield;
import statuseffect.StackingProperty;
import statuseffect.StatusEffect;
import statuseffect.StatusZaWarudo;
import ui.Text;
import ui.TextFormat;
import unit.Unit;

public class ZaWarudo extends Spell {
	public static final float STOP_TIME = 10;
	public static final float FINAL_MESSAGE_TIME = 2;
	float timer;
	Text timerText;
	public ZaWarudo(Unit owner) {
		super(owner, 0, 0, "ZA WARUDO", "Stop time, and instantly go to the spell pick phase", "res/spell/zawarudo.png", true);
		this.registerFieldEffect("za warudo");
	}
	@Override
	public void onActivate() {
		this.getMap().forcePickPhase();
		this.getMap().unpauseAll();
		this.timer = STOP_TIME;
		for(Unit u : this.getMap().getUnits()) {
			if(!u.hasStatusEffect("zawarudo") && !u.equals(this.owner)) {
				StatusEffect s = new StatusEffect(u, StackingProperty.UNSTACKABLE_REPLACE, "zawarudo", 0.001f, false, false, 1);
				//s.setMoveSpeedModifier(0);
				s.setDisableUnitControl(true);
				u.addStatusEffect(s);
				u.pauseSpellTimer();
				u.setTimeScale(0);
				for(Shield shield : u.getShields()) {
					shield.setTimeScale(0);
				}
			}
		}
		for(Projectile p : this.getMap().getProjectiles()) {
			if(!p.hasStatusEffect("zawarudo")) {
				StatusEffect s = new StatusEffect(p, StackingProperty.UNSTACKABLE_REPLACE, "zawarudo", timer, false, false, 1);
				s.setMoveSpeedModifier(0);
				p.addStatusEffect(s);
			}
		}
		this.getMap().pauseSpells();
		this.castUnpause();
	}
	@Override
	public void onSpellSetMap() {
		this.timerText = new Text(this.getMap().getUI(), new Point(Game.WINDOW_WIDTH/2 - 400, Game.WINDOW_HEIGHT/4), 800, 6, Color.white, "", TextFormat.CENTER_JUSTIFIED);
		this.timerText.setUseOutline(true);
		this.timerText.setElementToRemoveWith(this);
		this.getMap().getUI().addUIElement(timerText);
	}
	@Override
	public void onSpellUpdate() {
		this.timer -= this.getFrameTime();
		if(this.timer > 0) {
			this.timerText.setText("time left:|" + (int)(this.timer + 1));
			for(Unit u : this.getMap().getUnits()) {
				if(!u.hasStatusEffect("zawarudo") && !u.equals(this.owner)) {
					StatusEffect s = new StatusEffect(u, StackingProperty.UNSTACKABLE_REPLACE, "zawarudo", 0, false, false, 1);
					s.setMoveSpeedModifier(0);
					s.setDisableUnitControl(true);
					u.setTimeScale(0);
					u.addStatusEffect(s);
					u.pauseSpellTimer();
					for(Shield shield : u.getShields()) {
						shield.setTimeScale(0);
					}
				}
			}
			for(Projectile p : this.getMap().getProjectiles()) {
				if(!p.hasStatusEffect("zawarudo")) {
					p.addStatusEffect(new StatusZaWarudo(p, timer));
				}
			}
		} else { //timer <= 0
			if(this.timer + this.getFrameTime() >= 0) {
				this.getMap().pauseAll();
				this.setPause(false);
				this.getMap().unpauseSpells();
				for(Unit u : this.getMap().getUnits()) {
					u.unpauseSpellTimer();
					u.putMoveCooldown();
					u.setTimeScale(1);
					for(Shield shield : u.getShields()) {
						shield.setTimeScale(1);
					}
				}
			}
			this.timerText.setText("time moves again...");
			if(this.timer <= -FINAL_MESSAGE_TIME) {
				this.getMap().unpauseAll();
				this.timerText.setText("");
				this.finishSpell();
			}
		}
	}
}
