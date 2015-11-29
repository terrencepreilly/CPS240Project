
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

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
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			updateLoc = new Vector(-1f*PLAYER_SPEED,0f);
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			updateLoc = new Vector(PLAYER_SPEED, 0f);
		if (e.getKeyCode() == KeyEvent.VK_UP)
			updateLoc = new Vector(0f,-1f*PLAYER_SPEED);
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			updateLoc = new Vector(0f, PLAYER_SPEED);
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
