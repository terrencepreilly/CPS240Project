import java.awt.event.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * The main game class.  Used for debugging and testing items until networking 
 * is resolved. 
 */
public class CollisionDetectionRenderExample extends JFrame implements Runnable {
	private volatile boolean running;
	private Thread gameThread;
	private BufferStrategy bs;
	private DisplayMode displayMode;
	private GraphicsEnvironment ge;
	private GraphicsDevice gd;
	private Canvas canvas;
	
	private ArrayList<Character> allActors = new ArrayList<Character>(); //this is all the actors, which will have all their box colliders
	private ArrayList<Scenic> allObjects = new ArrayList<Scenic>(); 
	
	private Scenic mainObstacle;
	
	//dangerous will be a test character that moves around, for collision detecting on TWO characters
	private Character dangerous;
	private Character mainC; //test of our drawCharacter class
	private GameKeyboard gameKeyboard;

	private boolean mainCMoved; // if mainCMoved, move Enemy //TODO reset

	/**
	 * Create the GUI, add elements, start the game thread.
	 */
	protected void createGUI(){
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();
		displayMode = gd.getDisplayMode();
		
		canvas = new Canvas();
		canvas.setSize(displayMode.getWidth(), displayMode.getHeight());
		canvas.setBackground(Color.BLACK);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		
		setTitle("This is the JFRAME title");
		setIgnoreRepaint(true);
		pack();
		
		setVisible(true);
		
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		canvas.requestFocus();

		mainCMoved = false;
		gameThread = new Thread(this);
		gameThread.start();
	}

	/**
	 * Process user input.
	 */
	private void processInput(){
		if(gameKeyboard.processInput(KeyEvent.VK_LEFT)){
			//set the location of the character
			mainC.setLocation(new Vector(mainC.getLocation().x - 5.0f, mainC.getLocation().y));
			mainCMoved = true;
		}
		if(gameKeyboard.processInput(KeyEvent.VK_RIGHT)){
			mainC.setLocation(new Vector(mainC.getLocation().x + 5.0f, mainC.getLocation().y));
			mainCMoved = true;
		}
		if(gameKeyboard.processInput(KeyEvent.VK_UP)){
			mainC.setLocation(new Vector(mainC.getLocation().x, mainC.getLocation().y - 5.0f));
			mainCMoved = true;
		}
		if(gameKeyboard.processInput(KeyEvent.VK_DOWN)){
			mainC.setLocation(new Vector(mainC.getLocation().x, mainC.getLocation().y + 5.0f));
			mainCMoved = true;
		}
	}

	/**
	 * Move the enemy.  Enemy determines whether to use simpleStep or AStar.
	 */
	private void simpleMoveEnemy() {
		dangerous.buildPath(
			mainC.getLocation(),
			displayMode.getWidth(),
			displayMode.getHeight(),
			allActors,
			allObjects,
			mainC.getBoxCollider()
		);
		dangerous.step();
	}

	/**
	 * Detect collisions and restore characters to last good location.
	 */
	private void detectCollisions(){
		boolean needToReset = false;
		for(Character c: allActors){
			for(Scenic a: allObjects){
				if(a.getBoxCollider().intersects(c.getBoxCollider())){
					needToReset = true;
					c.setLocation(c.getLastGoodLocation());
				}
			}
			for(Character d: allActors){
				if(!(c == d) && c.getBoxCollider().intersects(d.getBoxCollider())){
					needToReset = true;
					
					c.setLocation(c.getLastGoodLocation());
					d.setLocation(d.getLastGoodLocation());
				}
			}
			if(!needToReset){
				c.setLastGoodLocation(c.getLocation());
			}
		}		
	}
	
	/**
	 * Render the frame. ???
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
			}while(bs.contentsRestored());
			bs.show();
		} while(bs.contentsLost());
	}
	
	/**
	 * Draw characters to the screen.
	 * @param g The graphics context.
	 */
	private void render(Graphics g) {
		g.setColor(Color.GREEN);
		g.drawString("HEALTH: " + mainC.getHealth(), 20, 20);
		
		//draw characters and obstacles
		for (Actor act : allActors) {
			g.drawImage(
				act.getImage(),
				(int) act.getLocation().x,
				(int) act.getLocation().y,
				null
			);
		}
		for (Scenic obj : allObjects) {
			g.drawImage( 
				obj.getImage(), 
				(int) obj.getLocation().x, 
				(int) obj.getLocation().y, 
				null 
			);
		}

		drawTestingInfo(g);
	}
	
	/**
	 * Write testing information to the screen.
	 * @param g The graphic context.
	 */
	private void drawTestingInfo(Graphics g) {
		g.drawString("Characters CURRENT LOCATION:" + mainC.getLocation(), 20, 35);
		g.drawString("Characters LAST GOOD LOCATION: " + mainC.getLastGoodLocation() , 20, 50);
		g.drawString("Characters boxCOLLIDER coordinates: " + mainC.getBoxCollider(), 20, 65);
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
		dangerous = new Character(mainPlayerImage, new Vector(800, 200));
		//set image and location of our drawCharacter class
		mainC = new Character(mainPlayerImage, new Vector(600, 400));
		

		for (Vector v : TEMPobsplace.getObstacles()) 
			allObjects.add( new Scenic(mainObstacleImage, v ));
		
		//add characters to the actor arraylist
		allActors.add(mainC);
		allActors.add(dangerous);

		//add new keyboard listener GameKeyboard
		gameKeyboard = new GameKeyboard(mainC);
		canvas.addKeyListener(gameKeyboard);
	}
	

	/**
	 * The main game loop.  Processes input, detects and resets any
	 * collisions, renders the images, and moves the enemy players.
	 * Also, pauses the thread for 10 milliseconds.
	 */
	private void gameLoop(){
		processInput();
		detectCollisions();
		renderFrame();
		if (mainCMoved) 
			simpleMoveEnemy();
		sleep(10L);
	}

	/**
	 * Pause the game for sleep milliseconds.
	 * @param sleep The length of time to pause the thread.
	 */	
	private void sleep(long sleep){
		try{
			Thread.sleep(sleep);
		} catch (InterruptedException ex) {}
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
		final CollisionDetectionRenderExample app = new CollisionDetectionRenderExample();
		
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
