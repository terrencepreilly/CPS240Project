
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
//			gamestate.flagForUpdate( character.getUniqueID() );	
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			updateDirection = -1 * PLAYER_TURN_SPEED;
//			gamestate.flagForUpdate( character.getUniqueID() );
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			gamestate.makeAttack(character);
		}
	}

        /**
         * Reset whether the key was pressed.
         * @param e The key to reset.
         */
        public synchronized void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			updateDirection = 0;
//			gamestate.flagForUpdate( character.getUniqueID() );
		}
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			updateDirection = 0;
//			gamestate.flagForUpdate( character.getUniqueID() );
		}
	}
	
	/**
	 * Process the keys that were pressed, updating character location.
	 */
	public void processInput(double delta){
		this.delta = delta;

		int dir = character.getDirection() + updateDirection;

		if (dir < 0)
			dir += 360;

		character.setDirection(dir);
		character.playerStep(delta);

		gamestate.flagForUpdate( character.getUniqueID() );

		gamestate.detectCollisions(character);
	}
	
	/**
	 * Overrides the abstract method of KeyListener. No action.
	 */
	public void keyTyped(KeyEvent e) {}
}
