import java.awt.Canvas;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.geom.Point2D;

import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;

import javax.imageio.ImageIO;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;


/**
 * The main game class.  Used for debugging and testing items until networking 
 * is resolved. 
 */
public class Zomble extends JFrame implements Runnable, GameConstants {
	private GameState gamestate;		// Game & Charaters
	private Character player;

	private Client client;			// Networking
	private int frames = 0;
	private BufferedImage mobImage;//south facing
	private BufferedImage mobImage1;//south facing2
	private BufferedImage mobImage2;//east facing
	private BufferedImage mobImage3;//west facing
	private BufferedImage mobImage4;//north facing
	private BufferedImage mobImage5;//north facing2
	private BufferedImage charImage;//south facing
	private BufferedImage charImage1;//south facing2
	private BufferedImage charImage2;//east facing
	private BufferedImage charImage3;//west facing
	private BufferedImage charImage4;//north facing
	private BufferedImage charImage5;//north facing2
	private BufferStrategy bs;		// Graphics
	private DisplayMode displayMode;
	private Canvas canvas;
	private BufferedImage playerImage;
	private BufferedImage obstacleImage;
	private BufferedImage enemyImage;
	private BufferedImage backgroundImage;
	private BufferedImage weaponImage;
	
	private GameKeyboard gameKeyboard;	// UI

	private volatile boolean running;	// Game Logic
	private Thread gameThread;

	private BackGround bg;

	private AudioInputStream audioBGMAmbience;
	private Clip clipBGMAmbience;
	
