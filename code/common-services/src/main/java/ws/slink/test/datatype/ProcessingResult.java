package ws.slink.test.datatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Result of one item processing (uploading for files or downloading for URLs)
 * Collection of ProcessingResult is returned as JSON array from upload service calls
 *  
 */
@JsonPropertyOrder({ "input", "result", "message"})
public class ProcessingResult {
	
	@JsonProperty
	public String input;
	@JsonProperty
	public String result;
	@JsonProperty
	public String message;
	@JsonProperty
	public String key;
	
	public ProcessingResult() {}
	
	public ProcessingResult(String input, String result, String message, String returnedKey) {
		this.input    = input;
		this.result   = result;
		this.message  = message;
		this.key      = returnedKey; 
	}
	
	public ProcessingResult(String input, String result, String returnedKey) {
		this.input    = input;
		this.result   = result;
		this.message  = "";
		this.key      = returnedKey;
	}
	
	public boolean ok() {
		return result.equalsIgnoreCase("ok");
	}
}
