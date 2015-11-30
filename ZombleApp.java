import java.awt.event.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

/**
 * The main game class.  Used for debugging and testing items until networking 
 * is resolved. 
 */
public class ZombleApp extends JFrame implements Runnable {
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
	private BackGround bg;
	
	private Scenic mainObstacle;
	
	//dangerous will be a test character that moves around, for collision detecting on TWO characters
	private Character dangerous;
	private Character mainC; //test of our drawCharacter class
	private GameKeyboard gameKeyboard;
	//private Mouse gameMouse;

	private boolean mainCMoved; // if mainCMoved, move Enemy //TODO reset
	
	/**
	 * Create the GUI, add elements, start the game thread.
	 */
	protected void createGUI(){
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(this); //set fullscreen mode!!!
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

		mainCMoved = true;
		//gameThread = new Thread(this); // no need to start new thread
		//gameThread.start();
	}

	/**
	 * Process user input.
	 */
	private void processInput(double delta){
		if(gameKeyboard.processInput(KeyEvent.VK_LEFT)){
			//set the location of the character
			//we'll utilize an interesting method based on facing direction. Of course the keys correspond to directions.
			//Up = 90, down = 270, left = 180, right = 0, based on euclidean plane geometry
			mainC.setDirection((mainC.getDirection() + 10));
		}
		if(gameKeyboard.processInput(KeyEvent.VK_RIGHT)){
			mainC.setDirection(mainC.getDirection() - 10);
		}
		if(gameKeyboard.processInput(KeyEvent.VK_SPACE)){
			
			mainC.attack();
			
			//player has attacked, get his sword
			if(mainC.isAttacking()){
				Sword s = (Sword) mainC.getWeapon();
				Arc2D.Float swordHit = s.getBoxCollider(mainC);
				
				System.out.println("We swung our sword");
			
				for(int i = 0; i < allActors.size(); i++){
					Character c = allActors.get(i);
					if((!c.equals(mainC)) && swordHit.intersects(c.getBoxCollider())){
						c.setHealth(c.getHealth() - s.getDamage());
						System.out.println("We hit an enemy, and their health is now: " + c.getHealth());
						if(c.getHealth() <= 0){
							allActors.remove(c);
						}
					}
				}
			}
		}
		if(gameKeyboard.processInput(KeyEvent.VK_DOWN)){
			//mainC.setLocation(new Vector(mainC.getLocation().x, mainC.getLocation().y + 5.0f));
			mainCMoved = true;
		}
		
		//do NOT use mouse, doesn't quite work properly
		//mainC.setDirection(gameMouse.getDirection()); //set new direction
		
		//this is the current x,y coord of our character
		double xCoord = mainC.getLocation().x, yCoord = mainC.getLocation().y;
		//this is our characters x, y coord translated to a euclidean plane, with the monitors center as point (0,0)
		double properXCoord, properYCoord;
		properXCoord = xCoord - (canvas.getWidth() / 2);
		properYCoord = (canvas.getHeight() / 2) - yCoord;		
		
		//THIS represents our position vector based on our speed. You see we're TRYING to
		//go a certain distance with this position vector, but we need to determine it's x and y coord.
		//we ALWAYS know the length of this vector, it's the characterSpeed!!!
		double directionX, directionY;
		
		//THESE USE DELTA to ADJUST the STEPSIZE appropriately based on framerate
		directionX =  (Math.cos(Math.toRadians(mainC.getDirection())) * characterSpeed * delta * 60); //return position vectors X coordinate
		directionY = (Math.sin(Math.toRadians(mainC.getDirection())) * characterSpeed * delta * 60); //return position vectors Y coordinate
		
		properXCoord += directionX; //add characters x position with position vector
		properYCoord += directionY; // "           " y "                           "
					
		//now change coordinates BACK to monitor coordinates
		xCoord =  properXCoord + (canvas.getWidth() / 2);
		yCoord = (canvas.getHeight() / 2) - properYCoord;
		//now that we have the position we know what coords to add.
		//simply add these to the character current x and y coord (in standard form
		mainC.setLocation(new Vector((float)xCoord, (float)yCoord)); //simply update characters location.
	}

