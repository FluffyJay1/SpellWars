
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
		GameMap.drawFromImageData(g, "drawfn res/panel/redoutline.png 160 500 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 160 500 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 160 635 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 160 635 200 135 0 255 255 255 255 0|drawr 200 662 120 81 255 0 0 255|drawfn res/panel/redoutline.png 160 770 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 160 770 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 160 905 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 160 905 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 360 500 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 360 500 200 135 0 255 255 255 255 0|drawr 380 513 160 108 255 255 0 255|drawfn res/panel/redoutline.png 360 635 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 360 635 200 135 0 255 255 255 255 0|drawr 380 648 160 108 255 255 0 255|drawr 400 662 120 81 255 0 0 255|drawfn res/panel/redoutline.png 360 770 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 360 770 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 360 905 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 360 905 200 135 0 255 255 255 255 0|drawr 380 918 160 108 255 255 0 255|drawfn res/panel/redoutline.png 560 500 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 560 500 200 135 0 255 255 255 255 0|drawr 580 513 160 108 255 255 0 255|drawfn res/panel/redoutline.png 560 635 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 560 635 200 135 0 255 255 255 255 0|drawr 580 648 160 108 255 255 0 255|drawr 600 662 120 81 255 0 0 255|drawfn res/panel/redoutline.png 560 770 200 135 0 255 255 255 255 0|drawfn res/panel/cracked.png 560 770 200 135 0 255 255 255 255 0|drawr 580 783 160 108 255 255 0 255|drawfn res/panel/redoutline.png 560 905 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 560 905 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 760 500 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 760 500 200 135 0 255 255 255 255 0|drawr 780 513 160 108 255 255 0 255|drawfn res/panel/redoutline.png 760 635 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 760 635 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 760 770 200 135 0 255 255 255 255 0|drawfn res/panel/hole.png 760 770 200 135 0 255 255 255 255 0|drawfn res/panel/redoutline.png 760 905 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 760 905 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 960 500 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 960 500 200 135 0 255 255 255 255 0|drawr 980 513 160 108 255 255 0 255|drawfn res/panel/blueoutline.png 960 635 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 960 635 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 960 770 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 960 770 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 960 905 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 960 905 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 1160 500 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1160 500 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 1160 635 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1160 635 200 135 0 255 255 255 255 0|drawr 1180 648 160 108 255 255 0 255|drawfn res/panel/blueoutline.png 1160 770 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1160 770 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 1160 905 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1160 905 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 1360 500 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1360 500 200 135 0 255 255 255 255 0|drawr 1380 513 160 108 255 255 0 255|drawfn res/panel/blueoutline.png 1360 635 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1360 635 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 1360 770 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1360 770 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 1360 905 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1360 905 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 1560 500 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1560 500 200 135 0 255 255 255 255 0|drawr 1580 513 160 108 255 255 0 255|drawfn res/panel/blueoutline.png 1560 635 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1560 635 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 1560 770 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1560 770 200 135 0 255 255 255 255 0|drawfn res/panel/blueoutline.png 1560 905 200 135 0 255 255 255 255 0|drawfn res/panel/normal.png 1560 905 200 135 0 255 255 255 255 0|drawe 242 555 36 24 120 120 120 120|drawe 1642 555 36 24 120 120 120 120|drawe 664 691 25 17 120 120 120 120|drawe 600 676 25 17 120 120 120 120|drawe 570 662 25 17 120 120 120 120|drawe 716 648 25 17 120 120 120 120|drawe 1091 783 25 17 120 120 120 120|drawe 1108 740 25 17 120 120 120 120|drawe 1372 698 25 17 120 120 120 120|drawe 1413 558 25 17 120 120 120 120|drawe 1521 592 25 17 120 120 120 120|drawe 1629 558 25 17 120 120 120 120|drawr 230 602 39 10 120 150 185 255|drawi res/idgaf.png 240 530 40 34 0 220 180 90 255 0|drawi res/particle_explosion.png 358 457 201 201 0 255 255 255 255 0|drawi res/particle_explosion.png 593 449 217 217 0 255 255 255 255 0|drawi res/particle_genericYellow.png 661 621 32 32 0 255 255 255 255 0|drawi res/particle_genericYellow.png 597 298 32 32 0 255 255 255 255 0|drawi res/particle_genericYellow.png 566 29 32 32 0 255 255 255 255 0|drawi res/particle_genericYellow.png 713 -209 32 32 0 255 255 255 255 0|drawi res/particle_explosion.png 800 442 231 231 0 255 255 255 255 0|drawi res/particle_genericYellow.png 1104 -476 32 32 0 255 255 255 255 0|drawi res/particle_genericYellow.png 1088 -273 32 32 0 255 255 255 255 0|drawi res/particle_explosion.png 1040 433 248 248 0 255 255 255 255 0|drawi res/particle_explosion.png 1185 564 256 256 0 255 255 255 255 0|drawi res/particle_explosion.png 1281 425 264 264 0 255 255 255 255 0|drawi res/particle_genericYellow.png 1410 -874 32 32 0 255 255 255 255 0|drawi res/particle_genericYellow.png 1518 -895 32 32 0 255 255 255 255 0|drawi res/particle_genericYellow.png 1369 -642 32 32 0 255 255 255 255 0|drawr 1630 602 15 10 120 150 185 255|drawi res/idgaf.png 1640 530 40 34 0 220 180 90 255 2|drawi res/particle_explosion.png 1537 429 256 256 0 255 255 255 255 0|drawi res/particle_genericYellow.png 1626 -948 32 32 0 255 255 255 255 0|draww res/trail_lightning.png 286 624 309 581 312 583 295 628 255 255 255 255|draww res/trail_lightning.png 263 666 286 624 295 628 278 673 255 255 255 255|draww res/trail_lightning.png 244 705 263 666 278 673 261 712 255 255 255 255|draww res/trail_lightning.png 783 315 801 271 805 272 792 319 255 255 255 255|draww res/trail_lightning.png 762 359 783 315 792 319 777 364 255 255 255 255|draww res/trail_lightning.png 743 403 762 359 777 364 764 411 255 255 255 255|draww res/trail_lightning.png 725 448 743 403 764 411 751 457 255 255 255 255|draww res/trail_lightning.png 707 493 725 448 751 457 739 503 255 255 255 255|draww res/trail_lightning.png 689 537 707 493 739 503 726 550 255 255 255 255|draww res/trail_lightning.png 671 582 689 537 726 550 714 596 255 255 255 255|draww res/trail_lightning.png 669 584 671 582 714 596 715 598 255 255 255 255|draww res/trail_lightning.png 734 38 759 -2 762 -1 743 42 255 255 255 255|draww res/trail_lightning.png 710 79 734 38 743 42 724 86 255 255 255 255|draww res/trail_lightning.png 685 121 710 79 724 86 708 132 255 255 255 255|draww res/trail_lightning.png 662 163 685 121 708 132 690 176 255 255 255 255|draww res/trail_lightning.png 641 206 662 163 690 176 674 221 255 255 255 255|draww res/trail_lightning.png 617 248 641 206 674 221 658 267 255 255 255 255|draww res/trail_lightning.png 609 263 617 248 658 267 652 283 255 255 255 255|draww res/trail_lightning.png 713 -181 742 -219 748 -216 724 -174 255 255 255 255|draww res/trail_lightning.png 684 -143 713 -181 724 -174 702 -131 255 255 255 255|draww res/trail_lightning.png 656 -104 684 -143 702 -131 679 -89 255 255 255 255|draww res/trail_lightning.png 627 -65 656 -104 679 -89 658 -46 255 255 255 255|draww res/trail_lightning.png 600 -25 627 -65 658 -46 636 -3 255 255 255 255|draww res/trail_lightning.png 573 14 600 -25 636 -3 614 38 255 255 255 255|draww res/trail_lightning.png 582 -2 573 14 614 38 624 22 255 255 255 255|draww res/trail_lightning.png 850 -379 883 -414 888 -410 862 -369 255 255 255 255|draww res/trail_lightning.png 817 -343 850 -379 862 -369 838 -328 255 255 255 255|draww res/trail_lightning.png 787 -305 817 -343 838 -328 812 -287 255 255 255 255|draww res/trail_lightning.png 756 -268 787 -305 812 -287 788 -246 255 255 255 255|draww res/trail_lightning.png 725 -231 756 -268 788 -246 765 -204 255 255 255 255|draww res/trail_lightning.png 729 -238 725 -231 765 -204 769 -211 255 255 255 255|draww res/trail_lightning.png 1173 -416 1201 -456 1209 -451 1192 -406 255 255 255 255|draww res/trail_lightning.png 1146 -376 1173 -416 1192 -406 1173 -362 255 255 255 255|draww res/trail_lightning.png 1120 -335 1146 -376 1173 -362 1155 -317 255 255 255 255|draww res/trail_lightning.png 1095 -294 1120 -335 1155 -317 1138 -272 255 255 255 255|draww res/trail_lightning.png 1097 -298 1095 -294 1138 -272 1140 -277 255 255 255 255|draww res/trail_lightning.png 1203 -591 1237 -625 1246 -617 1220 -577 255 255 255 255|draww res/trail_lightning.png 1170 -555 1203 -591 1220 -577 1195 -536 255 255 255 255|draww res/trail_lightning.png 1137 -519 1170 -555 1195 -536 1171 -494 255 255 255 255|draww res/trail_lightning.png 1119 -500 1137 -519 1171 -494 1158 -471 255 255 255 255|draww res/trail_lightning.png 1444 -742 1479 -777 1481 -775 1460 -731 255 255 255 255|draww res/trail_lightning.png 1413 -705 1444 -742 1460 -731 1439 -688 255 255 255 255|draww res/trail_lightning.png 1382 -667 1413 -705 1439 -688 1420 -644 255 255 255 255|draww res/trail_lightning.png 1376 -659 1382 -667 1420 -644 1417 -635 255 255 255 255|draww res/trail_lightning.png 1468 -903 1516 -914 1521 -899 1482 -872 255 255 255 255|draww res/trail_lightning.png 1421 -884 1468 -903 1482 -872 1445 -842 255 255 255 255|draww res/trail_lightning.png 1429 -889 1421 -884 1445 -842 1452 -847 255 255 255 255|draww res/trail_lightning.png 1610 -929 1659 -935 1660 -932 1617 -908 255 255 255 255|draww res/trail_lightning.png 1562 -916 1610 -929 1617 -908 1577 -882 255 255 255 255|draww res/trail_lightning.png 1537 -909 1562 -916 1577 -882 1560 -867 255 255 255 255|draww res/trail_lightning.png 1655 -956 1660 -954 1659 -910 1653 -908 255 255 255 255|drawi res/particle_genericYellow.png 1690 331 6 6 0 255 255 255 37 0|drawi res/particle_genericYellow.png 1623 -616 3 3 -180 255 255 255 16 0|drawi res/particle_genericYellow.png 1650 256 4 4 -180 255 255 255 26 0|drawi res/particle_genericWhite.png 581 701 3 3 -118 255 255 255 18 0|drawi res/particle_genericWhite.png 629 756 1 1 -106 255 255 255 8 0|drawi res/particle_genericYellow.png 1691 -256 5 5 0 255 255 255 32 0|drawi res/particle_genericYellow.png 1676 -576 1 1 0 255 255 255 7 0|drawi res/particle_genericYellow.png 1684 -702 13 13 0 255 255 255 71 0|drawi res/particle_genericYellow.png 1640 -532 11 11 -180 255 255 255 62 0|drawi res/particle_genericYellow.png 1674 -848 8 8 0 255 255 255 43 0|drawi res/particle_genericYellow.png 1673 -334 13 13 0 255 255 255 73 0|drawi res/particle_genericYellow.png 1639 286 11 11 -180 255 255 255 64 0|drawi res/particle_genericYellow.png 1682 146 9 9 0 255 255 255 50 0|drawi res/particle_genericYellow.png 1621 -774 16 16 -180 255 255 255 94 0|drawi res/particle_genericYellow.png 1669 -498 25 25 0 255 255 255 140 0|drawi res/particle_genericYellow.png 1637 97 21 21 -180 255 255 255 120 0|drawi res/particle_genericYellow.png 1616 125 23 23 -180 255 255 255 134 0|drawi res/particle_genericYellow.png 1657 -364 25 25 0 255 255 255 142 0|drawi res/particle_genericYellow.png 1627 161 18 18 -180 255 255 255 106 0|drawi res/particle_genericYellow.png 1656 -252 24 24 0 255 255 255 130 0|drawi res/particle_genericYellow.png 1619 433 19 19 -180 255 255 255 109 0|drawi res/particle_genericYellow.png 1677 72 19 19 0 255 255 255 107 0|drawi res/particle_genericYellow.png 1674 -670 23 23 0 255 255 255 130 0|drawi res/particle_genericYellow.png 1656 -230 36 36 0 255 255 255 193 0|drawi res/particle_genericYellow.png 1668 -309 34 34 0 255 255 255 190 0|drawi res/particle_genericYellow.png 1656 474 35 35 0 255 255 255 194 0|drawi res/particle_genericYellow.png 1618 229 32 32 -180 255 255 255 182 0|drawi res/particle_genericYellow.png 1662 -187 33 33 0 255 255 255 187 0|drawi res/particle_genericYellow.png 1658 279 30 30 0 255 255 255 171 0|drawi res/particle_genericYellow.png 1632 -349 35 35 -180 255 255 255 196 0|drawi res/particle_genericYellow.png 1624 -748 32 32 -180 255 255 255 172 0|drawi res/particle_genericYellow.png 1667 -801 35 35 0 255 255 255 192 0|drawi res/particle_genericYellow.png 1671 -931 32 32 0 255 255 255 176 0|drawi res/particle_genericYellow.png 267 650 6 6 70 255 255 255 61 0|drawi res/particle_genericYellow.png 154 697 19 19 182 255 255 255 124 0|drawi res/particle_genericYellow.png 263 610 20 20 75 255 255 255 152 0|drawi res/particle_genericYellow.png 302 718 22 22 336 255 255 255 145 0|drawi res/particle_genericYellow.png 254 703 16 16 318 255 255 255 105 0|drawi res/particle_genericYellow.png 1638 42 46 46 0 255 255 255 247 0|drawi res/particle_genericYellow.png 1634 259 43 43 -180 255 255 255 243 0|drawi res/particle_genericYellow.png 1634 390 43 43 -180 255 255 255 247 0|drawi res/particle_genericYellow.png 1633 -356 43 43 -180 255 255 255 244 0|drawi res/particle_genericYellow.png 1637 -147 43 43 -180 255 255 255 247 0|drawi res/particle_genericYellow.png 1640 -770 43 43 0 255 255 255 244 0|drawi res/particle_genericYellow.png 1642 374 45 45 0 255 255 255 244 0|drawi res/particle_genericYellow.png 1644 -687 44 44 0 255 255 255 247 0|drawi res/particle_genericYellow.png 1640 -461 45 45 0 255 255 255 246 0|drawi res/particle_genericYellow.png 1639 120 45 45 0 255 255 255 245 0|drawt 400~60 579 400 14 20 16 22 1 1 255 255 255 255 0 0 0 255|drawt ~60 565 400 10 15 10 15 1 1 255 255 0 255 0 0 0 255|drawt ~-160 565 400 10 16 13 17 2 1 255 255 0 255 0 0 0 255|drawt 385~1460 579 400 14 20 16 22 1 1 255 255 255 255 0 0 0 255|drawt ~1460 565 400 10 15 10 15 1 1 255 255 0 255 0 0 0 255|drawt ~1240 565 400 10 16 13 17 2 1 255 255 0 255 0 0 0 255|drawi res/blank.png 0 0 800 400 0 160 202 255 255 0|drawur 0 0 800 400 0 0 0 0|drawt ~400 36 400 22 40 28 42 0 1 255 255 235 255 0 0 0 255|drawt ~412 128 386 10 18 16 24 0 1 230 230 240 255 0 0 0 255|drawi res/blank.png 1120 0 800 400 0 160 202 255 255 0|drawur 1120 0 800 400 0 0 0 0|drawt ~1520 36 400 22 40 28 42 0 1 255 255 235 255 0 0 0 255|drawt ~1532 128 386 10 18 16 24 0 1 230 230 240 255 0 0 0 255|drawt GET READY!~760 539 400 24 0 28 24 1 1 255 255 255 255 0 0 0 255|drawt time until next spell selection: 10~760 24 400 16 24 18 26 1 1 255 255 255 255 0 0 0 255|drawr 16 46 48 48 40 40 80 255|drawr 16 133 48 48 40 40 80 255|drawr 16 220 48 48 40 40 80 255|drawr 16 308 48 48 40 40 80 255|drawr 1856 46 48 48 40 40 80 255|drawr 1856 133 48 48 40 40 80 255|drawr 1856 220 48 48 40 40 80 255|drawr 1856 308 48 48 40 40 80 255|");
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