	/**
	 * Create the GUI, add elements, start the game thread.
	 */
	protected void createGUI(){
		gamestate = new GameState();
		client = new Client("localhost", 8000, gamestate); 

		player = null;
		while (player == null)
			player = client.getPlayer();

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		displayMode = gd.getDisplayMode();
		
		canvas = new Canvas();
		canvas.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		canvas.setBackground(Color.BLACK);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		
		setTitle("Zombles");
		setIgnoreRepaint(true);
		pack();
		
		setVisible(true);
		
		//Initialize audio
		try {
			audioBGMAmbience = AudioSystem.getAudioInputStream(new File("AudioFiles/sndBGMAmbience.wav")); //Freesound.org
			clipBGMAmbience = AudioSystem.getClip();
		}

		catch (UnsupportedAudioFileException LUE) {
			System.out.println("Error: Audio filetype is invalid!");
		}
		catch (LineUnavailableException LUE) {
			System.out.println("Error: The line is unavailable!");
		}
		catch (IOException LUE) {
			System.out.println("Error: The file is unavailable!");
		}
		
		//Start the BGM loop
		loopClip(audioBGMAmbience,clipBGMAmbience);
		
		
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		canvas.requestFocus();
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	/**
	 * Loop a specified sound.
 	 * @param ais The AudioInputStream of the particilar sound
	 * @param sound The Clip that the sound belongs to
	 */
	private void loopClip(AudioInputStream ais,Clip sound) {
		try {
			
			if (sound.isOpen() == true) {
				if(sound.isRunning() || sound.isActive()) {
					sound.stop();
					sound.flush();
				}
			}
			else {
				sound.open(ais);
			}
			sound.setFramePosition(0);
			sound.start();
			sound.loop(Clip.LOOP_CONTINUOUSLY);
			
		}
		catch (LineUnavailableException LUE) {
			System.out.println("Error: The line is unavailable!");
		}
		catch (IOException LUE) {
			System.out.println("Error: The file is unavailable!");
		}
	}

	/**
	 * Process user input.
	 */
	private void processInput(double delta) {
		gameKeyboard.processInput(delta);
		gamestate.detectCollisions(player);
	}

	/**
	 * Render the frame.
	 */
	private void renderFrame() {
		do{
			do{
				Graphics g = null;
				try{
					g = bs.getDrawGraphics();
					g.clearRect(0, 0, displayMode.getWidth(), displayMode.getHeight());
					g.drawImage(backgroundImage, 0, 0, java.awt.Color.BLACK, null);
					render(g);
				} finally {
					if(g != null){
						g.dispose();
					}
				}
			} while(bs.contentsRestored());
			bs.show();
		} while(bs.contentsLost());
	}
	//TODO make so each character has own actions 
	public BufferedImage action(BufferedImage move, Character c){
		try {
			this.player = c;
			//player images
			charImage = ImageIO.read(new File("Images//playerFrwd.png"));
			charImage1 = ImageIO.read(new File("Images//playerFrwd2.png"));
			charImage2 = ImageIO.read(new File("Images//playerSide.png"));
			charImage3 = ImageIO.read(new File("Images//playerSide2.png"));
			charImage4 = ImageIO.read(new File("Images//playerBack.png"));
			charImage5 = ImageIO.read(new File("Images//playerBack2.png"));
			charImage6 = ImageIO.read(new File("Images//plFrAtk.png"));
			charImage7 = ImageIO.read(new File("Images//playDead.png"));

			//zombie images
			mobImage = ImageIO.read(new File("Images//zombieFrwd.png"));
			mobImage1 = ImageIO.read(new File("Images//zombieFrwd2.png"));
			mobImage2 = ImageIO.read(new File("Images//zombieSide.png"));
			mobImage3 = ImageIO.read(new File("Images//zombieSide2.png"));
			mobImage4 = ImageIO.read(new File("Images//zombieBack.png"));
			mobImage5 = ImageIO.read(new File("Images//zombieBack2.png"));
			mobImage6 = ImageIO.read(new File("Images//zombAtk.png"));
			mobImage7 = ImageIO.read(new File("Images//zombDead.png"));

			frames++;
			if(frames >= 80){
				frames = 0;
			}
			
			
			/*if(character.getDirection() == UP ||character.getDirection() == DOWN || character.getDirection() == LEFT ||character.getDirection() == RIGHT && character.getHealth() == 0){
				zomble.playerImage = charImage7;
				zomble.enemyImage = mobImage7;
			}*/
			//&& !character.isAttacking && character.getHealth() > 0
			
			if(c.getDirection() >= 270){
				if(frames > 0 && frames <40){
					playerImage = charImage;
					enemyImage = mobImage;
				}
				else if(frames >=40 && frames < 80){
					playerImage = charImage1;
					enemyImage = mobImage1;
				}
			}
			/*else if(c.getDirection() == DOWN && c.isAttacking == true && c.getHealth() > 0){
					if(frames > 0 && frames <15){
						//playerImage = charImage6;
						//enemyImage = mobImage6;
					}
					else if(frames >=15 && frames <30){
						//playerImage = charImage7;
						//enemyImage = mobImage7;
				}
			}
			/*else{
					zomble.playerImage = charImage1;
					zomble.enemyImage = mobImage;
				}*/
			// && !character.isAttacking && character.getHealth() > 0
			if(c.getDirection() >= 0 && c.getDirection()< 90){
				if(frames > 0 && frames <40){
					playerImage = charImage2;
					enemyImage = mobImage2;
				}
				else if(frames >=40 && frames < 80){
					playerImage = charImage3;
					enemyImage = mobImage3;
				}
			}
		/*	else if(c.getDirection() == RIGHT && c.isAttacking == true && c.getHealth() > 0){
					if(frames > 0 && frames <15){
						//playerImage = charImage6;
						//enemyImage = mobImage6;
					}
					else if(frames >=15 && frames <30){
						//playerImage = charImage7;
						//enemyImage = mobImage7;
					}

				}
			/*	else{
					zomble.playerImage = charImage2;
					zomble.enemyImage = mobImage3;
				}*/
			// && !character.isAttacking && character.getHealth() > 0
			if(c.getDirection()>= 180 && c.getDirection() < 270){
				if(frames >0 && frames <40){
					playerImage = horizontalFlip(charImage2);
					enemyImage = horizontalFlip(mobImage2);
				}
				else if(frames >=40 && frames < 80){
					playerImage = horizontalFlip(charImage3);
					enemyImage = horizontalFlip(mobImage3);
				}
			}
			/*	else if(c.getDirection() == LEFT && c.isAttacking == true && c.getHealth() > 0){
					if(frames > 0 && frames <15){
						//playerImage = charImage6;
						//enemyImage = mobImage6;
					}
					else if(frames >=15 && frames <30){
						//playerImage = charImage7;
						//enemyImage = mobImage7;
					}

				}
				/*else{
					zomble.playerImage = horizontalFlip(charImage2);
					zomble.enemyImage = horizontalFlip(mobImage3);
				}*/
			
			// && !character.isAttacking && character.getHealth() > 0
			if(c.getDirection() >= 90 && c.getDirection()< 180){
				if(frames > 0 && frames <40){
					playerImage = charImage4;
					enemyImage = mobImage4;
				}
				else if(frames >=40 && frames < 80){
					playerImage = charImage5;
					enemyImage = mobImage5;
				}
			}
			/*	else if(c.getDirection() == UP && c.isAttacking == true){
					if(frames > 0 && frames <15){
						//playerImage = charImage6;
						//enemyImage = mobImage6;
					}
					else if(frames >=15 && frames <30){
						//playerImage = charImage7;
						//enemyImage = mobImage7;
					}

				}*/
		}catch (IOException e) {
					e.printStackTrace();
				}
				/*	else{
					zomble.playerImage = charImage4;
					zomble.enemyImage = mobImage4;

				}*/
		

		
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
	/**
	 * Draw all Characters to the screen.
	 * @param g The graphics context.
	 */
	private void render(Graphics g) {
		HashMap<Integer, Character> characters = gamestate.getCharacters();
		g.drawImage(backgroundImage, 0, 0, this);
		for (Integer uid : characters.keySet()) {
			Character c = characters.get(uid);
			if(c.getType() == PLAYER){
				c.setImage(action(playerImage, c));
			}
			if(c.getType() == ENEMY){
				c.setImage(action(enemyImage, c));
			}
			g.drawImage(
				gamestate.getImage(c),
				(int) c.getBoxCollider().getLocation().x,
				(int) c.getBoxCollider().getLocation().y,
				null
			);
			if (c.getAttacking()) {
				System.out.println("ATTACK!");
				g.drawImage(
					c.getWeapon().getImage(),
					(int) c.getLocation().x,
					(int) c.getLocation().y,
					null
				);
				drawAngryDot(g, c);
			}
			drawHealthBar(g, c);
		}
		for (Scenic s : gamestate.getObstacles()) {
			g.drawImage(
				obstacleImage,
				(int) s.getLocation().x,
				(int) s.getLocation().y,
				null
			);
		}
		characters = null;
	}

	//TODO erase testing method
	private void drawAngryDot(Graphics g, Character c) {
		int xcoord = (int) c.getLocation().x - 10;
		int ycoord = (int) c.getLocation().y;
		g.setColor(new java.awt.Color(255, 0, 100, 200));
		g.fillRect(xcoord, ycoord, 4, 4);
	}

	/**
	 * Draws a health bar above each Character.
	 * @param g The graphic's context.
	 * @param c The Character whose health bar is being drawn.
	 */
	private void drawHealthBar(Graphics g, Character c) {
		int xcoord = (int) c.getLocation().x;
		int ycoord = (int) c.getLocation().y - 8;
		int totalWidth = (int) c.getBoxCollider().getWidth();
		int h = c.getHealth();
		if (h < 0) h = 0;
		int greenWidth = (int) ((float) h / (float) DEFAULT_HEALTH * totalWidth);
		int redWidth = totalWidth - greenWidth;
		g.setColor(new java.awt.Color(0, 255, 0, 200));
		g.fillRect(xcoord, ycoord, greenWidth, 4);
		g.setColor(new java.awt.Color(255, 0, 0, 200));
		g.fillRect(xcoord + greenWidth, ycoord, redWidth, 4);
	}

	/**
	 * Initialize global variables, load images, set obstacles.
	 */
	private void initialize(){
		//need to create our character
		playerImage = null;
		obstacleImage = null;
		enemyImage = null;
		backgroundImage = null;
		weaponImage = null;

		try {
			//playerImage = ImageIO.read(new File(PLR_IMAGE_FILENAME));
			obstacleImage = ImageIO.read(new File(OBS_IMAGE_FILENAME));
		//	enemyImage = ImageIO.read(new File(ENE_IMAGE_FILENAME));
			backgroundImage = ImageIO.read(new File(BAC_IMAGE_FILENAME));
			weaponImage = ImageIO.read(new File(WEA_IMAGE_FILENAME));

		} catch (IOException e) {
			e.printStackTrace();
		}

		//add new keyboard listener GameKeyboard
		gameKeyboard = new GameKeyboard( player, gamestate );
		canvas.addKeyListener(gameKeyboard);
	}
	

	/**
	 * The main game loop.  Processes input, detects and resets any
	 * collisions, renders the images, and moves the enemy players.
	 * Also, pauses the thread for 10 milliseconds.
	 */
	private void gameLoop(double delta) {
		processInput(delta);
		renderFrame();
		try { Thread.sleep(10L); } catch (InterruptedException ex) {}
	}

	/**
	 * Call the game loop while "running" global variable is true.
	 */
	public void run(){
		running = true;
		long currentTime = System.nanoTime();
		long lastTime = currentTime;
		double nsPerFrame;

		initialize();

		while(running){
			currentTime = System.nanoTime();
			nsPerFrame = currentTime - lastTime;
			gameLoop(nsPerFrame / 1.0E9);
			lastTime = currentTime;
		}
	}
	
	/**
	 * Set "running" to false and joins the gameThread to stop the game.
	 */
	public void onWindowClosing(){
		try{
			running = false;
			gameThread.join();
		} catch(InterruptedException ex) {}
		System.exit(0);
	}

	public static void main(String[] args ){
		final Zomble app = new Zomble();
		
		app.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				app.onWindowClosing();
			}
		});		
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				app.createGUI();
			}
		});
	}
}
