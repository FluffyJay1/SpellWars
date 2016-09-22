
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
import mechanic.GameMap;
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
		GameMap.drawFromImageData(g, "drawfn res/panel/redoutline.png 160 500 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 160 500 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 160 635 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 160 635 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 160 770 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 160 770 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 160 905 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 160 905 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 360 500 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 360 500 200 135 0 255 255 255 255 0|drawr 380 513 160 108 255 255 0 255|drawfn res/panel/redoutline.png 360 635 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 360 635 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 360 770 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 360 770 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 360 905 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 360 905 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 560 500 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 560 500 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 560 635 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 560 635 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 560 770 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 560 770 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 560 905 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 560 905 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 760 500 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 760 500 200 135 0 255 255 255 255 0|drawr 780 513 160 108 255 255 0 255|drawfn res/panel/redoutline.png 760 635 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 760 635 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 760 770 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 760 770 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 760 905 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 760 905 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 960 500 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 960 500 200 135 0 255 255 255 255 0|drawr 980 513 160 108 255 255 0 255|drawfn res/panel/blueoutline.png 960 635 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 960 635 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 960 770 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 960 770 200 135 0 255 255 255 255 0|drawr 980 783 160 108 255 255 0 255|drawr 1000 797 120 81 255 0 0 255|drawfn res/panel/blueoutline.png 960 905 200 135 0 255 255 255 255 0|drawfn res/panel/cracked.png 960 905 200 135 0 255 255 255 255 0|drawr 980 918 160 108 255 255 0 255|drawfn res/panel/blueoutline.png 1160 500 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1160 500 200 135 0 255 255 255 255 0|drawr 1180 513 160 108 255 255 0 255|drawfn res/panel/blueoutline.png 1160 635 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1160 635 200 135 0 255 255 255 255 0|drawr 1180 648 160 108 255 255 0 255|drawfn res/panel/blueoutline.png 1160 770 200 135 0 255 255 255 255 0|drawfn res/panel/cracked.png 1160 770 200 135 0 255 255 255 255 0|drawr 1180 783 160 108 255 255 0 255|drawfn res/panel/blueoutline.png 1160 905 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1160 905 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 1360 500 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1360 500 200 135 0 255 255 255 255 0|drawr 1380 513 160 108 255 255 0 255|drawfn res/panel/blueoutline.png 1360 635 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1360 635 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 1360 770 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1360 770 200 135 0 255 255 255 255 0|drawr 1380 783 160 108 255 255 0 255|drawr 1400 797 120 81 255 0 0 255|drawfn res/panel/blueoutline.png 1360 905 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1360 905 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 1560 500 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1560 500 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 1560 635 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1560 635 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 1560 770 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1560 770 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 1560 905 200 135 0 255 255 255 255 0|drawfn res/panel/cracked.png 1560 905 200 135 0 255 255 255 255 0|drawe 242 555 36 24 120 120 120 120|drawe 1642 555 36 24 120 120 120 120|drawe 1361 809 25 17 120 120 120 120|drawe 906 781 25 17 120 120 120 120|drawe 818 848 25 17 120 120 120 120|drawe 857 723 25 17 120 120 120 120|drawe 844 760 25 17 120 120 120 120|drawe 718 664 25 17 120 120 120 120|drawe 539 598 25 17 120 120 120 120|drawe 502 632 25 17 120 120 120 120|drawe 307 589 25 17 120 120 120 120|drawr 230 602 14 10 120 150 185 255|drawi res/idgaf.png 240 530 40 34 0 220 180 90 255 0|drawi res/particle_genericYellow.png 303 -912 32 32 0 255 255 255 255 0|drawi res/particle_genericYellow.png 536 -790 32 32 0 255 255 255 255 0|drawi res/particle_explosion.png 406 473 168 168 0 255 255 255 255 2|drawi res/particle_genericYellow.png 499 -831 32 32 0 255 255 255 255 0|drawi res/particle_explosion.png 655 481 152 152 0 255 255 255 255 2|drawi res/particle_genericYellow.png 715 -623 32 32 0 255 255 255 255 0|drawi res/particle_genericYellow.png 854 -238 32 32 0 255 255 255 255 0|drawi res/particle_genericYellow.png 841 -388 32 32 0 255 255 255 255 0|drawi res/particle_genericYellow.png 903 280 32 32 0 255 255 255 255 0|drawi res/particle_genericYellow.png 815 93 32 32 0 255 255 255 255 0|drawi res/particle_explosion.png 895 489 137 137 0 255 255 255 255 2|drawi res/particle_explosion.png 1170 498 119 119 0 255 255 255 255 2|drawi res/particle_explosion.png 1412 505 104 104 0 255 255 255 255 2|drawi res/particle_genericYellow.png 1357 582 32 32 0 255 255 255 255 0|drawr 1630 602 18 10 120 150 185 255|drawi res/idgaf.png 1640 530 40 34 0 220 180 90 255 2|draww res/trail_lightning.png 1005 510 996 462 1004 460 1016 507 255 255 255 255|draww res/trail_lightning.png 1014 557 1005 510 1016 507 1032 552 255 255 255 255|draww res/trail_lightning.png 1024 604 1014 557 1032 552 1048 598 255 255 255 255|draww res/trail_lightning.png 1033 651 1024 604 1048 598 1063 643 255 255 255 255|draww res/trail_lightning.png 1041 698 1033 651 1063 643 1078 689 255 255 255 255|draww res/trail_lightning.png 1046 718 1041 698 1078 689 1083 708 255 255 255 255|draww res/trail_lightning.png 1244 302 1229 256 1234 254 1255 297 255 255 255 255|draww res/trail_lightning.png 1261 347 1244 302 1255 297 1277 340 255 255 255 255|draww res/trail_lightning.png 1276 392 1261 347 1277 340 1298 383 255 255 255 255|draww res/trail_lightning.png 1292 438 1276 392 1298 383 1321 426 255 255 255 255|draww res/trail_lightning.png 1307 483 1292 438 1321 426 1342 469 255 255 255 255|draww res/trail_lightning.png 1323 528 1307 483 1342 469 1362 513 255 255 255 255|draww res/trail_lightning.png 1338 574 1323 528 1362 513 1383 556 255 255 255 255|draww res/trail_lightning.png 1351 607 1338 574 1383 556 1396 590 255 255 255 255|draww res/trail_lightning.png 830 31 820 -15 825 -17 841 27 255 255 255 255|draww res/trail_lightning.png 842 78 830 31 841 27 859 72 255 255 255 255|draww res/trail_lightning.png 853 125 842 78 859 72 879 116 255 255 255 255|draww res/trail_lightning.png 864 171 853 125 879 116 897 161 255 255 255 255|draww res/trail_lightning.png 874 218 864 171 897 161 914 206 255 255 255 255|draww res/trail_lightning.png 885 265 874 218 914 206 931 251 255 255 255 255|draww res/trail_lightning.png 896 303 885 265 931 251 942 289 255 255 255 255|draww res/trail_lightning.png 747 -128 732 -174 737 -175 757 -132 255 255 255 255|draww res/trail_lightning.png 757 -81 747 -128 757 -132 777 -88 255 255 255 255|draww res/trail_lightning.png 770 -34 757 -81 777 -88 796 -43 255 255 255 255|draww res/trail_lightning.png 780 12 770 -34 796 -43 816 0 255 255 255 255|draww res/trail_lightning.png 791 59 780 12 816 0 834 45 255 255 255 255|draww res/trail_lightning.png 808 116 791 59 834 45 854 101 255 255 255 255|draww res/trail_lightning.png 774 -392 754 -436 761 -440 789 -400 255 255 255 255|draww res/trail_lightning.png 791 -347 774 -392 789 -400 816 -360 255 255 255 255|draww res/trail_lightning.png 811 -303 791 -347 816 -360 842 -319 255 255 255 255|draww res/trail_lightning.png 827 -258 811 -303 842 -319 867 -278 255 255 255 255|draww res/trail_lightning.png 848 -212 827 -258 867 -278 891 -233 255 255 255 255|draww res/trail_lightning.png 757 -501 733 -542 742 -549 774 -513 255 255 255 255|draww res/trail_lightning.png 781 -459 757 -501 774 -513 806 -477 255 255 255 255|draww res/trail_lightning.png 803 -416 781 -459 806 -477 837 -439 255 255 255 255|draww res/trail_lightning.png 827 -375 803 -416 837 -439 867 -401 255 255 255 255|draww res/trail_lightning.png 837 -359 827 -375 867 -401 877 -385 255 255 255 255|draww res/trail_lightning.png 621 -711 592 -750 595 -754 634 -724 255 255 255 255|draww res/trail_lightning.png 650 -672 621 -711 634 -724 673 -695 255 255 255 255|draww res/trail_lightning.png 679 -634 650 -672 673 -695 710 -663 255 255 255 255|draww res/trail_lightning.png 712 -591 679 -634 710 -663 749 -622 255 255 255 255|draww res/trail_lightning.png 471 -829 436 -862 440 -869 484 -849 255 255 255 255|draww res/trail_lightning.png 503 -794 471 -829 484 -849 527 -823 255 255 255 255|draww res/trail_lightning.png 536 -756 503 -794 527 -823 568 -792 255 255 255 255|draww res/trail_lightning.png 391 -875 350 -901 352 -906 398 -891 255 255 255 255|draww res/trail_lightning.png 430 -846 391 -875 398 -891 444 -875 255 255 255 255|draww res/trail_lightning.png 469 -819 430 -846 444 -875 489 -856 255 255 255 255|draww res/trail_lightning.png 502 -794 469 -819 489 -856 528 -835 255 255 255 255|draww res/trail_lightning.png 290 -889 255 -922 265 -944 313 -928 255 255 255 255|draww res/trail_lightning.png 306 -876 290 -889 313 -928 332 -917 255 255 255 255|drawi res/particle_genericYellow.png 287 77 2 2 0 255 255 255 12 0|drawi res/particle_genericYellow.png 282 -553 9 9 0 255 255 255 56 0|drawi res/particle_genericYellow.png 275 -692 6 6 0 255 255 255 37 0|drawi res/particle_genericYellow.png 235 -644 10 10 -180 255 255 255 54 0|drawi res/particle_genericYellow.png 244 -699 8 8 -180 255 255 255 47 0|drawi res/particle_genericYellow.png 289 -274 5 5 0 255 255 255 31 0|drawi res/particle_genericYellow.png 247 -640 11 11 -180 255 255 255 64 0|drawi res/particle_genericYellow.png 283 -307 7 7 0 255 255 255 42 0|drawi res/particle_genericYellow.png 238 146 4 4 -180 255 255 255 21 0|drawi res/particle_genericWhite.png 1134 774 4 4 -69 255 255 255 24 0|drawi res/particle_genericWhite.png 1063 947 9 9 -78 255 255 255 49 0|drawi res/particle_genericWhite.png 1000 857 6 6 -117 255 255 255 35 0|drawi res/particle_genericYellow.png 237 509 11 11 -180 255 255 255 65 0|drawi res/particle_genericYellow.png 224 -445 13 13 -180 255 255 255 74 0|drawi res/particle_genericYellow.png 225 -479 5 5 -180 255 255 255 32 0|drawi res/particle_genericYellow.png 238 434 19 19 -180 255 255 255 106 0|drawi res/particle_genericYellow.png 282 390 11 11 0 255 255 255 64 0|drawi res/particle_genericYellow.png 260 -859 17 17 0 255 255 255 98 0|drawi res/particle_genericYellow.png 232 -455 15 15 -180 255 255 255 83 0|drawi res/particle_genericYellow.png 240 225 12 12 -180 255 255 255 65 0|drawi res/particle_genericYellow.png 224 -173 9 9 -180 255 255 255 52 0|drawi res/particle_genericYellow.png 227 -797 18 18 -180 255 255 255 100 0|drawi res/particle_genericYellow.png 221 -871 28 28 -180 255 255 255 157 0|drawi res/particle_genericYellow.png 227 -364 29 29 -180 255 255 255 166 0|drawi res/particle_genericYellow.png 231 518 25 25 -180 255 255 255 138 0|drawi res/particle_genericYellow.png 272 -758 28 28 0 255 255 255 158 0|drawi res/particle_genericYellow.png 273 -25 27 27 0 255 255 255 152 0|drawi res/particle_genericYellow.png 264 158 29 29 0 255 255 255 160 0|drawi res/particle_genericYellow.png 215 -151 30 30 -180 255 255 255 165 0|drawi res/particle_genericYellow.png 273 -275 28 28 0 255 255 255 152 0|drawi res/particle_genericYellow.png 235 356 29 29 -180 255 255 255 162 0|drawi res/particle_genericYellow.png 213 -783 29 29 -180 255 255 255 159 0|drawi res/particle_genericWhite.png 1248 777 22 22 -90 255 255 255 126 0|drawi res/particle_genericWhite.png 1225 687 25 25 -99 255 255 255 137 0|drawi res/particle_genericWhite.png 1262 596 23 23 -86 255 255 255 132 0|drawi res/particle_genericWhite.png 1327 621 18 18 -69 255 255 255 96 0|drawi res/particle_genericWhite.png 1263 567 30 30 -85 255 255 255 168 0|drawi res/particle_genericWhite.png 1237 779 29 29 -100 255 255 255 165 0|drawi res/particle_genericWhite.png 1249 791 30 30 -81 255 255 255 163 0|drawi res/particle_genericWhite.png 1249 719 27 27 -88 255 255 255 149 0|drawi res/particle_genericWhite.png 1255 790 33 33 -68 255 255 255 177 0|drawi res/particle_genericWhite.png 1223 635 24 24 -97 255 255 255 135 0|drawi res/particle_genericWhite.png 1327 626 26 26 -67 255 255 255 149 0|drawi res/particle_genericWhite.png 1225 699 26 26 -99 255 255 255 151 0|drawi res/particle_genericWhite.png 1257 665 21 21 -87 255 255 255 123 0|drawi res/particle_genericWhite.png 1304 710 29 29 -62 255 255 255 165 0|drawi res/particle_genericYellow.png 1146 805 9 9 165 255 255 255 84 0|drawi res/particle_genericYellow.png 1249 851 3 3 238 255 255 255 30 0|drawi res/particle_genericYellow.png 235 366 36 36 -180 255 255 255 207 0|drawi res/particle_genericYellow.png 222 -394 37 37 -180 255 255 255 205 0|drawi res/particle_genericYellow.png 222 -619 39 39 -180 255 255 255 221 0|drawi res/particle_genericYellow.png 255 -628 39 39 0 255 255 255 221 0|drawi res/particle_genericYellow.png 259 149 41 41 0 255 255 255 221 0|drawi res/particle_genericYellow.png 223 -658 39 39 -180 255 255 255 219 0|drawi res/particle_genericYellow.png 228 -639 38 38 -180 255 255 255 204 0|drawi res/particle_genericYellow.png 249 470 37 37 0 255 255 255 213 0|drawi res/particle_genericYellow.png 260 -324 38 38 0 255 255 255 215 0|drawi res/particle_genericYellow.png 222 -152 37 37 -180 255 255 255 208 0|drawi res/particle_genericYellow.png 997 672 18 18 159 255 255 255 200 0|drawi res/particle_genericYellow.png 1087 723 25 25 316 255 255 255 218 0|drawi res/particle_genericYellow.png 994 661 31 31 154 255 255 255 194 0|drawi res/particle_genericYellow.png 1056 646 23 23 86 255 255 255 198 0|drawi res/particle_genericYellow.png 1052 669 32 32 80 255 255 255 222 0|drawt 361~60 579 400 14 20 16 22 1 1 255 255 255 255 0 0 0 255|drawt ~60 565 400 10 15 10 15 1 1 255 255 0 255 0 0 0 255|drawt ~-160 565 400 10 16 13 17 2 1 255 255 0 255 0 0 0 255|drawt 370~1460 579 400 14 20 16 22 1 1 255 255 255 255 0 0 0 255|drawt ~1460 565 400 10 15 10 15 1 1 255 255 0 255 0 0 0 255|drawt ~1240 565 400 10 16 13 17 2 1 255 255 0 255 0 0 0 255|drawi res/blank.png 0 0 800 400 0 160 202 255 255 0|drawur 0 0 800 400 0 0 0 0|drawt ~400 36 400 22 40 28 42 0 1 255 255 235 255 0 0 0 255|drawt ~412 128 386 10 18 16 24 0 1 230 230 240 255 0 0 0 255|drawi res/blank.png 1120 0 800 400 0 160 202 255 255 0|drawur 1120 0 800 400 0 0 0 0|drawt ~1520 36 400 22 40 28 42 0 1 255 255 235 255 0 0 0 255|drawt ~1532 128 386 10 18 16 24 0 1 230 230 240 255 0 0 0 255|drawt GET READY!~760 539 400 24 0 28 24 1 1 255 255 255 255 0 0 0 255|drawt time until next spell selection: 11~760 24 400 16 24 18 26 1 1 255 255 255 255 0 0 0 255|drawr 16 46 48 48 40 40 80 255|drawr 16 133 48 48 40 40 80 255|drawr 16 220 48 48 40 40 80 255|drawr 16 308 48 48 40 40 80 255|drawr 1856 46 48 48 40 40 80 255|drawr 1856 133 48 48 40 40 80 255|drawr 1856 220 48 48 40 40 80 255|drawr 1856 308 48 48 40 40 80 255|");
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