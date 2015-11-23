
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

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
		System.out.println("GAMEKEYBOARD:\tconstructor\tfinished");
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
		if (Arrays.asList( new Integer[] {
			KeyEvent.VK_LEFT,
			KeyEvent.VK_RIGHT,
			KeyEvent.VK_UP,
			KeyEvent.VK_DOWN
		}).contains( e.getKeyCode() ))
			System.out.println("GAMEKEYBOARD\tkeyPressed\t" + character.getLocUpdate());
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
		if (Arrays.asList( new Integer[] {
                        KeyEvent.VK_LEFT,
                        KeyEvent.VK_RIGHT,
                        KeyEvent.VK_UP,
                        KeyEvent.VK_DOWN
                }).contains( e.getKeyCode() ))
                        System.out.println("GAMEKEYBOARD\tkeyReleased");
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
