package ws.slink.test.datastore;

import java.awt.image.BufferedImage;

import org.springframework.web.multipart.MultipartFile;

import ws.slink.test.datatype.ProcessingResult;

public interface ImageDataWriter {

	public ProcessingResult save(BufferedImage image, String key, String fileName);
	public ProcessingResult save(MultipartFile mpFile);
	public ProcessingResult save(String urlString);
	public ProcessingResult save(String fileName, byte[] data);
	
}
