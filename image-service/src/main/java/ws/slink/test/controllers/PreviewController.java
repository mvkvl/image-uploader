package ws.slink.test.controllers;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ws.slink.test.datastore.ImageDataReader;
import ws.slink.test.datatype.ImageMetaData;
import ws.slink.test.datatype.RawImage;

@RestController
public class PreviewController {

	@Autowired
	@Qualifier("preview")
	ImageDataReader previewDataReader;

	@CrossOrigin(origins = "*")
	@GetMapping(path = "/preview", produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<ImageMetaData> previews() {
		return previewDataReader.list();
	}

	@CrossOrigin(origins = "*")
	@GetMapping(path = "/preview/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ImageMetaData preview(@PathVariable("id") String id) {
		return previewDataReader.get(id);
	}

	
	@CrossOrigin(origins = "*")
	@GetMapping(path = "/preview/view/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody ResponseEntity<byte[]> viewPreview(@PathVariable("id") String id) throws IOException {
		RawImage image = previewDataReader.raw(id);
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.valueOf(image.contentType));
		return new ResponseEntity<byte[]>(IOUtils.toByteArray(image.input), headers, HttpStatus.OK);
	}

}
