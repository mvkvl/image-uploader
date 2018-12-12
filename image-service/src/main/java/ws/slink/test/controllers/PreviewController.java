package ws.slink.test.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ws.slink.test.datastore.PreviewDataStore;
import ws.slink.test.model.ImageMetaData;

@RestController
public class PreviewController {

	@Autowired
	PreviewDataStore previewDataStore;

	@CrossOrigin(origins = "*")
	@GetMapping(path = "/preview", produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<ImageMetaData> previews() {
		return previewDataStore.list();
	}

	@CrossOrigin(origins = "*")
	@GetMapping(path = "/preview/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ImageMetaData preview(@PathVariable("id") int id) {
		return previewDataStore.get(id);
	}

	
	@CrossOrigin(origins = "*")
	@GetMapping(path = "/preview/view/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public FileSystemResource viewPreview(@PathVariable("id") int id) {
		return previewDataStore.raw(id);
	}

}
