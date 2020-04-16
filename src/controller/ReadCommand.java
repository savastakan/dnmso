package controller;

import domain.DNMSO;
import domain.DnmsoFactory;
import service.PredicitonFactory;

/**
 * read files .
 */
public class ReadCommand extends AbstractCommand {


	/*execute command
	 * @see tr.edu.iyte.dnmso.controller.Command#execute()
	 */
	public DNMSO execute() {
        DNMSO dnmso = getContainer();
        if (dnmso==null){
            DnmsoFactory dnmsoFactory = new DnmsoFactory();
            dnmso = dnmsoFactory.createDnmso();
        }
        String[] sourceArgs;
        System.out.println(getArgs().length);
        if(getArgs().length==3){
            sourceArgs = new String[] {"read","-p",getArgs()[1],"-s",getArgs()[2]};
        }  else{
            sourceArgs = new String[] {"read","-p",getArgs()[1]};
        }

    	PredicitonFactory predicitonFactory = new PredicitonFactory();
		return (DNMSO) predicitonFactory.getService(getArgs()[1]).run(dnmso,sourceArgs);
	}

}
