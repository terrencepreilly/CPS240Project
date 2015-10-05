
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;



/*
 * Created by Tyler Beachnau
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
 */

public class GameKeyboard implements KeyListener{

	//Delta will be implemented to multiply the time by the walk speed, this will control the walk speed every frame!
	//private double delta = 0.0;
	private Character character; //hold reference for main player
	
	private boolean[] keyPressed = new boolean[256];
		
	//constructor
	public GameKeyboard(Character character) {
		this.character = character;
	}
	
	//methods that must be implemented
	public void keyTyped(KeyEvent e) {
		//must utilize this method because we implemented KeyListener
		//can potentially use this to open up a menu since keyTyped is different from keyPressed/keyReleased
	}

	public synchronized void keyPressed(KeyEvent e) {
		
		//e.getKeyCode will return the integer value of the key being pressed.
		//we then take that value of the keyPressed array, and set it to true
		//to represent that the key has been pressed. If it is released then
		//the keyReleased method will set it to false :)
		keyPressed[e.getKeyCode()] = true;
	}
	
	//When a key is pressed, you want to put the code for what to do in here!!!!
	public void processInput(){
		//take the array, it will tell us if a key is still pressed down
		//basically if it is still pressed, do something
		
		if(keyPressed[KeyEvent.VK_LEFT]){
			//set the location of the character
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
	
	//this will return a bool value for the key in question as to whether or not it is being pressed
	//this can be useful for moving a character indirectly. An example follows below:
	public boolean processInput(int key) {
		return keyPressed[key];
	}
	// call the method as such:
	// if(processInput(KeyEvent.VK_DOWN)) { character.setLocation[ code here for setting location for down key press] }
	//the above call will return whether the down button value is true (down key is currently being pressed). Then, based on that
	//you can choose what course of action to take

	//this will alter the keyPressed array, when a key is released, set it's value to false
	public synchronized void keyReleased(KeyEvent e) {
		
		//grab the KeyCode of the key being released, set it to false in the keyPressed array
		//same principle as the keyPressed method
		keyPressed[e.getKeyCode()] = false;
	}
}
