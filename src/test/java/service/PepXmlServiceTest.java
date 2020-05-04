package service;


import domain.DNMSO;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class PepXmlServiceTest {
    @Test
    public void read() {
        DNMSO dnmso = null;
        PepXMLService pepXMLService = new PepXMLService();
        String[] args = {"read","-p","data"+ File.separator +"denovopeptides.xml","-n","5"};
        dnmso = pepXMLService.run(dnmso,args );
        Assert.assertEquals(5, dnmso.getPrediction().size());
        String[] args2 = {"read","-p","data"+ File.separator +"denovopeptides.xml"};
        dnmso =  pepXMLService.run(dnmso,args2 );
        Assert.assertEquals(265, dnmso.getPrediction().size());
    }
    @Test
    public void validation(){
		PepXMLService pepXMLService = new PepXMLService();
		boolean result = pepXMLService.isValid(new File("data"+ File.separator +"denovopeptides.xml"));
        Assert.assertTrue(result);
		result = pepXMLService.isValid(new File("data"+ File.separator +"Qtof_ELVISLIVESK.lut"));
        Assert.assertFalse(result);
		result = pepXMLService.isValid(new File("data"+ File.separator +"tiny.pwiz.mzML"));
        Assert.assertFalse(result);
		result = pepXMLService.isValid(new File("data"+ File.separator +"data.mzXML"));
        Assert.assertFalse(result);
		result = pepXMLService.isValid(new File("data"+ File.separator +"pepnovo_results.txt"));
        Assert.assertFalse(result);
    }

}
