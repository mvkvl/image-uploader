package ws.slink.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import ws.slink.test.tools.GracefulShutdown;

@SpringBootApplication
public class UploadServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(UploadServiceApplication.class);
	
		public static void main(String[] args) throws Exception {
		logger.info("starting upload service");
        SpringApplication app = new SpringApplication(UploadServiceApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
	}

		
	@Bean
	public GracefulShutdown gracefulShutdown() {
	    return new GracefulShutdown();
	}
	
	@Bean
	public ConfigurableServletWebServerFactory webServerFactory(final GracefulShutdown gracefulShutdown) {
	    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
	    factory.addConnectorCustomizers(gracefulShutdown);
	    return factory;
	}		
}
