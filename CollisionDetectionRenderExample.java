package RenderTests;

import java.awt.event.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import manipulation.util.BoxCollider;
import manipulation.util.Vector;
import manipulation.util.GameKeyboard;
import actors.util.Actor;
import actors.util.Background;
import actors.util.Scenic;
import actors.util.Character;

import java.awt.geom.Point2D;

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
	
	private float characterSpeed = 7.0f; //the SPEED of the character
	
	private ArrayList<Character> allActors = new ArrayList<Character>(); //this is all the actors, which will have all their box colliders
	private ArrayList<Scenic> allObjects = new ArrayList<Scenic>(); 
	
	private Scenic mainObstacle;
	
	//dangerous will be a test character that moves around, for collision detecting on TWO characters
	private Character dangerous;
	private Character mainC; //test of our drawCharacter class
	private Background mainBackground;
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
			//we'll utilize an interesting method based on facing direction. Of course the keys correspond to directions.
			//Up = 90, down = 270, left = 180, right = 0, based on euclidean plane geometry
			//mainC.setDirection(180);
			mainC.setDirection(mainC.getDirection() + 10);
			//mainC.setLocation(new Vector(mainC.getLocation().x - 5.0f, mainC.getLocation().y));
			mainCMoved = true;
		}
		if(gameKeyboard.processInput(KeyEvent.VK_RIGHT)){
			
			mainC.setDirection(mainC.getDirection() - 10);
			//mainC.setLocation(new Vector(mainC.getLocation().x + 5.0f, mainC.getLocation().y));
			mainCMoved = true;
		}
		if(gameKeyboard.processInput(KeyEvent.VK_UP)){
			//mainC.setLocation(new Vector(mainC.getLocation().x, mainC.getLocation().y - 5.0f));
			//mainC.setDirection(90);
			characterSpeed += 0.5;
			mainCMoved = true;
		}
		if(gameKeyboard.processInput(KeyEvent.VK_DOWN)){
			//mainC.setLocation(new Vector(mainC.getLocation().x, mainC.getLocation().y + 5.0f));
			//mainC.setDirection(270);
			characterSpeed -= 0.5;
			//now we've set the direction. The player is ALWAYS moving, so we simply change the location EVERY update
			mainCMoved = true;
		}		
		//this is the current x,y coord of our character
		float xCoord = mainC.getLocation().x, yCoord = mainC.getLocation().y;
		//this is our characters x, y coord translated to a euclidean plane, with the monitors center as point (0,0)
		float properXCoord, properYCoord;
		properXCoord = xCoord - (canvas.getWidth() / 2);
		properYCoord = (canvas.getHeight() / 2) - yCoord;		
		
		//THIS represents our position vector based on our speed. You see we're TRYING to
		//go a certain distance with this position vector, but we need to determine it's x and y coord.
		//we ALWAYS know the length of this vector, it's the characterSpeed!!!
		float directionX, directionY;
		directionX = (float) (Math.cos(Math.toRadians(mainC.getDirection())) * characterSpeed); //return position vectors X coordinate
		directionY = (float) (Math.sin(Math.toRadians(mainC.getDirection())) * characterSpeed); //return position vectors Y coordinate
		
		properXCoord += directionX; //add characters x position with position vector
		properYCoord += directionY; // "           " y "                           "
				
		//now change coordinates BACK to monitor coordinates
		xCoord = properXCoord + (canvas.getWidth() / 2);
		yCoord = (canvas.getHeight() / 2) - properYCoord;
		//now that we have the position we know what coords to add.
		//simply add these to the character current x and y coord (in standard form
		mainC.setLocation(new Vector(xCoord, yCoord)); //simply update characters location.
	}

	/**
	 * Move the enemy.  Enemy determines whether to use simpleStep or AStar.
	 */
	private void simpleMoveEnemy() {
		/*
		dangerous.buildPath(
			mainC.getLocation(),
			displayMode.getWidth(),
			displayMode.getHeight(),
			allActors,
			allObjects,
			mainC.getBoxCollider()
		);
		dangerous.step();
		*/
	}

	// This function is FUGLY.  
	// Check the intersects function -- it may not want you to have things
	// right on the edge.
	private Vector getAGoodLocation(BoxCollider obsBC, BoxCollider chaBC) {
		// Find an overlapping vertex.
		Point2D.Float overlaps = null;
		for (Point2D.Float pf : chaBC.getVertices()) {
			if (obsBC.contains( pf ))
				overlaps = pf;
		} 

		// find closest edge 
		Point2D.Float top = new Point2D.Float(0f, obsBC.getLocation().y);
		Point2D.Float left = new Point2D.Float(obsBC.getLocation().x, 0f);
		Point2D.Float right = new Point2D.Float(obsBC.getLocation().x + (float) obsBC.getWidth(), 0f);
		Point2D.Float bottom = new Point2D.Float(0f, obsBC.getLocation().y + (float) obsBC.getHeight());

		Point2D.Float closest = null;
		for (Point2D.Float pf : Arrays.asList( new Point2D.Float[] { top, left, right, bottom } ) ) {
			if (closest == null || overlaps.distance(closest) > overlaps.distance(pf))
				closest = pf;
		}

		// Update one coordinate to push outside.
		Vector addVec = new Vector(0f, 0f);
		if (closest.equals(top)) 
			addVec.y = -1*(overlaps.y - closest.y) - 0.1f;  
		else if (closest.equals(left))
			addVec.x = -1*(overlaps.x - closest.x) - 0.1f;
		else if (closest.equals(right))
			addVec.x = closest.x - overlaps.x + 0.1f; 
		else if (closest.equals(bottom))
			addVec.y = closest.y - overlaps.y + 0.1f; 
		
		return chaBC.getLocation().add(addVec);
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
					c.setLocation( getAGoodLocation(a.getBoxCollider(), c.getBoxCollider()) );
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
		/**
		 * Code below is horribly ugly, sorry (Tyler), but it works well enough for keeping the character within bounds
		 * and correctly enough bounces the character off at angles. Tried finding different methods for calculating
		 * angles but this one seemed the simplest. if anyone has a better method, please feel free to change it!
		 */
		//THIS CODE performs background/boundary checking on the characters and repositions and bounces character off "walls"
		if(mainC.getLocation().x > canvas.getWidth() || mainC.getLocation().x < 0
				|| mainC.getLocation().y < 0 || mainC.getLocation().y > canvas.getHeight()){			
			float x = mainC.getLocation().x, y = mainC.getLocation().y; //grab current character coordinates
			int angle = mainC.getDirection(); //grab characters angle of direction
			if(x < 0){ //hitting left wall
				if(angle >= 180){
					angle = 360 - angle % 90; //change angle for bounce back
				} else{
					angle = 90 - angle % 90;
				}
				mainC.setLocation(new Vector(0, y));
			} else if( y < 0){ //hitting top wall
				if(angle >= 90){
					angle = 270 - angle % 90;
				} else{
					angle = 360 - angle % 90;
				}
				mainC.setLocation(new Vector(x, 0));
			} else if( x > canvas.getWidth()){ //hitting right wall
				if(angle >= 270){
					angle = 270 - angle % 90;
				} else{
					angle = 180 - angle % 90;
				}
				mainC.setLocation(new Vector(canvas.getWidth(), y));
			} else{ //hit bottom
				if(angle >= 270){
					angle = 90 - angle % 90;
				} else{
					angle = 180 - angle % 90;
				}
				mainC.setLocation(new Vector(x, canvas.getHeight()));
			}
			mainC.setDirection(angle); //set new direction
		}
	}
	
	/**
	 * Render the frame using double buffering strategy, double buffer, two do loops :)
	 */
	private void renderFrame() {
		do{
			do{
				Graphics g = null; //create Graphics object
				try{
					g = bs.getDrawGraphics(); //get graphics from our bufferstrategy
					g.clearRect(0, 0, displayMode.getWidth(), displayMode.getHeight()); //clear the screen (basically draws rectangle over the whole screen
					render(g); //call OUR custome render function every time
				} finally {
					if(g != null){ //dispose if g is not null
						g.dispose();
					}
				}
			}while(bs.contentsRestored());
			bs.show(); //make next available buffer visible
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
			mainPlayerImage = ImageIO.read(new File("/Users/TheOne/Pictures/personal/mainCharacterIdle_1.png"));
			mainObstacleImage = ImageIO.read(new File("/Users/TheOne/Pictures/personal/mainObstacle.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		dangerous = new Character(mainPlayerImage, new Vector(canvas.getWidth() / 4, canvas.getHeight() / 4));
		//set image and location of our drawCharacter class
		mainC = new Character(mainPlayerImage, new Vector(canvas.getWidth() / 2, canvas.getHeight() / 2));
		mainBackground = new Background(canvas.getWidth(), canvas.getHeight());
		/*
		for (Vector v : TEMPobsplace.getObstacles()) 
			allObjects.add( new Scenic(mainObstacleImage, v ));
		*/
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
		//uncommenting the line below increases players speed over time, DO NOT use a value at or greater than 1, values lower than .08 are pretty acceptable
		//but player speed can quickly go out of control, once we have a boundary it'll be nicer, and we can set a speed cap as well
		//characterSpeed += 0.08f;
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
