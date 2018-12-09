package ws.slink.test.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
//@EnableEurekaClient
public class UploadServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(UploadServiceApplication.class);

	
	public static void main(String[] args) throws Exception {
		logger.info("starting upload service");
        SpringApplication app = new SpringApplication(UploadServiceApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
	}

}
