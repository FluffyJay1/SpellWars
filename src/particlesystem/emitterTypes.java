package particlesystem;

public enum emitterTypes{
	POINT,				//NO KEYVALUES
	
	POINT_DIRECTION,	//KEYVALUE 1: DIRECTION (IN DEGREES)
						//KEYVALUE 2: SPREAD (IN DEGREES)
	
	CIRCLE_RANDOM,		//KEYVALUE 1: MIN RADIUS (IN PIXELS)
						//KEYVALUE 2: MAX RADIUS (IN PIXELS)
	LINE_RANDOM
						//KEYVALUE 1: OTHER POINT X
						//KEYVALUE 2: OTHER POINT Y
						//KEYVALUE 3: WIDTH (IN PIXELS)
}
/*
 * Basic explanation of the types:
 * 	POINT
 * All particles come from a point
 * 
 * 	POINT_DIRECTION
 * Particle start from a point and travel in a direction
 * 
 * 	CIRCLE_RANDOM
 * Particles start inside a circle, and if the min radius isn't 0, they'll spawn in a donut shape
 * 
 * 	LINE_RANDOM
 * Particles start anywhere on a line between two points
 */