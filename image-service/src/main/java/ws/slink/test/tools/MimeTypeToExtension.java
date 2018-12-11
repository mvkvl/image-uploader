package ws.slink.test.tools;

public class MimeTypeToExtension {

	public String convert(String type) {
		String [] parts = type.split("/");
		if (parts.length < 2)
			return "";
		else if ("jpeg".equalsIgnoreCase(parts[1]))
			return ".jpg";
		else
			return parts[1];
	}
	
}
