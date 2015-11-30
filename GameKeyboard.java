
import java.awt.geom.Arc2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import java.util.HashMap;

import java.util.concurrent.locks.*;

/**
 * A utility for handling Keyboard input.
 */
class GameKeyboard implements KeyListener, GameConstants {
	private GameState gamestate;
	private Character character;
	private Vector updateLoc = new Vector(0f, 0f);

	/**
	 * Create a new GameKeyboard instance.
	 * @param character The character this keyboard controls.
	 */
	public GameKeyboard(Character character, GameState gamestate) {
		this.character = character;
		this.gamestate = gamestate;
	}
	
	/**
	 * Record that this key was pressed.
	 * @param e The KeyEvent for the key that was pressed.
	 */
	public synchronized void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//			updateLoc = new Vector(-1f*PLAYER_SPEED,0f);
			character.setDirection(character.getDirection() + 10);
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
//			updateLoc = new Vector(PLAYER_SPEED, 0f);
			character.setDirection(character.getDirection() - 10);
		}

		float xCoord = character.getLocation().x;
		float yCoord = character.getLocation().y;
		float properXCoord = xCoord - ((float) SCREEN_WIDTH / 2f);
		float properYCoord = ((float) SCREEN_HEIGHT / 2f) - yCoord;
		float directionX;
		float directionY;
		directionX = ((float) Math.cos(Math.toRadians( character.getDirection())) * PLAYER_SPEED * 60f);
		directionY = ((float) Math.sin(Math.toRadians( character.getDirection())) * PLAYER_SPEED * 60f);
		properXCoord += directionX;
		properYCoord += directionY;
		float nxCoord = properXCoord + ((float) SCREEN_WIDTH / 2f);
		float nyCoord = ((float) SCREEN_HEIGHT / 2f) - properYCoord;

		updateLoc = new Vector(nxCoord - xCoord, nyCoord - yCoord);

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

	}

        /**
         * Reset whether the key was pressed.
         * @param e The key to reset.
         */
        public synchronized void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			updateLoc = new Vector(0f, updateLoc.y);
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			updateLoc = new Vector(0f, updateLoc.y);
                if (e.getKeyCode() == KeyEvent.VK_UP)
			updateLoc = new Vector(updateLoc.x, 0f);
                if (e.getKeyCode() == KeyEvent.VK_DOWN)
			updateLoc = new Vector(updateLoc.x, 0f);
	}
	
	/**
	 * Process the keys that were pressed, updating character location.
	 */
	public void processInput(){
		Vector currLoc = character.getBoxCollider().getLocation();
		character.setLocation( currLoc.add(updateLoc) );
		if (updateLoc.magnetude() != 0f)
			gamestate.flagForUpdate( character.getUniqueID() );
	}
	
	/**
	 * Overrides the abstract method of KeyListener. No action.
	 */
	public void keyTyped(KeyEvent e) {}
}
