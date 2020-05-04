package service;
import domain.DNMSO;
import domain.DNMSOFactory;
import domain.Spectra;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class MzMLServiceTest {

	@Test
	public void readbyID() {
		String[] args = {"read","-s","data"+ File.separator +"tiny.pwiz.mzML","-i","20"};
		DNMSOFactory dnmsoFactory = new DNMSOFactory();
		DNMSO DNMSO = dnmsoFactory.createDNMSO();
		MzMLService mzMLService = new MzMLService();	
		DNMSO =  mzMLService.run(DNMSO, args);
		for (Spectra spectra :  DNMSO.getSpectra() ){
			Assert.assertEquals(1, spectra.getSpectrum().size());
		}
	}
	@Test
	public void readbyIDs() {
		String[] args = {"read","-s","data"+ File.separator +"tiny.pwiz.mzML","-i","19,20,21"};
		DNMSOFactory dnmsoFactory = new DNMSOFactory();
		DNMSO DNMSO = dnmsoFactory.createDNMSO();
		MzMLService mzMLService = new MzMLService();	
		DNMSO = mzMLService.run(DNMSO, args);
		for (Spectra spectra :  DNMSO.getSpectra() ){
			Assert.assertEquals(3, spectra.getSpectrum().size());
		}
	}
	@Test
	public void readbyAll() {
		String[] args = {"read","-s","data"+ File.separator +"tiny.pwiz.mzML"};
		DNMSOFactory dnmsoFactory = new DNMSOFactory();
		DNMSO DNMSO = dnmsoFactory.createDNMSO();
		MzMLService mzMLService = new MzMLService();	
		DNMSO = (DNMSO) mzMLService.run(DNMSO, args);
		for (Spectra spectra :  DNMSO.getSpectra() ){
			Assert.assertEquals(4, spectra.getSpectrum().size());
		}
	}

	@Test
	public void validation() {
		MzMLService mzMLService = new MzMLService();
		boolean isValid = mzMLService.isValid(new File("data"+ File.separator +"tiny.pwiz.mzML"));
		Assert.assertTrue(isValid);
		isValid = mzMLService.isValid(new File("data"+ File.separator +"Qtof_ELVISLIVESK.lut"));
		Assert.assertFalse(isValid);
		isValid = mzMLService.isValid(new File("data"+ File.separator +"data.mzXML"));
		Assert.assertFalse(isValid);
		isValid = mzMLService.isValid(new File("data"+ File.separator +"pepnovo_results.txt"));
		Assert.assertFalse(isValid);
	}
}
