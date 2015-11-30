/**
 * A container interface for game constants.
 */
public interface GameConstants {
	// Speed of player and enemy
	public final float PLAYER_SPEED = 2.5f;
	public final float ACTOR_SPEED = 2.5f;
	public static final float ATTACK_SPEED = 0.5f;

	// Pathfinding variables
	public final boolean USE_CORNER_ASTAR = true;
	public final float ASTAR_RATE = 0.2f;

	// Spawn rates for enemies and obstacles.
	public final int ZOMBIE_SPAWN = 10;
	public final int OBSTACLE_SPAWN = 10;

	// The random seed
	public final int RANDSEED = 309;

	// Images for characters and obstacles
	public final String OBS_IMAGE_FILENAME = "obstacle.png";
	public final String PLR_IMAGE_FILENAME = "character.png";

	//---------------INTERNAL CONSTANTS, DO NOT CHANGE---------------//
	public final Integer PLAYER = 0;
	public final Integer ENEMY = 1;
	public final Integer OBSTACLE = 2;
	public final int END_UID_REQUEST = -217;
	// GameDelta.uniqueID for requesting update of GameState
	public final int UPDATE_REQUEST = -2;
	// Kills the thread this uniqueID is sent to.
	public final int THREAD_KILL = -3;

	public final int SCREEN_WIDTH = 1000;
	public final int SCREEN_HEIGHT = 800;

	public final float ENEMY_WIDTH = 20f;
	public final float ENEMY_HEIGHT = 20f;

	public final int RIGHT = 0;
	public final int LEFT = 180;
	public final int UP = 90;
	public final int DOWN = 270;
}
