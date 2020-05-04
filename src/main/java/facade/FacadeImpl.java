/*
 * 
 */
package facade;


import controller.CommandFactory;
import domain.DNMSO;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class FacadeImpl.
 * @author Savas TAKAN
 * @version $Revision: 1.0 $
 */
public class FacadeImpl implements Facade {

    public DNMSO handle(DNMSO container, final String[] args)  {
    	CommandFactory commandFactory = new CommandFactory();
        return commandFactory.createCommand(container,args).execute();
    }

	public DNMSO handle(String[] args) {
		CommandFactory commandFactory = new CommandFactory();
		return commandFactory.createCommand(args).execute();
	}

	public DNMSO read(DNMSO dnmso, String source){
		FacadeFactory facadeFactory = new FacadeFactory();
    	String[] args = {"read",source};
        return facadeFactory.getFacade().handle(dnmso,args);
	}
   
    public DNMSO read(String source) {
    	FacadeFactory facadeFactory = new FacadeFactory();
    	String[] args = {"read",source};
        return facadeFactory.getFacade().handle(args);
    }

  
    public DNMSO write(DNMSO dnmso, String target) {
    	FacadeFactory facadeFactory = new FacadeFactory();
    	String[] writeArgs = {"write",target};
        return facadeFactory.getFacade().handle(dnmso,writeArgs);
    }

 
    public DNMSO convert(String source, String target) {
    	String args[] = {"convert",source,target};
		FacadeFactory facadeFactory = new FacadeFactory();
        return facadeFactory.getFacade().handle(args);
    }


    public DNMSO merge(List<String> inputFiles, String outputFile) {
    	List<String> argsList = new ArrayList<String>(); 
    	argsList.add("merge");
    	argsList.addAll(inputFiles);
    	argsList.add(outputFile);
    	String[] args = new String[argsList.size()];
    	argsList.toArray(args);
    	FacadeFactory facadeFactory = new FacadeFactory();
        return facadeFactory.getFacade().handle(args);
    }

 
    public DNMSO validate(String source) {
    	FacadeFactory facadeFactory = new FacadeFactory();
		String args[] = {"validate",source};
        return facadeFactory.getFacade().handle(args);
    }
}


