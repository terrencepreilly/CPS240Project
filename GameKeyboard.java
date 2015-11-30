package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/**
 * GameKeyboard.java
 *
 * Custom KeyListener/Keyboard class
 * We want this to move our character
 * Only problem is how do we do that without passing the
 * entire character and allowing it to be manipulated?
 * We don't. :) Well we could, but I prefer this way for now.
 * If we wanted we could re-write the process input method to return a
 * value. We would simply have to pass in the integer KeyCode of the value we wish to analyze
 * I have written such a method below. We may need this later because for boundary checking and such
 * it may be better to write complex if/case structures NOT in the GameKeyboard class
 * @author Tyler Beachnau
 */
public class GameKeyboard implements KeyListener{

	private Character character; //hold reference for main player
	
	private boolean[] keyPressed = new boolean[256];
		
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
		try{
			keyPressed[e.getKeyCode()] = true;
		} catch(ArrayIndexOutOfBoundsException aioob){
			//just ignore, means a key beyond normal keyboard (windows for example), was pressed
		}
	}
	
	/**
	 * Process the keys that were pressed, updating character location.
	 */
	public void processInput(){
		if(keyPressed[KeyEvent.VK_LEFT]){
			character.setLocation(new Vector(character.getLocation().x - 5.0f, character.getLocation().y));
		}
		if(keyPressed[KeyEvent.VK_RIGHT]){
			character.setLocation(new Vector(character.getLocation().x + 5.0f, character.getLocation().y));
		}
		if(keyPressed[KeyEvent.VK_UP]){
			character.setLocation(new Vector(character.getLocation().x, character.getLocation().y - 5.0f));
		}
		if(keyPressed[KeyEvent.VK_DOWN]){
			character.setLocation(new Vector(character.getLocation().x, character.getLocation().y + 5.0f));
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
		try{
			keyPressed[e.getKeyCode()] = false;
		} catch(ArrayIndexOutOfBoundsException aioobe){
			//do nothing, releasing a key that is uncommon (windows key for example)
		}
	}

	/**
	 * Overrides the abstract method of KeyListener. No action.
	 */
	public void keyTyped(KeyEvent e) {}
}
