public class TestGameState {
	public static void main(String[] args) {
		GameState gs = new GameState();
		Character enemy = gs.createCharacter(null);
		enemy.setType(GameConstants.ENEMY);
		enemy.setUniqueID(0);
		gs.add(enemy);
		System.out.println(gs);

		GameDelta gd = gs.createGameDelta(enemy);
		System.out.println(gd);

		GameState gs2 = new GameState();
		gs2.applyGameDelta(gd);
		System.out.println("GS2: " + gs2);

		enemy.setLocation(new Vector(100f, 100f));
		System.out.println("Enemy moved");
		System.out.println(gs);
		System.out.println(gs2);

		gs2.applyGameDelta( gs.createGameDelta(enemy) );
		System.out.println(gs);
		System.out.println(gs2);
		
	}
}
