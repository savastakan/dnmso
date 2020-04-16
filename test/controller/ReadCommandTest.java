package controller;

import org.junit.Assert;
import org.junit.Test;
import domain.DNMSO;

import java.io.File;

public class ReadCommandTest {
	@Test
	public void read() {
		String args[] = {"read","data"+ File.separator +"test.dnmso"};
		ReadCommand readCommand = new ReadCommand();
		readCommand.setArgs(args);
		DNMSO DNMSO = readCommand.execute();
		Assert.assertEquals(4, DNMSO.getPredictions().get(0).getPrediction().size());
	}
}
