/*
 * 
 */
package tr.edu.iyte.dnmso.service;

import tr.edu.iyte.dnmso.domain.DNMSO;
import tr.edu.iyte.dnmso.domain.DnmsoFactory;
import tr.edu.iyte.dnmso.domain.Spectrum;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mslib.peak.Peak;
import mslib.spectrum.MassSpectrum;

// TODO: Auto-generated Javadoc
/**
 * User: ST Date: 16.09.2011 Time: 12:35
 * @author  Savas TAKAN
 * @version  $Revision: 1.0 $
 */
public abstract class AbstractService implements Service{
	/**
	 * @uml.property  name="container"
	 */
	private Object container;
	/**
	 * @uml.property  name="properties"
	 */
	private HashMap<String, String> properties;
	
	/*
	 * -s : spectrum file
	 * -p : prediction file
	 * -m : merge
	 * -i : index
	 * -n : number of prediction
	 */
	
	public void processSettings(Object container,String[] args){
		properties = new HashMap<String, String>();
		if (container!=null){
			setContainer(container);
		} else {
			DnmsoFactory dnmsoFactory = new DnmsoFactory();
			setContainer(dnmsoFactory.createDnmso());
		}
		
		properties.put(ServiceTag.COMMAND.toString(), args[0]);
		for (int i =1; i< args.length -1 ; i=i+2){
			properties.put(args[i], args[i+1]);
		}
	}
    public boolean findSpectrum(String fileName,Integer scanID){
    	DnmsoFactory dnmsoFactory = new DnmsoFactory();
       DNMSO dnmso = (DNMSO)getContainer();
        if (dnmso!=null){
        	dnmso.getSpectra().findSpectrum(dnmso, fileName, String.valueOf(scanID));
        }
       return false;
    }
    public String convertPeakToCvs(Collection<Peak> peaks){
    	String cvs="";
    	for(Peak peak : peaks){

    		cvs = cvs.concat(String.valueOf(peak.getX())+","+String.valueOf(peak.getY()+";"));
    		
    	} 	
		return cvs;
    	
    }
    public Collection<Peak> convertCvsToPeak(String cvs){

    	Peak peak = new Peak();
    	StringTokenizer st = new StringTokenizer(cvs,",;");
    	Collection<Peak> peaks = new ArrayList<Peak>();
    	while (st.hasMoreTokens()) {
    		String x_s = st.nextToken();
    		String y_s = st.nextToken();
    
    		Double x = Double.parseDouble(x_s);
    		Double y = Double.parseDouble(y_s);
			peak.set(x, y);
			peaks.add(peak);
		}
    	return peaks;
    }
    public List<String> getNotFindingSpectrumIds(String name, List<String> indexList) {
    	DnmsoFactory dnmsoFactory = new DnmsoFactory();
        List<String> newList = new ArrayList<String>();
        DNMSO dnmso = (DNMSO)getContainer();
        if (dnmso.getSpectra().size() ==0){
            return   indexList;
        }
        if (indexList.size()>0){
        	for (int i=0;i<indexList.size();i++){
        		if (dnmso.getSpectra().findSpectrum(dnmso, name, indexList.get(i))==null){
        			newList.add(indexList.get(i));
        		}
        	}

        }
        return newList;
    }

	
    


	//indexList:1,,2,4,6,8,,5
    /**
     * Creates the index list.
     *
     * @param indexListString the index list string
    
     * @return the list */
    protected List<String> createIndexList(String indexListString){

        List<String> indexList = new ArrayList<String>();
       Pattern pattern = Pattern.compile("(\\d+),,(\\d+),?");
       Matcher matcher = pattern.matcher(indexListString);
        while(matcher.find()){
            int begin = Integer.parseInt(matcher.group(1));
            int end = Integer.parseInt(matcher.group(2));
            if (begin<= end ){
                 for(int i = begin;i<=end;i++){
                      indexList.add(String.valueOf(i));
                 }
            }
        }

        indexListString = matcher.replaceAll("");
        pattern = Pattern.compile(",?(\\d+),?");
        matcher = pattern.matcher(indexListString);
        while(matcher.find()){

             int index = Integer.parseInt(matcher.group(1));
            indexList.add(String.valueOf(index));
        }
        return indexList;
    }

    /**
     * Validate writable.
     *
     * @param path the path
    
     * @return true, if successful */
    protected boolean validateWritable(String path){
        File file =new File(path);
         if (!file.exists()) return false;
         if (!file.isDirectory()) return false;
         if (!file.canWrite()) return false;
        return true;
    }

    /**
     * Validate readable.
     *
     * @param path the path
    
     * @return true, if successful */
    protected boolean validateReadable(String path){
       File file =new File(path);
        if (!file.exists()) return false;
        if (!file.isDirectory()) return false;
        if (!file.canRead()) return false;
       return true;
    }

    /**
     * Gets the file name.
     *
     * @param path the path
    
     * @return the file name */
    protected String getFileName(String path){
    	String fileName=null;
		Matcher fileNamematcher = Pattern.compile("(?:\\S+\\\\)?(\\S+)$").matcher(path);
		while (fileNamematcher.find()) {
			fileName = fileNamematcher.group(1);
		}
		//System.out.println(fileName);
		return fileName;
    }

    /**
     * Gets the file path.
     *
     * @param path the path
    
     * @return the file path */
    protected String getFilePath(String path){
    	
    	String filePath=null;
		Matcher fileNamematcher = Pattern.compile("(\\S+\\\\)\\S+").matcher(path);
		while (fileNamematcher.find()) {
			filePath = fileNamematcher.group(1);
		}
		
		return filePath;
    }
    

	/**
	 * @return
	 * @uml.property  name="container"
	 */
	public Object getContainer() {
		
		return container;
	}
	/**
	 * @param container
	 * @uml.property  name="container"
	 */
	public void setContainer(Object container) {
		this.container = container;
	}
	


	/**
	 * @return
	 * @uml.property  name="properties"
	 */
	public HashMap<String, String> getProperties() {
		return properties;
	}


	/**
	 * @param properties
	 * @uml.property  name="properties"
	 */
	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}



}
