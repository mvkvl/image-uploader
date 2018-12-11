package ws.slink.test.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PreviewController {

//	@Autowired
//	PreviewDataStore previewDataStore;

	@CrossOrigin(origins = "*")
	@GetMapping(path = "/preview", produces = MediaType.APPLICATION_JSON_VALUE)
	public String previews() {
		return "JSON encoded preview list";
	}

	@CrossOrigin(origins = "*")
	@GetMapping(path = "/preview/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String preview(@PathVariable("id") String imageId) {
		return "JSON encoded preview (id: " + imageId + ")";
	}

	
	@CrossOrigin(origins = "*")
	@GetMapping(path = "/preview/view/{id}")
	public String viewPreview(@PathVariable("id") String imageId) {
		return "preview " + imageId;
	}

}
