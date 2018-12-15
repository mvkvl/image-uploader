package ws.slink.test.datatype;

import java.io.InputStream;

public class RawImage {

	public InputStream   input;
	public String  contentType;
	
	public RawImage(InputStream input, String contentType) {
		this.input = input;
		this.contentType = contentType;
	}
	
}
