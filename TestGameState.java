public class TestGameState {
	public static void main(String[] args) {
		GameState gs = new GameState();
		Character c = gs.createCharacter(null);
		c.setType(GameConstants.PLAYER);
		gs.add(c);

		for (Integer uid : gs.players.keySet())
			System.out.println(gs.players.get(uid));
	}
}
