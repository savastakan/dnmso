package controller;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

import domain.DNMSO;

public class ValidateCommandTest {

	@Test
	public void validate() {
		String args[] = {"validate","data"+ File.separator +"PepNovo.dnmso"};
		ValidateCommand validateCommand = new ValidateCommand();
		validateCommand.setArgs(args);
		DNMSO DNMSO = validateCommand.execute();
		Assert.assertNotNull(DNMSO);
		String args1[] = {"validate","data"+ File.separator +"pepnovo_results.txt"};
		validateCommand.setArgs(args1);
		DNMSO = validateCommand.execute();
		Assert.assertNull(DNMSO);
	}

}
