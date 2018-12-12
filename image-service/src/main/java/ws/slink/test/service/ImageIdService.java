package ws.slink.test.service;

/**
 * ID service - needed to generate numeric Id for each uploaded file 
 *              so that files with same names won't overwrite each other;
 *              also Id is used for querying a file with GET request to service 
 * 
 */
public interface ImageIdService {
	
	public int id();
	public String strId();
	public String strId(int id);
	public int nextId();
	public String nextStrId();

}
