package ws.slink.test.datastore;

import java.util.Collection;

import ws.slink.test.model.ImageMetaData;

public interface PreviewDataStore {
	
	public Collection<ImageMetaData> previews();
	public ImageMetaData preview(int id);
	
}
