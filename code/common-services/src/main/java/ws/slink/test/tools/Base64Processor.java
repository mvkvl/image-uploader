package ws.slink.test.tools;

import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.multipart.MultipartFile;


/**
 * 
 * Base64 processing utilities
 * 
 */
public class Base64Processor {

	/**
	 * Extract the MIME type from a base64 string
	 * @param encoded Base64 string
	 * @return MIME type string
	 */
	public String extractMimeType(final String encoded) {
	    final Pattern mime = Pattern.compile("^data:([a-zA-Z0-9]+/[a-zA-Z0-9]+).*,.*");
	    final Matcher matcher = mime.matcher(encoded);
	    if (!matcher.find())
	        return "";
	    return matcher.group(1).toLowerCase();
	}
	
	/**
	 * Cleanup MIME type from a base64 string
	 * @param encoded Base64 string
	 * @return Base64 string without optional prefix 
	 *         (i.e. "data:image/png;base64,")
	 */
	public String clean(final String encoded) {
	    final Pattern patter = Pattern.compile("^data:([a-zA-Z0-9]+/[a-zA-Z0-9]+).*,");
	    return patter.matcher(encoded).replaceAll("");
	}
	
	/**
	 * Decode Base64 encoded image into byte [], 
	 * cleaning input from optional prefix
	 * @param encoded Base64 string
	 * @return byte[] array of image bytes 
	 */
	public byte[] decode(final String encoded) {
		return Base64.getDecoder().decode(clean(encoded));
	}
	
	/**
	 * Encode byte array image into Base64 string, 
	 * @param byte[] array of image bytes
	 * @return encoded Base64 string 
	 */
	public String encode(final byte [] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}

	/**
	 * Encode MultipartFile into Base64 string, 
	 * @param MultipartFile
	 * @return encoded Base64 string 
	 */
	public String encode(final MultipartFile file) {
		try {
			return Base64.getEncoder().encodeToString(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			return "<no image>";
		}
	}

	
}
