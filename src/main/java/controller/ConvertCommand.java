/*
 * 
 */
package controller;



import domain.DNMSO;
import domain.DNMSOFactory;
import service.PredictionFactory;

public class ConvertCommand extends AbstractCommand {

	public DNMSO execute() {
		DNMSOFactory dnmsoFactory = new DNMSOFactory();
    	DNMSO dnmso = dnmsoFactory.createDNMSO();
    	//read sourcePath parameter
        String[] sourceArgs = null;
    	 if (getArgs().length ==3){
             sourceArgs =new String[] {"read","-p",getArgs()[1]};
         }   else {
             sourceArgs =new String[] {"read","-p",getArgs()[1],"-s",getArgs()[3]};
         }

    	PredictionFactory predictionFactory = new PredictionFactory();
		dnmso = predictionFactory.getService(getArgs()[1]).run(dnmso,sourceArgs); // getArgs()[3] inputServiceName
		String[] targetArgs = {"write","-o",getArgs()[2]}; //targetPath : getArgs()[2]
		dnmso = (DNMSO) predictionFactory.getDNMSOService().run(dnmso,targetArgs);
        return dnmso;
    }
}
