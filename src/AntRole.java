
public enum AntRole {
	UNKNOWN,	//Role has not been defined yet
	SCAVENGER,	//Searches for food; avoids conflict
	SOLDIER,	//Attacks enemy bases; engages combat
	GUARD, 		//Stays near the base; avoid conflict until enemy gets close
	ENEMY		//Enemy ant
}
