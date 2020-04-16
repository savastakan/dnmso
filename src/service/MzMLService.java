/*
 * 
 */
package service;

import mslib.Peak;
import mslib.MassSpectrum;

import org.xml.sax.SAXException;

import domain.DNMSO;
import domain.DnmsoFactory;
import domain.Spectrum;
import uk.ac.ebi.pride.tools.jmzreader.JMzReaderException;
import uk.ac.ebi.pride.tools.mzml_wrapper.MzMlWrapper;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import java.io.File;
import java.util.List;
import java.util.Map;


// TODO: Auto-generated Javadoc
/**
 * The Class MzMLService.
 * @author Savas TAKAN
 * @version $Revision: 1.0 $
 */
public class MzMLService extends AbstractService{

	/**
	 * Creates the spectrum.
	 *
	 * @param mzmlSpectrum the mzml spectrum
	 * @return the dNML
	 */
	private DNMSO createSpectrum(uk.ac.ebi.pride.tools.jmzreader.model.Spectrum mzmlSpectrum ){
		DNMSO dnmso = (DNMSO)getContainer();
		String spectrumFilePath = getProperties().get(ServiceTag.SPECTRA_FILE_PATH.toString());
		File file = new File(spectrumFilePath);
		Map< Double, Double > peakLists = mzmlSpectrum.getPeakList();
		MassSpectrum spectrum = new MassSpectrum();
		for (double key :peakLists.keySet()){
			Peak peak = new Peak();
			peak.set(key, peakLists.get(key));
			spectrum.add(peak);
		}
		if (mzmlSpectrum.getPrecursorMZ()!=null) spectrum.setPrecursorMass(mzmlSpectrum.getPrecursorMZ());
		if (mzmlSpectrum.getPrecursorIntensity()!=null) spectrum.setPrecursorIntensity(mzmlSpectrum.getPrecursorIntensity());
		if (mzmlSpectrum.getId()!=null) spectrum.setScanNumber(Integer.parseInt(mzmlSpectrum.getId().split("=")[1]));
		spectrum.setFileName(file.getName());
		dnmso.getSpectra().addSpectrum(spectrum);
		setContainer(dnmso);
		return dnmso;
	}

	/**
	 * Creates the dnmso specta.
	 *
	 * @return the DNMSO
	 */
	private DNMSO read(){
    	MzMlWrapper wrapper;
    	DNMSO dnmso = (DNMSO) getContainer();
    	String spectrumFilePath = getProperties().get("-s");
    	File spectrumFile = new File(spectrumFilePath);
    	
    	try {
            List<String> indexList =null;
            String indexes =  getProperties().get("-i");
            if (indexes!=null) {
                indexList = createIndexList(indexes);
            }

            indexList = getNotFindingSpectrumIds(spectrumFile.getName(),indexList);

			wrapper = new MzMlWrapper(spectrumFile);
            
			if (indexList!=null){
				for (String id : indexList) {
					uk.ac.ebi.pride.tools.jmzreader.model.Spectrum mzmlSpectrum = wrapper.getSpectrumById("scan="+id);
	                //System.out.print(mzmlSpectrum.getId());
	                dnmso = createSpectrum(mzmlSpectrum);

				}
			} else {
				List<String> ids =wrapper.getSpectraIds();
				for (String id : ids) {
					uk.ac.ebi.pride.tools.jmzreader.model.Spectrum mzmlSpectrum = wrapper.getSpectrumById(id);
					dnmso = createSpectrum(mzmlSpectrum);
					
				}
			}
		} catch (JMzReaderException e) {
			e.printStackTrace();
		}
    	setContainer(dnmso);
		return dnmso;
    }




    /* 
    * @see tr.edu.iyte.dnmso.service.Service#run(java.lang.Object, java.lang.String[])
    */
public DNMSO run(Object targetDnml,String[] args) {
	processSettings((DNMSO)targetDnml, args);
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
