package tr.edu.iyte.dnmso.service;



import junit.framework.Assert;
import org.junit.Test;
import tr.edu.iyte.dnmso.domain.DNMSO;
import tr.edu.iyte.dnmso.domain.DnmsoFactory;

import java.io.File;

public class DnmlServiceTest {


	@Test
	public void read() {
		DNMSO dnmso =null;
		DnmlService dnmlService = new DnmlService();
		String[] args = {"read","data"+ File.separator +"test.dnml"};
        dnmso = dnmlService.run(dnmso,args );
		Assert.assertEquals(4, dnmso.getPredictions().get(0).getPrediction().size());
	}
    /**
     * Test of write method, of class mzMlFile.
     */
    @Test
    public void write() {
    	DnmsoFactory dnmsoFactory = new DnmsoFactory();
    	DNMSO dnmso = dnmsoFactory.createDnmso();
    	
		DnmlService dnmlService = new DnmlService();
		String[] readArgs = {"read","data"+ File.separator +"test.dnml"};
		DNMSO readDNMSO = dnmlService.run(dnmso,readArgs );

    	String[] writeArgs = {"write","data"+ File.separator +"PepNovoWriteTest.dnml"};
    	DNMSO writeDNMSO =  dnmlService.run(readDNMSO, writeArgs);
        Assert.assertEquals(writeDNMSO.getPredictions().get(0).getPrediction().size(), readDNMSO.getPredictions().get(0).getPrediction().size());

    }
    
	@Test
	public void merge() {
		DnmlService dnmlService = new DnmlService();
		String[] pepNovoArgs = {"read","data"+ File.separator +"PepNovo.dnml"};
		DNMSO pepNovoDNMSO = dnmlService.run(null,pepNovoArgs );
		String[] lutefiskXPArgs = {"read","data"+ File.separator +"LutefiskXP.dnml"};
		DNMSO mergedDNMSO = dnmlService.run(pepNovoDNMSO,lutefiskXPArgs);

    	String[] writeArgs = {"write","data"+File.separator+"merged.dnml"};
    	dnmlService.run(mergedDNMSO, writeArgs);
		Assert.assertEquals(6, mergedDNMSO.getPredictions().get(0).getPrediction().size());

	}
	
	@Test
	public void validation() {
		DnmlService dnmlService = new DnmlService();
		boolean result = dnmlService.isValid(new File("data"+ File.separator +"test.dnml"));
		Assert.assertEquals(true, result);
		result = dnmlService.isValid(new File("data"+ File.separator +"Qtof_ELVISLIVESK.lut"));
		Assert.assertEquals(false, result);
		result = dnmlService.isValid(new File("data"+ File.separator +"tiny.pwiz.mzML"));
		Assert.assertEquals(false, result);
		result = dnmlService.isValid(new File("data"+ File.separator +"data.mzXML"));
		Assert.assertEquals(false, result);
		result = dnmlService.isValid(new File("data"+ File.separator +"pepnovo_results.txt"));
		Assert.assertEquals(false, result);
		
	}

}
