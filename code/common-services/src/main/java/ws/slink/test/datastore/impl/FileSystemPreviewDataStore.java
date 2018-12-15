package ws.slink.test.datastore.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name="images.datastore.type", havingValue="fs", matchIfMissing=false)
@Qualifier("preview")
public class FileSystemPreviewDataStore extends FileSystemDataStore {

	private static final Logger logger = LoggerFactory.getLogger(FileSystemPreviewDataStore.class);

	@Value("${images.datastore.fs.path.preview}")
    private String filePath;
	protected String getFilePath() {
		logger.debug("getFilePath(): " + filePath);
		return filePath;
	}
	
	@Value("${images.view.path.preview:}")
    private String viewPath;
	protected String getViewPath(String fileName) {
		logger.debug("getViewPath(): " + viewPath);
		return urlProvider.get(viewPath) + "/" + fileName;
	}

	@Value("${images.view.path.image:}")
    private String originalViewPath;
	protected String getOriginalViewPath(String fileName) {
		logger.debug("getOriginalViewPath(): " + originalViewPath);
		return urlProvider.get(originalViewPath) + "/" + fileName;
	}

}
