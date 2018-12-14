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
	
	protected String getURLPath() {
		return "preview";
	}

}
