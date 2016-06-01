package projectile;

import mechanic.GameMap;
import mechanic.Panel;
import mechanic.Point;

public class BouncingOrbProjectile extends Projectile {
	static final boolean IGNORE_HOLES = false;
	static final boolean DEBUG = false;
	int numSquares;
	boolean clearUnitsHitNextTime;
	public BouncingOrbProjectile(double damage, double speed, Point gridLoc, int teamID, int numSquares) {
		super(damage, speed, new Point(GameMap.getFuturePoint(new Point(), (char)teamID).x * -1, 1), gridLoc, "res/betterTower.png", teamID, false, true, IGNORE_HOLES);
		//this.setVel(new Point(GameMap.getFuturePoint(new Point(), (char)teamID).x * -1, 1));
		this.numSquares = numSquares;
		this.clearUnitsHitNextTime = false;
		this.setDrawHeight(12);
		this.setImageScale(2);
		this.drawShadow = true;
	}
	@Override
	public void onProjectileUpdate() {
		if(this.clearUnitsHitNextTime && this.thinkTimer/this.thinkInterval < 0.5) {
			this.unitsHit.clear();
			this.clearUnitsHitNextTime = false;
		}
	}
	@Override
	public void onThink() {
		/* deprecated because it would hit some people twice but not others based on position
		if(this.clearUnitsHitNextTime) {
			this.unitsHit.clear();
			this.clearUnitsHitNextTime = false;
		}
		*/
		if(this.numSquares <= 0) {
			this.setRemove(true);
		} else {
			this.numSquares--;
		}
		Panel topPanel = this.getMap().getPanelAt(Point.add(this.getGridLoc(), new Point(0, -1)));
		Panel toprightPanel = this.getMap().getPanelAt(Point.add(this.getGridLoc(), new Point(1, -1)));
		Panel rightPanel = this.getMap().getPanelAt(Point.add(this.getGridLoc(), new Point(1, 0)));
		Panel bottomrightPanel = this.getMap().getPanelAt(Point.add(this.getGridLoc(), new Point(1, 1)));
		Panel bottomPanel = this.getMap().getPanelAt(Point.add(this.getGridLoc(), new Point(0, 1)));
		Panel bottomleftPanel = this.getMap().getPanelAt(Point.add(this.getGridLoc(), new Point(-1, 1)));
		Panel leftPanel = this.getMap().getPanelAt(Point.add(this.getGridLoc(), new Point(-1, 0)));
		Panel topleftPanel = this.getMap().getPanelAt(Point.add(this.getGridLoc(), new Point(-1, -1)));
		if(this.getMap().getPanelAt(this.getGridLoc()) != null && this.getMap().getPanelAt(this.getGridLoc()).teamID != this.teamID) {
			if(this.vel.y == -1) {
				if(topPanel != null) {
					if(topPanel.teamID == this.teamID) {
						this.vel.y *= -1;
						this.clearUnitsHitNextTime = true;
						if(DEBUG)
						System.out.println("1");
					}
				} else {
					this.vel.y *= -1;
					this.clearUnitsHitNextTime = true;
					if(DEBUG)
					System.out.println("2");
				}
			}
			if(this.vel.y == 1) {
				if(bottomPanel != null) {
					if(bottomPanel.teamID == this.teamID) {
						this.vel.y *= -1;
						this.clearUnitsHitNextTime = true;
						if(DEBUG)
						System.out.println("3");
					}
				} else {
					this.vel.y *= -1;
					this.clearUnitsHitNextTime = true;
					if(DEBUG)
					System.out.println("4");
				}
			}
			if(this.vel.x == -1) {
				if(leftPanel != null) {
					if(leftPanel.teamID == this.teamID) {
						this.vel.x *= -1;
						this.clearUnitsHitNextTime = true;
						if(DEBUG)
						System.out.println("5");
					}
				} else {
					this.vel.x *= -1;
					this.clearUnitsHitNextTime = true;
					if(DEBUG)
					System.out.println("6");
				}
			}
			if(this.vel.x == 1) {
				if(rightPanel != null) {
					if(rightPanel.teamID == this.teamID) {
						this.vel.x *= -1;
						this.clearUnitsHitNextTime = true;
						if(DEBUG)
						System.out.println("7");
					}
				} else {
					this.vel.x *= -1;
					this.clearUnitsHitNextTime = true;
					if(DEBUG)
					System.out.println("8");
				}
			}
			//IF TOP AND BOTTOM ARE UNACCESSIBLE
			if((topPanel == null || topPanel.teamID == this.teamID) && (bottomPanel == null || bottomPanel.teamID == this.teamID)) {
				this.vel.y = 0;
				if(DEBUG)
				System.out.println("9");
				//this.clearUnitsHitNextTime = true;
			}
			//IF LEFT AND RIGHT ARE UNACCESSIBLE
			if((leftPanel == null || leftPanel.teamID == this.teamID) && (rightPanel == null || rightPanel.teamID == this.teamID)) {
				this.vel.x = 0;
				if(DEBUG)
				System.out.println("10");
				//this.clearUnitsHitNextTime = true;
			}
			//Top left corner
			if(leftPanel != null && leftPanel.teamID != this.teamID 
					&& topPanel != null && topPanel.teamID != this.teamID 
					&& this.vel.x == -1 && this.vel.y == -1
					&& topleftPanel != null && topleftPanel.teamID == this.teamID) {
				this.vel.x = 1;
				this.vel.y = 1;
				if(DEBUG)
				System.out.println("11");
				if(bottomPanel == null || bottomPanel.teamID == this.teamID) {
					this.vel.y = -1;
					if(DEBUG)
					System.out.println("12");
				}
				if(rightPanel == null || rightPanel.teamID == this.teamID) {
					this.vel.x = -1;
					if(DEBUG)
					System.out.println("13");
				}
				this.clearUnitsHitNextTime = true;
				//Bottom left corner AND topleft
				if(leftPanel != null && leftPanel.teamID != this.teamID 
						&& bottomPanel != null && bottomPanel.teamID != this.teamID 
						&& this.vel.x == -1 && this.vel.y == 1
						&& bottomleftPanel != null && bottomleftPanel.teamID == this.teamID) {
					this.vel.y = 0;
					if(DEBUG)
					System.out.println("14");
				}
			}
			//Bottom left corner
			if(leftPanel != null && leftPanel.teamID != this.teamID 
					&& bottomPanel != null && bottomPanel.teamID != this.teamID 
					&& this.vel.x == -1 && this.vel.y == 1
					&& bottomleftPanel != null && bottomleftPanel.teamID == this.teamID) {
				this.vel.x = 1;
				this.vel.y = -1; 
				if(DEBUG)
				System.out.println("15");
				if(topPanel == null || topPanel.teamID == this.teamID) {
					this.vel.y = 1;
					if(DEBUG)
					System.out.println("16");
				}
				if(rightPanel == null || rightPanel.teamID == this.teamID) {
					this.vel.x = -1;
					if(DEBUG)
					System.out.println("17");
				}
				this.clearUnitsHitNextTime = true;
				//Top left corner AND bottomleft
				if(leftPanel != null && leftPanel.teamID != this.teamID 
						&& topPanel != null && topPanel.teamID != this.teamID 
						&& this.vel.x == -1 && this.vel.y == -1
						&& topleftPanel != null && topleftPanel.teamID == this.teamID) {
					this.vel.y = 0;
					if(DEBUG)
					System.out.println("18");
				}
			}
			//Bottom right corner
			if(rightPanel != null && rightPanel.teamID != this.teamID 
					&& bottomPanel != null && bottomPanel.teamID != this.teamID 
					&& this.vel.x == 1 && this.vel.y == 1
					&& bottomrightPanel != null && bottomrightPanel.teamID == this.teamID) {
				this.vel.x = -1;
				this.vel.y = -1; 
				if(DEBUG)
				System.out.println("19");
				if(topPanel == null || topPanel.teamID == this.teamID) {
					this.vel.y = 1;
					if(DEBUG)
					System.out.println("20");
				}
				if(leftPanel == null || leftPanel.teamID == this.teamID) {
					this.vel.x = 1;
					if(DEBUG)
					System.out.println("21");
				}
				this.clearUnitsHitNextTime = true;
			}
			//Top right corner
			if(rightPanel != null && rightPanel.teamID != this.teamID 
					&& topPanel != null && topPanel.teamID != this.teamID 
					&& this.vel.x == 1 && this.vel.y == -1
					&& toprightPanel != null && toprightPanel.teamID == this.teamID) {
				this.vel.x = -1;
				this.vel.y = 1; 
				if(DEBUG)
				System.out.println("22");
				if(bottomPanel == null || bottomPanel.teamID == this.teamID) {
					this.vel.y = -1;
					if(DEBUG)
					System.out.println("23");
				}
				if(leftPanel == null || leftPanel.teamID == this.teamID) {
					this.vel.x = 1;
					if(DEBUG)
					System.out.println("24");
				}
				this.clearUnitsHitNextTime = true;
			}
			//SPECIAL CASES
			if(this.vel.y != 0 && (leftPanel == null || leftPanel.teamID == this.teamID) && (toprightPanel == null || toprightPanel.teamID == this.teamID) && (bottomrightPanel == null || bottomrightPanel.teamID == this.teamID)
					&& (rightPanel == null || rightPanel.teamID == this.teamID) && (topleftPanel == null || topleftPanel.teamID == this.teamID) && (bottomleftPanel == null || bottomleftPanel.teamID == this.teamID)) {
				this.vel.x = 0;
				if(DEBUG)
				System.out.println("25");
			}
			
		} else {
			if(topPanel == null && this.vel.y == -1) {
				this.vel.y = 1;
			}
			if(bottomPanel == null && this.vel.y == 1) {
				this.vel.y = -1;
			}
			if(leftPanel == null && this.vel.x == -1) {
				this.vel.x = 1;
			}
			if(rightPanel == null && this.vel.x == 1) {
				this.vel.x = -1;
			}
		}
	}
}
