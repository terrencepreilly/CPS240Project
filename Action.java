import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
/*
 * Determines character direction based on keyPressed and correctly applies images to show character movement.
 * Also applies striking actions based on attack key (space bar) being pressed.
 */
public class Action {

	private GameKeyboard gameKeyboard;
	private BufferedImage charImage;//south facing
	private BufferedImage charImage1;//south facing2
	private BufferedImage charImage2;//east facing
	private BufferedImage charImage3;//west facing
	private BufferedImage charImage4;//north facing
	private BufferedImage charImage5;//north facing2
	private int frames = 0;
	private BufferedImage mobImage;//south facing
	private BufferedImage mobImage1;//south facing2
	private BufferedImage mobImage2;//east facing
	private BufferedImage mobImage3;//west facing
	private BufferedImage mobImage4;//north facing
	private BufferedImage mobImage5;//north facing2
	private BufferedImage playerImage;
	private BufferedImage enemyImage;
	/*
	 * Returns the correct image based on character direction
	 */
	public Action(BufferedImage action){
		action(action);
	}
	//TODO create player/zombie image for attacking action
	public BufferedImage action(BufferedImage move){
		try {
			//player images
			charImage = ImageIO.read(new File("playerFrwd.png"));
			charImage1 = ImageIO.read(new File("playerFrwd2.png"));
			charImage2 = ImageIO.read(new File("playerSide.png"));
			charImage3 = ImageIO.read(new File("playerSide2.png"));
			charImage4 = ImageIO.read(new File("playerBack.png"));
			charImage5 = ImageIO.read(new File("playerBack2.png"));
			
			//zombie images
			mobImage = ImageIO.read(new File("zombieFrwd.png"));
			mobImage1 = ImageIO.read(new File("zombieFrwd2.png"));
			mobImage2 = ImageIO.read(new File("zombieSide.png"));
			mobImage3 = ImageIO.read(new File("zombieSide2.png"));
			mobImage4 = ImageIO.read(new File("zombieBack.png"));
			mobImage5 = ImageIO.read(new File("zombieBack2.png"));

			frames++;
			if(frames >= 30){
				frames = 0;
			}
			if(gameKeyboard.charDirection == 1){
				
				if(gameKeyboard.keyPressed[KeyEvent.VK_DOWN]){
					if(frames > 0 && frames <15){
						playerImage = charImage;
						enemyImage = mobImage;
					}
					else if(frames >=15 && frames < 30){
						playerImage = charImage1;
						enemyImage = mobImage1;
					}
				}
				else if(gameKeyboard.keyPressed[KeyEvent.VK_DOWN] && gameKeyboard.keyPressed[KeyEvent.VK_SPACE]){
					if(frames > 0 && frames <15){
						//playerImage = charImage6;
						//enemyImage = mobImage6;
					}
					else if(frames >=15 && frames <30){
						//playerImage = charImage7;
						//enemyImage = mobImage7;
					}

				}
				else{
					playerImage = charImage1;
					enemyImage = mobImage;
				}
			}
			if(gameKeyboard.charDirection == 2){
				if(gameKeyboard.keyPressed[KeyEvent.VK_RIGHT]){
					if(frames > 0 && frames <15){
						playerImage = charImage2;
						enemyImage = mobImage2;
					}
					else if(frames >=15 && frames < 30){
						playerImage = charImage3;
						enemyImage = mobImage3;
					}
				}
				else if(gameKeyboard.keyPressed[KeyEvent.VK_RIGHT] && gameKeyboard.keyPressed[KeyEvent.VK_SPACE]){
					if(frames > 0 && frames <15){
						//playerImage = charImage6;
						//enemyImage = mobImage6;
					}
					else if(frames >=15 && frames <30){
						//playerImage = charImage7;
						//enemyImage = mobImage7;
					}

				}
				else{
					playerImage = charImage2;
					enemyImage = mobImage3;
				}
			}
			if(gameKeyboard.charDirection == 3){
				if(gameKeyboard.keyPressed[KeyEvent.VK_LEFT]){
					if(frames >0 && frames <15){
						playerImage = horizontalFlip(charImage2);
						enemyImage = horizontalFlip(mobImage2);
					}
					else if(frames >=15 && frames < 30){
						playerImage = horizontalFlip(charImage3);
						enemyImage = horizontalFlip(mobImage3);
					}
				}
				else if(gameKeyboard.keyPressed[KeyEvent.VK_LEFT] && gameKeyboard.keyPressed[KeyEvent.VK_SPACE]){
					if(frames > 0 && frames <15){
						//playerImage = charImage6;
						//enemyImage = mobImage6;
					}
					else if(frames >=15 && frames <30){
						//playerImage = charImage7;
						//enemyImage = mobImage7;
					}

				}
				else{
					playerImage = horizontalFlip(charImage2);
					enemyImage = horizontalFlip(mobImage3);
				}
			}
			if(gameKeyboard.charDirection == 0){
				if(gameKeyboard.keyPressed[KeyEvent.VK_UP]){
					if(frames > 0 && frames <15){
						playerImage = charImage4;
						enemyImage = mobImage4;
					}

					else if(frames >=15 && frames < 30){
						playerImage = charImage5;
						enemyImage = mobImage5;
					}
				}
				else if(gameKeyboard.keyPressed[KeyEvent.VK_UP] && gameKeyboard.keyPressed[KeyEvent.VK_SPACE]){
					if(frames > 0 && frames <15){
						//playerImage = charImage6;
						//enemyImage = mobImage6;
					}
					else if(frames >=15 && frames <30){
						//playerImage = charImage7;
						//enemyImage = mobImage7;
					}

				}
				else{
					playerImage = charImage4;
					enemyImage = mobImage4;

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
			return move;
	}
	/*
	 * flips character image so they are facing correct way when going west
	 */
	public static BufferedImage horizontalFlip(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, img.getType());
        Graphics g = flippedImage.createGraphics();
        g.drawImage(img, 0, 0, width, height, width, 0, 0, height, null);
        g.dispose();
        return flippedImage;
    }


}
