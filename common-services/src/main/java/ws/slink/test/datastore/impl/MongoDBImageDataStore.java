package ws.slink.test.datastore.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ws.slink.test.datastore.ImageDataReader;
import ws.slink.test.datastore.ImageDataWriter;
import ws.slink.test.datastore.MongoDBImageRepository;
import ws.slink.test.datatype.Base64EncodedImageJson;
import ws.slink.test.datatype.ImageMetaData;
import ws.slink.test.datatype.ProcessingResult;
import ws.slink.test.datatype.RawImage;
import ws.slink.test.error.UnsupportedMethodException;
import ws.slink.test.tools.Base64Processor;
import ws.slink.test.url.URLProvider;

@Service
@ConditionalOnProperty(name="images.datastore.type", havingValue="mongodb", matchIfMissing=false)
@Qualifier("image")
public class MongoDBImageDataStore implements ImageDataWriter, ImageDataReader {

	private static final Logger logger = LoggerFactory.getLogger(MongoDBImageDataStore.class);

	@Value("${images.view.path.image:}")
	private String imageViewURLPath;
	
	@Autowired
	URLProvider urlProvider;

	@Autowired
	private MongoDBImageRepository repository;
	
	@Override
	public ProcessingResult save(String fileName, byte[] data) {
		try {
			String type = URLConnection.guessContentTypeFromName(fileName);
			Base64EncodedImageJson base64Image 
   		          = new Base64EncodedImageJson(fileName, type, new Base64Processor().encode(data));
			repository.save(base64Image);
			return new ProcessingResult(fileName, "ok", "file successfully saved", base64Image.id);
		} catch (Exception e) {
			e.printStackTrace();
			return new ProcessingResult(fileName, "error", e.getMessage(), "");
		}
	}

	@Override
	public ProcessingResult save(BufferedImage image, String key, String fileName) {
		// not used for saving uploaded images 
		throw new UnsupportedMethodException();
	}

	@Override
	public ProcessingResult save(MultipartFile mpFile) {
		try {
			return save(mpFile.getOriginalFilename(), mpFile.getBytes());
		} catch (IOException e) {
			return new ProcessingResult(mpFile.getOriginalFilename(), "error", e.getMessage(), "");
		}
	}

	@Override
	public ProcessingResult save(String urlString) {
		String fileName = "";
		try {
			
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();

			       fileName = FilenameUtils.getName(url.getPath());
			String type     = conn.getContentType();
			
			if (!type.toLowerCase().contains("image")) {
				logger.error("unsupported mime type received: '" + type + "'");
				return new ProcessingResult(urlString, "error", "unsupported mime type '" + type + "'");
			}			
			
			return save(fileName, IOUtils.toByteArray(conn.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
			return new ProcessingResult(fileName, "error", e.getMessage(), "");
		}
	}

	@Override
	public Collection<ImageMetaData> list() {
		Collection<ImageMetaData> result = new ArrayList<>();
		repository.findAll().stream().forEach(b64i -> result.add(
				new ImageMetaData(b64i.id, 
						          b64i.getName(), 
						          urlProvider.get(imageViewURLPath) + "/" + b64i.id,
						          "")
		));
		return result;
	}

	@Override
	public ImageMetaData get(String id) {
		return repository
				 .findById(id)
				 .map(b64i -> 
				        new ImageMetaData(b64i.id, 
						                  b64i.getName(), 
				                          urlProvider.get(imageViewURLPath) + "/" + id, 
				                          ""))
				 .orElse(new ImageMetaData());
	}

	@Override
	public RawImage raw(String id) {
		return repository
				 .findById(id)
		         .map(b64i ->
		         	new RawImage(new ByteArrayInputStream(
		         			       new Base64Processor().decode(b64i.base64)), 
		         			     b64i.type))
		         .orElse(new RawImage(new ByteArrayInputStream(new byte[1]), "image/png"));
	}

	@Override
	public BufferedImage read(String id) throws IOException {
		Base64EncodedImageJson b64i = repository.findById(id).orElse(new Base64EncodedImageJson("", "", ""));
		return ImageIO.read(new ByteArrayInputStream(new Base64Processor().decode(b64i.base64)));
	}

}
