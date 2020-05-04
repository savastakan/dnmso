package controller;

import domain.DNMSO;
import domain.DNMSOFactory;
import service.PredictionFactory;

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
            DNMSOFactory dnmsoFactory = new DNMSOFactory();
            dnmso = dnmsoFactory.createDNMSO();
        }
        String[] sourceArgs;
        System.out.println(getArgs().length);
        if(getArgs().length==3){
            sourceArgs = new String[] {"read","-p",getArgs()[1],"-s",getArgs()[2]};
        }  else{
            sourceArgs = new String[] {"read","-p",getArgs()[1]};
        }

    	PredictionFactory predictionFactory = new PredictionFactory();
		return (DNMSO) predictionFactory.getService(getArgs()[1]).run(dnmso,sourceArgs);
	}

}
