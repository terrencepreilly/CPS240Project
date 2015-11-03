//This is the Server Class to support multiple clients on our Network for a multiplayer game
 
import java.net.*;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;

public class GameServer {
	Character player;
	CollisionDetectionRenderExample game;
	static ServerSocket serverSocket;//create server socket
	static DataOutputStream out;
	static DataInputStream in;
	static Character[] user = new Character[10];//create array to hold users/players (10 max)
	private BufferedImage image;
	private Vector location;
	GameClient client;

	public GameServer(CollisionDetectionRenderExample game){
		this.game = game;
		try{
			image = ImageIO.read(new File("character.png"));
			System.out.println("Starting Server...");//notify user that the server is starting
			serverSocket =new ServerSocket(7777);//create new server socket with port number
			System.out.println("Server Started");//notify server is started
			while(true){//while server is active (used for multiple clients connecting)
				Socket socket = serverSocket.accept();//listen for client connections to accept
				for(int i = 0; i<5; i++){//search for open thread for client connecting
					if(user[i] == null){//check if there is an open space for user connecting
						out= new DataOutputStream(socket.getOutputStream());
						in = new DataInputStream(socket.getInputStream());
						System.out.println("Connection from: "+socket.getInetAddress());//print the clients ipAddress that connected
						location = new Vector(0, 0);//create new vector location for the user connecting
						user[i] = new Character(image, location, out, in, user, i);//create character for user
						Thread th = new Thread(user[i]);//create thread for user
						th.start();//start thread
						break;
					}
				}
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
}
