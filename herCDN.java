import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class herCDN {
	
	//IP and port numbers for herCDN server
	private static String herCDNIP = "127.0.0.1";
	private static int herCDNPort = 40415;
	
	
	//main 
	public static void main(String args[]) throws Exception
	{
		while(true)
		{
			//contacting herCDN server
			ServerSocket herCDNSocket = new ServerSocket(herCDNPort);
			Socket herCDNConnection = herCDNSocket.accept();
			
			ObjectOutputStream outFromClient = new ObjectOutputStream(herCDNConnection.getOutputStream());
			ObjectInputStream inFromClient = new ObjectInputStream(herCDNConnection.getInputStream());
			
			//request HTTP from the client to get the file 
			HTTPRequest userReq = (HTTPRequest) inFromClient.readObject();
		
			//checks if there is a request and outputs the request received if there is 
			if(userReq != null)
			{
				System.out.println("Received HTTPRequest from client: " + userReq.getFile() + " | " + userReq.getVersion() + " | " + userReq.getHostIP());
			}
			HTTPResponse serverResponse = new HTTPResponse("file.mp4", 200);
			outFromClient.writeObject(serverResponse);
			
			//close connection and socket
			herCDNConnection.close();
			herCDNSocket.close();
		}
	}
}
