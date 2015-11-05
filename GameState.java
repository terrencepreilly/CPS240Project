public class GameState {
	ArrayList<Character> players;
	ArrayList<Character> enemies;
	ArrayList<Scenic> obstacles;

	ArrayList<Character> updated; 	// Any time a player is updated, add player
					// or enemy to this

	HashMap<Character, Vector> prevCoords;
	HashMap<Character, Integer> prevHealths;

	class GameDelta {
		// Store updated coordinates by hash values of characters
		// store updated healths by hash values of characters
		void update(Character c);
	}

	public void applyGameDelta(GameDelta gd);
}
