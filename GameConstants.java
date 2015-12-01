/**
 * A container interface for game constants.
 */
public interface GameConstants {
	// Speed of player and enemy
	public final float PLAYER_SPEED = 2.5f;
	public final float ACTOR_SPEED = 2.5f;
	public static final float ATTACK_SPEED = 0.5f;

	public final int PLAYER_TURN_SPEED = 5;

	// Pathfinding variables
	public final boolean USE_CORNER_ASTAR = true;
	public final float ASTAR_RATE = 0.2f;

	// Spawn rates for enemies and obstacles.
	public final int ZOMBIE_SPAWN = 2;
	public final int OBSTACLE_SPAWN = 0;

	// The random seed
	public final int RANDSEED = 309;

	// Images for characters and obstacles
	public final String IMGFLD = "Images/";
	public final String OBS_IMAGE_FILENAME = IMGFLD + "redCar.png";
	public final String PLR_IMAGE_FILENAME = IMGFLD + "characterForward1.png";
	public final String ENE_IMAGE_FILENAME = IMGFLD + "zombieForward1.png";
	public final String BAC_IMAGE_FILENAME = IMGFLD + "bg.png";
	public final String WEA_IMAGE_FILENAME = IMGFLD + "swordImage.png";

	//---------------INTERNAL CONSTANTS, DO NOT CHANGE---------------//
	public final Integer PLAYER = 0;
	public final Integer ENEMY = 1;
	public final Integer OBSTACLE = 2;
	public final Integer BORDER_OBSTACLE = 3;
	public final int END_UID_REQUEST = -217;
	// GameDelta.uniqueID for requesting update of GameState
	public final int UPDATE_REQUEST = -2;
	// Kills the thread this uniqueID is sent to.
	public final int THREAD_KILL = -3;

	public final int SCREEN_WIDTH = 1500;
	public final int SCREEN_HEIGHT = 800;

	public final float ENEMY_WIDTH = 20f;
	public final float ENEMY_HEIGHT = 20f;

	public final int RIGHT = 0;
	public final int LEFT = 180;
	public final int UP = 90;
	public final int DOWN = 270;
}
