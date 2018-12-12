package ws.slink.test.convert.datastore;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileSystemImageDataStore implements ImageDataStore {

	private static final Logger logger = LoggerFactory.getLogger(FileSystemImageDataStore.class);
	
	@Value("${images.datastore.fs.path.upload}")
    private String uploadPath;
	
	public File get(String fileName) {
		logger.debug("get file '" + fileName +"'");
		return new File(uploadPath + File.separator + fileName);
	}

	
}
