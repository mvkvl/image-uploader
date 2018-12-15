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
@Document(collection = "previews")
public class Base64EncodedPreviewJson {

	private static final int RANDOM_NAME_LENGTH = 16;

	@Id
	public String id;

	@JsonProperty("key")
	public String key;
	
	@JsonProperty("type")
	public String type;

	@JsonProperty("base64")
	public String base64;
	
	public Base64EncodedPreviewJson() {}
	
	public Base64EncodedPreviewJson(String key, String type, String base64) {
        this.key      = (key != null && !key.isEmpty()) ? key : randomName();
        this.type     = type;
        this.base64   = base64;
    }
	
	/**
	 * generates random name for an image file if it's not passed as a 'name' json property
	 */
	private String randomName() {
		key = RandomString.get(RANDOM_NAME_LENGTH);
		return key;
	}
	
	/**
	 * returns 'name' property of json input if it exists
	 * if 'name' is null set it to random string and return 
	 */
	public String getName() {
		return (null != key && !key.isEmpty()) ? key : randomName(); 
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
}
