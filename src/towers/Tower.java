
package towers;
import java.util.ArrayList;

import particlesystem.ParticleEmitter;
import particlesystem.emitterTypes;
import projectiles.Laser;
import mechanic.GameElement;
import mechanic.GameMap;
import mechanic.Point;
import monsters.Monster;

public class Tower extends GameElement {
	private GameMap map;
	
	ArrayList<GameElement> elementsInRange = new ArrayList<GameElement>();
	
	private float attackRange;		//in pixels
	private float baseAttackTime;	//in seconds
	private float baseAttackSpeed;		//in percent, the tower's base attack speed (usually 100)
	private float finalAttackSpeed;		//in percent, after all the bonuses from buffs
	private float attackInterval;	//for internal use, in seconds, calculated each time attack speed changes
	private float attackCooldown = 0 ;	//for internal use, decremented each frame and checks if cooldown < 0
	private GameElement attackTarget;		//its target
	/*
	 * Explanation of base attack time and attack speed:
	 * 
	 * Each tower has a certain base attack time in seconds. 
	 * If the attack speed is at 100 (which is the default), then the tower will attack once every (base attack time) seconds.
	 * If the attack speed is at 200 (which is gained through upgrades or auras or whatever), then the tower will attack once every (base attack time / 2) seconds, or twice as often.
	 * Basically the formula can be thought like this:
	 * Time between attack = (base attack time)/(attack speed / 100)
	 * and:
	 * DPS = (attack damage) / ((base attack time)/(attack speed / 100))
	 * or just
	 * DPS = (attack damage) * (attack speed / 100) / (base attack time)
	 * 
	 * This way we gain an easy way to stack attack speed bonuses while also gaining leverage over their properties (i.e. a cannon that attacks once every 20 seconds won't suddenly become OP if we give it attack speed since we can just set its base attack time really high)
	 */
	public Tower(double x, double y, GameMap map, float attackRange, float baseAttackTime, float baseAttackSpeed){
		super();
		this.changeLoc(new Point(x,y));
		this.map = map;
		this.attackRange = attackRange;
		this.baseAttackTime = baseAttackTime;
		this.baseAttackSpeed = baseAttackSpeed;
	}
	
	/*
	 * Updates the tower's elements in range list
	 * 
	 * For use in towers that do something in an area around them (like a slow tower)
	 */
	public void updateElementInRangeList(){
		elementsInRange.clear();
		for(GameElement e : map.getElements()) {
			if((float)Point.getDistance(e.getLoc(), this.getLoc()) <= this.attackRange) {
				elementsInRange.add(e);
			}
		}
	}
	
	/*
	 * Finds a monster in range and sets that as its target
	 * 
	 * Should be called only when its old target dies or runs out of range
	 * 
	 * Also it will target the enemy that's the first on the list, meaning that theoretically it will target the front enemy
	 */
	public void acquireTarget() {
		this.attackTarget = null;
		for(GameElement e : map.getElements()) {
			if((float)Point.getDistance(e.getLoc(), this.getLoc()) <= this.attackRange && e instanceof Monster) {
				this.attackTarget = e;
				System.out.println("TARGET FOUND B0SS");
				break;
			}
		}
	}
	
	/*
	 * Checks if its target can no longer be targeted (if it's ded or it's out of range)
	 */
	public boolean targetIsTargetable(){
		if(this.attackTarget == null || map.getElements().contains(this.attackTarget) == false || Point.getDistance(this.attackTarget.getLoc(), this.getLoc()) > this.attackRange) {
			return false;
		} else {
			return true;
		}
	}
	
	/*
	 * Makes the tower attack, if there is a target
	 */
	public void attack() {
		if(this.targetIsTargetable() == false) { //if target is outside of range or it ded
			this.acquireTarget();
		}
		if(this.attackTarget != null && map.getElements().contains(this.attackTarget)) { //if there is a target
			map.addProjectile(new Laser(this, this.attackTarget)); //TODO this should be changed to be more modifiable in terms of damage
			
			/*THINGS BELOW ARE COMPLETELY UNNECESSARY*/
			String i = "res/explosion.png";
			ParticleEmitter pe = new ParticleEmitter(this.getLoc(), emitterTypes.LINE_RADIAL, i, true, /*point, emitter type, image path, alphaDecay*/
					0.1f, 0.1f, /*particle start scale*/
					0.1f, 0.1f, /*particle end scale*/
					1.5f, /*drag*/
					0, 0, /*rotational velocity*/
					0.6f, 0.9f, /*min and max lifetime*/
					20, 30, /*min and max launch speed*/
					0, 10, /*emitter lifetime, emission rate (if emitter lifetime is 0, then it becomes instant and emission rate becomes number of particles)*/
					(float)attackTarget.getLoc().getX(), (float)attackTarget.getLoc().getY(), 0, 0, map); /*keyvalues and map*/
			map.addParticleEmitter(pe);
			/*THINGS ABOVE ARE COMPLETELY UNNECESSARY*/
		}
		this.attackCooldown = this.attackInterval;
	}
	
	public void updateAttackSpeed() {
		this.finalAttackSpeed = this.baseAttackSpeed;
		/*
		 * INITIALIZING PSEUDO CODE
		 * FOR EACH BUFF IN ITS ARRAYLIST OF BUFFS
		 * 	ADD TO THE FINAL ATTACK SPEED
		 */
		this.attackInterval = this.baseAttackTime * 100 / this.finalAttackSpeed;
	}
	
	
	/*
	 * All towers have access to the acquire target, attack, etc. functions
	 * They can override them as they will depending on the tower
	 * (e.g. for a slow tower, it will override the attack function with its own code of applying a slow debuff)
	 */
	
	
	/*
	 * This is the basic loop that each tower should go through
	 * 
	 * 1. Update its attack speed
	 * 2. If it's time to attack, then it will try to attack a target, else it will decrement its attack cooldown
	 * 3. Go to step 1
	 */
	
	@Override
	public void update() {
		this.updateAttackSpeed();
		if(this.attackCooldown > 0) {
			this.attackCooldown -= this.getFrameTime();
		} else {
			this.attack();
		}
	}
	
	public float getAttackRange(){
		return this.attackRange;
	}
	public float getAttackSpeed(){
		return this.finalAttackSpeed;
	}
	public float getBaseAttackTime(){
		return this.baseAttackTime;
	}
	public GameElement getTarget(){
		return this.attackTarget;
	}
}
