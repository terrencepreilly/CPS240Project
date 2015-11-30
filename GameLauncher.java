/*
 * Will launch full fledged menu
 * For now simply launches the game
 */

public class GameLauncher {

	public static void main(String[] args){
		
		//create thread of our menu
		Thread gamemenuThread = new Thread(new MenuApp());
		Thread gameThread = new Thread(new ZombleApp());
				
		gameThread.start();		
	}
	
	//TODO - Update menu launch method
	//Launch the menu
	public static void launchMenu(){
	}
}
