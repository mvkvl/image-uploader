package ws.slink.test.uploader;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ws.slink.test.datastore.ImageDataWriter;
import ws.slink.test.datatype.ProcessingResult;
import ws.slink.test.service.RabbitMQSender;

@Service
public class MultipartImageUploader {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MultipartImageUploader.class);

	@Autowired
	@Qualifier("image")
	ImageDataWriter imageDataWriter;

	@Autowired
	RabbitMQSender rabbitMQSender;
	
	private ProcessingResult process(MultipartFile mpFile) {
		ProcessingResult result = imageDataWriter.save(mpFile);
		if (result.ok())
			rabbitMQSender.send(result.key);
		return result;
	}
	
	public Collection<ProcessingResult> upload(MultipartFile [] files) {
		return Arrays.asList(files).parallelStream().map(this::process).collect(Collectors.toList());
	}

}
