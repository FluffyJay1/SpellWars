
package states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import mechanic.Game;
import mechanic.GameMap;
import monsters.DefaultMonster;
import towers.DefaultTower;

public class StateGame extends BasicGameState{
	
	GameMap map;
	
	public StateGame(){
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame arg1) throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame arg1){
		container.setClearEachFrame(true);
		System.out.println("THis isn't called");
		map = new GameMap(125, 125);
		map.placeTower(new DefaultTower(75.0 * 800 / 125,75.0 * 800 / 125, map)); //MAGIC NUMBER ALERT
		map.spawnCreep(new DefaultMonster(0, 0));
	}

	@Override
	public void render(GameContainer container, StateBasedGame arg1, Graphics g) throws SlickException {
		map.draw(g);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		map.update();
		map.spawnCreep(new DefaultMonster(Math.random() * 800/*125*/, Math.random() * 800 /*125*/)); // MAGIC NUMBER
	}

	@Override
	public int getID() {
		return Game.STATE_GAME;
	}
	
}