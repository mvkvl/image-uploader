package ws.slink.test.datastore;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import ws.slink.test.model.ImageMetaData;
import ws.slink.test.tools.FileTools;
import ws.slink.test.tools.URLProvider;

@Service
public class FileSystemPreviewDataStore implements PreviewDataStore {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(FileSystemImageDataStore.class);

	@Value("${images.datastore.fs.path.preview}")
    private String previewPath;

	@Autowired
	FileTools fileTools;

	@Autowired
	URLProvider urlProvider;

	@Override
	public Collection<ImageMetaData> list() {
		List<ImageMetaData> result = new ArrayList<>();
		
		File dir = new File(previewPath);
		FileFilter fileFilter = new WildcardFileFilter("*_*.*");
		File[] files = dir.listFiles(fileFilter);

		for (File file: files)
			result.add(new FileTools().meta(file, urlProvider.get("preview/view")));
		
		return result;
	}

	@Override
	public ImageMetaData get(int id) {
		File file = fileTools.getFileById(id, previewPath);
		if (null != file)
			return fileTools.meta(file, urlProvider.get("preview/view"));
		else
			return new ImageMetaData();
	}
	
	@Override
	public FileSystemResource raw(int id) {
		String fname = fileTools.getFileNameById(id, previewPath);
		if (null != fname && !fname.isEmpty())
			return new FileSystemResource(fileTools.getFileById(id, previewPath));
		else 
			return null;
	}

	
}
