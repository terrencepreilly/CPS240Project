<
//This is the Server Class to support multiple clients on our Network for a multi-player game
 

import java.net.*;
import java.io.*;

public class Server {
	
	static ServerSocket serverSocket;//create server socket
	static Users[] user = new Users[10];//create array to hold users/players (10 max)
	
	public static void main(String[] args) throws Exception{
		System.out.println("Starting Server...");//notify user that the server is starting
		serverSocket =new ServerSocket(7777);//create new server socket with port number
		System.out.println("Server Started");//notify server is started
		while(true){//while server is active
			Socket socket = serverSocket.accept();//listen for connections
			for(int i = 0; i<10; i++){//collect information from users connecting
				if(user[i] == null){
					System.out.println("Connection from: "+socket.getInetAddress());
					DataOutputStream out= new DataOutputStream(socket.getOutputStream());
					DataInputStream in = new DataInputStream(socket.getInputStream());

					user[i] = new Users(out, in, user, i);//assign user
					Thread th = new Thread(user[i]);//create thread for user
					th.start();
					break;
				}
			}
		}
	}

}
class Users implements Runnable{
	//Define thread class for handling a new connection
	DataOutputStream out;
	DataInputStream in;
	Users[] user = new Users[10];
	int playerID;
	int playerIDin;
	int xin;
	int yin;
	//Construct thread
	public Users(DataOutputStream out, DataInputStream in, Users[] user, int pID){
		this.out = out;
		this.in = in;
		this.user = user;
		this.playerID = pID;
		
	}
	//Run the thread
	public void run() {
		try {
			out.writeInt(playerID);
		} catch (IOException e1) {
			System.out.println("Failed to send PlayerID");
		}
		while(true){//continuously serve client while connected
			try{
				playerIDin  = in.readInt();
				xin = in.readInt();
				yin = in.readInt();
				for(int i = 0; i<10; i++){
					if(user[i] != null){
						user[i].out.writeInt(playerIDin);
						user[i].out.writeInt(xin);
						user[i].out.writeInt(yin);
					}
				}
			}
			catch(IOException e){
				user[playerID] = null;
			}
		}
	}
	
}
>
