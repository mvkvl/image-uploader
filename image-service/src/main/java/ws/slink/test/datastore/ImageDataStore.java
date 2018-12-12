package ws.slink.test.datastore;

import java.util.Collection;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import ws.slink.test.model.ImageMetaData;
import ws.slink.test.model.ProcessingResult;

public interface ImageDataStore {

	public ProcessingResult save(MultipartFile mpFile);
	public ProcessingResult save(String urlString);
	public ProcessingResult save(String fileName, byte[] data);
	
	public Collection<ImageMetaData> list();
	public ImageMetaData get(int id);
	public FileSystemResource raw(int id);
		
}
