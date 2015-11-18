public interface GameConstants {
	public final Integer PLAYER = 0;
	public final Integer ENEMY = 1;

	// GameDelta.uniqueID for requesting a new ID.
	public final int UID_REQUEST = -1; 
	// GameDelta.uniqueID for requesting update of GameState
	public final int UPDATE_REQUEST = -2;

	// The number of Zombies to spawn at the start of the game.
	// (Eventually change to a base spawn rate.)
	public final int ZOMBIE_SPAWN = 2;

	// Kills the thread this uniqueID is sent to.
	public final int THREAD_KILL = -3;
}
