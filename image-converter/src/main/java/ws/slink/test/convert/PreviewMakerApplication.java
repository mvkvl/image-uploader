package ws.slink.test.convert;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import ws.slink.test.convert.datastore.ImageDataStore;
import ws.slink.test.convert.datastore.PreviewDataStore;
import ws.slink.test.convert.processor.ImageConverter;

@EnableBinding(Sink.class)
@SpringBootApplication
public class PreviewMakerApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(PreviewMakerApplication.class);
	
	@Autowired
	ImageConverter imageConverter;

	@Autowired
	ImageDataStore imageDataStore;

	@Autowired
	PreviewDataStore previewDataStore;

	public static void main(String[] args) throws Exception {
		logger.info("starting converter service");
        SpringApplication app = new SpringApplication(PreviewMakerApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
	}
	
	@StreamListener(target = Sink.INPUT)
    public void processImageFile(String fileName) throws IOException {
		logger.info("processing " + fileName);
		previewDataStore.save(
				imageConverter.resize(
						ImageIO.read(imageDataStore.get(fileName)))
				      , fileName);
    }

}
