package ws.slink.test;

import java.io.IOException;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import ws.slink.test.convert.processor.ImageConverter;
import ws.slink.test.datastore.ImageDataReader;
import ws.slink.test.datastore.ImageDataWriter;
import ws.slink.test.datatype.ProcessingResult;

@EnableBinding(Sink.class)
@SpringBootApplication
public class PreviewMakerApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(PreviewMakerApplication.class);

	@Autowired
	ImageConverter imageConverter;

	@Autowired
	@Qualifier("image")
	ImageDataReader imageReader;

	@Autowired
	@Qualifier("preview")
	ImageDataWriter previewWriter;
	
	public static void main(String[] args) throws Exception {
		logger.info("starting converter service");
        SpringApplication app = new SpringApplication(PreviewMakerApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
	}
	
	@StreamListener(target = Sink.INPUT)
    public void processImageFile(ProcessingResult imageSaveResult) throws IOException {
		logger.info("processing " + imageSaveResult);
		previewWriter.save(
			    imageConverter.resize(imageReader.read(imageSaveResult.key))
			   ,imageSaveResult.key, URLConnection.guessContentTypeFromName(imageSaveResult.input));
    }

}
