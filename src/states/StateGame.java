
package states;

import mechanic.Game;
import mechanic.GameMap;
import mechanic.Point;
import monsters.DefaultMonster;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import particlesystem.ParticleEmitter;
import particlesystem.emitterTypes;
import towers.DefaultTower;

public class StateGame extends BasicGameState{
	int delay = 0; //UNECCESARY VARIABLE, FOUND IN UPDATE
	
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
		map.spawnCreep(new DefaultMonster(0, 0, map));
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame arg1, Graphics g) throws SlickException {
		g.clear();
		map.draw(g);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		map.update();
		map.passFPS(arg0.getFPS());
		delay--;
		if(delay <= 0)
		{
			map.spawnCreep(new DefaultMonster(Math.random() * 800/*125*/, Math.random() * 800 /*125*/, map)); // MAGIC NUMBER
			delay = 1500;
		}
	}

	@Override
	public int getID() {
		return Game.STATE_GAME;
	}
	
}