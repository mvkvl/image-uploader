package ws.slink.test.datastore.impl;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ws.slink.test.datastore.ImageDataReader;
import ws.slink.test.datastore.ImageDataWriter;
import ws.slink.test.datastore.MongoDBPreviewRepository;
import ws.slink.test.datatype.Base64EncodedPreviewJson;
import ws.slink.test.datatype.ImageMetaData;
import ws.slink.test.datatype.ProcessingResult;
import ws.slink.test.datatype.RawImage;
import ws.slink.test.tools.Base64Processor;
import ws.slink.test.url.URLProvider;

@Service
@ConditionalOnProperty(name="images.datastore.type", havingValue="mongodb", matchIfMissing=false)
@Qualifier("preview")
public class MongoDBPreviewDataStore implements ImageDataWriter, ImageDataReader {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MongoDBPreviewDataStore.class);
	
	@Autowired
	URLProvider urlProvider;
	
	@Autowired
	private MongoDBPreviewRepository repository;

	@Autowired
	@Qualifier("image")
	MongoDBImageDataStore imageDS;

	@Override
	public Collection<ImageMetaData> list() {
		Collection<ImageMetaData> result = new ArrayList<>();
		repository.findAll().stream().forEach(b64i -> result.add(
				new ImageMetaData(b64i.id, 
						          b64i.getName(), 
						          urlProvider.get("image/view") + "/" + b64i.id,
						          imageDS.get(b64i.key).link)
		));
		return result;
	}
	
	@Override
	public ImageMetaData get(String id) {
		Base64EncodedPreviewJson base64Image = repository.findById(id).orElse(new Base64EncodedPreviewJson("", "", ""));
		return new ImageMetaData(base64Image.id, 
				                 base64Image.getName(), 
				                 urlProvider.get("image/view") + "/" + id,
				                 imageDS.get(base64Image.key).link);
	}
	
	@Override
	public RawImage raw(String id) {
		Base64EncodedPreviewJson b64i = repository.findById(id).orElse(new Base64EncodedPreviewJson("", "", ""));
		return new RawImage(new ByteArrayInputStream(new Base64Processor().decode(b64i.base64)), b64i.type);
	}

	@Override
	public ProcessingResult save(String key, byte[] data) {
		try {
			String type = URLConnection.guessContentTypeFromName(key);
			Base64EncodedPreviewJson base64Image 
   		          = new Base64EncodedPreviewJson(key, 
   		        		                         type, 
   		        		                         new Base64Processor().encode(data));
			repository.save(base64Image);
			return new ProcessingResult(key, "ok", "file successfully saved", base64Image.id);
		} catch (Exception e) {
			e.printStackTrace();
			return new ProcessingResult(key, "error", e.getMessage(), "");
		}
	}

	@Override
	public ProcessingResult save(BufferedImage image, String key) {
		return save(key, ((DataBufferByte) image.getData().getDataBuffer()).getData());
	}

	@Override
	public ProcessingResult save(MultipartFile mpFile) {
		return null;
//		try {
//			return save(mpFile.getOriginalFilename(), mpFile.getBytes());
//		} catch (IOException e) {
//			return new ProcessingResult(mpFile.getOriginalFilename(), "error", e.getMessage(), "");
//		}		
	}

	@Override
	public ProcessingResult save(String urlString) {
		return null;
//		String fileName = "";
//		try {
//			
//			URL url = new URL(urlString);
//			URLConnection conn = url.openConnection();
//
//			       fileName = FilenameUtils.getName(url.getPath());
//			String type     = conn.getContentType();
//			
//			if (!type.toLowerCase().contains("image")) {
//				logger.error("unsupported mime type received: '" + type + "'");
//				return new ProcessingResult(urlString, "error", "unsupported mime type '" + type + "'");
//			}			
//			
//			return save(fileName, IOUtils.toByteArray(conn.getInputStream()));
//		} catch (Exception e) {
//			return new ProcessingResult(fileName, "error", e.getMessage(), "");
//		}
	}

	@Override
	public BufferedImage read(String id) throws IOException {
		Base64EncodedPreviewJson b64i = repository.findById(id).orElse(new Base64EncodedPreviewJson("", "", ""));
		return ImageIO.read(new ByteArrayInputStream(new Base64Processor().decode(b64i.base64)));
	}

}