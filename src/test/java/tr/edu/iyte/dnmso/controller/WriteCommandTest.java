package tr.edu.iyte.dnmso.controller;



import org.junit.Assert;
import org.junit.Test;
import tr.edu.iyte.dnmso.domain.DNMSO;

import java.io.File;
public class WriteCommandTest {

	@Test
	public void write() {	
		String args[] = {"read","data"+ File.separator +"Qtof_ELVISLIVESK.lut"};
		ReadCommand readCommand = new ReadCommand();
		readCommand.setArgs(args);
		DNMSO DNMSO = readCommand.execute();
		Assert.assertEquals(4, DNMSO.getPredictions().get(0).getPrediction().size());

		WriteCommand writeCommand = new WriteCommand();
		String[] targetArgs = {"write","data"+ File.separator + "LutefiskXP.dnmso"}; //targetPath : getArgs()[2]
    	writeCommand.setArgs(targetArgs);	
    	writeCommand.setContainer(DNMSO);
		DNMSO = writeCommand.execute();
		Assert.assertEquals(4, DNMSO.getPredictions().get(0).getPrediction().size());

	}

}
