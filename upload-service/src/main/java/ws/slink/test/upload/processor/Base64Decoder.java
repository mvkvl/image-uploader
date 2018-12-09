package ws.slink.test.upload.processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Base64Decoder {

	/**
	 * Extract the MIME type from a base64 string
	 * @param encoded Base64 string
	 * @return MIME type string
	 */
	public static String extractMimeType(final String encoded) {
	    final Pattern mime = Pattern.compile("^data:([a-zA-Z0-9]+/[a-zA-Z0-9]+).*,.*");
	    final Matcher matcher = mime.matcher(encoded);
	    if (!matcher.find())
	        return "";
	    return matcher.group(1).toLowerCase();
	}
	
	
	
}
