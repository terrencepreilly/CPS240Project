public interface GameConstants {
	public final Integer PLAYER = 0;
	public final Integer ENEMY = 1;

	public final float PLAYER_SPEED = 2.5f;

	public final int END_UID_REQUEST = -217;

	// GameDelta.uniqueID for requesting update of GameState
	public final int UPDATE_REQUEST = -2;

	// The number of Zombies to spawn at the start of the game.
	// (Eventually change to a base spawn rate.)
	public final int ZOMBIE_SPAWN = 2;

	// Kills the thread this uniqueID is sent to.
	public final int THREAD_KILL = -3;
}
