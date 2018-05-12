
public class DNSRecord {

	private String name;
	private String value;
	private int type;
	
	/*Types corresponding to numbers are below
	* NS = 1
	* A = 2
 	* R = 3
	* CN = 4
	*/
	
	 /*Constructor for DNSRecord
	 * @param  name the name of the record
	 * @param  value the IP of the record
	 * @param type the DNS type
	 */
	public DNSRecord(String name, String value, int type)
	{
		this.name = name;
		this.value = value;
		this.type = type;
	}
	
	
	 /*Gets the name of the record
	 * @return the name 
	 */
	public String getName()
	{
		return name;
	}
	
	/*Gets the IP value of the record
	 * @return the value 
	 */
	public String getValue()
	{
		return value;
	}
	
	/*Gets the type of the record
	 * @return the type 
	 */
	public int getType()
	{
		return type;
	}
}
