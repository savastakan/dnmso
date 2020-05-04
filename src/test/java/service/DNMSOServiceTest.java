package service;

import domain.DNMSO;
import domain.DNMSOFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class DNMSOServiceTest {

	@Test
	public void read(){
		DNMSOFactory dnmsoFactory = new DNMSOFactory();
		DNMSOService dnmsoService = new DNMSOService();
		DNMSO readDNMSO = dnmsoFactory.createDNMSO();
        String[] args = {"read","-p","data"+File.separator +"deneme.obo"};
		readDNMSO = (DNMSO) dnmsoService.run(readDNMSO,args);
		Assert.assertEquals(1, readDNMSO.getPrediction().size());
	}
	@Test
	public void write() {
		DNMSOFactory dnmsoFactory = new DNMSOFactory();
		DNMSO targetDNMSO = dnmsoFactory.createDNMSO();
		LutefiskXPService lutefiskXPService = new LutefiskXPService();
		String[] lutefiskXPArgs = {"read", "-p","data"+File.separator +"Qtof_ELVISLIVESK.lut"};
		targetDNMSO =(DNMSO) lutefiskXPService.run(targetDNMSO,lutefiskXPArgs);
		DNMSOService dnmsoService = new DNMSOService();
		dnmsoService.run(targetDNMSO,new String[] {"write","-o","data"+File.separator +"deneme.obo"});
		DNMSO readDNMSO = dnmsoFactory.createDNMSO();
		readDNMSO = (DNMSO) dnmsoService.run(readDNMSO,new String[] {"read","-p","data"+File.separator +"deneme.obo"});
		Assert.assertEquals(1, readDNMSO.getPrediction().size());
	}
	@Test
	public void validation(){
		DNMSOService dnmsoService = new DNMSOService();
		boolean result = dnmsoService.isValid(new File("data"+File.separator +"deneme.obo"));
		Assert.assertTrue(result);
	    result = dnmsoService.isValid(new File("data"+ File.separator +"PepNovo.dnml"));
		Assert.assertFalse(result);
        result = dnmsoService.isValid(new File("data"+ File.separator +"Qtof_ELVISLIVESK.lut"));
		Assert.assertFalse(result);
        result = dnmsoService.isValid(new File("data"+ File.separator +"tiny.pwiz.mzML"));
		Assert.assertFalse(result);
        result = dnmsoService.isValid(new File("data"+ File.separator +"data.mzXML"));
		Assert.assertFalse(result);
        result = dnmsoService.isValid(new File("data"+ File.separator +"pepnovo_results.txt"));
		Assert.assertFalse(result);
	}

}
