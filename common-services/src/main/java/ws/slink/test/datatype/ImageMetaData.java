package ws.slink.test.datatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "link", "original-link"})
public class ImageMetaData {

	@JsonProperty
	public String id;
	
	@JsonProperty
	public String name;
	
	@JsonProperty
	public String link;

	@JsonProperty("orginal-link")
	public String originalLink;

	public ImageMetaData(String id, String name, String link, String originalLink) {
		this.id           = id;
		this.name         = name;
		this.link         = link;
		this.originalLink = originalLink;
	}
	
	public ImageMetaData() {}
}
