import java.awt.Canvas;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.KeyEvent;
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


/**
 * The main game class.  Used for debugging and testing items until networking 
 * is resolved. 
 */
public class Zomble extends JFrame implements Runnable, GameConstants {
	private GameState gamestate;		// Game & Charaters
	private Character player;
	private Client client;			// Networking
	private int frames = 0;		//frame count for character movement
	private BufferStrategy bs;		// Graphics
	private DisplayMode displayMode;
	private Canvas canvas;
	private BufferedImage playerImage;
	private BufferedImage obstacleImage;
	private BufferedImage enemyImage;
	private BufferedImage charImage;//south facing
	private BufferedImage charImage2;//east facing
	private BufferedImage charImage3;//west facing
	private BufferedImage charImage4;//north facing
	private GameKeyboard gameKeyboard;	// UI
	private Image background;
	private volatile boolean running;	// Game Logic
	private Thread gameThread;


	/**
	 * Create the GUI, add elements, start the game thread.
	 */
	protected void createGUI(){
		gamestate = new GameState();
		client = new Client("localhost", 8000, gamestate);
		player = client.getPlayer();

		client.requestUpdate();

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		displayMode = gd.getDisplayMode();
		
		canvas = new Canvas();
		canvas.setSize(displayMode.getWidth(), displayMode.getHeight());
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
	private void processInput() {
		gameKeyboard.processInput();
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
		player.setImage(action(playerImage));
		HashMap<Integer, Character> characters = gamestate.getCharacters();
		for (Integer uid : characters.keySet()) {
			Character c = characters.get(uid);
			g.drawImage(
				(c.getType() == ENEMY) ? enemyImage : playerImage,
				(int) c.getLocation().x,
				(int) c.getLocation().y,
				null
			);
		}
		for (Scenic s : gamestate.getObstacles()) {
			g.drawImage(
				obstacleImage,
				(int) s.getLocation().x,
				(int) s.getLocation().y,
				null
			);
		}
	}
	/* 
	 *Make background of character transparent 
	 */
	public BufferedImage makeTransparent(BufferedImage image){
		BufferedImage transparent = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i<image.getWidth(); i++){
			for(int j = 0; j<image.getHeight(); j++){
				if(image.getRGB(i, j) != image.getRGB(0, 0)){
					transparent.setRGB(i, j, image.getRGB(i, j));
				}
				
			}
		}
		return transparent;
	}
	/*
	 * Returns the correct image based on character direction
	 */
	public BufferedImage action(BufferedImage move){
		try {
			charImage = ImageIO.read(new File("playerFrwd.png"));
			charImage = makeTransparent(charImage);
			charImage2 = ImageIO.read(new File("img2.png"));
			charImage2 = makeTransparent(charImage2);
			charImage3 = ImageIO.read(new File("img3.png"));
			charImage3 = makeTransparent(charImage3);
			charImage4 = ImageIO.read(new File("img4.png"));
			charImage4 = makeTransparent(charImage4);
			//mainPlayerImage = ImageIO.read(new File("character.png"));
			//mainObstacleImage = ImageIO.read(new File("obstacle.png"));
			frames++;
			if(frames >= 50){
				frames = 0;
			}
			if(gameKeyboard.charDirection == 1){
				playerImage = charImage;
			}
			if(gameKeyboard.charDirection == 2){
				if(gameKeyboard.keyPressed[KeyEvent.VK_RIGHT]){
					if(frames > 0 && frames <25){
						playerImage = charImage2;
					}
					else if(frames >=25 && frames < 50){
						playerImage = charImage3;
					}
				}else{
					playerImage = charImage2;
				}
			}
			if(gameKeyboard.charDirection == 3){
				if(gameKeyboard.keyPressed[KeyEvent.VK_LEFT]){
					if(frames >0 && frames <25){
						playerImage = horizontalFlip(charImage2);
					}
					else if(frames >=25 && frames < 50){
						playerImage = horizontalFlip(charImage3);
					}
				}else{
					playerImage = horizontalFlip(charImage2);
				}
			}
			if(gameKeyboard.charDirection == 0){
				playerImage = charImage4;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
			/*frames++;
			if(frames>=30){
				frames = 0;
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
	 * Initialize global variables, load images, set obstacles.
	 */
	private void initialize(){
		//need to create our character
		playerImage = null;
		obstacleImage = null;
		enemyImage = null;

		try {
			playerImage = ImageIO.read(new File("playerFrwd.png"));
			obstacleImage = ImageIO.read(new File("obstacle.png"));
			enemyImage = ImageIO.read(new File("zombieFrwd.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		//add new keyboard listener GameKeyboard
		gameKeyboard = new GameKeyboard( player );
		canvas.addKeyListener(gameKeyboard);
	}
	

	/**
	 * The main game loop.  Processes input, detects and resets any
	 * collisions, renders the images, and moves the enemy players.
	 * Also, pauses the thread for 10 milliseconds.
	 */
	private void gameLoop(){
		processInput();
		renderFrame();
		try { Thread.sleep(10L); } catch (InterruptedException ex) {}
	}

	/**
	 * Call the game loop while "running" global variable is true.
	 */
	public void run(){
		running = true;
		initialize();
		while(running){
			gameLoop();
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
