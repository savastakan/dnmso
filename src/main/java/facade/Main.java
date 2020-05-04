package facade;

import controller.CommandFactory;

public class Main {
	public static void main(String[] args) {
		// String[] args_ = {"validate","data/lutefisk.dnmso"};
		try {
		if (args.length > 0){
			CommandFactory commandFactory = new CommandFactory();
			commandFactory.createCommand(args).execute();
		
		} else {
			System.out.println("there is no argument");
		}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("There are some problem, please check command...");
		}				
	}
}
