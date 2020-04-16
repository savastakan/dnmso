/*
 * 
 */
package service;

import java.io.File;


/**
 * User: ST
 * Date: 13.09.2011
 * Time: 13:32
 * @author Savas TAKAN
 * @version $Revision: 1.0 $
 */
public interface Service {
	public String getServiceName();
	/**
	 * Run.
	 *
	 * @param container the container
	 * @param args the args
	
	 * @return the object */
	Object run(Object container, String[] args);

	boolean isValid(File file);
	
}
