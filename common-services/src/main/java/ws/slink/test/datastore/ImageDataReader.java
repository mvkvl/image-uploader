package ws.slink.test.datastore;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;

import ws.slink.test.datatype.ImageMetaData;
import ws.slink.test.datatype.RawImage;

public interface ImageDataReader {

	public Collection<ImageMetaData> list();
	public ImageMetaData get(String id);
	public RawImage raw(String id) throws IOException;
	public BufferedImage read(String id) throws IOException; 

}
