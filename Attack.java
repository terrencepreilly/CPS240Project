import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Time;

import javax.imageio.ImageIO;
/*
 * Handles player/enemy health when being attacked, as well as handles changing player/enemy image when they've 
 * been killed and revives player/enemy 5 seconds after dying by restoring the character image to standing and their health to full.
 */
public class Attack implements GameConstants{

	int health;
	Character player;
	Vector v;
	Character player2;
	int time;
	/*
	 * handle when player is being attacked
	 */
	public Attack(int health, BufferedImage playerImg, int charID){
		int dmg = 2;
		
		//TODO detect if player is intersecting w/ enemy (enemy is intersecting w/ swinging player)
		//and create player/zombie image for death.
		while(health > 0){// && rivals collide(interact)
			time++;
			if(time == 20){
				this.health = health - dmg;
				time = 0;
			}
		}
		if(health == 0){
			try{
				playerImg = ImageIO.read(new File("playerDead.png"));
				v = player.getLocation();
				charID = player.getUniqueID();
				//put where detect collision w/ enemy and call Attack class from there
				removePlayer(playerImg, charID, v);
				
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	/*
	 * reset player image from dead to standing forward facing w/ full health
	 */
	public Character removePlayer(BufferedImage revive, int playID, Vector v){
		
		try{
			revive.wait(5000);
			revive = ImageIO.read(new File("playerFwd.png"));
			
			if(player.getUniqueID().equals(playID)){ // && player still intersecting w/ enemy
				this.player.setLocation(v);
				this.player.setHealth(10);
				new Attack(player.getHealth(), revive, playID);
			}
		}catch(IOException | InterruptedException e){
			e.printStackTrace();
		}
		return this.player;
	}
	
}
