
//Client class for our Network connection of our multi-player game
import java.net.*;

import javax.swing.JFrame;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

//
public class GameClient extends Thread{
	static Socket socket;
	static DataInputStream in;
	static DataOutputStream out;
	CollisionDetectionRenderExample game;
	int playerID;
	int[] x = new int[10];
	int[] y = new int[10];
	boolean left,down,right,up;//create keys used for movement
	int playerx;
	int playery;

	//gather and assign information from/to client connecting
	public GameClient(CollisionDetectionRenderExample game){
		this.game = game;
		
		try{
		System.out.println("Connecting...");//advise user that they are connecting to the server
		socket = new Socket("localhost", 7777);//Create socket with (host, port)
		System.out.println("Connection Successful!");
		in = new DataInputStream(socket.getInputStream());
		playerID = in.readInt();
		out = new DataOutputStream(socket.getOutputStream());
		Input input = new Input(in, this);
		Thread thread = new Thread(input);
		thread.start();
		//Thread thread2 = new Thread(this);
		//thread2.start();
		
		}catch(Exception e){
			e.printStackTrace();
		}

	}

class Input implements Runnable{
	DataInputStream in;
	GameClient client;
	public Input(DataInputStream in, GameClient c){
		this.in = in;
		this.client = c;
	}

	public void run() {
		while(true){
			game.run();

		}
	}
}
}
