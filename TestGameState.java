public class TestGameState {
	public static void main(String[] args) {
		GameState gs = new GameState();
		Character c = gs.createCharacter(null);
		c.setType(GameConstants.PLAYER);
		gs.add(c);

		System.out.println(gs.createGameDelta( c ));

		System.out.println(gs);
	}
}
