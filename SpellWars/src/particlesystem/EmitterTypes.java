package particlesystem;

public enum EmitterTypes{
	POINT_RADIAL,				//NO KEYVALUES
	
	POINT_DIRECTION,	//KEYVALUE 1: DIRECTION (IN DEGREES)
						//KEYVALUE 2: SPREAD (IN DEGREES)
	
	CIRCLE_RADIAL,		//KEYVALUE 1: MIN RADIUS (IN PIXELS)
						//KEYVALUE 2: MAX RADIUS (IN PIXELS)
	
	CIRCLE_DIRECTION,	//KEYVALUE 1: MIN RADIUS (IN PIXELS)
						//KEYVALUE 2: MAX RADIUS (IN PIXELS)
						//KEYVALUE 3: DIRECTION (IN DEGREES)
						//KEYVALUE 4: SPREAD (IN DEGREES)
	
	CIRCLE_RANDOM,		//KEYVALUE 1: MIN RADIUS (IN PIXELS)
						//KEYVALUE 2: MAX RADIUS (IN PIXELS)
	
	LINE_RADIAL,		//KEYVALUE 1: OTHER POINT X
						//KEYVALUE 2: OTHER POINT Y
						//KEYVALUE 3: MIN WIDTH (IN PIXELS)
						//KEYVALUE 4: MAX WIDTH (IN PIXELS)
	
	LINE_RANDOM			//KEYVALUE 1: OTHER POINT X
						//KEYVALUE 2: OTHER POINT Y
						//KEYVALUE 3: MIN WIDTH (IN PIXELS)
						//KEYVALUE 4: MAX WIDTH (IN PIXELS)
}
/*
 * Basic explanation of the types:
 * 	POINT_RADIAL
 * All particles come from a point and go outwards
 * 
 * 	POINT_DIRECTION
 * Particle start from a point and travel in a direction
 * 
 * 	CIRCLE_RADIAL
 * Particle start inside a circle, and if min radius > 0, then they'll spawn in a donut shape, and they'll travel facing away from the center
 * 
 * 	CIRCLE_RANDOM
 * Particles start inside a circle, and if the min radius > 0, they'll spawn in a donut shape, and they'll travel in random directions
 * 
 * 	LINE_RADIAL
 * Particles start at a certain distance from a line and travel outwards
 * 
 * 	LINE_RANDOM
 * Particles start at a certain distance from a line and travel in random directions
 */