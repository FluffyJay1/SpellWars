
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
import towers.Tower;

public class StateGame extends BasicGameState{
	int delay = 0; //UNECCESARY VARIABLE, FOUND IN UPDATE
	double systemTime; //USED FOR FINDING FPS, USED IN UPDATE
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
		map = new GameMap(20, 20);
		/*
		map.placeTower(new DefaultTower(200, 400, map, 300, 1, 100, 150)); //MAGIC NUMBER ALERT
		map.placeTower(new DefaultTower(400, 400, map, 300, 1, 250, 125));
		map.placeTower(new DefaultTower(600, 400, map, 300, 1, 450, 100));
		*/
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
		map.passFrameTime((float) ((System.nanoTime() - systemTime) / 1000000000)); //calculates difference in time per frame , and magic number is there since 1 second is 10^9 nanoseconds
		systemTime = System.nanoTime();
		delay--;
		if(delay <= 0)
		{
			for(int i = 0; i < (int)(Math.random() * 8); i++) {
				map.spawnCreep(new DefaultMonster(/*Math.random() * 800*/0, Math.random() * 400 + 200 /*400*/, map)); // MAGIC NUMBER
			}
			delay = (int) (1000 + Math.random() * 500);
		}
	}
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		//PLACE A TOWER
		Tower theTower = new DefaultTower(x, y, map, 150, 1, 150, 50);
		map.placeTower(theTower);
	}
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		map.passMousePosition(newx, newy);
	}

	@Override
	public int getID() {
		return Game.STATE_GAME;
	}
	
}