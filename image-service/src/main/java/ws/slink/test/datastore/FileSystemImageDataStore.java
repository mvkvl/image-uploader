package ws.slink.test.datastore;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ws.slink.test.model.ImageMetaData;
import ws.slink.test.model.ProcessingResult;
import ws.slink.test.service.id.ImageIdService;

@Service
public class FileSystemImageDataStore implements ImageDataStore {

	private static final Logger logger = LoggerFactory.getLogger(FileSystemImageDataStore.class);
	
	@Autowired
	ImageIdService imageIdService;

	@Value("${images.datastore.fs.path.upload}")
    private String uploadPath;
	
	@Override
	public ProcessingResult save(MultipartFile mpFile) {
		String name = imageIdService.nextStrId() + "_" + mpFile.getOriginalFilename(); 
		File convFile = new File(uploadPath + File.separator + name);
		logger.debug("saving file '" + name + "' to: " + uploadPath + File.separator + name);
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
			fos.write(mpFile.getBytes());
			fos.close();
			return new ProcessingResult(mpFile.getOriginalFilename(), "ok", "file successfully uploaded");
        } catch (IOException e) {
        	logger.error("error saving file '" + name + "': " + e.getMessage());
			return new ProcessingResult(mpFile.getOriginalFilename(), "error", e.getMessage());
//			e.printStackTrace();
        }
	}

	@Override
	public ProcessingResult save(String fileName, byte[] data) {
		String name = imageIdService.nextStrId() + "_" + fileName;
		try (OutputStream stream = new FileOutputStream(uploadPath + File.separator + name)) {
		    stream.write(data);
		    return new ProcessingResult(fileName, "ok", "file successfully uploaded");
		} catch (FileNotFoundException e) {
        	logger.error("error saving file '" + name + "' [FileNotFound]: " + e.getMessage());
			return new ProcessingResult(fileName, "error", "FileNotFound error saving file");
//			e.printStackTrace();
		} catch (IOException e) {
        	logger.error("error saving file '" + name + "' [IOException]: " + e.getMessage());
			return new ProcessingResult(fileName, "error", "IOException error saving file");
//			e.printStackTrace();
		}
	}
	
	@Override
	public ProcessingResult save(String urlString) {
		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			
			String outputFileName = imageIdService.nextStrId() + "_" + FilenameUtils.getName(url.getPath());
			
			if (!conn.getContentType().toLowerCase().contains("image")) {
				logger.error("unsupported mime type received: '" + conn.getContentType() + "'");
				return new ProcessingResult(urlString, "error", "unsupported mime type '" + conn.getContentType() + "'");
			}			
			
			try (InputStream   input = conn.getInputStream();
				 OutputStream output = new FileOutputStream(uploadPath + File.separator + outputFileName);
				) {
				byte[] buffer = new byte[8 * 1024];
		    	int bytesRead;
		    	while ((bytesRead = input.read(buffer)) != -1)
		    		output.write(buffer, 0, bytesRead);
		    	
		    	return new ProcessingResult(urlString, "ok", "URL successfully processed");
			}
		} catch (MalformedURLException e) {
//			e.printStackTrace();
        	logger.error("error processing URL '" + urlString + "' [MalformedURLException]: " + e.getMessage());
			return new ProcessingResult(urlString, "error", "MalformedURLException: " + e.getMessage());
		} catch (IOException e) {
//			e.printStackTrace();
        	logger.error("error processing URL '" + urlString + "' [IOException]: " + e.getMessage());
			return new ProcessingResult(urlString, "error", "IOException: " + e.getMessage());
		}
	}

	
	private ImageMetaData processFile(File file) {
		String fname = file.getName();
		String idStr = fname.split("_")[0];
		int    id    = -1;
		try { id = Integer.parseInt(idStr); } catch (Exception ex) {}
		return new ImageMetaData(id, fname, "link-to-view-file");
	}
	
	private File getFileById(int id) {
		String strId = imageIdService.strId(id);
		File dir = new File(uploadPath);
		FileFilter fileFilter = new WildcardFileFilter(strId + "_*.*");
		File[] files = dir.listFiles(fileFilter);
		if (null != files && files.length > 0)
			return files[0];
		else
			return null;
	}
	private String getFileNameById(int id) {
		try {
			return getFileById(id).getName();
		} catch (Exception ex) {
			return "";
		}
	}
	
	@Override
	public Collection<ImageMetaData> images() {
		
		List<ImageMetaData> result = new ArrayList<>();
		
		File dir = new File(uploadPath);
		FileFilter fileFilter = new WildcardFileFilter("*_*.*"); // RegexFileFilter
		File[] files = dir.listFiles(fileFilter);

		for (File file: files)
			result.add(processFile(file));
		
		return result;
	}

	@Override
	public ImageMetaData image(int id) {
		File file = getFileById(id);
		if (null != file)
			return processFile(file);
		else
			return new ImageMetaData();
	}

	public FileSystemResource raw(int id) {
		String fname = getFileNameById(id);
		if (null != fname && !fname.isEmpty())
			return new FileSystemResource(getFileById(id));
		else 
			return null;
	}

}
