package ws.slink.test.datastore;

import java.util.Collection;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import ws.slink.test.model.ImageMetaData;
import ws.slink.test.model.ProcessingResult;

public class MongoDBImageDataStore implements ImageDataStore {

	@Override
	public ProcessingResult save(MultipartFile mpFile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessingResult save(String urlString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessingResult save(String fileName, byte[] data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ImageMetaData> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageMetaData get(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileSystemResource raw(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
