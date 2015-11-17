import java.util.ArrayList;

/**
 * A class which handles the enemy characters of the game.  Should be
 * run on the same computer as the Server.
 */
public class ZombieClient extends AbstractClient implements Runnable {
	private ArrayList<Character> zombies;
	private Thread zombieThread;
	private boolean running;  // goes forever (will be contained in Server)
				// Is this a good idea?

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
	 * Update positions of all enemies, send to Server.
	 */
	private void moveCharacters() { 
		System.out.println("Move Characters!");
		for (Character z : zombies)
			System.out.println("\t" + z);
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
