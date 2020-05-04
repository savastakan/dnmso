package service;



import domain.DNMSO;
import domain.DNMSOFactory;
import domain.Spectra;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class MzXmlServiceTest {

	@Test
	public void readbyID() {
		DNMSOFactory dnmsoFactory = new DNMSOFactory();
		DNMSO DNMSO = dnmsoFactory.createDNMSO();
		String[] args = {"read","-s","data"+ File.separator +"data.mzXML","-i","1"};
		MzXmlService mzXmlService = new MzXmlService();
		DNMSO = mzXmlService.run(DNMSO, args);
		for (Spectra spectra : DNMSO.getSpectra()){
			Assert.assertEquals(1, spectra.getSpectrum().size());
		}

	}
	@Test
	public void readbyIDs() {
		DNMSOFactory dnmsoFactory = new DNMSOFactory();
		DNMSO DNMSO = dnmsoFactory.createDNMSO();
		String[] args = {"read","-s","data"+ File.separator +"data.mzXML","-i","1,2"};
		MzXmlService mzXmlService = new MzXmlService();
		DNMSO = mzXmlService.run(DNMSO, args);
		for (Spectra spectra : DNMSO.getSpectra()){
			Assert.assertEquals(2, spectra.getSpectrum().size());
		}
	}
	@Test
	public void readbyAll() {
		DNMSOFactory dnmsoFactory = new DNMSOFactory();
		DNMSO DNMSO = dnmsoFactory.createDNMSO();
		String[] args = {"read","-s","data"+ File.separator +"data.mzXML"};
		MzXmlService mzXmlService = new MzXmlService();
		DNMSO = mzXmlService.run(DNMSO, args);
		for (Spectra spectra : DNMSO.getSpectra()){
			Assert.assertEquals(386, spectra.getSpectrum().size());
		}
	}

	@Test
	public void isValid(){
		MzXmlService mzXmlService = new MzXmlService();
		boolean isValid = mzXmlService.isValid(new File("data"+ File.separator +"data.mzXML"));
		Assert.assertTrue(isValid);
	}

}
