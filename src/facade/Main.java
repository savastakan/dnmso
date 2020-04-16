package facade;

import controller.CommandFactory;

public class Main {
	public static void main(String[] args) {
		try {
		if (args.length > 0){
			CommandFactory commandFactory = new CommandFactory();
			commandFactory.createCommand(args).execute();
		
		} else {
			System.out.println("there is no argument");
		}
		}catch (Exception e) {
			System.out.println("There are some problem, please check command...");
		}				
	}
}
