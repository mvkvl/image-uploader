package ws.slink.test.datastore;

import org.springframework.beans.factory.annotation.Value;

public class FileSystemPreviewDataStore {

	@Value("${images.datastore.fs.path.preview}")
    private String previewPath;

}
