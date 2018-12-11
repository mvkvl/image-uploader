package ws.slink.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import ws.slink.test.tools.Base64Decoder;
import ws.slink.test.tools.RandomString;

/**
 * Class for decoding JSON representation of Base64-encoded image
 * 
 */
public class Base64EncodedImageJson {

	private static final int RANDOM_NAME_LENGTH = 16;
	
	@JsonProperty("name")
	public String name;
	
	@JsonProperty("type")
	public String type;
	
	@JsonProperty("base64")
	public String base64;
	
	/**
	 * generates random name for an image file if it's not passed as a 'name' json property
	 */
	private String randomName() {
		name = RandomString.get(RANDOM_NAME_LENGTH);
		return name;
	}
	
	/**
	 * returns 'name' property of json input if it exists
	 * if 'name' is null set it to random string and return 
	 */
	public String getName() {
		return (null != name && !name.isEmpty()) ? name : randomName(); 
	}
	
	/**
	 * returns MIME type of input Base64-encoded image,
	 * if it's represented as a string of type: "data:image/png;base64,iVBORw0K...."
	 * 
	 * if Base64 string does not contain "data:image/png;base64," prefix,
	 * returns 'type' property of json
	 */
	public String getType() {
		String mt = new Base64Decoder().extractMimeType(base64);
		return (null != mt && !mt.isEmpty()) ? mt : type;
	}

	/**
	 * used for toString to truncate base64 string
	 */
	public String base64sub() {
		return (base64.length() > 63) ? 
				base64.substring(0, 30) + "..." + base64.substring(base64.length() - 30) :
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
