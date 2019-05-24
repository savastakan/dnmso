/*
 * 
 */
package tr.edu.iyte.dnmso.controller;



import tr.edu.iyte.dnmso.domain.DNMSO;
import tr.edu.iyte.dnmso.domain.DnmsoFactory;
import tr.edu.iyte.dnmso.service.PredicitonFactory;

/**
 * convert file to DNMSO.
 */
public class ConvertCommand extends AbstractCommand {

    /* execute command
	 * @see tr.edu.iyte.dnmso.controller.Command#execute()
	 */
	public DNMSO execute() {
		DnmsoFactory dnmsoFactory = new DnmsoFactory();
    	DNMSO dnmso = dnmsoFactory.createDnmso();
    	//read sourcePath parameter
        String[] sourceArgs = null;
    	 if (getArgs().length ==3){
             sourceArgs =new String[] {"read","-p",getArgs()[1]};
         }   else {
             sourceArgs =new String[] {"read","-p",getArgs()[1],"-s",getArgs()[3]};
         }

    	PredicitonFactory predicitonFactory = new PredicitonFactory();
		dnmso = (DNMSO) predicitonFactory.getService(getArgs()[1]).run(dnmso,sourceArgs); // getArgs()[3] inputServiceName
		String[] targetArgs = {"write","-o",getArgs()[2]}; //targetPath : getArgs()[2]
		dnmso = (DNMSO) predicitonFactory.getDNMSOService().run(dnmso,targetArgs);
        return dnmso;
    }
}
