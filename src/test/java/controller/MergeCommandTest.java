package controller;



import domain.DNMSO;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class MergeCommandTest {

	@Test
	public void merge() {

		String args[] = {"merge","data"+ File.separator +"convertedLutefisk.dnmso",
				"data"+ File.separator +"convertedPepNovo.dnmso",
				"data"+ File.separator +"merged.dnmso"};
		MergeCommand mergeCommand = new MergeCommand();
		mergeCommand.setArgs(args);
		DNMSO dnmso = mergeCommand.execute();
		Assert.assertEquals(2,dnmso.getPrediction().size());
		Assert.assertEquals(301,dnmso.getSpectra().size());
		Assert.assertEquals(4, dnmso.getPrediction().size());
		Assert.assertEquals(60, dnmso.getPrediction().size());

	}

}
