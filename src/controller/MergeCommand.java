package controller;

import domain.DNMSO;
import domain.DnmsoFactory;
import service.PredicitonFactory;

/**
 * merge files and convert DNMSO.
 */
public class MergeCommand extends AbstractCommand {

	
	/* execute the command
	 * @see tr.edu.iyte.dnmso.controller.Command#execute()
	 */
	public DNMSO execute() {
        DNMSO dnmso = getContainer();
        if (dnmso == null){
            DnmsoFactory dnmsoFactory = new DnmsoFactory();
            dnmso = dnmsoFactory.createDnmso();
        }
    	for (int i = 1; i< getArgs().length-1;i++){
        	String[] firstDNMLArgs = {"read","-p",getArgs()[i]};
        	PredicitonFactory predicitonFactory = new PredicitonFactory();
            dnmso = (DNMSO) predicitonFactory.getService(getArgs()[i]).run(dnmso,firstDNMLArgs);
    	}
		String[] writeDNMLArgs = {"write","-o",getArgs()[getArgs().length-1]};
		PredicitonFactory predicitonFactory = new PredicitonFactory();
		return (DNMSO) predicitonFactory.getDNMSOService().run(dnmso,writeDNMLArgs);
	}
}
