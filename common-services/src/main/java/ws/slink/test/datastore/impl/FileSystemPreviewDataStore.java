package ws.slink.test.datastore.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name="images.datastore.type", havingValue="fs", matchIfMissing=false)
@Qualifier("preview")
public class FileSystemPreviewDataStore extends FileSystemDataStore {

	@Value("${images.datastore.fs.path.preview}")
    private String filePath;
	protected String getFilePath() {
		return filePath;
	}
	
	@Value("${images.view.path.preview:}")
    private String viewPath;
	protected String getViewPath(String fileName) {
		return urlProvider.get(viewPath) + "/" + fileName;
	}

	@Value("${images.view.path.image:}")
    private String originalViewPath;
	protected String getOriginalViewPath(String fileName) {
		return urlProvider.get(originalViewPath) + "/" + fileName;
	}

}
