package controller;



import domain.DNMSO;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class ConvertCommandTest {

	/**
	 * Convert lutefisk xp.
	 */

	@Test
	public void convertLutefiskXP() {

		String args[] = {"convert","data"+ File.separator +"Qtof_ELVISLIVESK.lut","data"+File.separator+"convertedLutefisk.dnmso"};
		ConvertCommand convertCommand = new ConvertCommand();
		convertCommand.setArgs(args);
		DNMSO DNMSO = convertCommand.execute();
		Assert.assertEquals(4, DNMSO.getPrediction().size());

	}
	@Test
	public void convertPepXML() {

		String args[] = {"convert","data"+ File.separator +"denovopeptides.xml","data"+File.separator+"convertedPepXML.dnmso"};
		ConvertCommand convertCommand = new ConvertCommand();
		convertCommand.setArgs(args);
		DNMSO dnmso = convertCommand.execute();
		Assert.assertEquals(260, dnmso.getPrediction().size());

	}

	 @Test
	 public void convertPepNovo(){
			String args[] = {"convert","data"+ File.separator +"pepnovoplus_output.txt","data"+File.separator+"convertedPepNovo.dnmso","data"+File.separator+"data.mzXML"};
			ConvertCommand convertCommand = new ConvertCommand();
			convertCommand.setArgs(args);
			DNMSO dnmso = convertCommand.execute();
			Assert.assertEquals(60, dnmso.getPrediction().size());
	 }

}
