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
	
	public ProcessingResult() {}
	
	public ProcessingResult(String input, String result, String message) {
		this.input   = input;
		this.result  = result;
		this.message = message;
	}
	
	public ProcessingResult(String input, String result) {
		this.input   = input;
		this.result  = result;
		this.message = "";
	}
	
}
