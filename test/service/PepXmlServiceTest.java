package service;


import junit.framework.Assert;
import org.junit.Test;
import domain.DNMSO;

import java.io.File;

public class PepXmlServiceTest {
    @Test
    public void read() {
        DNMSO dnmso = null;
        PepXMLService pepXMLService = new PepXMLService();
        String[] args = {"read","-p","data"+ File.separator +"denovopeptides.xml","-n","5"};
        dnmso = (DNMSO) pepXMLService.run(dnmso,args );
        Assert.assertEquals(5, dnmso.getPredictions().get(0).getPrediction().size());
        String[] args2 = {"read","-p","data"+ File.separator +"denovopeptides.xml"};
        dnmso = (DNMSO) pepXMLService.run(dnmso,args2 );
        Assert.assertEquals(260, dnmso.getPredictions().get(1).getPrediction().size());
    }
    @Test
    public void validation(){
		PepXMLService pepXMLService = new PepXMLService();
		boolean result = pepXMLService.isValid(new File("data"+ File.separator +"denovopeptides.xml"));
		Assert.assertEquals(true, result);
		result = pepXMLService.isValid(new File("data"+ File.separator +"Qtof_ELVISLIVESK.lut"));
		Assert.assertEquals(false, result);
		result = pepXMLService.isValid(new File("data"+ File.separator +"tiny.pwiz.mzML"));
		Assert.assertEquals(false, result);
		result = pepXMLService.isValid(new File("data"+ File.separator +"data.mzXML"));
		Assert.assertEquals(false, result);
		result = pepXMLService.isValid(new File("data"+ File.separator +"pepnovo_results.txt"));
		Assert.assertEquals(false, result);
    }

}
