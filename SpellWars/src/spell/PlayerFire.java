package spell;

import mechanic.GameMap;
import mechanic.Point;
import projectile.ElectroBoltProjectile;
import projectile.Grenade;
import projectile.KnifeProjectile;
import projectile.Projectile;
import unit.Unit;

public class PlayerFire extends Spell {
	public PlayerFire(Unit owner) {
		super(owner, 0.25f, 0.05f, "Player Fire", "Generic fire, used by player", "res/xmoney.png", false);
	}
	@Override
	public void onActivate() {
		Projectile projectile = new Projectile(5 * this.owner.finalDamageModifier, 9, GameMap.clampDirection((char) (GameMap.getOppositeDirection((char)this.owner.teamID))), this.owner.gridLoc, "res/particle_genericWhite.png", this.owner.teamID, true, true, true);
		//Projectile projectile = new Grenade(20, 0.5, Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.direction), 4), 80, 10, this.owner.gridLoc, "res/particle_genericYellow.png", this.owner.teamID);
		projectile.setDrawHeight(10);
		projectile.setImageScale(2);
		projectile.drawShadow = true;
		projectile.setFlashPanel(false);
		this.map.addGameElement(projectile);
		this.finishSpell();
	}
}
