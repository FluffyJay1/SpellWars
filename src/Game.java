

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import states.StateGame;
import states.StateMenu;

public class Game extends StateBasedGame {
	public static int STATE_MENU = 0;
	public static int STATE_GAME = 1;
	public static int STATE_HELP = 2;
	
	Map<String, Image> images = new HashMap<String, Image>();
	
	public static void main(String[] args) throws SlickException{
		AppGameContainer app = new AppGameContainer(new Game("BTD Battles"));
		app.setDisplayMode(800, 800, false);
		app.start();
	}
	
	public Game(String title) {
		super(title);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		// TODO Auto-generated method stub
		addState(new StateMenu(STATE_MENU));
		addState(new StateGame(STATE_GAME));
			
	}


}