	/**
	 * Move the enemy.  Enemy determines whether to use simpleStep or AStar.
	 */
	private void simpleMoveEnemy() {
		
		for(Character c: allActors){
			if(!c.isPlayer()){
				c.buildPath(
					mainC.getLocation(),
					displayMode.getWidth(),
					displayMode.getHeight(),
					allActors, 
					allObjects, 
					mainC.getBoxCollider()
				);
				c.step();
			}
		}		
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
	private void detectCollisions(double delta){
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
					
					//We now allow players to "pass through" enemies, but take damage
					//c.setLocation(c.getLastGoodLocation());
					//d.setLocation(d.getLastGoodLocation());
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
			mainC.setDirection(angle);
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
		
		g.drawImage(
			bg.getImage(),
			0,
			0,
			null
		);
		
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
		//TODO - Implement to work with animations, change image based on direction??? ETC
		//This will render a weapon IF our character is attacking!!!
		if(mainC.isAttacking()){
			g.drawImage(
				mainC.getWeapon().getImage(),
				(int) mainC.getLocation().x,
				(int) mainC.getLocation().y,
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
		BufferedImage mainEnemyImage = null;
		BufferedImage mainBackground = null;
		BufferedImage mainWeaponImage = null;
		try {
			mainPlayerImage = ImageIO.read(new File("C:\\Users\\Tyler_2\\Pictures\\mcFront1.png"));
			mainObstacleImage = ImageIO.read(new File("C:\\Users\\Tyler_2\\Pictures\\obs1.png"));
			mainEnemyImage = ImageIO.read(new File("C:\\Users\\Tyler_2\\Pictures\\eFront1.png"));
			mainBackground = ImageIO.read(new File("C:\\Users\\Tyler_2\\Pictures\\bg.png"));
			mainWeaponImage = ImageIO.read(new File("C:\\Users\\Tyler_2\\Pictures\\swordImage.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		dangerous = new Character(mainEnemyImage, new Vector(canvas.getWidth() / 4, canvas.getHeight() / 4));
		//set image and location of our drawCharacter class
		mainC = new Character(mainPlayerImage, new Vector(600, 20));
		mainC.isPlayer(true);
		bg = new BackGround(mainBackground, new Vector(0,0));
		
		//FOR SOME reason obstacles are causing issues with collission, which then causes a problem
		//with AStar
		/*
		for (Vector v : TEMPobsplace.getObstacles()) 
			allObjects.add( new Scenic(mainObstacleImage, v ));		
		*/
		
		//add characters to the actor arraylist
		allActors.add(mainC);
		allActors.add(dangerous);
		allActors.add(new Character(mainEnemyImage, new Vector( 300, 400)));
		allActors.add(new Character(mainPlayerImage, new Vector(600, 800)));
		
		mainC.addWeapon(new Sword(mainWeaponImage, 10, 80));

		//add new keyboard listener GameKeyboard
		gameKeyboard = new GameKeyboard(mainC);
		//gameMouse = new Mouse(canvas);
		canvas.addKeyListener(gameKeyboard);
		//canvas.addMouseListener(gameMouse);
		//canvas.addMouseMotionListener(gameMouse);
		
		//DISABLE the mouse, so we cannot see it!!!
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.createImage("");
		Cursor cursor = tk.createCustomCursor(image, new Point(0, 0), "");
		setCursor(cursor); //set as our empty cursor
	}
	

	/**
	 * The main game loop.  Processes input, detects and resets any
	 * collisions, renders the images, and moves the enemy players.
	 * Also, pauses the thread for 10 milliseconds.
	 */
	private void gameLoop(double delta){
		
		mainC.updateCharacterV(delta);
		processInput(delta);
		detectCollisions(delta);
		renderFrame();
		if (mainCMoved) 
			simpleMoveEnemy();
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
		createGUI(); //make the GUI
		initialize(); //initialize everything
		long currentTime = System.nanoTime();
		long lastTime = currentTime;
		double nsPerFrame;
		running = true;
		
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
}

