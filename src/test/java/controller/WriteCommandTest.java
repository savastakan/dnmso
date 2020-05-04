package controller;



import domain.DNMSO;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
public class WriteCommandTest {

	@Test
	public void write() {	
		String args[] = {"read","data"+ File.separator +"Qtof_ELVISLIVESK.lut"};
		ReadCommand readCommand = new ReadCommand();
		readCommand.setArgs(args);
		DNMSO dnmso = readCommand.execute();
		Assert.assertEquals(4, dnmso.getPrediction().size());

		WriteCommand writeCommand = new WriteCommand();
		String[] targetArgs = {"write","data"+ File.separator + "LutefiskXP.dnmso"}; //targetPath : getArgs()[2]
    	writeCommand.setArgs(targetArgs);	
    	writeCommand.setDNMSO(dnmso);
		dnmso = writeCommand.execute();
		Assert.assertEquals(4, dnmso.getPrediction().size());

	}

}
