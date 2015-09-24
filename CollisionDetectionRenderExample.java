package RenderTests;

import java.awt.event.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import actors.util.Character;
import actors.util.Scenic;
import javax.imageio.ImageIO;
import javax.swing.*;
import manipulation.util.*;
import manipulation.util.Vector;


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
	private Scenic ob1;
	private Scenic ob2;
	private Scenic ob3;
	private Scenic ob4;
	
	//dangerous will be a test character that moves around, for collision detecting on TWO characters
	private Character dangerous;
	private Character mainC; //test of our drawCharacter class
	private GameKeyboard gameKeyboard;
	
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
				
		gameThread = new Thread(this);
		gameThread.start();
	}
	private void processInput(){
		//gameKeyboard.processInput();
		if(gameKeyboard.processInput(KeyEvent.VK_LEFT)){
			//set the location of the character
			mainC.setLocation(new Vector(mainC.getLocation().x - 5.0f, mainC.getLocation().y));
		}
		if(gameKeyboard.processInput(KeyEvent.VK_RIGHT)){
			mainC.setLocation(new Vector(mainC.getLocation().x + 5.0f, mainC.getLocation().y));
		}
		if(gameKeyboard.processInput(KeyEvent.VK_UP)){
			mainC.setLocation(new Vector(mainC.getLocation().x, mainC.getLocation().y - 5.0f));
		}
		if(gameKeyboard.processInput(KeyEvent.VK_DOWN)){
			mainC.setLocation(new Vector(mainC.getLocation().x, mainC.getLocation().y + 5.0f));
		}
	}
	
	private void detectCollisions(){
		//code in here will detect collisions
		boolean needToReset = false;
		//for every character test
		for(Character c: allActors){
			//check for collisions between CHARACTERS and SCENICS
			for(Scenic a: allObjects){
				if(BoxCollider.detectCollision(a, c)){
					//if our character is inside ANY object, set needToReset to true
					//and IMMEDIETLY set location to lastGoodLocation to get it OUT of the object
					needToReset = true;
					c.setLocation(c.getLastGoodLocation());
				}
			}
			//check for collisions between TWO CHARACTERS			
			for(Character d: allActors){
				if(!(c == d) && BoxCollider.detectCollision(c, d)){
					//if our character is inside ANY object, set needToReset to true
					//and IMMEDIETLY set location to lastGoodLocation to get it OUT of the object
					needToReset = true;
					
					c.setLocation(c.getLastGoodLocation());
					d.setLocation(d.getLastGoodLocation());
				}
				//now we've reached the END of all scenic objects. If needToReset is still false
				//this is we checked EVERY object and our character is not inside any of them then
				//we can assign lastGoodLocation to current Location :)
			}
			if(!needToReset){
				c.setLastGoodLocation(c.getLocation());
			}
		}		
	}
	
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
	
	private void render(Graphics g) {
		
		g.setColor(Color.GREEN);
		g.drawString("HEALTH: " + mainC.getHealth(), 20, 20);
		
		//draw special info
		g.drawString("Characters CURRENT LOCATION:" + mainC.getLocation(), 20, 35);
		g.drawString("Characters LAST GOOD LOCATION: " + mainC.getLastGoodLocation() , 20, 50);
		g.drawString("Characters boxCOLLIDER coordinates: " + mainC.getBoxCollider(), 20, 65);
		
		g.drawString("Ob1 boxCOLLIDER coordinates: " + ob1.getBoxCollider(), 20, 80);
		g.drawString("Ob2 boxCOLLIDER coordinates: " + ob2.getBoxCollider(), 20, 95);
		
		//draw characters and obstacles
		g.drawImage(dangerous.getImage(), (int) dangerous.getLocation().x, (int)dangerous.getLocation().y, null);
		g.drawImage(mainC.getImage(), (int) mainC.getLocation().x, (int)mainC.getLocation().y, null);
		g.drawImage(mainObstacle.getImage(), (int)mainObstacle.getLocation().x, (int)mainObstacle.getLocation().y, null);
		g.drawImage(ob1.getImage(), (int)ob1.getLocation().x, (int)ob1.getLocation().y, null);
		g.drawImage(ob2.getImage(), (int)ob2.getLocation().x, (int)ob2.getLocation().y, null);
		g.drawImage(ob3.getImage(), (int)ob3.getLocation().x, (int)ob3.getLocation().y, null);
		g.drawImage(ob4.getImage(), (int)ob4.getLocation().x, (int)ob4.getLocation().y, null);

	}
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
		dangerous = new Character(mainPlayerImage, new Vector(800, 200));
		//set image and location of our drawCharacter class
		mainC = new Character(mainPlayerImage, new Vector(600, 400));
		//create an obstacle
		mainObstacle = new Scenic(mainObstacleImage, new Vector(300, 300));
		
		//assign 4 random cube obstacles
		ob1 = new Scenic(mainObstacleImage, new Vector((float)(Math.random() * 800), (float)(Math.random() * 700)));
		ob2 = new Scenic(mainObstacleImage, new Vector((float)(Math.random() * 800), (float)(Math.random() * 700)));
		ob3 = new Scenic(mainObstacleImage, new Vector((float)(Math.random() * 800), (float)(Math.random() * 700)));
		ob4 = new Scenic(mainObstacleImage, new Vector((float)(Math.random() * 800), (float)(Math.random() * 700)));

		
		//add characters to the actor arraylist
		allActors.add(mainC);
		allActors.add(dangerous);
		
		//add all objects to the arraylist
		allObjects.add(mainObstacle);
		
		allObjects.add(ob1);
		allObjects.add(ob2);
		allObjects.add(ob3);
		allObjects.add(ob4);
		
		//add new keyboard listener GameKeyboard
		gameKeyboard = new GameKeyboard(mainC);
		canvas.addKeyListener(gameKeyboard);
	}
	
	private void gameLoop(){
		processInput();
		detectCollisions();
		renderFrame();
		sleep(10L);
	}
	
	private void sleep(long sleep){
		try{
			Thread.sleep(sleep);
		} catch (InterruptedException ex) {}
	}
	public void run(){
		running = true;
		initialize();
		while(running){
			gameLoop();
		}
	}
	
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
