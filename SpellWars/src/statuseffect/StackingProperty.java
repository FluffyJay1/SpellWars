package statuseffect;

public enum StackingProperty {
	UNSTACKABLE_REFRESH_DURATION,	//whenever a new stack is added, it just refreshes the duration of the old one and doesn't add the new one
	
	UNSTACKABLE_REPLACE,			//whenever a new stack is added, the old one is replaced
	
	STACKABLE_REFRESH_DURATION,		//whenever a new stack is added, all the existing stacks have their durations refreshed too
	
	STACKABLE_INDEPENDENT;			//whenever a new stack is added, nothing special happens
}
