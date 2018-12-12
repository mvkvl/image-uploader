package ws.slink.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Result of one item processing (uploading for files or downloading for URLs)
 * Collection of ProcessingResult is returned as JSON array from upload service calls
 *  
 * @author kami
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
	public String fileName;
	
	public ProcessingResult() {}
	
	public ProcessingResult(String input, String result, String message, String fileName) {
		this.input    = input;
		this.result   = result;
		this.message  = message;
		this.fileName = fileName; 
	}
	
	public ProcessingResult(String input, String result, String fileName) {
		this.input    = input;
		this.result   = result;
		this.message  = "";
		this.fileName = fileName;
	}
	
	public boolean ok() {
		return result.equals("ok");
	}
}
