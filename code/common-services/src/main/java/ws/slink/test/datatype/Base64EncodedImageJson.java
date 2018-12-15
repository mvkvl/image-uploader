package ws.slink.test.datatype;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import ws.slink.test.tools.Base64Processor;
import ws.slink.test.tools.RandomString;

/**
 * Class for decoding JSON representation of Base64-encoded image
 * 
 */
@Document(collection = "images")
public class Base64EncodedImageJson {

	private static final int RANDOM_NAME_LENGTH = 16;

	@Id
	public String id;

	@JsonProperty("name")
	public String name;
	
	@JsonProperty("type")
	public String type;
	
	@JsonProperty("base64")
	public String base64;
	
	public Base64EncodedImageJson() {}
	
	public Base64EncodedImageJson(String name, String type, String base64) {
        this.name   = (name != null && !name.isEmpty()) ? name : randomName();
        this.type   = type;
        this.base64 = base64;
    }
	
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
		String mt = new Base64Processor().extractMimeType(base64);
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
