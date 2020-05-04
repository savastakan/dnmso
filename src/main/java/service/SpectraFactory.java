package service;

import domain.DNMSO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SpectraFactory {

    private final HashMap<String, String> serviceTypes = new HashMap<>();


    public SpectraFactory(){
        getProperties(serviceTypes);
    }

	private Service findService(File file){
		if (file.exists()){
		for (String serviceName : serviceTypes.keySet()) {
			
			try {
				Service service = (Service)Class.forName(serviceTypes.get(serviceName)).getDeclaredConstructor().newInstance();
				
				if (service.isValid(file)) {
					return service;
				}
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		} 
		return null;	
	}
	

    private void getProperties(HashMap<String, String> properties){
        ResourceBundle bundle = ResourceBundle.getBundle("spectra");
        Enumeration<String> en = bundle.getKeys();
		while (en.hasMoreElements()) {
			String key = en.nextElement();
			properties.put(key, bundle.getString(key));
		}
	}

    public void getSpectra(AbstractService predictionService){
       // System.out.println(predictionService.getProperties());
        String spectrumFilePath = predictionService.getProperties().get(ServiceTag.SPECTRA_FILE_PATH.toString());
        //System.out.println(spectrumFilePath);
    	File spectrumFile = new File(spectrumFilePath);
    	if(spectrumFile.isDirectory()){
    		File[] files = spectrumFile.listFiles();
    		for (int k = 0; k < Objects.requireNonNull(files).length; k++) {
    			Service service = findService(files[k]);
    			String[] args = {"read","-s",files[k].getAbsolutePath()};
				assert service != null;
				service.run(predictionService.getDNMSO(), args);
    		}
    	}
    	else if (spectrumFile.isFile()){
            DNMSO dnmso = getFiles(predictionService);
            predictionService.setDNMSO(dnmso);
    	}
	}

    

	private DNMSO getFiles(AbstractService predictionService) {
    	String line;
    	BufferedReader in = null;
    	String spectrumFilePath = predictionService.getProperties().get("-s");
		try {// batch
			Vector<String> args = new Vector<>();
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
				predictionService.setDNMSO(service.run(predictionService.getDNMSO(),(String[]) args.toArray()));
			}
		} catch (Exception e) { // only file 
			Vector<String> args = new Vector<>();
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
			assert service != null;
			DNMSO dnmso =  service.run(predictionService.getDNMSO(), arguments);
            predictionService.setDNMSO(dnmso);
	    	return dnmso;
		} finally {
			try {
				assert in != null;
				in.close();
			} catch (IOException ignored) {

			}	
		}
		return (DNMSO)predictionService.getDNMSO();
	}



}
