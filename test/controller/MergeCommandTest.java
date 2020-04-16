package controller;



import junit.framework.Assert;
import org.junit.Test;
import domain.DNMSO;

import java.io.File;

public class MergeCommandTest {

	@Test
	public void merge() {

		String args[] = {"merge","data"+ File.separator +"convertedLutefisk.dnmso",
				"data"+ File.separator +"convertedPepNovo.dnmso",
				"data"+ File.separator +"merged.dnmso"};
		MergeCommand mergeCommand = new MergeCommand();
		mergeCommand.setArgs(args);
		DNMSO DNMSO = mergeCommand.execute();
		Assert.assertEquals(2,DNMSO.getPredictions().size());
		Assert.assertEquals(301,DNMSO.getSpectra().size());
		Assert.assertEquals(4, DNMSO.getPredictions().get(0).getPrediction().size());
		Assert.assertEquals(60, DNMSO.getPredictions().get(1).getPrediction().size());

	}

}
