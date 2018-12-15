package ws.slink.test.datastore.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;

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
import ws.slink.test.datastore.MongoDBPreviewRepository;
import ws.slink.test.datatype.Base64EncodedPreviewJson;
import ws.slink.test.datatype.ImageMetaData;
import ws.slink.test.datatype.ProcessingResult;
import ws.slink.test.datatype.RawImage;
import ws.slink.test.error.UnsupportedMethodException;
import ws.slink.test.tools.Base64Processor;
import ws.slink.test.tools.MimeTypeToExtension;
import ws.slink.test.url.URLProvider;

@Service
@ConditionalOnProperty(name="images.datastore.type", havingValue="mongodb", matchIfMissing=false)
@Qualifier("preview")
public class MongoDBPreviewDataStore implements ImageDataWriter, ImageDataReader {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MongoDBPreviewDataStore.class);
	
	@Value("${images.view.path.preview:}")
	private String imageViewURLPath;

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
						          urlProvider.get(imageViewURLPath) + "/" + b64i.id,
						          imageDS.get(b64i.key).link)
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
				                          imageDS.get(b64i.key).link))
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
		Base64EncodedPreviewJson b64i = repository.findById(id).orElse(new Base64EncodedPreviewJson("", "", ""));
		return ImageIO.read(new ByteArrayInputStream(new Base64Processor().decode(b64i.base64)));
	}

	@Override
	public ProcessingResult save(BufferedImage image, String key, String mimeType) {
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write(image, new MimeTypeToExtension().convert(mimeType, true), output);
			Base64EncodedPreviewJson b64i 
   		          = new Base64EncodedPreviewJson(
   		        		  key, 
   		        		  mimeType, 
   		        		  new Base64Processor().encode(
   		        				  output.toByteArray()
   		        		  )
   		        	);
			repository.save(b64i);
			return new ProcessingResult(key, "ok", "file successfully saved", b64i.id);
		} catch (Exception e) {
			e.printStackTrace();
			return new ProcessingResult(key, "error", e.getMessage(), "");
		}
	}

	@Override
	public ProcessingResult save(String fileName, byte[] data) {
		// not used for saving previews 
		throw new UnsupportedMethodException();
	}

	@Override
	public ProcessingResult save(MultipartFile mpFile) {
		// not used for saving previews 
		throw new UnsupportedMethodException();
	}

	@Override
	public ProcessingResult save(String urlString) {
		// not used for saving previews 
		throw new UnsupportedMethodException();
	}

}
