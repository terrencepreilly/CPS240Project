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

import java.util.concurrent.locks.*;


/**
 * The main game class.  Used for debugging and testing items until networking 
 * is resolved. 
 */
public class Zomble extends JFrame implements Runnable, GameConstants {
	private GameState gamestate;		// Game & Charaters
	private Character player;

	private Client client;			// Networking

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
		
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		canvas.requestFocus();
		gameThread = new Thread(this);
		gameThread.start();
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

	/**
	 * Draw all Characters to the screen.
	 * @param g The graphics context.
	 */
	private void render(Graphics g) {
		Lock lock = gamestate.getLock();
		lock.lock();
		HashMap<Integer, Character> characters = gamestate.getCharacters();
		for (Integer uid : characters.keySet()) {
			Character c = characters.get(uid);
			g.drawImage(
				(c.getType() == ENEMY) ? enemyImage : playerImage,
				(int) c.getBoxCollider().getLocation().x,
				(int) c.getBoxCollider().getLocation().y,
				null
			);
			if (c.getAttacking()) {
				g.drawImage(
					c.getWeapon().getImage(),
					(int) c.getLocation().x,
					(int) c.getLocation().y,
					null
				);
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
		lock.unlock();
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
		int greenWidth = (int) ((float) c.getHealth() / 
			(float) DEFAULT_HEALTH * totalWidth);
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
			playerImage = ImageIO.read(new File(PLR_IMAGE_FILENAME));
			obstacleImage = ImageIO.read(new File(OBS_IMAGE_FILENAME));
			enemyImage = ImageIO.read(new File(ENE_IMAGE_FILENAME));
			//backgroundImage = ImageIO.read(new File(BAC_IMAGE_FILENAME));
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
		player.updateCharacterV(delta);
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
