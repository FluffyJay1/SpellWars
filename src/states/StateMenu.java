
package states;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StateMenu extends BasicGameState implements ComponentListener{
	
	private MouseOverArea start;
	private Image startButton;
	private Image gameTitle;
	private StateBasedGame game;
	private int id;
	
	public StateMenu(int id){
		this.id = id;
	}
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		// TODO Auto-generated method stub
		startButton = new Image("res/StartButton.png");
	}
	

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
    	start = new MouseOverArea(container,startButton,512, 364, 180,90);
    	this.game  = game;
    }

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		start.render(container, g);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return id;
	}


	@Override
	public void componentActivated(AbstractComponent com) {
		// TODO Auto-generated method stub
		if (com == start )
			game.enterState(id);
	}

}