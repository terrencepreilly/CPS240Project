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

import javax.imageio.ImageIO;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 * The main game class.  Used for debugging and testing items until networking 
 * is resolved. 
 */
public class Zomble extends JFrame implements Runnable {
	private volatile boolean running;
	private Thread gameThread;
	private BufferStrategy bs;
	private DisplayMode displayMode;
	private Canvas canvas;
	
	private GameKeyboard gameKeyboard;

	/**
	 * Create the GUI, add elements, start the game thread.
	 */
	protected void createGUI(){
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
	private void processInput(){
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
	 * Initialize global variables, load images, set obstacles.
	 */
	private void initialize(){
		//need to create our character
		BufferedImage mainPlayerImage = null;
		BufferedImage mainObstacleImage = null;
		try {
			mainPlayerImage = ImageIO.read(new File("character.png"));
			mainObstacleImage = ImageIO.read(new File("obstacle.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		// add characters 
		// add obstacles

		//add new keyboard listener GameKeyboard
		//gameKeyboard = new GameKeyboard(mainC);
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
