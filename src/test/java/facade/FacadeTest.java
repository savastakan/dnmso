package facade;



import domain.DNMSO;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class FacadeTest {

	@Test
	public void read() {
		FacadeFactory facadeFactory = new FacadeFactory();
		String[] args = {"read","data"+ File.separator +"convertedLutefisk.dnmso"};
		DNMSO dnmso = facadeFactory.getFacade().handle(args);
		Assert.assertEquals(1, dnmso.getSpectra().iterator().next().getSpectrum().size());
	}
	@Test
	public void write() {
		FacadeFactory facadeFactory = new FacadeFactory();
		String[] readArgs = {"read","data"+ File.separator +"Qtof_ELVISLIVESK.lut"};
		DNMSO dnmso = facadeFactory.getFacade().handle(readArgs);
		Assert.assertEquals(1, dnmso.getSpectra().iterator().next().getSpectrum().size());

		String[] writeArgs = {"write","data"+File.separator+"facadeWriteTest.dnmso"};
		dnmso = facadeFactory.getFacade().handle(dnmso,writeArgs);
		Assert.assertEquals(1, dnmso.getSpectra().iterator().next().getSpectrum().size());

	}
	@Test
	public void merge() {
		FacadeFactory facadeFactory = new FacadeFactory();
		String args[] = {"merge","data"+ File.separator +"denovopeptides.xml",
				"data"+ File.separator +"Qtof_ELVISLIVESK.lut",
				"data"+ File.separator +"mergedFacadeTest.dnmso"};
		DNMSO dnmso = facadeFactory.getFacade().handle(args);
		Assert.assertEquals(261, dnmso.getSpectra().iterator().next().getSpectrum().size());
		Assert.assertEquals(260, dnmso.getPrediction().size());

	}
	@Test
	public void convert() {

		String args[] = {"convert","data"+ File.separator +"Qtof_ELVISLIVESK.lut","data"+File.separator+"convertFacade.dnmso"};
		FacadeFactory facadeFactory = new FacadeFactory();
		DNMSO dnmso = facadeFactory.getFacade().handle(args);
		System.out.println(dnmso.getPrediction().size());
		Assert.assertEquals(4, dnmso.getPrediction().size());

	}
	@Test
	public void validate(){
		FacadeFactory facadeFactory = new FacadeFactory();
		String args[] = {"validate","data"+ File.separator +"PepNovo.dnmso"};
		DNMSO dnmso = facadeFactory.getFacade().handle(args);
		Assert.assertNotNull(dnmso);
	}
}
