public interface GameConstants {
	public final Integer PLAYER = 0;
	public final Integer ENEMY = 1;
	public final Integer OBSTACLE = 2;

	public final float PLAYER_SPEED = 2.5f;
	public final float ACTOR_SPEED = 2.5f;

	public final int END_UID_REQUEST = -217;

	// GameDelta.uniqueID for requesting update of GameState
	public final int UPDATE_REQUEST = -2;

	// The number of Zombies to spawn at the start of the game.
	// (Eventually change to a base spawn rate.)
	public final int ZOMBIE_SPAWN = 20;
	public final int OBSTACLE_SPAWN = 5;

	// Kills the thread this uniqueID is sent to.
	public final int THREAD_KILL = -3;

	public final int RANDSEED = 319;

	public final String OBS_IMAGE_FILENAME = "obstacle.png";
	public final String PLR_IMAGE_FILENAME = "character.png";

	public final int SCREEN_WIDTH = 1000;
	public final int SCREEN_HEIGHT = 800;

	public final float ENEMY_WIDTH = 20f;
	public final float ENEMY_HEIGHT = 20f;
}
