package service;
import org.junit.Assert;
import org.junit.Test;
import domain.DNMSO;
import domain.DnmsoFactory;

import java.io.File;

public class MzMLServiceTest {

	@Test
	public void readbyID() {
		String[] args = {"read","-s","data"+ File.separator +"tiny.pwiz.mzML","-i","20"};
		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		DNMSO DNMSO = dnmsoFactory.createDnmso();
		MzMLService mzMLService = new MzMLService();	
		DNMSO = (DNMSO) mzMLService.run(DNMSO, args);

		Assert.assertEquals(1, DNMSO.getSpectra().getSpectrum().size());
	}
	@Test
	public void readbyIDs() {
		String[] args = {"read","-s","data"+ File.separator +"tiny.pwiz.mzML","-i","19,20,21"};
		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		DNMSO DNMSO = dnmsoFactory.createDnmso();
		MzMLService mzMLService = new MzMLService();	
		DNMSO = (DNMSO) mzMLService.run(DNMSO, args);
		Assert.assertEquals(3, DNMSO.getSpectra().getSpectrum().size());
	}
	@Test
	public void readbyAll() {
		String[] args = {"read","-s","data"+ File.separator +"tiny.pwiz.mzML"};
		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		DNMSO DNMSO = dnmsoFactory.createDnmso();
		MzMLService mzMLService = new MzMLService();	
		DNMSO = (DNMSO) mzMLService.run(DNMSO, args);
		Assert.assertEquals(4, DNMSO.getSpectra().getSpectrum().size());
	}

	@Test
	public void readbyInterval() {
		String[] args = {"read","-s","data"+ File.separator +"tiny.pwiz.mzML","-i","19,,21"};
		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		DNMSO DNMSO = dnmsoFactory.createDnmso();
		MzMLService mzMLService = new MzMLService();	
		DNMSO = (DNMSO) mzMLService.run(DNMSO, args);
		Assert.assertEquals(3, DNMSO.getSpectra().getSpectrum().size());
	}
	@Test
	public void validation() {
		MzMLService mzMLService = new MzMLService();
		boolean isValid = mzMLService.isValid(new File("data"+ File.separator +"tiny.pwiz.mzML"));
		Assert.assertEquals(true, isValid);
		isValid = mzMLService.isValid(new File("data"+ File.separator +"PepNovo.dnml"));
		Assert.assertEquals(false, isValid);
		isValid = mzMLService.isValid(new File("data"+ File.separator +"Qtof_ELVISLIVESK.lut"));
		Assert.assertEquals(false, isValid);
		isValid = mzMLService.isValid(new File("data"+ File.separator +"data.mzXML"));
		Assert.assertEquals(false, isValid);
		isValid = mzMLService.isValid(new File("data"+ File.separator +"pepnovo_results.txt"));
		Assert.assertEquals(false, isValid);

	}
}
