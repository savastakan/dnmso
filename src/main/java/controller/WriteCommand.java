package controller;

import domain.DNMSO;
import service.PredictionFactory;

public class WriteCommand  extends AbstractCommand  {


	public DNMSO execute() {
		String[] targetArgs = {"write","-o",getArgs()[1]}; //targetPath : getArgs()[2]
		PredictionFactory predictionFactory = new PredictionFactory();
		return predictionFactory.getDNMSOService().run(this.getContainer(),targetArgs);
	}

}
