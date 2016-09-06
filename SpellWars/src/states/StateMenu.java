
package states;


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import ui.PlayerTypeSelector;
import ui.StartButton;
import ui.Text;
import ui.TextFormat;
import ui.UI;
import ui.UIBox;
import ui.UIBoxOrigin;
import mechanic.Game;
import mechanic.Point;

public class StateMenu extends BasicGameState implements ComponentListener{
	
	private MouseOverArea start;
	private Image startButton;
	private Image gameTitle;
	private StateBasedGame game;
	private UI ui;
	private StartButton button;
	String string;
	PlayerTypeSelector leftSelector;
	PlayerTypeSelector rightSelector;
	
	public StateMenu(){

	}
	public void init(GameContainer container, StateBasedGame arg1){
		// TODO Auto-generated method stub
		try{
		startButton = new Image("res/StartButton.png");
		}
		catch (SlickException e){
			System.out.println("Didn't find startbutton???");
		}
		ui = new UI();
		button = new StartButton(ui, new Point(Game.WINDOW_WIDTH/2, Game.WINDOW_HEIGHT/2));
		ui.addUIElement(button);
		leftSelector = new PlayerTypeSelector(ui, new Point(400,800));
		ui.addUIElement(leftSelector);
		rightSelector = new PlayerTypeSelector(ui, new Point(Game.WINDOW_WIDTH - 400,800));
		ui.addUIElement(rightSelector);
	}
	

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
    	//start = new MouseOverArea(container,startButton,512, 364, 180,90, this);

    	this.game  = game;
    }

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		//start.render(container, g);
		//g.drawRect(Game.WINDOW_WIDTH/2, Game.WINDOW_HEIGHT/2, 400, 300);
		ui.draw(g);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		// TODO Auto-generated method stub
		ui.passFrameTime((float)arg2 / 1000);
		ui.update();
		if(button.isPressed) {
			Game.leftPlayer = leftSelector.getPlayerType();
			Game.leftLevel = leftSelector.level;
			Game.rightPlayer = rightSelector.getPlayerType();
			Game.rightLevel = rightSelector.level;
			game.enterState(Game.STATE_GAME);
		}
	}
	@Override
	public void keyPressed(int key, char c) {
		switch(key) {
		case Input.KEY_W:
			leftSelector.changeIndex(-1);
			break;
		case Input.KEY_S:
			leftSelector.changeIndex(1);
			break;
		case Input.KEY_A:
			leftSelector.changeLevel(-1);
			break;
		case Input.KEY_D:
			leftSelector.changeLevel(1);
			break;
		case Input.KEY_UP:
			rightSelector.changeIndex(-1);
			break;
		case Input.KEY_DOWN:
			rightSelector.changeIndex(1);
			break;
		case Input.KEY_LEFT:
			rightSelector.changeLevel(-1);
			break;
		case Input.KEY_RIGHT:
			rightSelector.changeLevel(1);
			break;
		}
	}
	@Override
	public void mousePressed(int button, int x, int y) {
		ui.onClick();
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		ui.passMouseLoc(new Point(newx, newy));
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return Game.STATE_MENU;
	}


	@Override
	public void componentActivated(AbstractComponent com) {
		if (com == start ) {
			game.enterState(Game.STATE_GAME);
			
		}
	}

}