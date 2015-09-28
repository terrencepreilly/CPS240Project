/*
 * TYLER BEACHNAU
 * FullScreenRenderingExample.java
 * 
 * This is to show you guys the basics of the Game Loop and rendering thread, and how we
 * hopefully will put images to the screen. If you don't understand it all, that's fine,
 * I probably barely do either as I'm crashing through this 2D book, so this will PROBABLY
 * not be how our actual game code looks. But if you look through this code and try to understand
 * hopefully you'll get an idea of what's happening and it may help you with your code.
 * 
 * The program will basically render an image in full-screen and put an FPS counter.
 * It also imports a custom image and displays it on the screen. You can imagine that by
 * doing that we can import smaller images, move them, etc. For now I just used a giant
 * background to fill the screen :)
 * 
 * You MUST have the support file FrameRate.java or it won't work. You can package it
 * however you want, or just create new packages in Eclipse and label them javagames.render (put this file in that)
 * and javagames.util (put FrameRate.java in that).
 * If you make a new package for javagames.util and put FrameRate in that don't forget to alter
 * the import line #36 below. I.E. if you make a new package called "blahblah.util" then delete line
 * #36 and put "import blahblah.util;" Or if you just shove FrameRate.java in the same package as this code
 * then just remove the import line completely! ALSO IMPORTANT NOTE ON LINE 154, READ THAT BEFORE EXECUTING THIS PROGRAM
 */


package javagames.render;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javagames.util.*;

public class FullScreenRenderingExample extends JFrame implements Runnable{

	private FrameRate frameRate; //framerate object to utilize frameRate values
	private BufferStrategy bs; //our buffer strategy
	// private volatile boolean running; 
	private Thread gameThread; //our gameThread thread, not much more to be said
	private GraphicsDevice graphicsDevice; //in java.awt, basically the GPU/hardware
	private DisplayMode currentDisplayMode; //pulled from GraphicsDevice, basically the ACTUAL monitor/screen being used

	//constructor for this class, just creates a new frameRate object
	public FullScreenRenderingExample() {
		frameRate = new FrameRate();
	}

	//method to creat and show the GUI
	protected void createAndShowGUI(){
		//don't repaint screen, bufferstrategy handles this
		setIgnoreRepaint(true);
		setUndecorated(true); //can cause issues with full screen mode
		
		setBackground(Color.BLACK); //change background view

		//GraphicsEnvironment can getLocalGraphics environment (what screen we're using)
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		//assign the screen to a GraphicsDevice object to be able to manipulate
		graphicsDevice = ge.getDefaultScreenDevice();
		//grab the display mode currently being used from the graphics device (our screen)
		currentDisplayMode = graphicsDevice.getDisplayMode();
		//check to see if we can use full screen mode
		if(!graphicsDevice.isFullScreenSupported()) {
			System.err.println("ERROR: Not Supported!!!");
			System.exit(0);
		}

		//pass this object (the JFrame FullScreenRenderingExample) to the setFullScreenWindow method
		graphicsDevice.setFullScreenWindow(this);
		//sets displayMode using the getDisplayMode() method, created below
		graphicsDevice.setDisplayMode(getDisplayMode());

		//automatically creates a buffer strategy using 2 buffers (argument passed)
		createBufferStrategy(2);
		//assign bufferstrategy to bs
		bs = getBufferStrategy();

		//THIS IS THE CODE to allow ESCAPING from the window
		addKeyListener(new KeyAdapter() {
			public void keyPressed( KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					shutDown();
				}
			}
		});

		//start a new thread, this JFrame class
		gameThread = new Thread(this);
		gameThread.start();
	} //end createAndShowGUI method

	//get's displayMode, we alter this to get native screen support
	private DisplayMode getDisplayMode() {
		//currentDisplayMode.getWidth/getHeight on a DisplayMode object will return the screens width and height
		//should properly fill the screen with your screen width, height, bitdepth, and refresh rate
		return new DisplayMode(currentDisplayMode.getWidth(), currentDisplayMode.getHeight(), currentDisplayMode.getBitDepth(), currentDisplayMode.getRefreshRate());
	}

	//this is a method inherited from the Runnable interface
	public void run(){
		//basically, set this thread to run
		// running = true;
		frameRate.initialize(); //start frameRate object sets values to 0
		//while the thread is running, perform the gameLoop over and over
		// while(running) {
		while (gameThread != null) {
			gameLoop();
		}
	}

	//the gameLoop will be constantly run as it is part of the gameThread, and will run until the gameThread Thread is stopped
	public void gameLoop() {
		//main utilization of these do loops is for page-flipping to render better
		do{
			do{
				//each time this loop is run we reset g to be null
				Graphics g = null;
				try {
					g = bs.getDrawGraphics();
					//clear our bufferingscreen
					g.clearRect(0, 0, getWidth(), getHeight());
					//call our render loop
					render(g);
				} finally {
					//if g has an image in it already, dispose of it
					if(g != null) {
						g.dispose();
					}
				}
			} while(bs.contentsRestored());
			bs.show();
		} while(bs.contentsLost());
	}//end gameLoop

	
	//this is our render thread
	//this will render all our stuffs
	//remember this will be run constantly, over and over 
	private void render(Graphics g){
		//utilize frameRate mode to calculate fps
		frameRate.calculate();
		
		//assign text color
		g.setColor(Color.GREEN);
		
		//THIS will load in an image, we can manipulate it later
		//basically we need to use the BufferedImage class to load in a .png/.jpg/etc file.
		//try to assign it (we also need to use the file class obviously).
		BufferedImage ourBackground = null;
		try{
			//this will assign a custom image to the BufferedImage object named ourBackground
			//to change this, AND enable it to work on your machine you'll need to create or link
			//your own image, and include the path. Make sure to escape sequence and backslashes.
			//Basically find a picture on your pc, right click it, select properties, copy past the "location"
			//into the argument for the new File() object
			ourBackground = ImageIO.read(new File("C:\\Users\\Tyler_2\\Pictures\\Ty'sPics\\flowerbed_1920x1080.jpg"));
		} catch (IOException e) {}; //need to catch IOException
		
		//use Graphics.drawImage to place the image. the two 0's represent the co-ordinates
		g.drawImage(ourBackground, 0, 0, null);
		//this draws the "FPS = ??" string and the "Press ESC to exit" strings same premise as the above code.
		g.drawString(frameRate.getFrameRate(), 30, 30);
		g.drawString("Press ESC to exit...", 30, 60);
	}

	//this is to safely shutdown the program
	protected void shutDown() {
		//try{
			//running = false; //stop the thread, set boolean running to false
		//	gameThread.join(); //wait for the gameThread to die
			gameThread = null;
			System.out.println("Game loop stopped!!!"); //prints to console
			graphicsDevice.setDisplayMode(currentDisplayMode); //reset display mode
			graphicsDevice.setFullScreenWindow(null); //no longer utilize fullscreen mode
			System.out.println("Display Restored...");
		//} //catch (InterruptedException e) {
		//	e.printStackTrace();
		//}
		System.exit(0); //end program for sure
	}

	//main class, obviously
	public static void main(String[] args) {
		//create our class final enables it not be changed
		final FullScreenRenderingExample app = new FullScreenRenderingExample();
		//use this to actually run it. Comment out the next 5 lines, then uncomment the line that follows and see what happens
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				app.createAndShowGUI();
			}
		});
		//app.createAndShowGUI();
		//the program will still run, but without using SwingUtilities issues can arise with stability
	}//end main class method
}//end FullScreenRenderingExample class
