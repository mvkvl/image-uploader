package ws.slink.test.convert.datastore;

import java.awt.image.BufferedImage;

public interface PreviewDataStore {
	
	public boolean save(BufferedImage image, String fileName);
	
}
