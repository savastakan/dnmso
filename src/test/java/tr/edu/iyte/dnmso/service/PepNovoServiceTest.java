package tr.edu.iyte.dnmso.service;

import org.junit.Assert;
import org.junit.Test;
import tr.edu.iyte.dnmso.domain.DNMSO;
import tr.edu.iyte.dnmso.domain.DnmsoFactory;

import java.io.File;

public class PepNovoServiceTest {
	@Test
	public void read() {
		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		DNMSO targetDNMSO = dnmsoFactory.createDnmso();
		PepNovoService pepNovoService = new PepNovoService();
		String[] pepNovoArgs = {"read","-p","data"+ File.separator +"pepnovoplus_output.txt","-s","data"+ File.separator +"data.mzXML","-n","2"};
		targetDNMSO = pepNovoService.run(targetDNMSO,pepNovoArgs);
		Assert.assertEquals(2, targetDNMSO.getPredictions().get(0).getPrediction().size());
		
		targetDNMSO = dnmsoFactory.createDnmso();
		String[] pepNovoArgs2 = {"read","-p","data"+ File.separator +"pepnovo_results.txt","-s","data"+ File.separator +"data.mzXML","-n","2"};
		targetDNMSO = pepNovoService.run(targetDNMSO,pepNovoArgs2);
		Assert.assertEquals(2, targetDNMSO.getPredictions().get(0).getPrediction().size());
		
		targetDNMSO = dnmsoFactory.createDnmso();
		String[] pepNovoArgs3 = {"read","-p","data"+ File.separator +"pepnovoplus_output.txt","-s","data"+ File.separator +"data.mzXML"};
		targetDNMSO = pepNovoService.run(targetDNMSO,pepNovoArgs3);
		Assert.assertEquals(60, targetDNMSO.getPredictions().get(0).getPrediction().size());
		
		targetDNMSO = dnmsoFactory.createDnmso();
		String[] pepNovoArgs4 = {"read","-p","data"+ File.separator +"pepnovo_results.txt","-s","data"+ File.separator +"data.mzXML"};
		targetDNMSO = pepNovoService.run(targetDNMSO,pepNovoArgs4);
		Assert.assertEquals(4, targetDNMSO.getPredictions().get(0).getPrediction().size());
	}
	@Test
	public void validation() {
		PepNovoService PepNovoService= new PepNovoService();
		boolean isValid = PepNovoService.isValid(new File( "data"+ File.separator +"pepnovoplus_output.txt" ));
		Assert.assertEquals(true, isValid);
		isValid = PepNovoService.isValid(new File( "data"+ File.separator +"data.mzXML" ));
		Assert.assertEquals(false, isValid);
		isValid = PepNovoService.isValid(new File( "data"+ File.separator +"Qtof_ELVISLIVESK.lut" ));
		Assert.assertEquals(false, isValid);
		isValid = PepNovoService.isValid(new File( "data"+ File.separator +"PepNovo1.dnml" ));
		Assert.assertEquals(false, isValid);
		isValid = PepNovoService.isValid(new File( "data"+ File.separator +"tiny.pwiz.mzML" ));
		Assert.assertEquals(false, isValid);
	}
}
