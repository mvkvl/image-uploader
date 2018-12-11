package ws.slink.test.service.id;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * ID service - needed to generate numeric Id for each uploaded file 
 *              so that files with same names won't overwrite each other;
 *              also Id is used for querying a file with GET request to service 
 *
 * FileSystem implementation: add 1 to largest image id in upload directory
 * 
 */

@Service
@Qualifier("fs")
public class ImageIdServiceImplFs implements ImageIdService {

	@Value("${images.datastore.fs.path.upload}")
    private String uploadPath;

	// amount of digits in string representation of image Id
	public static final int ID_STRING_LENGTH = 9;
	
	private int currentId = -1;
	
	/**
	 * gets new id from uploaded files
	 * ( called once on first call to 'getNewId()', 
	 *   sets 'currentId' variable, then this variable 
	 *   is increased on each call to 'getNewId()' )
	 * 
	 */
	private int findId() {
		File dir = new File(uploadPath);
		FileFilter fileFilter = new WildcardFileFilter("*_*.*"); // RegexFileFilter
		File[] files = dir.listFiles(fileFilter);
		if (null == files || files.length == 0)
			currentId = 1;
		else {
			Arrays.sort(files);
			String id = files[files.length - 1].getName().split("_")[0];
			try {
				currentId = Integer.parseInt(id) + 1;
			} catch (NumberFormatException ex) {
				// on error returns 0 //TODO: perhaps far not best solution
				currentId = 0;
			}
		}
		return currentId;
	}
	

	@Override
	public int id() {
		return currentId;
	}

	@Override
	public String strId() {
		String fmt = "%0" + ID_STRING_LENGTH + "d";
		return String.format(fmt, nextId());
	}

	public String strId(int id) {
		String fmt = "%0" + ID_STRING_LENGTH + "d";
		return String.format(fmt, id);
	}

	
	/**
	 * returns an id for new file 
	 * 
	 */
	@Override
	synchronized public int nextId() {
		return (currentId < 0) ? findId() : ++currentId;
	}


	/**
	 * returns new id in String format
	 * 
	 */
	@Override
	public String nextStrId() {
		String fmt = "%0" + ID_STRING_LENGTH + "d";
		return String.format(fmt, nextId());
	}
	
}
