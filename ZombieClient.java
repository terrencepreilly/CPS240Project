import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

/**
 * A class which handles the enemy characters of the game.  Should be
 * run on the same computer as the Server.
 */
public class ZombieClient extends AbstractClient implements Runnable {
	private boolean running;  // goes forever (will be contained in Server)
				// Is this a good idea?
	private HashMap<Character, Vector> prevUpdate;
	private HashMap<Character, Integer> targetMap; // each zombie's target
	private int screenWidth;
	private int screenHeight;

	/**
	 * Create a new instance of ZombieClient, which holds an array of Zombies
	 * and updates their positions.
	 * @return A new instance of ZombieClient.
	 */
	public ZombieClient() { 
		super("localhost", 8000, null, getZombieRequests()); 
		targetMap = new HashMap<>();
		prevUpdate = new HashMap<>();
		for (Character c : charactersCreated)
			prevUpdate.put(c, c.getLocation().add(new Vector(0f, 0f)));
		System.out.println("ZOMBIECLIENT:\tconstructor\tfinished");
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gd = ge.getDefaultScreenDevice();
                DisplayMode displayMode = gd.getDisplayMode();
		screenWidth = displayMode.getWidth();
		screenHeight = displayMode.getHeight();
	}

	/**
	 * Get the list of Zombies with which to populate this list.
	 * @return A LinkedList of requests for UIDs, terminated with 
	 * END_UID_REQUEST.
	 */
	private static LinkedList<Integer> getZombieRequests() {
		LinkedList<Integer> requestUIDs = new LinkedList<>();

                for (int i = 0; i < ZOMBIE_SPAWN; i++)
                        requestUIDs.addLast( ENEMY );
                requestUIDs.addLast( END_UID_REQUEST );
		System.out.printf("ZOMBIECLIENT:\tgetZombieRequests\t%d requested\n", requestUIDs.size());
		return requestUIDs;
	}

	/**
	 * For each zombie, assign a target (some character in the array after
	 * the last zombie was added.)  Will choose whichever actor that is not
	 * an ENEMY, which is closest to this zombie.  Places assigned targets
	 * into a target Map.
	 */
	private void assignTargets() {
		System.out.println("ZOMBIECLIENT:\tassignTargets");
		for (Character zomb : charactersCreated) {
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
		System.out.println("ZOMBIECLIENT:\tassignTargets\t" + targetMap.size() + " tagets assigned");
	}

	private void moveCharacter(Character z) {
		if (targetMap.get(z) == null || targetMap.get(z) == -1)
			return;
		BoxCollider targetBC = gamestate.characters.get( targetMap.get(z) ).getBoxCollider();
		z.buildPath(
			targetBC.getLocation(), // target
			screenWidth,
			screenHeight,
			gamestate.characters.values(), // TODO MUST BE ARRAYLIST
			gamestate.obstacles, // TODO MUST BE ARRAYLIST
			targetBC// target
		);
		((Actor) z).step();
		flagIfMoved(z);
	}

	private void flagIfMoved(Character c) {
		if (prevUpdate.get(c).magnetude() != c.getLocation().magnetude()) {
			gamestate.flagForUpdate(c);
			prevUpdate.put(c, c.getLocation().add(new Vector(0f, 0f)));
		}
	}

	/**
	 * Update positions of all enemies, send to Server.
	 */
	private void moveCharacters() { 
		for (Character z : charactersCreated)
			moveCharacter(z);
	}

	/**
	 * Run this thread -- move all Characters, update Server, and pause.
	 */
	public void run() {
		running = true;
		System.out.println("ZOMBIECLIENT:\trun");
		int count = 0;
		while (running) {
			// detectCollisions?
			count = count > 20 ? 0 : count + 1;
			if (count == 0)
				assignTargets();

			moveCharacters();

			try { Thread.sleep( 100L ); } // TODO refine time
			catch (InterruptedException ex) {}
		}
	}

	public static void main(String[] args) {
		ZombieClient z = new ZombieClient();
		Thread zthread = new Thread(z);
		zthread.start();
	}
}
