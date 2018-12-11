package ws.slink.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "rname", "link"})
public class ImageMetaData {

	@JsonProperty
	public int id;
	
	@JsonProperty
	public String name;
	
	@JsonProperty
	public String link;

	public ImageMetaData(int id, String name, String link) {
		this.id = id;
		this.name = name;
		this.link = link;
	}
	
	public ImageMetaData() {}
}
