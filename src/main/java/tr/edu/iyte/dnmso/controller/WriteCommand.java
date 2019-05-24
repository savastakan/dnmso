package tr.edu.iyte.dnmso.controller;

import tr.edu.iyte.dnmso.domain.DNMSO;
import tr.edu.iyte.dnmso.service.PredicitonFactory;

/**
 * write file as DNMSO.
 */
public class WriteCommand  extends AbstractCommand  {


	/*
	 * @see tr.edu.iyte.dnmso.controller.Command#execute()
	 */
	public DNMSO execute() {
		String[] targetArgs = {"write","-o",getArgs()[1]}; //targetPath : getArgs()[2]
		PredicitonFactory predicitonFactory = new PredicitonFactory();
		return (DNMSO) predicitonFactory.getDNMSOService().run(this.getContainer(),targetArgs);
	}

}
