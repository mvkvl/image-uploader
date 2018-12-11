package ws.slink.test.tools;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Base64Decoder {

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
	
}
