/*
 * 
 */
package service;

import domain.DNMSO;
import domain.DNMSOFactory;
import domain.Spectra;
import domain.Spectrum;
import org.xml.sax.SAXException;
import uk.ac.ebi.pride.tools.jmzreader.JMzReaderException;
import uk.ac.ebi.pride.tools.mzml_wrapper.MzMlWrapper;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MzMLService extends AbstractService{

	private Spectrum createSpectrum(uk.ac.ebi.pride.tools.jmzreader.model.Spectrum mzmlSpectrum ){
		Map< Double, Double > peakLists = mzmlSpectrum.getPeakList();
		Spectrum spectrum = new Spectrum();
		String csvData = "";
		for (double key :peakLists.keySet()){
			csvData = csvData.concat(key + "," + peakLists.get(key) + ";");
		}
		spectrum.setCsvData(csvData);

		if (mzmlSpectrum.getPrecursorMZ()!=null) spectrum.setPrecursorMZ(mzmlSpectrum.getPrecursorMZ());
		if (mzmlSpectrum.getPrecursorIntensity()!=null) spectrum.setPrecursorIntensity(mzmlSpectrum.getPrecursorIntensity());
		if (mzmlSpectrum.getId()!=null) spectrum.setScanId(Integer.parseInt(mzmlSpectrum.getId().split("=")[1]));
		spectrum.setSpectrumId(mzmlSpectrum.getId());
		return spectrum;
	}

	private DNMSO read(){
		MzMlWrapper wrapper;
		DNMSO dnmso = getDNMSO();
		if (dnmso.getSpectra() != null){
			dnmso.setSpectra(new LinkedList<>());
		}
		String spectrumFilePath = getProperties().get("-s");
		File spectrumFile = new File(spectrumFilePath);
		Spectra spectra = new Spectra();
		spectra.setSpectrum(new LinkedList<>());

		try {
			List<String> indexList =null;
			String indexes =  getProperties().get("-i");
			if (indexes!=null) {
				indexList = createIndexList(indexes);
			}

			wrapper = new MzMlWrapper(spectrumFile);
			if (indexList!=null){
				for (String id : indexList) {
					uk.ac.ebi.pride.tools.jmzreader.model.Spectrum mzmlSpectrum = wrapper.getSpectrumById("scan="+id);
					Spectrum spectrum = createSpectrum(mzmlSpectrum);
					spectra.getSpectrum().add(spectrum);
				}
			} else {
				List<String> ids =wrapper.getSpectraIds();
				for (String id : ids) {
					uk.ac.ebi.pride.tools.jmzreader.model.Spectrum mzmlSpectrum = wrapper.getSpectrumById(id);
					Spectrum spectrum = createSpectrum(mzmlSpectrum);
					spectra.getSpectrum().add(spectrum);
				}
			}
		} catch (JMzReaderException e) {
			e.printStackTrace();
		}
		dnmso.getSpectra().add(spectra);
		setDNMSO(dnmso);
		return dnmso;
	}




	/*
    * @see tr.edu.iyte.dnmso.service.Service#run(java.lang.Object, java.lang.String[])
    */
public DNMSO run(DNMSO targetDnml, String[] args) {
	processSettings(targetDnml, args);
	if (getProperties().get(ServiceTag.COMMAND.toString()).equals("read")) return read();
        return null;
	}


/* 
 * @see tr.edu.iyte.dnmso.service.Service#isValid(java.io.File)
 */
public boolean isValid(File mzML) {
	boolean isValid = true;
	  try {
		    // Lookup a factory for the W3C XML Schema language
		    SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		    // Compile the schemas.
		    Schema schema = factory.newSchema(this.getClass().getResource("/mzML1.1.1-idx.xsd"));
		    Validator validator = schema.newValidator();
		    // load the file to validate
		    Source source = new StreamSource(mzML);

		    // check the document
		    validator.validate(source);

	    }catch (SAXException ex) {
	    	isValid = false;
	    } catch (Exception ex) {
	      ex.printStackTrace();
	    }
	return isValid;
}


/* 
 * @see tr.edu.iyte.dnmso.service.Service#getServiceName()
 */
public String getServiceName() {
	// TODO Auto-generated method stub
	return "MzMLService";
}
}
