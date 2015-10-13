package particlesystem;

public enum emitterTypes{
	POINT,				//NO KEYVALUES
	CIRCLE_RANDOM,		//KEYVALUE 1: MIN RADIUS
						//KEYVALUE 2: MAX RADIUS
	LINE_RANDOM
						//KEYVALUE 1: POINT 1 X
						//KEYVALUE 2: POINT 1 Y
						//KEYVALUE 3: POINT 2 X
						//KEYVALUE 4: POINT 2 Y
}
/*
 * Basic explanation of the types:
 * 	POINT
 * All particles come from a point
 * 
 * 	CIRCLE_RANDOM
 * Particles start inside a circle, and if the min radius isn't 0, they'll spawn in a donut shape
 * 
 * 	LINE_RANDOM
 * Particles start anywhere on a line between two points
 */