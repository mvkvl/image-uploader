package ws.slink.test.convert.datastore;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileSystemPreviewDataStore implements PreviewDataStore {

	private static final Logger logger = LoggerFactory.getLogger(FileSystemImageDataStore.class);
	
	@Value("${images.datastore.fs.path.preview}")
    private String previewPath;

	@Override
	public boolean save(BufferedImage image, String fileName) {
		logger.debug("saving image to " + previewPath + File.separator + fileName);
		try {
			ImageIO.write(image, 
					      FilenameUtils.getExtension(fileName), 
					      new File(previewPath + File.separator + fileName));
			return true;
		} catch (IOException ex) {
			logger.error("Error saving preview: " + ex.getMessage());
		}
		return false;
	}
	
}
