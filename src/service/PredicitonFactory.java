/*
 * 
 */
package service;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

// TODO: Auto-generated Javadoc
/**
 * User: ST Date: 13.09.2011 Time: 13:37
 * @author  ST
 * @version  $Revision: 1.0 $
 */
public class PredicitonFactory {
    

    
    /** The service types. */
    private HashMap<String, String> serviceTypes = new HashMap<String, String>();

    /**
     * Instantiates a new service factory.
     */
    public PredicitonFactory(){
        getProperties(serviceTypes, "service/services");
    }
	
	/**
	 * Find service.
	 *
	 * @param path the path
	 * @return the service
	 */
	private Service findService(String path){
		File file = new File(path);
		if (file.exists()){
		for (String serviceName : serviceTypes.keySet()) {
			
			try {
				Service service = (Service)Class.forName(serviceTypes.get(serviceName)).newInstance();
				if (service.isValid(file)) {
					//System.out.println(service.getServiceName() + ", file :"+file.getPath());
					return service;
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		} 
		return null;	
	}

    /**
     * Gets the properties.
     *
     * @param properties the properties
     * @param path the path
     * @return the properties
     */
    private void getProperties(HashMap<String, String> properties,String path){
        ResourceBundle bundle = ResourceBundle.getBundle(path);
        Enumeration<String> en = bundle.getKeys();
        for (; en.hasMoreElements(); ) {
            String key = en.nextElement();
            properties.put(key, bundle.getString(key));
        }
    }
  
    
    /**
     * Gets the service.
     *
     * @param path the path
     * @return the service
     */
    public Service getService(String path){
		return findService(path);
    }
    
    /**
     * Gets the dNML service.
     *
     * @return the dNML service
     */
    public Service getDNMSOService(){
		return new DnmsoOboService();
    }
}
