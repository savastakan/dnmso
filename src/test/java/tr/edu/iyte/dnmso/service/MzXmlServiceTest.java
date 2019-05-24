package tr.edu.iyte.dnmso.service;



import org.junit.Assert;
import org.junit.Test;
import tr.edu.iyte.dnmso.domain.DNMSO;
import tr.edu.iyte.dnmso.domain.DnmsoFactory;

import java.io.File;

public class MzXmlServiceTest {

	@Test
	public void readbyID() {
		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		DNMSO DNMSO = dnmsoFactory.createDnmso();
		String[] args = {"read","-s","data"+ File.separator +"data.mzXML","-i","1"};
		MzXmlService mzXmlService = new MzXmlService();
		DNMSO = mzXmlService.run(DNMSO, args);
		Assert.assertEquals(1, DNMSO.getSpectra().getSpectrum().size());
	}
	@Test
	public void readbyIDs() {
		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		DNMSO DNMSO = dnmsoFactory.createDnmso();
		String[] args = {"read","-s","data"+ File.separator +"data.mzXML","-i","1,2"};
		MzXmlService mzXmlService = new MzXmlService();
		DNMSO = mzXmlService.run(DNMSO, args);
		Assert.assertEquals(2, DNMSO.getSpectra().getSpectrum().size());
	}
	@Test
	public void readbyAll() {
		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		DNMSO DNMSO = dnmsoFactory.createDnmso();
		String[] args = {"read","-s","data"+ File.separator +"data.mzXML"};
		MzXmlService mzXmlService = new MzXmlService();
		DNMSO = mzXmlService.run(DNMSO, args);
		Assert.assertEquals(386, DNMSO.getSpectra().getSpectrum().size());
	}

	@Test
	public void readbyInterval() {
		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		DNMSO DNMSO = dnmsoFactory.createDnmso();
		String[] args = {"read","-s","data"+ File.separator +"data.mzXML","-i","1,,10,11"};
		MzXmlService mzXmlService = new MzXmlService();
		DNMSO = mzXmlService.run(DNMSO, args);
		Assert.assertEquals(11, DNMSO.getSpectra().getSpectrum().size());
	}
	@Test
	public void isValid(){
		MzXmlService mzXmlService = new MzXmlService();
		boolean isValid = mzXmlService.isValid(new File("data"+ File.separator +"data.mzXML"));
		Assert.assertEquals(true, isValid);
		isValid = mzXmlService.isValid(new File("data"+ File.separator +"Qtof_ELVISLIVESK.lut"));
		Assert.assertEquals(false, isValid);
		isValid = mzXmlService.isValid(new File("data"+ File.separator +"PepNovo1.dnmso"));
		Assert.assertEquals(false, isValid);
		isValid = mzXmlService.isValid(new File("data"+ File.separator +"tiny.pwiz.mzML"));
		Assert.assertEquals(false, isValid);
		isValid = mzXmlService.isValid(new File("data"+ File.separator +"pepnovo_results.txt"));
		Assert.assertEquals(false, isValid);
	}

}
