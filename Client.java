import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Client {

	//IP and port numbers for servers 
	static String hisCinemaIP = "127.0.0.1";
	static String clientIP = "127.0.0.1";
	static String localDNSIP = "127.0.0.1";
	static int clientPort = 40410;
	static int hisCinemaPort = 40414;
	static int localDNSPort = 40408;
	static int herCDNPort = 40415;
	
	//main
	public static void main(String args[]) throws IOException
	{
		String video = "";
		try 
		{
			//ask for user input for video number
			System.out.println("Choose number for corresponding video");
			System.out.println("[1] Video1");
			System.out.println("[2] Video2");
			System.out.println("[3] Video3");
			System.out.println("[4] Video4");
			System.out.println();
			Scanner videoInput = new Scanner(System.in);
			int videoNum = videoInput.nextInt();
			videoInput.close();
			
			//Switch case to set video variable to the number the user enters
			switch(videoNum)
			{
				case 1:
					video = "http://video.hiscinema.com/F1";
					break;
				case 2:
					video = "http://video.hiscinema.com/F2";
					break;
				case 3:
					video = "http://video.hiscinema.com/F3";
					break;
				case 4:
					video = "http://video.hiscinema.com/F4";
					break;
			}
			
			//contacting hisCinema
			Socket hisCinemaSocket = new Socket(hisCinemaIP, hisCinemaPort);
			ObjectInputStream hisCinemaIn = new ObjectInputStream(hisCinemaSocket.getInputStream());
			ObjectOutputStream hisCinemaOut = new ObjectOutputStream(hisCinemaSocket.getOutputStream());
			
			//request video from hisCinema
			HTTPRequest reqToHisCinema = new HTTPRequest(video, "1.1", clientIP);
			hisCinemaOut.writeObject(reqToHisCinema);
			HTTPResponse replyFromHisCinema = (HTTPResponse) hisCinemaIn.readObject();
			
			System.out.println("Received HTTPResponse from hisCinema: " + replyFromHisCinema.getFile() + " | " + replyFromHisCinema.getReply());
			
			String message = replyFromHisCinema.getFile();
			
			//Getting ip through localDNS
			DatagramSocket DNSsocket = new DatagramSocket(clientPort);
			InetAddress address = InetAddress.getByName(localDNSIP);
			
			byte[] sendData = new byte[1024];
			byte[] receiveData = new byte[1024];
			sendData = message.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, localDNSPort);
			DNSsocket.send(sendPacket);
			
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			DNSsocket.receive(receivePacket); 
			String receiveS = new String(receivePacket.getData());
			System.out.println("IP address for the video file is at: " + receiveS);
			
			//contacting herCDN
			Socket herCDNSocket = new Socket(receiveS, herCDNPort);
			ObjectInputStream herCDNIn = new ObjectInputStream(herCDNSocket.getInputStream());
			ObjectOutputStream herCDNOut = new ObjectOutputStream(herCDNSocket.getOutputStream());
			
			//request the video from herCDN
			HTTPRequest reqToHerCDN= new HTTPRequest(video, "1.1", clientIP);
			herCDNOut.writeObject(reqToHerCDN);
			
			//receive reply from herCDN and output 
			HTTPResponse replyFromHerCDN = (HTTPResponse) herCDNIn.readObject();
			System.out.println("Received HTTPResponse from herCDN: " + replyFromHerCDN.getFile() + " | " + replyFromHerCDN.getReply());
			
			
			
			
		} 
		catch (SocketException e) 
		{
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
	}
	
	
}
