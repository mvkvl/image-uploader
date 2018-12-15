package ws.slink.test.datastore.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name="images.datastore.type", havingValue="fs", matchIfMissing=false)
@Qualifier("image")
public class FileSystemImageDataStore extends FileSystemDataStore {

	private static final Logger logger = LoggerFactory.getLogger(FileSystemImageDataStore.class);

	@Value("${images.datastore.fs.path.upload}")
    private String filePath;
	protected String getFilePath() {
		logger.debug("getFilePath(): " + filePath);
		return filePath;
	}

	@Value("${images.view.path.image:}")
    private String viewPath;
	protected String getViewPath(String fileName) {
		logger.debug("getViewPath(): " + viewPath);
		return urlProvider.get(viewPath) + "/" + fileName;
	}

	protected String getOriginalViewPath(String fileName) {
		logger.debug("getOriginalViewPath(): \"\"");
		return "";
	}

}
