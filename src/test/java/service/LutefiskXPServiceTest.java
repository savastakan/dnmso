package service;


import domain.DNMSO;
import domain.DNMSOFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class LutefiskXPServiceTest {

	@Test
	public void read() {
		DNMSOFactory dnmsoFactory = new DNMSOFactory();
		LutefiskXPService lutefiskXPService = new LutefiskXPService();
		DNMSO targetDNMSO = dnmsoFactory.createDNMSO();
		String[] lutefiskXPArgs = {"read", "-p","data/Qtof_ELVISLIVESK.lut","-n","2"};
		targetDNMSO = lutefiskXPService.run(targetDNMSO,lutefiskXPArgs);
		Assert.assertEquals(2, targetDNMSO.getPrediction().size());
		
		targetDNMSO = dnmsoFactory.createDNMSO();
		String[] lutefiskXPArgs2 = {"read","-p", "data/Qtof_ELVISLIVESK.lut"};
		targetDNMSO =(DNMSO) lutefiskXPService.run(targetDNMSO,lutefiskXPArgs2);
		Assert.assertEquals(4, targetDNMSO.getPrediction().size());
		
	}
	@Test
	public void validation() {
		LutefiskXPService lutefiskXPService = new LutefiskXPService();
		boolean isValid = lutefiskXPService.isValid(new File("data/Qtof_ELVISLIVESK.lut"));
		Assert.assertTrue(isValid);
		isValid = lutefiskXPService.isValid(new File("data/PepNovo1.dnml"));
		Assert.assertFalse(isValid);
		isValid = lutefiskXPService.isValid(new File("data/tiny.pwiz.mzML"));
		Assert.assertFalse(isValid);
		isValid = lutefiskXPService.isValid(new File("data/data.mzXML"));
		Assert.assertFalse(isValid);
		isValid = lutefiskXPService.isValid(new File("data/pepnovo_results.txt"));
		Assert.assertFalse(isValid);
	}

}
