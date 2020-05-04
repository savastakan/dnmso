/*
 * 
 */
package controller;

import domain.DNMSO;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;


public class CommandFactory {



    private final HashMap<String, String> commandTypes = new HashMap<>();

    private void getProperties(HashMap<String, String> properties){
        ResourceBundle bundle = ResourceBundle.getBundle("commands");
        Enumeration<String> en = bundle.getKeys();
        while (en.hasMoreElements()) {
            String key = en.nextElement();
            properties.put(key, bundle.getString(key));
        }
    }

    public CommandFactory() {
        getProperties(commandTypes);
    }


    //convert
    public Command createCommand(DNMSO dnmso, final String[] args)  {
         Command command=null;
        String commandPath = commandTypes.get(args[0]);
        if (( commandPath != null )) {
             try {
				command = (Command) Class.forName(commandPath).getDeclaredConstructor().newInstance();
				command.setArgs(args);
				command.setDNMSO(dnmso);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
				e.printStackTrace();
			}

        }
        return command;
    }

    public Command createCommand(final String[] args)  {
         Command command=null;
        String commandPath = commandTypes.get(args[0]);
        if (( commandPath != null )) {
             try {
				command = (Command) Class.forName(commandPath).getDeclaredConstructor().newInstance();
				command.setArgs(args);
			} catch (InstantiationException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
				e.printStackTrace();
			}

        }
        return command;
    }
}
