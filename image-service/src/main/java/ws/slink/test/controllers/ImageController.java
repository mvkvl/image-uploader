package ws.slink.test.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ws.slink.test.datastore.ImageDataStore;
import ws.slink.test.model.ImageMetaData;

@RestController
public class ImageController {

	@Autowired
	ImageDataStore imageDataStore;
	
	@GetMapping(path = "/image", produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<ImageMetaData> images() {
		return imageDataStore.list();
	}

	
	@GetMapping(path = "/image/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ImageMetaData image(@PathVariable("id") int imageId) {
		return imageDataStore.get(imageId);
	}

	
	@GetMapping(path = "/image/view/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody FileSystemResource viewImage(@PathVariable("id") int imageId) {
		return imageDataStore.raw(imageId);
	}

	
}
