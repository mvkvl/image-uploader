package ws.slink.test.controllers;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import ws.slink.test.datatype.Base64EncodedImageJson;
import ws.slink.test.datatype.ProcessingResult;
import ws.slink.test.uploader.Base64ImageUploader;
import ws.slink.test.uploader.InternetImageUploader;
import ws.slink.test.uploader.MultipartImageUploader;

/**
 *   POST request is being handled depending on "content-type" header:
 *     1) application/json                  - process a JSON array of Base64-encoded images 
 *     2) multipart/form-data               - process a list of files
 *     3) application/x-www-form-urlencoded - process a list of URLs
 *   
 *   in fact 2nd method could handle both - files list and URLs list,
 *   but for clarity URLs are handled separately
 * 
 */
@RestController
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	@Autowired
	MultipartImageUploader multipartUploader;
	
	@Autowired
	Base64ImageUploader    base64Uploader;
	
	@Autowired
	InternetImageUploader  urlUploader;

	/**
	 * 
	 * @param imagesEncoded - a JSON array of Base64-encoded images to be uploaded
	 * @return ProcessingResult for each JSON-object
	 * 
	 */
	@CrossOrigin(origins = "*")
	@PostMapping(path = "/upload", consumes = "application/json", produces = "application/json")
	public DeferredResult<Collection<ProcessingResult>> processJSON(@RequestBody Base64EncodedImageJson [] imagesEncoded) {
		logger.debug("processing application/json request");
		DeferredResult<Collection<ProcessingResult>> deferredResult = new DeferredResult<>();
		CompletableFuture.supplyAsync( () -> base64Uploader.upload(imagesEncoded))
		                 .whenComplete( (result, error) -> {
		                	 deferredResult.setResult(result);
		                	 if (null != error) {
		                		 logger.error("ERROR: " + error.getMessage());
		                	 }
		                 });
		return deferredResult;
//		return base64Uploader.upload(imagesEncoded);
	}

	/**
	 * 
	 * @param files - a list of files to be uploaded
	 * @return ProcessingResult for each file
	 * 
	 */
	@CrossOrigin(origins = "*")
	@PostMapping(path = "/upload", consumes = "multipart/form-data", produces = "application/json")
	public DeferredResult<Collection<ProcessingResult>> processMultipart(@RequestParam("file") MultipartFile [] files) {
		logger.debug("processing multipart/form-data request");
		DeferredResult<Collection<ProcessingResult>> deferredResult = new DeferredResult<>();
		CompletableFuture.supplyAsync( () -> multipartUploader.upload(files))
		                 .whenComplete( (result, error) -> {
		                	 deferredResult.setResult(result);
		                	 if (null != error) {
		                		 logger.error("ERROR: " + error.getMessage());
		                	 }
		                 });
		return deferredResult;
//		return multipartUploader.upload(files);
	}

	/**
	 * 
	 * @param urls - a list of image URLs to be downloaded
	 * @return ProcessingResult for each URL
	 * 
	 */
	@CrossOrigin(origins = "*")
	@PostMapping(path = "/upload", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	public DeferredResult<Collection<ProcessingResult>> processUrls(@RequestParam("url") String [] urls) {
		logger.debug("processing application/x-www-form-urlencoded request");
		DeferredResult<Collection<ProcessingResult>> deferredResult = new DeferredResult<>();
		CompletableFuture.supplyAsync( () -> urlUploader.download(urls))
		                 .whenComplete( (result, error) -> {
		                	 deferredResult.setResult(result);
		                	 if (null != error) {
		                		 logger.error("ERROR: " + error.getMessage());
		                	 }
		                 });
		return deferredResult;
//		return urlUploader.download(urls);
	}


}
