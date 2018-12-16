package ws.slink.test.tools;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

@Service
public class FileTools {
	
	/**
	 * 
	 * Generate fileName based on originalFileName
	 * (if file with same name exists in output directory, append suffix to originalFileName)
	 * 
	 * @param originalFileName
	 * @param path
	 * @return fileName with optional suffix
	 */
	public String getFileName(String originalFileName, String path) {
		File       file = new File(path + File.separator + originalFileName);
		String fileName = originalFileName;
		int         idx = 0;
		while (file.exists()) {
			fileName = FilenameUtils.removeExtension(originalFileName)
					 + String.format("_%04d.", ++idx) 
					 + FilenameUtils.getExtension(originalFileName);  
			file = new File(path + File.separator + fileName);
			
		}
		
		// (?)
		// touch a file (so that another thread won't get same FileName, 
		// while this file is not created yet 
		try  { new FileOutputStream(file).close(); }
		catch (Exception e) { e.printStackTrace(); }
		
		return fileName;
	}
}
