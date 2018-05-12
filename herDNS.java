import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;


public class herDNS {
	
	//ArrayList containing DNS records 
	private ArrayList<DNSRecord> recordTable;
	
	//IP and port numbers for herDNS server
	private static int herDNSPort = 40411;
	String HerIP = "127.0.0.1";
	
	/*Constructor for herDNS that adds 2 records to the recordTable
	 */
	public herDNS()
	{
		recordTable = new ArrayList<DNSRecord>();
		recordTable.add(new DNSRecord("video.hiscinema.com", "www.herCDN.com", 4));
		recordTable.add(new DNSRecord("www.herCDN.com", HerIP ,2));
	}
	
	/*Checks the record table for a msg with the correct type 
	 * @param msg the string message to query for 
	 * @return DNSRecord the record
	 */
	public DNSRecord checkTable(String msg)
	{
		while(msg.length() > 0)
		{
			for(DNSRecord rec : recordTable)
			{
				//return if type A
				if(msg.equals(rec.getName()) && rec.getType() == 2)
				{
					return rec;
				}
				//keep searching for ip address if type NS
				else if(msg.equals(rec.getName()) && (rec.getType() == 4))
				{
					return checkTable(rec.getValue());
				}
			}
			msg = msg.substring(1, msg.length());
		}
		return null;
	}
	
	public static void main(String args[]) throws Exception
	{
		//initializing DNS 
		herDNS dns = new herDNS();
		DatagramSocket herDNSSocket = new DatagramSocket(herDNSPort);
		byte[] receiveData = new byte[6024];
		
		//receive packet and get the port number of it
		DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
		herDNSSocket.receive(receivedPacket);
		InetAddress address = receivedPacket.getAddress();
		int port = receivedPacket.getPort();
		
		//saves the response in a string and
		String response = new String(receivedPacket.getData());
		response = response.substring(receivedPacket.getOffset(), receivedPacket.getLength());
		
		//adds the response record to the record table 
		DNSRecord record = dns.checkTable(response);
		System.out.println("Found IP address " + record.getValue() + " to video.hiscinema.com in herDNS");
		
		//sends the IP record to herDNS socket
		byte[] send = record.getValue().getBytes();
		DatagramPacket sendPacket = new DatagramPacket(send, send.length, address, port);
		System.out.println("Sending IP from herDNS");
		herDNSSocket.send(sendPacket);
		herDNSSocket.close();
	}
}
