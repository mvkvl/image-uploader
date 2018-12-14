package ws.slink.test.uploader;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ws.slink.test.datastore.ImageDataWriter;
import ws.slink.test.datatype.ProcessingResult;
import ws.slink.test.service.RabbitMQSender;

@Service
public class InternetImageUploader {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(InternetImageUploader.class);

	@Autowired
	@Qualifier("image")
	ImageDataWriter imageDataWriter;

	@Autowired
	RabbitMQSender rabbitMQSender;

	private ProcessingResult process(String url) {
		ProcessingResult result =  imageDataWriter.save(url);
		if (result.ok())
			rabbitMQSender.send(result.key);
		return result;			
	}

	public Collection<ProcessingResult> download(String [] urls) {
		return Arrays.asList(urls).parallelStream().map(this::process).collect(Collectors.toList());
	}
	
}
