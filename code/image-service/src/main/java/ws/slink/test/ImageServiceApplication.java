package ws.slink.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;

@SpringBootApplication
public class ImageServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(ImageServiceApplication.class);
	
	public static void main(String[] args) throws Exception {
		logger.info("starting image service");
        SpringApplication app = new SpringApplication(ImageServiceApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
	}
	
	// needed to inject 'HttpServletRequest' in URLProvider
	@Bean 
	public RequestContextListener requestContextListener(){
	    return new RequestContextListener();
	} 
}
