package ws.slink.test.tools;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ws.slink.test.model.ImageMetaData;
import ws.slink.test.service.ImageIdService;

@Service
public class FileTools {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(FileTools.class);

	@Autowired
	ImageIdService imageIdService;
	
	public ImageMetaData meta(File file, String urlStr) {
		String fname = file.getName();
		String idStr = fname.split("_")[0];
		int    id    = -1;
		try { id = Integer.parseInt(idStr); } catch (Exception ex) {}
		return new ImageMetaData(id, fname, urlStr + "/" + id);
	}

	public File getFileById(int id, String path) {
		String strId = imageIdService.strId(id);
		File dir = new File(path);
		FileFilter fileFilter = new WildcardFileFilter(strId + "_*.*");
		File[] files = dir.listFiles(fileFilter);
		if (null != files && files.length > 0)
			return files[0];
		else
			return null;
	}
	
	public String getFileNameById(int id, String path) {
		try {
			return getFileById(id, path).getName();
		} catch (Exception ex) {
			return "";
		}
	}
	
}
