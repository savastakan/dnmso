package controller;

import domain.DNMSO;
import domain.DNMSOFactory;
import service.PredictionFactory;


public class MergeCommand extends AbstractCommand {


	public DNMSO execute() {
		PredictionFactory predictionFactory = new PredictionFactory();
        DNMSO dnmso = getContainer();
        if (dnmso == null){
            DNMSOFactory dnmsoFactory = new DNMSOFactory();
            dnmso = dnmsoFactory.createDNMSO();
        }
    	for (int i = 1; i< getArgs().length-1;i++){
        	String[] firstDNMLArgs = {"read","-p",getArgs()[i]};

            dnmso = (DNMSO) predictionFactory.getService(getArgs()[i]).run(dnmso,firstDNMLArgs);
    	}
		String[] writeDNMLArgs = {"write","-o",getArgs()[getArgs().length-1]};
		return (DNMSO) predictionFactory.getDNMSOService().run(dnmso,writeDNMLArgs);
	}
}
