import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;



/**
 * A utility for handling Keyboard input.
 * @author Tyler Beachnau
 */
class GameKeyboard implements KeyListener{

	private Character character; //hold reference for main player
	protected int charDirection = 1;//character facing south to start
	
	protected boolean[] keyPressed = new boolean[256];
		
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
		keyPressed[e.getKeyCode()] = true;
	}
	
	/**
	 * Process the keys that were pressed, updating character location.
	 */
	public void processInput(){
		if(keyPressed[KeyEvent.VK_LEFT]){
			character.setLocation(new Vector(character.getLocation().x - 5.0f, character.getLocation().y));
			charDirection = 3;
		}
		if(keyPressed[KeyEvent.VK_RIGHT]){
			character.setLocation(new Vector(character.getLocation().x + 5.0f, character.getLocation().y));
			charDirection = 2;
		}
		if(keyPressed[KeyEvent.VK_UP]){
			character.setLocation(new Vector(character.getLocation().x, character.getLocation().y - 5.0f));
			charDirection = 0;
		}
		if(keyPressed[KeyEvent.VK_DOWN]){
			character.setLocation(new Vector(character.getLocation().x, character.getLocation().y + 5.0f));
			charDirection = 1;
		}
		
	}
	
	/**
	 * Return true if key is being pressed.
	 * @param key The key being checked for whether or not it is pressed.
	 * @return True if the key is being pressed.
	 */
	public boolean processInput(int key) {
		return keyPressed[key];
	}

	/**
	 * Reset whether the key was pressed.
	 * @param e The key to reset.
	 */
	public synchronized void keyReleased(KeyEvent e) {
		keyPressed[e.getKeyCode()] = false;
	}

	/**
	 * Overrides the abstract method of KeyListener. No action.
	 */
	public void keyTyped(KeyEvent e) {}
}
