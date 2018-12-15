package ws.slink.test.tools;

public class MimeTypeToExtension {

	/**
	 * 
	 * Get file extension by mime type
	 * 
	 * @param type
	 * @return extension prepended with dot
	 */
	public String convert(String type) {
		return convert(type, false);
	}

	/**
	 * 
	 * Get file extension by mime type
	 * 
	 * @param type
	 * @param stripDot
	 * @return extension (optionally prepended with dot)
	 */
	public String convert(String type, boolean stripDot) {
		String [] parts = type.split("/");
		String ext = "";
		if (parts.length < 2)
			return "";
		else if ("jpeg".equalsIgnoreCase(parts[1]))
			ext = "jpg";
		else
			ext = parts[1];
		return (stripDot) ? ext : "." + ext;
	}

}
