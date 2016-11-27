package spell;

import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Panel;
import mechanic.PanelState;
import mechanic.Point;
import particlesystem.EmitterTypes;
import particlesystem.ParticleEmitter;
import particlesystem.Trail;
import projectile.Grenade;
import projectile.Projectile;
import unit.Unit;

public class ForgeSpiritFire extends Spell {
	public ForgeSpiritFire(Unit owner) {
		super(owner, 0.25f, 0, "Fire", "Fire a generic projectile", "res/particle_explosion.png", false );
	}
	@Override
	public void onActivate() {
		//String i = "res/particle_genericRed.png";
		/*
		float direction = 50;
		if(this.owner.direction == GameMap.ID_LEFT) {
			direction = 130;
		}
		ParticleEmitter pe = new ParticleEmitter(Point.add(this.owner.getLoc(), new Point(0, -80)), EmitterTypes.POINT_DIRECTION, GameMap.particle_genericRed, false, //point/parent, emitter type, image path, alphaDecay
				3.5f, 1.5f, //particle start scale
				0.0f, 0.0f, //particle end scale
				10.5f, //drag
				0, 0, //rotational velocity
				0.1f, 0.45f, //min and max lifetime
				200, 3200, //min and max launch speed
				0, 5, //emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles, if emitter lifetime is -1, then it lasts forever)
				direction, 40, 0, 0); //keyvalues
		this.getMap().addParticleEmitter(pe);
		*/
		Projectile projectile = new Projectile(25 * this.owner.finalDamageOutputModifier, 8, GameMap.clampDirection((char) (GameMap.getOppositeDirection((char)this.owner.teamID))), this.owner.gridLoc, "res/particle_explosion.png", this.owner.teamID, true, true, true);
		this.map.addGameElement(projectile);
		//projectile = new Grenade(50, 3, 4, 80, this.owner.direction, this.owner.gridLoc, "res/particle_genericRed.png", this.owner.teamID);
		//this.map.addGameElement(projectile);
		
		//Projectile projectilep;
		//projectilep = new Grenade(20, 0.8 * Point.getDistance(new Point(), Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.direction), 4)), Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.direction), 4), 80, this.owner.gridLoc, "res/particle_genericYellow.png", this.owner.teamID);
		//this.map.addGameElement(projectilep);
		
		/*
		//Point randomPoint = Point.proximity8(Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.direction), 4)).get(Point.roundToNearestInteger(Math.random() * 7.99 - 0.5));
		for(Point p : Point.proximity4(Point.scale(GameMap.getFuturePoint(new Point(), (char)this.owner.direction), 4))) {
		//for(Point p : Point.proximity4(randomPoint)) {
			projectile = new Grenade(20, 1.25, p, 80, 10, this.owner.gridLoc, "res/particle_genericYellow.png", this.owner.teamID);
			projectile.setImageScale(2);
			this.map.addGameElement(projectile);
		}
		*/
		this.finishSpell();
	}
}
