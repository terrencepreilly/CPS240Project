
import java.awt.geom.Point2D;
import java.awt.geom.Arc2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;

import java.util.concurrent.locks.*;

/**
 * A utility for handling Keyboard input.
 */
class GameKeyboard implements KeyListener, GameConstants {
	private GameState gamestate;
	private Character character;
	private int updateDirection;
	private double delta;

	/**
	 * Create a new GameKeyboard instance.
	 * @param character The character this keyboard controls.
	 */
	public GameKeyboard(Character character, GameState gamestate) {
		this.character = character;
		this.gamestate = gamestate;
		this.delta = 1d;
		updateDirection = 0;
	}
	
	/**
	 * Record that this key was pressed.
	 * @param e The KeyEvent for the key that was pressed.
	 */
	public synchronized void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			updateDirection = PLAYER_TURN_SPEED;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			updateDirection = -1 * PLAYER_TURN_SPEED;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			gamestate.makeAttack(character);
		}

/*
//		if (e.getKeyCode() == KeyEvent.VK_UP) {
//			updateLoc = new Vector(0f,-1f*PLAYER_SPEED);
//			character.setDirection(UP);
//		}
//		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
//			updateLoc = new Vector(0f, PLAYER_SPEED);
//			character.setDirection(DOWN);
//		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			character.attack();
			if (character.getAttacking()) {
				Sword s = (Sword) character.getWeapon();
				Arc2D.Float swordHit = s.getBoxCollider(character);

				System.out.println("We swung our sword");

				Lock lock = gamestate.getLock();
				lock.lock();
				HashMap<Integer, Character> characters = gamestate.getCharacters();
				for (Integer uid : characters.keySet()) {
					Character c = characters.get(uid);
					if ( character.getUniqueID() != uid && 
						swordHit.intersects(
						c.getBoxCollider())) {
						c.setHealth(c.getHealth() - s.getDamage());
						System.out.println("We hit an enemy, and their health is now: " + c.getHealth());
						if (c.getHealth() <= 0) {
							// Remove and send
							// a remove request
							// to the server
						}
					}
				}
				lock.unlock();
			}
		}
*/
	}
/*
	public void upAction() {
		Lock lock = gamestate.getLock();
		lock.lock();
		HashMap<Integer, Character> characters = gamestate.getCharacters();

		for (Integer uid : characters.keySet()) {
			Character c2 = characters.get(uid);
			boolean isNotSame = character.getUniqueID() != uid;
			float dist = (float) character.getBoxCollider().getWidth() / 2f;

			if (isNotSame && isClose(character, c2, dist)) {
				System.out.println("b: " + c2.getHealth());
				c2.setHealth( c2.getHealth() - HITTING_POWER );
				System.out.println("a: " + c2.getHealth());
				gamestate.flagForUpdate(c2);
			}
		}

		characters = null;
		lock.unlock();
	}

	public boolean isClose(Character c1, Character c2, float dist) {
		List<Point2D.Float> l1 = c1.getBoxCollider().getVertices();
		List<Point2D.Float> l2 = c2.getBoxCollider().getVertices();

		for (Point2D p1 : l1) {
			Vector v1 = new Vector(p1);
			for (Point2D p2 : l2) {
				Vector v2 = new Vector(p2);
				if (v1.distance(v2) < dist)
					return true;
			}
		}

		return false;
	}
*/
        /**
         * Reset whether the key was pressed.
         * @param e The key to reset.
         */
        public synchronized void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			updateDirection = 0;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			updateDirection = 0;
	}
	
	/**
	 * Process the keys that were pressed, updating character location.
	 */
	public void processInput(double delta){
		this.delta = delta;

		character.setDirection( character.getDirection() + updateDirection);
		character.playerStep(delta);

		gamestate.flagForUpdate( character.getUniqueID() );

		gamestate.detectCollisions(character);
	}
	
	/**
	 * Overrides the abstract method of KeyListener. No action.
	 */
	public void keyTyped(KeyEvent e) {}
}
