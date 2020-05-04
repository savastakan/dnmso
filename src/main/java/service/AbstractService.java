/*
 * 
 */
package service;

import domain.DNMSO;
import domain.DNMSOFactory;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class AbstractService implements Service{

	private DNMSO dnmso;

	private HashMap<String, String> properties;
	
	/*
	 * -s : spectrum file
	 * -p : prediction file
	 * -m : merge
	 * -i : index
	 * -n : number of prediction
	 */
	
	public void processSettings(DNMSO dnmso, String[] args){
		properties = new HashMap<>();
		if (dnmso!=null){
			setDNMSO(dnmso);
		} else {
			DNMSOFactory dnmsoFactory = new DNMSOFactory();
			setDNMSO(dnmsoFactory.createDNMSO());
		}
		
		properties.put(ServiceTag.COMMAND.toString(), args[0]);
		for (int i =1; i< args.length -1 ; i=i+2){
			properties.put(args[i], args[i+1]);
		}
	}

    protected boolean validateWritable(String path){
        File file = new File(path);
         if (!file.exists()) return false;
         if (!file.isDirectory()) return false;
		return file.canWrite();
	}

    protected boolean validateReadable(String path){
       File file =new File(path);
        if (!file.exists()) return false;
        if (!file.isDirectory()) return false;
		return file.canRead();
	}



	//indexList:1,,2,4,6,8,,5
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


	public DNMSO getDNMSO() {

		return dnmso;
	}

	public void setDNMSO(DNMSO dnmso) {
		this.dnmso = dnmso;
	}
	

	public HashMap<String, String> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}

	public static <T> T getLastElement(final Iterable<T> elements) {
		T lastElement = null;

		for (T element : elements) {
			lastElement = element;
		}

		return lastElement;
	}
	public static <T> T getFirstElement(final Iterable<T> elements) {
		return elements.iterator().next();
	}

}
