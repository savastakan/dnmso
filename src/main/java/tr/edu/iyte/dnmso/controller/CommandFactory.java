/*
 * 
 */
package tr.edu.iyte.dnmso.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

import tr.edu.iyte.dnmso.domain.DNMSO;

/**
 * A factory for creating Command objects.
 * @author  Savas Takan
 * @version  $Revision: 1.0 $
 */
public class CommandFactory {


    /** The command types. */
    private HashMap<String, String> commandTypes = new HashMap<String, String>();

    /**
     * Gets the properties.
     *
     * @param properties the properties
     * @param path the path
    
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
     * instantiates a new command factory.
     */
    public CommandFactory() {
        getProperties(commandTypes,"tr/edu/iyte/dnmso/controller/commands");
    }


    //convert

    /**
     * Creates a new Command object.
     *
     * @param args the args
    
     * @return the command */
    public Command createCommand(DNMSO container, final String[] args)  {
         Command command=null;
        String commandPath = commandTypes.get(args[0]);
        if (( commandPath != null )) {
             try {
				command = (Command) Class.forName(commandPath).newInstance();
				command.setArgs(args);
				command.setContainer(container);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {

				e.printStackTrace();
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			}
             
        }
        return command;
    }
    /**
     * Creates a new Command object.
     *
     * @param args the parameters
    
     * @return the command */
    public Command createCommand(final String[] args)  {
         Command command=null;
        String commandPath = commandTypes.get(args[0]);
        if (( commandPath != null )) {
             try {
				command = (Command) Class.forName(commandPath).newInstance();
				command.setArgs(args);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {

				e.printStackTrace();
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			}
             
        }
        return command;
    }
}
