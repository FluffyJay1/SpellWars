
package states;

import mechanic.Game;
import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Panel;
import mechanic.PlayerType;
import mechanic.Point;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import particlesystem.ParticleEmitter;
import projectile.Projectile;
import shield.RechargingShield;
import shield.ReflectShield;
import spell.CryoFreeze;
import spell.EndothermicBlasterPrimary;
import spell.EndothermicBlasterSecondary;
import spell.IceWall;
import spell.MeiBlizzard;
import spell.Omnislash;
import spell.PlayerFire;
import spell.Slash;
import spell.Spell;
import spell.TestFireball;
import statuseffect.StatusFrost;
import particlesystem.EmitterTypes;
import ui.SpellSelector;
import ui.StartButton;
import ui.Text;
import ui.TextFormat;
import ui.UI;
import ui.UIElement;
import unit.Boss;
import unit.Mei;
import unit.Player;
import unit.Trump;
import unit.Unit;

public class StateGame extends BasicGameState{
	float timescale = 1f;
	double systemTime; //USED FOR FINDING FPS, USED IN UPDATE
	int monstersSpawned = 0;
	GameMap map;
	Image backgroundImage;
	UI ui;
	Player leftPlayer;
	SpellSelector leftSelect;
	Player rightPlayer;
	SpellSelector rightSelect;
	boolean pickingPhase;
	static final float READY_TIME = 1.5f;
	float readyTimer;
	static final float BATTLE_PHASE_TIME = 15;
	float battlePhaseTimer;
	Text readyText;
	Text battlePhaseText;
	boolean devPause = false;
	public StateGame(){
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame arg1) throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame arg1){
		container.setClearEachFrame(true);
		this.setBackgroundImage("res/trail_lightning.png");
		map = new GameMap(8, 4, //dimensions of game grid
				Game.WINDOW_WIDTH * 5/6 /*1600 on 1920x1080 monitors*/, Game.WINDOW_HEIGHT * 540/1080 /*540 on 1920x1080 monitors*/, //dimensions of map
				new Point(Game.WINDOW_WIDTH/12, Game.WINDOW_HEIGHT * 500/1080)); //top left corner of map (map coordinates)
		ui = new UI();
		map.setUI(ui);
		Point leftStartLoc = new Point(0, 0);
		Point rightStartLoc = new Point(7, 3);
		leftPlayer = new Player(400, 5, GameMap.ID_LEFT, leftStartLoc);
		//this.map.getPanelAt(leftPlayer.gridLoc).unitStandingOnPanel = leftPlayer;
		leftSelect = new SpellSelector(ui, new Point(0, 0), GameMap.ID_LEFT, leftPlayer);
		rightPlayer = new Player(400, 5, GameMap.ID_RIGHT, rightStartLoc);
		//this.map.getPanelAt(rightPlayer.gridLoc).unitStandingOnPanel = rightPlayer;
		rightSelect = new SpellSelector(ui, new Point(Game.WINDOW_WIDTH, 0), GameMap.ID_RIGHT, rightPlayer);
		if(Game.leftPlayer.equals(PlayerType.PLAYER)) {
			map.addUnit(leftPlayer);
		} else {
			leftPlayer.setRemove(true);
		}
		if(Game.rightPlayer.equals(PlayerType.PLAYER)) {
			map.addUnit(rightPlayer);
		} else {
			rightPlayer.setRemove(true);
		}
		
		if(Game.leftPlayer.equals(PlayerType.COMPUTER)) {
			Unit boss = new Boss(leftStartLoc, (int)GameMap.ID_LEFT, Game.leftLevel);
			map.addUnit(boss);
		}
		if(Game.rightPlayer.equals(PlayerType.COMPUTER)) {
			Unit boss = new Boss(rightStartLoc, (int)GameMap.ID_RIGHT, Game.rightLevel);
			map.addUnit(boss);
		}
		
		if(Game.leftPlayer.equals(PlayerType.TRUMP)) {
			Unit boss = new Trump(leftStartLoc, (int)GameMap.ID_LEFT, Game.leftLevel);
			map.addUnit(boss);
		}
		if(Game.rightPlayer.equals(PlayerType.TRUMP)) {
			Unit boss = new Trump(rightStartLoc, (int)GameMap.ID_RIGHT, Game.rightLevel);
			map.addUnit(boss);
		}
		
		if(Game.leftPlayer.equals(PlayerType.MEI)) {
			Unit boss = new Mei(leftStartLoc, (int)GameMap.ID_LEFT, Game.leftLevel);
			map.addUnit(boss);
			//this.setBackgroundImage("res/0074.png");
		}
		if(Game.rightPlayer.equals(PlayerType.MEI)) {
			Unit boss = new Mei(rightStartLoc, (int)GameMap.ID_RIGHT, Game.rightLevel);
			map.addUnit(boss);
			//this.setBackgroundImage("res/0074.png");
		}
		
		
		battlePhaseText = new Text(ui, new Point(Game.WINDOW_WIDTH/2 - 200, 24), 400, 16, 24, 18, 26, Color.white, "time until next spell selection: ", TextFormat.CENTER_JUSTIFIED);
		battlePhaseText.setUseOutline(true);
		//ui.addUIElement(battlePhaseText);
		
		ui.addUIElement(leftSelect);
		ui.addUIElement(rightSelect);
		pickingPhase = false;
		readyTimer = 0;
		battlePhaseTimer = 0;
		readyText = new Text(ui, new Point(Game.WINDOW_WIDTH/2 - 200, Game.WINDOW_HEIGHT/2), 400, 24, 0, 28, 24, Color.white, "GET READY!", TextFormat.CENTER_JUSTIFIED);
		readyText.setUseOutline(true);
		ui.addUIElement(readyText);
	}

	@Override
	public void render(GameContainer container, StateBasedGame arg1, Graphics g) throws SlickException {
		//g.clear();
		backgroundImage.drawWarped(Game.WINDOW_WIDTH, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT, 0, Game.WINDOW_HEIGHT, 0, 0); //start with topright, then clockwise
		map.draw(g);
		if(!pickingPhase && this.readyTimer >= 0) {
		float ratio = readyTimer/READY_TIME;
			if(ratio > 0.8) {
				this.readyText.setLetterHeight((int)((1 - ratio) * 200));
				this.readyText.changeLoc(new Point(Game.WINDOW_WIDTH/2 - 200, Game.WINDOW_HEIGHT/2 - (1 - ratio) * 100));
			} else if(ratio < 0.2) {
				this.readyText.setLetterHeight((int)(ratio * 200));
				this.readyText.changeLoc(new Point(Game.WINDOW_WIDTH/2 - 200, Game.WINDOW_HEIGHT/2 - ratio * 100));
			} else {
				this.readyText.setLetterHeight(40);
				this.readyText.changeLoc(new Point(Game.WINDOW_WIDTH/2 - 200, Game.WINDOW_HEIGHT/2 - 20));
			}
		} else {
			this.readyText.setLetterHeight(0);
		}
		ui.draw(g);
		//map.drawGridHighlight(g, map.getMousePosition());
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		float frametime = (float) ((System.nanoTime() - systemTime) / 1000000000) * timescale;
		map.passFrameTime(frametime); //calculates difference in time per frame , and magic number is there since 1 second is 10^9 nanoseconds
		ui.passFrameTime(frametime);
		systemTime = System.nanoTime();
		pickingPhase = !(leftSelect.getIsReady() && rightSelect.getIsReady());
		if(!pickingPhase && this.readyTimer <= 0 && !devPause) {
			map.update();
			if(!this.map.isPaused()) {
				if(Game.leftPlayer.equals(PlayerType.PLAYER) || Game.rightPlayer.equals(PlayerType.PLAYER)) {
					this.battlePhaseTimer -= frametime;
				}
			}
		}
		if(battlePhaseTimer <= 0){
			this.pickingPhase = true;
			leftSelect.removeSelectedSpells();
			leftSelect.setPickingPhase(true);
			rightSelect.removeSelectedSpells();
			rightSelect.setPickingPhase(true);
			this.battlePhaseTimer = BATTLE_PHASE_TIME;
			this.readyTimer = READY_TIME;
			this.battlePhaseText.setRemove(true);
		}
		if(!pickingPhase && this.readyTimer >= 0) {
			if(this.battlePhaseText.getRemove()) {
				if(Game.leftPlayer.equals(PlayerType.PLAYER) || Game.rightPlayer.equals(PlayerType.PLAYER)) {
					ui.addUIElement(battlePhaseText);
				}
			}
			this.readyTimer -= frametime;
			
		} else {
			this.readyText.setLetterHeight(0);
		}
		battlePhaseText.setText("time until next spell selection: " + (int)(this.battlePhaseTimer + 0.9999));
		if(battlePhaseTimer < 3) {
			battlePhaseText.setColor(new Color(255, (int)((this.battlePhaseTimer * 255) % 255), 0));
		} else {
			battlePhaseText.setColor(Color.white);
		}
		ui.update();
	}
	@Override
	public void mousePressed(int button, int x, int y) {
		//PLACE A TOWER
		/*
		Tower theTower = new DefaultTower(x, y, 175, 1, 100, 50);
		//Tower theTower = new FrostTower(x, y, 1);
		if(map.isTowerAtGridPos(map.positionToGrid(new Point(x,y)))) {
			Tower existingTower = map.getTowerAtGridPos(map.positionToGrid(new Point(x,y)));
			if(existingTower.getLevel() == existingTower.getMaxLevel()) {
				map.removeTowerAtGridPos(map.positionToGrid(new Point(x,y)));
			} else {
				existingTower.levelUp();
			}
		} else {
			map.placeTower(theTower);
		}
		*/
		/*
		if(this.map.isTowerAtGridPos(this.map.positionToGrid(new Point(x, y)))) {
			map.removeTowerAtGridPos(this.map.positionToGrid(new Point(x, y)));
		}
		*/
		ui.onClick();
	}
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		//timescale = (float)newx/400;
		map.passMousePosition(new Point(newx, newy));
		ui.passMouseLoc(new Point(newx, newy));
	}
	@Override
	public void keyPressed(int key, char c) {
		String message = "";
		if(key == Input.KEY_SPACE) {
			this.devPause = !devPause;
		}
		if(key == Input.KEY_H) {
			leftPlayer.doDamage(399);
			rightPlayer.doDamage(399);
		}
		if(key == Input.KEY_J) {
			System.out.println("map.isPaused() is " + this.map.isPaused());
		}
		if(this.pickingPhase) {
			switch(key) {
			case Input.KEY_T:
				leftSelect.setPickingPhase(false);
				break;
			case Input.KEY_E:
				leftSelect.selectCurrentSpell();
				break;
			case Input.KEY_Q:
				leftSelect.deselectSpell();
				break;
			case Input.KEY_PERIOD:
				rightSelect.selectCurrentSpell();
				break;
			case Input.KEY_SLASH:
				rightSelect.deselectSpell();
				break;
			case Input.KEY_L:
				rightSelect.setPickingPhase(false);
				break;
			case Input.KEY_W:
				leftSelect.moveSelector(new Point(0, -1));
				break;
			case Input.KEY_S:
				leftSelect.moveSelector(new Point(0, 1));
				break;
			case Input.KEY_A:
				leftSelect.moveSelector(new Point(-1, 0));
				break;
			case Input.KEY_D:
				leftSelect.moveSelector(new Point(1, 0));
				break;
			case Input.KEY_UP:
				rightSelect.moveSelector(new Point(0, -1));
				break;
			case Input.KEY_DOWN:
				rightSelect.moveSelector(new Point(0, 1));
				break;
			case Input.KEY_LEFT:
				rightSelect.moveSelector(new Point(-1, 0));
				break;
			case Input.KEY_RIGHT:
				rightSelect.moveSelector(new Point(1, 0));
				break;
			default:
				break;
			}
		} else if (this.readyTimer <= 0) {
			switch(key) {
			case Input.KEY_E:
				leftPlayer.castNextAvailableSpell();
				break;
			case Input.KEY_Q:
				leftPlayer.castSpell(new PlayerFire(leftPlayer));
				break;
			case Input.KEY_W:
				leftPlayer.move(GameMap.ID_UP, true, true, true, true, true, false);
				break;
			case Input.KEY_S:
				leftPlayer.move(GameMap.ID_DOWN, true, true, true, true, true, false);
				break;
			case Input.KEY_A:
				leftPlayer.move(GameMap.ID_LEFT, true, true, true, true, true, false);
				break;
			case Input.KEY_D:
				leftPlayer.move(GameMap.ID_RIGHT, true, true, true, true, true, false);
				break;
			case Input.KEY_PERIOD:
				rightPlayer.castNextAvailableSpell();
				break;
			case Input.KEY_SLASH:
				rightPlayer.castSpell(new PlayerFire(rightPlayer));
				break;
			case Input.KEY_UP:
				rightPlayer.move(GameMap.ID_UP, true, true, true, true, true, false);
				break;
			case Input.KEY_DOWN:
				rightPlayer.move(GameMap.ID_DOWN, true, true, true, true, true, false);
				break;
			case Input.KEY_LEFT:
				rightPlayer.move(GameMap.ID_LEFT, true, true, true, true, true, false);
				break;
			case Input.KEY_RIGHT:
				rightPlayer.move(GameMap.ID_RIGHT, true, true, true, true, true, false);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public int getID() {
		return Game.STATE_GAME;
	}
	
	public void setBackgroundImage(String image) {
		try {
			this.backgroundImage = new Image(image);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}