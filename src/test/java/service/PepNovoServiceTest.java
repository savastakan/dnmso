package service;

import domain.DNMSO;
import domain.DNMSOFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class PepNovoServiceTest {
	@Test
	public void read() {
		DNMSOFactory dnmsoFactory = new DNMSOFactory();
		DNMSO targetDNMSO = dnmsoFactory.createDNMSO();
		PepNovoService pepNovoService = new PepNovoService();
		String[] pepNovoArgs = {"read","-p","data"+ File.separator +"pepnovoplus_output.txt","-s","data"+ File.separator +"data.mzXML","-n","2"};
		targetDNMSO = pepNovoService.run(targetDNMSO,pepNovoArgs);
		Assert.assertEquals(2, targetDNMSO.getPrediction().size());
		
		targetDNMSO = dnmsoFactory.createDNMSO();
		String[] pepNovoArgs2 = {"read","-p","data"+ File.separator +"pepnovo_results.txt","-s","data"+ File.separator +"data.mzXML","-n","2"};
		targetDNMSO = pepNovoService.run(targetDNMSO,pepNovoArgs2);
		Assert.assertEquals(2, targetDNMSO.getPrediction().size());

		
		targetDNMSO = dnmsoFactory.createDNMSO();
		String[] pepNovoArgs4 = {"read","-p","data"+ File.separator +"pepnovo_results.txt","-s","data"+ File.separator +"data.mzXML"};
		targetDNMSO = pepNovoService.run(targetDNMSO,pepNovoArgs4);
		Assert.assertEquals(4, targetDNMSO.getPrediction().size());
	}
	@Test
	public void validation() {
		PepNovoService PepNovoService= new PepNovoService();
		boolean isValid = PepNovoService.isValid(new File( "data"+ File.separator +"pepnovoplus_output.txt" ));
		Assert.assertTrue(isValid);
		isValid = PepNovoService.isValid(new File( "data"+ File.separator +"data.mzXML" ));
		Assert.assertFalse(isValid);
		isValid = PepNovoService.isValid(new File( "data"+ File.separator +"Qtof_ELVISLIVESK.lut" ));
		Assert.assertFalse(isValid);
		isValid = PepNovoService.isValid(new File( "data"+ File.separator +"PepNovo1.dnml" ));
		Assert.assertFalse(isValid);
		isValid = PepNovoService.isValid(new File( "data"+ File.separator +"tiny.pwiz.mzML" ));
		Assert.assertFalse(isValid);
	}
}
