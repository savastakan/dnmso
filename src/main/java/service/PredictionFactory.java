/*
 * 
 */
package service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;


public class PredictionFactory {
    

    private final HashMap<String, String> serviceTypes = new HashMap<>();


    public PredictionFactory(){
        getProperties(serviceTypes);
    }

	private Service findService(String path){
		File file = new File(path);
		if (file.exists()){
		for (String serviceName : serviceTypes.keySet()) {
			
			try {
				Service service = (Service)Class.forName(serviceTypes.get(serviceName)).getDeclaredConstructor().newInstance();
				if (service.isValid(file)) {
					//System.out.println(service.getServiceName() + ", file :"+file.getPath());
					return service;
				}
			} catch (InvocationTargetException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
				e.printStackTrace();
			}
		}
		} 
		return null;	
	}

    private void getProperties(HashMap<String, String> properties){
        ResourceBundle bundle = ResourceBundle.getBundle("services");
        Enumeration<String> en = bundle.getKeys();
		while (en.hasMoreElements()) {
			String key = en.nextElement();
			properties.put(key, bundle.getString(key));
		}
	}
  

    public Service getService(String path){
		return findService(path);
    }

	public Service getDNMSOService(){
		return new DNMSOService();
	}
}
