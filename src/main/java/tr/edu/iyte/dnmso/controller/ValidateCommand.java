package tr.edu.iyte.dnmso.controller;

import tr.edu.iyte.dnmso.domain.DNMSO;
import tr.edu.iyte.dnmso.service.PredicitonFactory;

import java.io.File;

public class ValidateCommand extends AbstractCommand  {

	public DNMSO execute() {
		try{
			PredicitonFactory predicitonFactory = new PredicitonFactory();
			if (predicitonFactory.getDNMSOService().isValid(new File(getArgs()[1]))){
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
