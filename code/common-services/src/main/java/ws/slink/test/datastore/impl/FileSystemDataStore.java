package ws.slink.test.datastore.impl;

import java.awt.image.BufferedImage;
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

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import ws.slink.test.datastore.ImageDataReader;
import ws.slink.test.datastore.ImageDataWriter;
import ws.slink.test.datatype.ImageMetaData;
import ws.slink.test.datatype.ProcessingResult;
import ws.slink.test.datatype.RawImage;
import ws.slink.test.tools.FileTools;
import ws.slink.test.url.URLProvider;

public abstract class FileSystemDataStore implements ImageDataWriter, ImageDataReader {

	private static final Logger logger = LoggerFactory.getLogger(FileSystemDataStore.class);

	@Autowired
	FileTools fileTools;

	@Autowired
	URLProvider urlProvider;
	
	abstract protected String getFilePath();
	abstract protected String getViewPath(String fileName);
	abstract protected String getOriginalViewPath(String fileName);
	
	@Override
	public ProcessingResult save(BufferedImage image, String key, String mimeType) {
		logger.debug("saving image to " + getFilePath() + File.separator + key);
		try {
			ImageIO.write(image, 
					      FilenameUtils.getExtension(key), 
					      new File(getFilePath() + File.separator + key));
			return new ProcessingResult(key, "ok", "file successfully saved", key);
		} catch (IOException e) {
        	logger.error("error saving file '" + key + "': " + e.getMessage());
			return new ProcessingResult(key, "error", e.getMessage(), "");
		}
	}

	@Override
	public ProcessingResult save(MultipartFile mpFile) {
		String name = fileTools.getFileName(mpFile.getOriginalFilename(), getFilePath()); 
		File convFile = new File(getFilePath() + File.separator + name);
		logger.debug("saving file '" + name + "' to: " + getFilePath() + File.separator + name);
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
			fos.write(mpFile.getBytes());
			fos.close();
			return new ProcessingResult(mpFile.getOriginalFilename(), "ok", "file successfully uploaded", name);
        } catch (IOException e) {
        	logger.error("error saving file '" + name + "': " + e.getMessage());
			return new ProcessingResult(mpFile.getOriginalFilename(), "error", e.getMessage(), "");
        }
	}

	@Override
	public ProcessingResult save(String fileName, byte[] data) {
		String name = fileTools.getFileName(fileName, getFilePath()); 
		try (OutputStream stream = new FileOutputStream(getFilePath() + File.separator + name)) {
		    stream.write(data);
		    return new ProcessingResult(fileName, "ok", "file successfully saved", name);
		} catch (FileNotFoundException e) {
        	logger.error("error saving file '" + name + "' [FileNotFound]: " + e.getMessage());
			return new ProcessingResult(fileName, "error", "FileNotFound error saving file", "");
		} catch (IOException e) {
        	logger.error("error saving file '" + name + "' [IOException]: " + e.getMessage());
			return new ProcessingResult(fileName, "error", "IOException error saving file", "");
		}
	}
	
	@Override
	public ProcessingResult save(String urlString) {
		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			String name = fileTools.getFileName(FilenameUtils.getName(url.getPath()), getFilePath()); 
			if (!conn.getContentType().toLowerCase().contains("image")) {
				logger.error("unsupported mime type received: '" + conn.getContentType() + "'");
				return new ProcessingResult(urlString, "error", "unsupported mime type '" + conn.getContentType() + "'");
			}			
			try (InputStream   input = conn.getInputStream();
				 OutputStream output = new FileOutputStream(getFilePath() + File.separator + name);
				) {
				byte[] buffer = new byte[8 * 1024];
		    	int bytesRead;
		    	while ((bytesRead = input.read(buffer)) != -1)
		    		output.write(buffer, 0, bytesRead);
		    	
		    	return new ProcessingResult(urlString, "ok", "URL successfully processed", name);
			}
		} catch (MalformedURLException e) {
        	logger.error("error processing URL '" + urlString + "' [MalformedURLException]: " + e.getMessage());
			return new ProcessingResult(urlString, "error", "MalformedURLException: " + e.getMessage(), "");
		} catch (IOException e) {
        	logger.error("error processing URL '" + urlString + "' [IOException]: " + e.getMessage());
			return new ProcessingResult(urlString, "error", "IOException: " + e.getMessage(), "");
		}
	}

	/**
	 *  to get a list of JSON-encoded files in data store
	 */
	@Override
	public Collection<ImageMetaData> list() {
		List<ImageMetaData> result = new ArrayList<>();
		File dir = new File(getFilePath());
		File[] files = dir.listFiles(new FileFilter() {
		    @Override
		    public boolean accept(File file) {
		        return !file.getName().startsWith(".");
		    }
		});
		for (File file: files)
			result.add(
				new ImageMetaData(file.getName(), 
							      file.getName(), 
							      getViewPath(file.getName()),
							      getOriginalViewPath(file.getName())
								 )
				);
		return result;
	}

	/**
	 *  to get a JSON-encoded file from data store
	 */
	@Override
	public ImageMetaData get(String name) {
		File file = new File(getFilePath() + File.separator + name);
		if (file.exists())
			return  new ImageMetaData(file.getName(), 
				                      file.getName(), 
				                      getViewPath(file.getName()),
				                      getOriginalViewPath(file.getName())
				                      );
		else
			return new ImageMetaData();
	}

	/**
	 *  to get a file from data store
	 */
	@Override
	public RawImage raw(String name) throws IOException {
		File file = new File(getFilePath() + File.separator + name);
		if (file.exists())
			return new RawImage(new FileSystemResource(file).getInputStream(),
					    URLConnection.guessContentTypeFromName(name));
		else
			return null;
	}

	/**
	 *  to get a file from data store
	 * @throws IOException 
	 */
	@Override
	public BufferedImage read(String name) throws IOException {
		return ImageIO.read(new FileSystemResource(getFilePath() + File.separator + name).getInputStream());
	}


}
