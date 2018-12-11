package ws.slink.test.uploader;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ws.slink.test.datastore.ImageDataStore;
import ws.slink.test.model.ProcessingResult;

@Service
public class MultipartImageUploader {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MultipartImageUploader.class);

	@Autowired
	ImageDataStore imageDataStore;

	public Collection<ProcessingResult> upload(MultipartFile [] files) {
		return Arrays.asList(files).parallelStream().map(imageDataStore::save).collect(Collectors.toList());
	}

}
