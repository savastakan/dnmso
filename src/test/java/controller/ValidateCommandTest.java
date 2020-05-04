package controller;

import java.io.File;

import domain.DNMSO;
import org.junit.Assert;

import org.junit.Test;

public class ValidateCommandTest {

	@Test
	public void validate() {
		String args[] = {"validate","data"+ File.separator +"PepNovo.dnmso"};
		ValidateCommand validateCommand = new ValidateCommand();
		validateCommand.setArgs(args);
		DNMSO dnmso = validateCommand.execute();
		Assert.assertNotNull(dnmso);
		String args1[] = {"validate","data"+ File.separator +"pepnovo_results.txt"};
		validateCommand.setArgs(args1);
		dnmso = validateCommand.execute();
		Assert.assertNull(dnmso);
	}

}
