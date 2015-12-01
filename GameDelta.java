/**
 * Represents a snapshot of a character at a given time.
 */
class GameDelta implements java.io.Serializable {
	Integer uniqueID;
	Vector locUpdate;
	int health;
	int type;
	long timestamp; 

	/**
	 * Create a snapshot of a character to pass between hosts.
	 * @param uniqueID The uniqueID of this Character. (Assigned by
	 *      an auto-fill counter in the Server.
	 * @param locUpdate The location of this Character.
	 * @param health The current health of this Character.
	 * @param type The type of character (GameState.PLAYER or 
	 *      GameState.ENEMY).
	 * @param timestamp The time this GameDelta was originally requested.
	 */
	public GameDelta(Integer uniqueID, Vector locUpdate, int health, int type, long timestamp) {
		this.uniqueID = uniqueID;
		this.locUpdate = locUpdate;
		this.health = health;
		this.type = type;
		this.timestamp = timestamp;
	}

        /**
         * Create a snapshot of a character to pass between hosts, with 
	 * no initial information save the uniqueID. (Used for Scenic objects.)
         * @param uniqueID The uniqueID of this Character. (Assigned by
         *      an auto-fill counter in the Server.
         */
	public GameDelta(Integer uniqueID) {
		this.uniqueID = uniqueID;
		this.locUpdate = null;
		this.health = 0;
		this.type = 0;
		this.timestamp = 0L;
	}

	/**
	 * Return a String representation of this GameDelta for testing purposes.
	 * @return A String representation of this GameDelta.
	 */
	public String toString() {
		return String.format(
			"delta: %s HP: %d ID: %d Type: %d Timestamp: %d",
			locUpdate.toString(), 
			health, 
			uniqueID, 
			type, 
			timestamp
		);
	}
}
