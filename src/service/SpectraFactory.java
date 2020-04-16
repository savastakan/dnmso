package service;

import domain.DNMSO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SpectraFactory {
    /** The spectra types. */
    private HashMap<String, String> serviceTypes = new HashMap<String, String>();


    /**
     * Instantiates a new service factory.
     */
    public SpectraFactory(){
        getProperties(serviceTypes, "service/spectra");
    }

    /**
     *   find service
     * @param file
     * @return
     */
	private Service findService(File file){
		if (file.exists()){
		for (String serviceName : serviceTypes.keySet()) {
			
			try {
				Service service = (Service)Class.forName(serviceTypes.get(serviceName)).newInstance();
				
				if (service.isValid(file)) {
					return service;
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
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

    public DNMSO getSpectra(AbstractService predictionService){
       // System.out.println(predictionService.getProperties());
        String spectrumFilePath = predictionService.getProperties().get(ServiceTag.SPECTRA_FILE_PATH.toString());
        //System.out.println(spectrumFilePath);
    	File spectrumFile = new File(spectrumFilePath);
    	if(spectrumFile.isDirectory()){
    		File files[] = spectrumFile.listFiles();
    		for (int k = 0; k < files.length; k++) {
    			Service service = findService(files[k]);
    			String[] args = {"read","-s",files[k].getAbsolutePath()};
    			service.run(predictionService.getContainer(), args);
    		}
    	}
    	else if (spectrumFile.isFile()){
            DNMSO dnmso =getFiles(predictionService);
            predictionService.setContainer(dnmso);
    	}
		return (DNMSO) predictionService.getContainer();
    }

    

	private DNMSO getFiles(AbstractService predictionService) {
    	String line;
    	BufferedReader in = null;
    	String spectrumFilePath = predictionService.getProperties().get("-s");
		try {// batch
			Vector<String> args = new Vector<String>();
			in = new BufferedReader(new FileReader(spectrumFilePath));
			while ((line = in.readLine()) != null) {
				args.add("read");
				StringTokenizer st = new StringTokenizer(line," ");
				String filePath = st.nextToken();
				args.add(st.nextToken());
				while(st.hasMoreTokens()){
					args.add(st.nextToken());
				}
				File file = new File(filePath);
				Service service = findService(file);
				predictionService.setContainer(service.run(predictionService.getContainer(),(String[]) args.toArray()));
			}
		} catch (Exception e) { // only file 
			Vector<String> args = new Vector<String>();
			args.add("read");
			args.add("-s");
			args.add(spectrumFilePath);
			if (predictionService.getProperties().get("-i") != null){
				args.add("-i");

				args.add(predictionService.getProperties().get("-i"));
			}
            String[] arguments = new String[args.size()];
            args.toArray(arguments);

			Service service = findService(new File(spectrumFilePath));
            DNMSO dnmso =  (DNMSO)service.run((DNMSO)predictionService.getContainer(), arguments);
            predictionService.setContainer(dnmso);
	    	return dnmso;
		} finally {
			try {
				in.close();
			} catch (IOException e) {

			}	
		}
		return (DNMSO)predictionService.getContainer();
	}



}
