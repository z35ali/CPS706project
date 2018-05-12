import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class hisDNS {
	//Port number and IP addresses are instantiated here.
	private static int hisDNSPort = 40409;
	private static int herDNSPort = 40411;
	private static int hisWebPort = 40412;
	private ArrayList<DNSRecord> recordTable;
	private static String IPHis = "127.0.0.1";
	private static String IPHer = "127.0.0.1";
	private int portNeeded;


	 /*Constructor for his DNS that adds 4 records to the recordTable.
		 */
	public hisDNS()
	{
		recordTable = new ArrayList<DNSRecord>();
		recordTable.add(new DNSRecord("www.hiscinema.com", IPHis, 2));
		recordTable.add(new DNSRecord("video.hiscinema.com", "herCDN.com",3));
		recordTable.add(new DNSRecord("herCDN.com", "NSherCDN.com",1));
		recordTable.add(new DNSRecord("NSherCDN.com", IPHer,2));
	}

	
	 /*Checks the table for msg with corresponding type 
		 * @param  msg the msg
		 *@return rec the record
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
					if(rec.getValue().equals("IPHer"))
					{
						portNeeded = herDNSPort;
					}

					else
					{
						portNeeded = hisWebPort;
					}
					return rec;
				}
				
				//when type NS, keep searching for IP
				else if(msg.equals(rec.getName()) && (rec.getType() == 1 || rec.getType() == 3))
				{
					return checkTable(rec.getValue());
				}
			}
			msg = msg.substring(1, msg.length());
		}
		return null;
	}

	
	
	 /*If msg is not found query in hisDNS
		 * @param  rec the record
		 * @param  msg the message
		 *  @return rec.getValue() the value corresponding to the record
		 */
	public String query(DNSRecord rec, String msg) throws Exception
	{
		//if msg is the same as the name of the DNSRecord, return the value of that record.
			if(msg.equals(rec.getName()))
			{
				return rec.getValue();
			}
			
		//otherwise,create new DatagramSocket and send data into that socket. Afterwards, return the received data from that socket.
			else
			{

				DatagramSocket hisDNSSocket = new DatagramSocket();
				InetAddress nextAddress = InetAddress.getByName(rec.getValue());
				byte[] sendData = msg.getBytes();
				DatagramPacket msgPacket = new DatagramPacket(sendData, sendData.length, nextAddress, 40411);
				hisDNSSocket.send(msgPacket);

				byte[] receivedData = new byte[1024];
				DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
				hisDNSSocket.receive(receivedPacket);
				String result = new String(receivedPacket.getData());

				return result;
			}

	}
	
//main class
	public static void main(String args[]) throws Exception
	{
		//needs to run without ending, so use an endless while loop.
		while(true)
		{

			//initialize
			hisDNS dns = new hisDNS();
			DatagramSocket hisDNSSocket = new DatagramSocket(hisDNSPort);
			byte[] receiveData = new byte[6024];

			//code for receiving
			DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
			hisDNSSocket.receive(receivedPacket);
			InetAddress address = receivedPacket.getAddress();
			int port = receivedPacket.getPort();
			String response = new String(receivedPacket.getData());
			response = response.substring(receivedPacket.getOffset(), receivedPacket.getLength());

			//if a response exists, print IP and name.
			DNSRecord record = dns.checkTable(response);
			System.out.println("Found IP address " + record.getValue() + " to " + record.getName() + " from hisDNS");
			String dnsResponse = dns.query(record, response);

			byte[] send = dnsResponse.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(send, send.length, address, port);
			//send IP then close socket.
			System.out.println("Sending IP from hisDNS");
			hisDNSSocket.send(sendPacket);
			hisDNSSocket.close();

		}
	}
}
