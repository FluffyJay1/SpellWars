
package mechanic;

import java.net.ServerSocket;
import java.net.Socket;
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
	public static final int WINDOW_WIDTH = 1920;
	public static final int WINDOW_HEIGHT = 1080;
	public static final int SERVER_PORT = 9091;
	public static final String DRAW_INFO_REQUEST_STRING = "di";
	StateGame stategame = new StateGame();
	public static PlayerType leftPlayer;
	public static int leftLevel;
	public static PlayerType rightPlayer;
	public static int rightLevel;
	public static ServerSocket serverSocket;
	public static ServerListenerThread serverListenerThread;
	public static Socket socket;
	
	public static Map<String, Image> images = new HashMap<String, Image>();
	
	
	public static void main(String[] args) throws SlickException{
		AppGameContainer app = new AppGameContainer(new Game("SpellWars"));
		app.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT, false);
		//app.setTargetFrameRate(15);
		app.start();
		
	}
	
	public Game(String title) {
		super(title);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		// TODO Auto-generated method stub
		addState(new StateMenu());
		addState(stategame);
			
	}
	public static Image getImage(String path){
		Image i = null;
		if(images.containsKey(path)) {
			i = images.get(path).copy();
		} else {
			try {
				i = new Image(path);
				images.put(path, i.copy());
			} catch (SlickException e) {
				System.out.println("Unable to load: " + path);
				e.printStackTrace();
			} finally {
				System.out.println("loaded into memory: " + path);
			}
		}
		return i;
	}
}
