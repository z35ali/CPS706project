import java.io.*;
import java.net.*;
public class hisCinema {

	//IP and port number of hisCinema server
	private static String hisCinemaIP = "127.0.0.1";
	private static int hisCinemaPort = 40414;
	
	//location of index.txt
	private static String filePath = "C:\\Users\\zafar\\eclipse-workspace\\706\\src\\index.txt";
	
	//main 
	public static void main(String args[]) throws Exception
	{
		while(true)
		{
			//contacting hisCinema server
			ServerSocket hisCinemaSocket = new ServerSocket(hisCinemaPort);
			Socket hisCinemaConnection = hisCinemaSocket.accept();
			
			ObjectOutputStream outFromClient = new ObjectOutputStream(hisCinemaConnection.getOutputStream());
			ObjectInputStream inFromClient = new ObjectInputStream(hisCinemaConnection.getInputStream());
			
			//request HTTP from the client to get the file 
			HTTPRequest userReq = (HTTPRequest) inFromClient.readObject();
		
			//checks if there is a request and outputs the request received if there is 
			if(userReq != null)
			{
				System.out.println("Received HTTPRequest from client: " + userReq.getFile() + " | " + userReq.getVersion() + " | " + userReq.getHostIP());
			}
			HTTPResponse serverResponse = null;
			
			//looks for the request in the index.txt file and if found outputs that the request is found
			
			FileReader file = new FileReader(filePath);
			BufferedReader reader = new BufferedReader(file);
			String nextLine;
			System.out.println("Checking to see if requested file is in database");
			while((nextLine = reader.readLine()) != null)
			{
				if(nextLine.equals(userReq.getFile()))
				{
					System.out.println("requested file is in the database");
					serverResponse = new HTTPResponse("video.hiscinema.com", 200);
					break;
				}
				else
				{
					serverResponse = new HTTPResponse("", 404);
				}
			}
			outFromClient.writeObject(serverResponse);
			
			//close file and reader
			reader.close();
			file.close();
			
			//close connection and socket
			hisCinemaConnection.close();
			hisCinemaSocket.close();
			
		}
	}
	
}
