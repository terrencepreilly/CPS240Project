/**
 * Represents a snapshot of a character at a given time.
 */
class GameDelta {
	Integer uniqueID;
	Vector coords;
	int health;
	int type;

	/**
	 * Create a snapshot of a character to pass between hosts.
	 * @param uniqueID The uniqueID of this Character. (Assigned by
	 *      an auto-fill counter in the Server.
	 * @param coords The location of this Character.
	 * @param health The current health of this Character.
	 * @param type The type of character (GameState.PLAYER or 
	 *      GameState.ENEMY).
	 */
	public GameDelta(Integer uniqueID, Vector coords, int health, int type) {
		this.uniqueID = uniqueID;
		this.coords = coords;
		this.health = health;
		this.type = type;
	}
}
