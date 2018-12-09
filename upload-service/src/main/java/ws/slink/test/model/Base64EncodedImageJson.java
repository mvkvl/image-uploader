package ws.slink.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Base64EncodedImageJson {

	
	@JsonProperty("name")
	public String name;
	
	@JsonProperty("type")
	public String type;
	
	@JsonProperty("base64")
	public String base64;
	
	public String base64sub() {
		return (base64.length() > 43) ? 
				base64.substring(0, 20) + "..." + base64.substring(base64.length() - 20) : 
				base64;   
	}
	
	public String toString() {
		return 
		  "{" +
		  "\"name\":\"" + name + "\", " + 		
		  "\"type\":\"" + type + "\", " + 		
		  "\"base64\":\"" + base64sub() + "\"" + 		
		  "}";
	}
}
