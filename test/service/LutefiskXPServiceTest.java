package service;


import org.junit.Assert;
import org.junit.Test;
import domain.DNMSO;
import domain.DnmsoFactory;

import java.io.File;

public class LutefiskXPServiceTest {

	@Test
	public void read() {
		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		LutefiskXPService lutefiskXPService = new LutefiskXPService();
		DNMSO targetDNMSO = dnmsoFactory.createDnmso();
		String[] lutefiskXPArgs = {"read", "-p","data/Qtof_ELVISLIVESK.lut","-n","2"};
		targetDNMSO =(DNMSO) lutefiskXPService.run(targetDNMSO,lutefiskXPArgs);
		Assert.assertEquals(2, targetDNMSO.getPredictions().get(0).getPrediction().size());
		
		targetDNMSO = dnmsoFactory.createDnmso();
		String[] lutefiskXPArgs2 = {"read","-p", "data/Qtof_ELVISLIVESK.lut"};
		targetDNMSO =(DNMSO) lutefiskXPService.run(targetDNMSO,lutefiskXPArgs2);
		Assert.assertEquals(4, targetDNMSO.getPredictions().get(0).getPrediction().size());
		
	}
	@Test
	public void validation() {
		LutefiskXPService lutefiskXPService = new LutefiskXPService();
		boolean isValid = lutefiskXPService.isValid(new File("data/Qtof_ELVISLIVESK.lut"));
		Assert.assertEquals(true, isValid);
		isValid = lutefiskXPService.isValid(new File("data/PepNovo1.dnml"));
		Assert.assertEquals(false, isValid);
		isValid = lutefiskXPService.isValid(new File("data/tiny.pwiz.mzML"));
		Assert.assertEquals(false, isValid);
		isValid = lutefiskXPService.isValid(new File("data/data.mzXML"));
		Assert.assertEquals(false, isValid);
		isValid = lutefiskXPService.isValid(new File("data/pepnovo_results.txt"));
		Assert.assertEquals(false, isValid);
	}

}
