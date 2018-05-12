import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class LocalDNS {

	//initialize the IP addresses, ports and recordtable.
	private static int localPort = 40408;
	private static int hisDNSPort = 40409;
	private static String IPHer = "127.0.0.1";
	private static String IPHis = "127.0.0.1";
	ArrayList<DNSRecord> recordTable;

	 /*Constructor for LocalDNS
		 */
	public LocalDNS()
	{
		recordTable = new ArrayList<DNSRecord>();
		recordTable.add(new DNSRecord("herCDN.com", "NSherCDN.com", 1));
		recordTable.add(new DNSRecord("NSherCDN.com", IPHer, 2));
		recordTable.add(new DNSRecord("hiscinema.com", "NShiscinema.com", 1));
		recordTable.add(new DNSRecord("NShiscinema.com", IPHis, 2));
	}

	
	/*If msg is not found query in hisDNS
	 * @param  rec the record
	 * @param  msg the message
	 * @return rec.getValue() the value corresponding to the record
	 */
	public String query(DNSRecord rec, String msg) throws Exception
	{
			if(msg.equals(rec.getName()))
			{
				return rec.getValue();
			}

			else
			{

				DatagramSocket localSocket = new DatagramSocket();
				InetAddress nextAddress = InetAddress.getByName(rec.getValue());
				byte[] sendData = msg.getBytes();
				DatagramPacket msgPacket = new DatagramPacket(sendData, sendData.length, nextAddress, hisDNSPort);
				localSocket.send(msgPacket);

				byte[] receivedData = new byte[1024];
				DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
				localSocket.receive(receivedPacket);
				String result = new String(receivedPacket.getData());

				return result;
			}

	}

	/*Checks the table for msg with corresponding type 
	 * @param  name the name of the record
	 * @return rec the record
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
				else if(msg.equals(rec.getName()) && rec.getType() == 1)
				{
					return checkTable(rec.getValue());
				}
			}
			msg = msg.substring(1, msg.length());
		}
		return null;
	}




	//main class
	public static void main(String args[]) throws Exception
	{

		//needs to run without ending, so use an endless while loop.
		while(true)
		{
			//initialize
			LocalDNS dns = new LocalDNS();
			DatagramSocket server = new DatagramSocket(localPort);
			byte[] receiveData = new byte[6024];

			DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
			server.receive(packet);
			InetAddress address = packet.getAddress();
			int port = packet.getPort();

			String response = new String(packet.getData());
			response = response.substring(packet.getOffset(), packet.getLength());
			DNSRecord record = dns.checkTable(response);
			System.out.println("Found IP address " + record.getValue() +" to " + record.getName() + " from local DNS");
			String dnsResponse = dns.query(record, response);

			byte[] send = dnsResponse.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(send, send.length, address, port);

			System.out.println("Sending IP from LocalDNS");
			server.send(sendPacket);
			server.close();
		}

	}
}
