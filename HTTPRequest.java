import java.io.Serializable;

public class HTTPRequest implements Serializable{

	private String file;
	private String version;
	private String hostIP;

	/*When called, assign file, version and hostIP to the variables used when calling the method
	 * @param requestedFile the file 
	 * @param version the version 
	 * @param hostIP the IP of the host
	 */
	public HTTPRequest(String requestedFile, String version, String hostIP)
	{
		this.file = requestedFile;
		this.version = version;
		this.hostIP = hostIP;
	}
	
	/*Get file String.
	*@return file the file
	*/
	public String getFile()
	{
		return file;
	}
	/*Get the Version
	*@return version version of the file.
	*/
	public String getVersion()
	{
		return version;
	}
	
	/*return host IP address.
	 *@return hostIP IP address of the host
	 */
	public String getHostIP()
	{
		return hostIP;
	}
	
	/*The file, version and hostIP are printed on one line
	 * @return toString 
	 */
	public String toString()
	{
		return "HTTP Request: [GET] " + file + " | " + version + " | " + hostIP + "\n";
	}
}
