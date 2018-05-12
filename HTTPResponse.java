import java.io.Serializable;

public class HTTPResponse implements Serializable{
	
	//initialize response (uses int value) and the file(as a string)
	private String file;
	private int reply;

	/*constructor for HTTPResponse
	*@param file that a response is being generated for.
	*@param reply the reply number
	*/
	public HTTPResponse(String file, int reply)
	{
		this.file = file;
		this.reply = reply;
	}
	
	
	/*Get file
	*@return file the file
	 */
	public String getFile()
	{
		return file;
	}
	
	/*Get the response
	*@return reply code which is used to generate a response
	*/
	public int getReply()
	{
		return reply;
	}
	
	/*Depending on the case (response type), give the appropriate response
	 * @return String toString
	 */
	public String toStringReply()
	{
		switch(reply)
		{
			case 200:
				return "OK";
			case 400:
				return "Bad Request";
			case 404:
				return "Not Found";
			case 301:
				return "Moved Permanently";
			case 505:
				return "HTTP Version Not Supported";
			default:
				return "NO RESPONSE MESSAGE";
		}


	}
}
