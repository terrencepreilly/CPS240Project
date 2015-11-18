import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class which handles the enemy characters of the game.  Should be
 * run on the same computer as the Server.
 */
public class ZombieClient extends AbstractClient implements Runnable {
	private ArrayList<Character> zombies;
	private Thread zombieThread;
	private boolean running;  // goes forever (will be contained in Server)
				// Is this a good idea?
	private HashMap<Character, Integer> targetMap; // each zombie's target

	/**
	 * Create a new instance of ZombieClient, which holds an array of Zombies
	 * and updates their positions.
	 * @return A new instance of ZombieClient.
	 */
	public ZombieClient() { 
		super("localhost", 8000, null); 

		zombies = new ArrayList<Character>();
		for (int i = 0; i < ZOMBIE_SPAWN; i++)
			zombies.add( requestCharacter(ENEMY) );
		for (Character z : zombies)
			gamestate.add(z);

		zombieThread = new Thread(this);
		zombieThread.start();
	}

	/**
	 * For each zombie, assign a target (some character in the array after
	 * the last zombie was added.)  Will choose whichever actor that is not
	 * an ENEMY, which is closest to this zombie.
	 */
	private void assignTargets() {
		for (Character zomb : zombies) {
			Vector zv = zomb.getBoxCollider().getLocation();
			Integer closeP = -1;

			for (Integer cuid : gamestate.characters.keySet()) {
				Character cplayer = gamestate.characters.get(cuid);
				if (cplayer.getType() == ENEMY)
					continue;

				Vector pv = cplayer.getBoxCollider().getLocation();
				if (closeP == -1)
					closeP = cuid;

				Vector cpv = gamestate.characters.get(closeP).getBoxCollider().getLocation(); 

				if (zv.distance(pv) < zv.distance(cpv))
					closeP = cuid;
			}

			targetMap.put(zomb, closeP);
		}
	}

	private void moveCharacter(Character z) {
		BoxCollider targetBC = gamestate.characters.get( targetMap.get(z) ).getBoxCollider();
		z.buildPath(
			targetBC.getLocation(), // target
			1000, // DisplayMode???????????
			800,
			gamestate.characters.values(), // TODO MUST BE ARRAYLIST
			gamestate.obstacles, // TODO MUST BE ARRAYLIST
			targetBC// target
		);
		z.step();
	}

	/**
	 * Update positions of all enemies, send to Server.
	 */
	private void moveCharacters() { 
		for (Character z : zombies)
			moveCharacter(z);
	}

	/**
	 * Run this thread -- move all Characters, update Server, and pause.
	 */
	public void run() {
		running = true;
		while (running) {
			// detectCollisions?
			moveCharacters();

			try { Thread.sleep( 100L ); } // TODO refine time
			catch (InterruptedException ex) {}
		}
	}

	public static void main(String[] args) {
		ZombieClient z = new ZombieClient();
	}
}
