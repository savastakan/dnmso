package tr.edu.iyte.dnmso.service;

import junit.framework.Assert;
import org.junit.Test;
import tr.edu.iyte.dnmso.domain.DNMSO;
import tr.edu.iyte.dnmso.domain.DnmsoFactory;

import java.io.File;

public class DnmsoOboServiceTest {

	@Test
	public void read(){
		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		DnmsoOboService dnmsoOboService = new DnmsoOboService();
		DNMSO readDNMSO = dnmsoFactory.createDnmso();
        String[] args = {"read","-p","data"+File.separator +"deneme.obo"};
		readDNMSO = (DNMSO) dnmsoOboService.run(readDNMSO,args);
		Assert.assertEquals(1, readDNMSO.getPredictions().size());
	}
	@Test
	public void write() {
		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		DNMSO targetDNMSO = dnmsoFactory.createDnmso();
		LutefiskXPService lutefiskXPService = new LutefiskXPService();
		String[] lutefiskXPArgs = {"read", "-p","data"+File.separator +"Qtof_ELVISLIVESK.lut"};
		targetDNMSO =(DNMSO) lutefiskXPService.run(targetDNMSO,lutefiskXPArgs);
		DnmsoOboService dnmsoOboService = new DnmsoOboService();
		dnmsoOboService.run(targetDNMSO,new String[] {"write","-o","data"+File.separator +"deneme.obo"});
		DNMSO readDNMSO = dnmsoFactory.createDnmso();
		readDNMSO = (DNMSO) dnmsoOboService.run(readDNMSO,new String[] {"read","-p","data"+File.separator +"deneme.obo"});
		Assert.assertEquals(1, readDNMSO.getPredictions().size());
	}
	@Test
	public void validation(){
		DnmsoOboService dnmsoOboService = new DnmsoOboService();
		boolean result = dnmsoOboService.isValid(new File("data"+File.separator +"deneme.obo"));
		Assert.assertEquals(true, result);
	    result = dnmsoOboService.isValid(new File("data"+ File.separator +"PepNovo.dnml"));
        Assert.assertEquals(false, result);
        result = dnmsoOboService.isValid(new File("data"+ File.separator +"Qtof_ELVISLIVESK.lut"));
        Assert.assertEquals(false, result);
        result = dnmsoOboService.isValid(new File("data"+ File.separator +"tiny.pwiz.mzML"));
        Assert.assertEquals(false, result);
        result = dnmsoOboService.isValid(new File("data"+ File.separator +"data.mzXML"));
        Assert.assertEquals(false, result);
        result = dnmsoOboService.isValid(new File("data"+ File.separator +"pepnovo_results.txt"));
        Assert.assertEquals(false, result);
	}

}
