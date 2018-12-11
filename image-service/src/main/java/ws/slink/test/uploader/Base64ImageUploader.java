package ws.slink.test.uploader;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ws.slink.test.datastore.ImageDataStore;
import ws.slink.test.model.Base64EncodedImageJson;
import ws.slink.test.model.ProcessingResult;
import ws.slink.test.tools.Base64Decoder;
import ws.slink.test.tools.MimeTypeToExtension;

@Service
public class Base64ImageUploader {

	private static final Logger logger = LoggerFactory.getLogger(Base64ImageUploader.class);

	@Autowired
	ImageDataStore imageDataStore;

	private ProcessingResult save(Base64EncodedImageJson encodedImage) {

		// as an extension of this service, we can detect 
		// file type by binary data itself
		// for now if mime type is not detected, skip this file
		String type = encodedImage.getType();
		if (null == type || type.isEmpty()) {
			logger.error("mime type is not detected for image '" + encodedImage.getName() +"'");
			return new ProcessingResult(encodedImage.getName(), "error", "mime type can't be detected");
		}

		String [] parts = type.split("/");
		// some problem with received mime type string 
		if (2 != parts.length) {
			logger.error("mime type is corrupted for image '" + encodedImage.getName() +"': '" + type + "'");
			return new ProcessingResult(encodedImage.getName(), "error", "mime type '" + type + "'is corrupted");
		}
		
		// some problem with received mime type string 
		if (!"image".equalsIgnoreCase(parts[0])) {
			logger.error("mime type is not an image type for '" + encodedImage.getName() +"': '" + type + "'");
			return new ProcessingResult(encodedImage.getName(), "error", "mime type '" + type + "'is not an image");
		}
		
		String name      = encodedImage.getName();
		String extension = FilenameUtils.getExtension(name);
		if (null == extension || extension.isEmpty()) {
			extension = new MimeTypeToExtension().convert(type);
			if (!name.toLowerCase().endsWith(extension.toLowerCase()))
				name += "." + extension;
		}
		
		logger.debug("saving file '" + name);

		return imageDataStore.save(name, new Base64Decoder().decode(encodedImage.base64));
	}
	
	public Collection<ProcessingResult> upload(Base64EncodedImageJson [] imagesEncoded) {
		return Arrays.asList(imagesEncoded).parallelStream().map(this::save).collect(Collectors.toList());
	}

}