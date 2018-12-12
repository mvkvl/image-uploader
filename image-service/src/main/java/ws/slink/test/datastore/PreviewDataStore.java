package ws.slink.test.datastore;

import java.util.Collection;

import org.springframework.core.io.FileSystemResource;

import ws.slink.test.model.ImageMetaData;

public interface PreviewDataStore {
	
	public Collection<ImageMetaData> list();
	public ImageMetaData get(int id);
	public FileSystemResource raw(int id);
	
}
