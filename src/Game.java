

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

public class Game extends BasicGame {
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
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		g.drawImage(images.get("Cool Math Games"), 0, 0);
	}
	
	@Override
	public void init(GameContainer arg0) throws SlickException {
		Image i = new Image("res/images.png");
		images.put("Cool Math Games", i);
	}

	@Override
	public void update(GameContainer container, int arg1) throws SlickException {
		Input in = container.getInput();
		Point point = new Point(0, 0);
		
		
	}

}
