<
//Client class for our Network connection of our multi-player game
import java.net.*;
import java.applet.Applet;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

//
public class Client extends Applet implements Runnable, KeyListener{
	static Socket socket;
	static DataInputStream in;
	static DataOutputStream out;
	int playerID;
	int[] x = new int[10];
	int[] y = new int[10];
	boolean left,down,right,up;//create keys used for movement
	int playerx;
	int playery;

	//gather and assign information from/to client connecting
	public void init(){
		setSize(500, 500);//size of gameplay window
		addKeyListener(this);
		try{
		System.out.println("Connection...");//advise user that they are connecting to the server
		socket = new Socket("localhost", 7777);//Create socket with (host, port)
		System.out.println("Connection Successful!");
		in = new DataInputStream(socket.getInputStream());
		playerID = in.readInt();
		out = new DataOutputStream(socket.getOutputStream());
		Input input = new Input(in, this);
		Thread thread = new Thread(input);//create thread for user connecting
		thread.start();
		Thread thread2 = new Thread(this);
		thread2.start();
		
		}catch(Exception e){
			System.out.println("Unable to connect Client");
		}

	}
	//continuously update coordinates of player(s)
	public void updateCoordinates(int pID, int x2, int y2){
		this.x[pID] = x2;
		this.y[pID] = y2;
	}
	//create character for player connecting
	public void paint(Graphics g){
		for(int i = 0; i<10; i++){
			g.drawOval(x[i], y[i], 10, 10);//character and its dimensions
		}
	}
	//check key being pressed to determine which direction the player is moving
	//and move player in the direction of key pressed 
	public void run() {
		while(true){
			if(right == true){
				playerx+=10;
			}
			if(left == true){
				playerx-=10;
			}
			if(down == true){
				playery+=10;
			}
			if(up == true){
				playery-=10;
			}
			if(right || left || up || down){
				try{
					out.writeInt(playerID);
					out.writeInt(playerx);
					out.writeInt(playery);
				}
				catch(Exception e){
					System.out.println("Error sending Coordinates.");
				}
			}
			repaint();
			try {
				Thread.sleep(400);//puts thread in sleep period for 400 milliseconds while it determines if there are any exceptions
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		
	}
	//get key that is being pressed by user/player
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case 37:
		left =true;
		break;
		case 38:
		up = true;
		break;
		case 39:
		right = true;
		break;
		case 40:
		down = true;	
		break;
	}
		
	}
	//get key released and react to correct movement
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case 37:
		left =false;
		break;
		case 38:
		up = false;
		break;
		case 39:
		right = false;
		break;
		case 40:
		down = false;	
		break;
	}
		
	}

	public void keyTyped(KeyEvent e) {
	
	}

}
//
class Input implements Runnable{
	DataInputStream in;
	Client client;
	public Input(DataInputStream in, Client c){
		this.in = in;
		this.client = c;
	}
	
	public void run() {
		while(true){
			try{
				int playerID = in.readInt();
				int x = in.readInt();
				int y = in.readInt();
				client.updateCoordinates(playerID, x, y);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	}
	
}
>
