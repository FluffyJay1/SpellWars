package states;

import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import mechanic.GameMap;

public class StateGame extends BasicGameState{
	
	GameMap map;
	private int id;

	public StateGame(int id){
		this.id = id;
	}
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		map = new GameMap(125, 125);
	}	

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		map.draw(g);
		
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		map.update();
	}

	@Override
	public int getID() {
		return id;
	}
	
}