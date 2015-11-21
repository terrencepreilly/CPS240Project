
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;



/**
 * A utility for handling Keyboard input.
 */
class GameKeyboard implements KeyListener, GameConstants {

	private Character character; //hold reference for main player

	/**
	 * Create a new GameKeyboard instance.
	 * @param character The character this keyboard controls.
	 */
	public GameKeyboard(Character character) {
		this.character = character;
	}
	
	/**
	 * Record that this key was pressed.
	 * @param e The KeyEvent for the key that was pressed.
	 */
	public synchronized void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			character.changeLocUpdate(new Vector(-1f*PLAYER_SPEED,0f));
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			character.changeLocUpdate(new Vector(PLAYER_SPEED, 0f));
		if (e.getKeyCode() == KeyEvent.VK_UP)
			character.changeLocUpdate(new Vector(0f,-1f*PLAYER_SPEED));
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			character.changeLocUpdate(new Vector(0f, PLAYER_SPEED));
	}

        /**
         * Reset whether the key was pressed.
         * @param e The key to reset.
         */
        public synchronized void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
                        character.changeLocUpdate(new Vector(PLAYER_SPEED,0f));
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                        character.changeLocUpdate(new Vector(-1f*PLAYER_SPEED,0f));
                if (e.getKeyCode() == KeyEvent.VK_UP)
                        character.changeLocUpdate(new Vector(0f,PLAYER_SPEED));
                if (e.getKeyCode() == KeyEvent.VK_DOWN)
                        character.changeLocUpdate(new Vector(0f,-1f*PLAYER_SPEED));
	}
	
	/**
	 * Process the keys that were pressed, updating character location.
	 */
	public void processInput(){
		character.step();
	}
	
	/**
	 * Overrides the abstract method of KeyListener. No action.
	 */
	public void keyTyped(KeyEvent e) {}
}
