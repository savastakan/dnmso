package tr.edu.iyte.dnmso.facade;



import junit.framework.Assert;
import org.junit.Test;
import tr.edu.iyte.dnmso.domain.DNMSO;

import java.io.File;

public class FacadeTest {

	@Test
	public void read() {
		FacadeFactory facadeFactory = new FacadeFactory();
		String[] args = {"read","data"+ File.separator +"convertedLutefisk.dnmso"};
		DNMSO DNMSO = facadeFactory.getFacade().handle(args);
		Assert.assertEquals(1, DNMSO.getSpectra().getSpectrum().size());
	}
	@Test
	public void write() {
		FacadeFactory facadeFactory = new FacadeFactory();
		String[] readArgs = {"read","data"+ File.separator +"Qtof_ELVISLIVESK.lut"};
		DNMSO DNMSO = facadeFactory.getFacade().handle(readArgs);
		Assert.assertEquals(1, DNMSO.getSpectra().getSpectrum().size());

		String[] writeArgs = {"write","data"+File.separator+"facadeWriteTest.dnmso"};
		DNMSO = facadeFactory.getFacade().handle(DNMSO,writeArgs);
		Assert.assertEquals(1, DNMSO.getSpectra().getSpectrum().size());

	}
	@Test
	public void merge() {
		FacadeFactory facadeFactory = new FacadeFactory();
		String args[] = {"merge","data"+ File.separator +"denovopeptides.xml",
				"data"+ File.separator +"Qtof_ELVISLIVESK.lut",
				"data"+ File.separator +"mergedFacadeTest.dnmso"};
		DNMSO DNMSO = facadeFactory.getFacade().handle(args);
		Assert.assertEquals(261, DNMSO.getSpectra().getSpectrum().size());
		Assert.assertEquals(260, DNMSO.getPredictions().get(0).getPrediction().size());

	}
	@Test
	public void convert() {

		String args[] = {"convert","data"+ File.separator +"Qtof_ELVISLIVESK.lut","data"+File.separator+"convertFacade.dnmso"};
		FacadeFactory facadeFactory = new FacadeFactory();
		DNMSO DNMSO = facadeFactory.getFacade().handle(args);
		System.out.println(DNMSO.getPredictions().get(0).getPrediction().size());
		Assert.assertEquals(4, DNMSO.getPredictions().get(0).getPrediction().size());

	}
	@Test
	public void validate(){
		FacadeFactory facadeFactory = new FacadeFactory();
		String args[] = {"validate","data"+ File.separator +"PepNovo.dnmso"};
		DNMSO DNMSO = facadeFactory.getFacade().handle(args);
		Assert.assertNotNull(DNMSO);
	}
}
