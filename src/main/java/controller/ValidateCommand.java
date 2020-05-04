package controller;

import domain.DNMSO;
import service.PredictionFactory;

import java.io.File;

public class ValidateCommand extends AbstractCommand  {

	public DNMSO execute() {
		try{
			PredictionFactory predictionFactory = new PredictionFactory();
			if (predictionFactory.getDNMSOService().isValid(new File(getArgs()[1]))){
				System.out.println("DNMSO file is valid");
				return new DNMSO();
			} else {
				System.out.println("DNMSO file is not valid");
			}

		} catch (Exception e) {
			System.out.println("DNMSO file is not valid");
		}
		return null;
	}
}
