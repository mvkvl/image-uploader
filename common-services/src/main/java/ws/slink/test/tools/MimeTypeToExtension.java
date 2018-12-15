package ws.slink.test.tools;

public class MimeTypeToExtension {

	public String convert(String type) {
		return convert(type, false);
	}

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
