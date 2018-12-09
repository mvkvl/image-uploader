package ws.slink.test.upload;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ws.slink.test.model.Base64EncodedImageJson;

//@RefreshScope
@RestController
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@CrossOrigin(origins = "*")
	@GetMapping("/")
	public String hello() {
		return "hello!";
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping(path = "/test", consumes = "application/json")
	public String testJSON(@RequestBody Base64EncodedImageJson [] imagesEncoded) {
		logger.info("processing application/json request");
		Arrays.asList(imagesEncoded).forEach(System.out::println);
		return Arrays.asList(imagesEncoded).toString();
	}

	@CrossOrigin(origins = "*")
	@PostMapping(path = "/test", consumes = "multipart/form-data", produces = "application/json")
	public String testMultipart(@RequestParam("file") MultipartFile [] files) {
		logger.info("processing multipart/form-data request");
		
		for(MultipartFile file: files) {
			System.out.println(file.getOriginalFilename() + ", " 
		                     + file.getName() + ", "
		                     + file.getContentType() + ", "
		                     + file.getSize());
		}
		return "files accepted: " + 
               Arrays.asList(files)
		             .stream()
		             .map(f -> f.getOriginalFilename() + " [" +
		                       f.getContentType()      + "]")
		             .collect(Collectors.toList());
	}

	@CrossOrigin(origins = "*")
	@PostMapping(path = "/test", consumes = "application/x-www-form-urlencoded")
	public String testFormUrlEncoded(@RequestParam(value = "url") String [] urls) {
		logger.info("processing application/x-www-form-urlencoded request");
		Arrays.asList(urls).forEach(System.out::println);
		return "urls received: " + Arrays.asList(urls);
	}

}
