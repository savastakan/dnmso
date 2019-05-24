/*
 * 
 */
package tr.edu.iyte.dnmso.service;

import tr.edu.iyte.dnmso.domain.DNMSO;
import tr.edu.iyte.dnmso.domain.Spectrum;
import uk.ac.ebi.pride.tools.jmzreader.JMzReaderException;
import uk.ac.ebi.pride.tools.mzxml_parser.MzXMLFile;
import uk.ac.ebi.pride.tools.mzxml_parser.MzXMLParsingException;

import java.io.File;
import java.util.List;

import mslib.peak.Peak;
import mslib.spectrum.MassSpectrum;



/**
 * The Class MzXmlService.
 * @author Savas Takan
 * @version $Revision: 1.0 $
 */
public class MzXmlService extends AbstractService {

	/**
	 * Gets the specta.
	 *
	
	 * @return the DNMSO */
	private DNMSO read(){
    	MzXMLFile mzxmlFile;
    	
		try {
			String spectrumFilePath = getProperties().get(ServiceTag.SPECTRA_FILE_PATH.toString());
			File spectrumFile = new File(spectrumFilePath);
			mzxmlFile = new MzXMLFile(spectrumFile);
            List<String> indexList=null;
            String indexes = getProperties().get(ServiceTag.SPECTRUM_INDEX.toString());
            if (indexes !=null){
                indexList = createIndexList(indexes);
            }

			if (indexList==null) indexList = mzxmlFile.getSpectraIds();
            indexList = getNotFindingSpectrumIds(spectrumFile.getName(),indexList);
            for (String index : indexList){
                    try{
                        uk.ac.ebi.pride.tools.jmzreader.model.Spectrum mzxmlSpectrum = mzxmlFile.getSpectrumById(index);
                        createSpectrum(mzxmlSpectrum);
                    } catch (JMzReaderException e) {
                       // System.out.println("index:"+index);
                    }
            }


		} catch (MzXMLParsingException e) {
			System.out.println("mzXML parse error : MzXMLParsingException ");
		}
        return (DNMSO) getContainer();
    }
	
	/**
	 * Convert csv format.
	 *
	 * @param mzxmlSpectrum the mzxml spectrum
	
	 * @return the spectrum */
	private DNMSO createSpectrum(uk.ac.ebi.pride.tools.jmzreader.model.Spectrum mzxmlSpectrum){
	
		String spectrumFilePath = getProperties().get(ServiceTag.SPECTRA_FILE_PATH.toString());
		File spectrumFile = new File(spectrumFilePath);
		DNMSO dnmso = (DNMSO) getContainer();
		MassSpectrum spectrum = new MassSpectrum();
		for (Double mz : mzxmlSpectrum.getPeakList().keySet()) {
		    Peak peak = new Peak();
		    peak.set(mz,mzxmlSpectrum.getPeakList().get(mz));
		    spectrum.add(peak);
		}		
		spectrum.setPrecursorMass(mzxmlSpectrum.getPrecursorMZ());
		spectrum.setPrecursorIntensity(mzxmlSpectrum.getPrecursorIntensity());

		spectrum.setScanNumber(mzxmlSpectrum.getId());
		spectrum.setFileName(spectrumFile.getName());
		dnmso.getSpectra().addSpectrum(spectrum);
		setContainer(dnmso);
		return dnmso;
	}




//read 1,,2,3,4
	/* (non-Javadoc)
   * @see tr.edu.iyte.dnmso.service.Service#run(java.lang.Object, java.lang.String)
   */
    public DNMSO run(Object targetDnml,String[] args) {
    	processSettings((DNMSO)targetDnml, args);
  
    	if (getProperties().get("command").equals("read")) return read();
		return null;
	}

	/* (non-Javadoc)
	 * @see tr.edu.iyte.dnmso.service.Service#isValid(java.io.File)
	 */
	public boolean isValid(File mzXML) {
		try {
			MzXMLFile mzXMLFile = new MzXMLFile(mzXML);
			if(mzXMLFile.getSpectraCount()>0)return true;
			return false;
		} catch (MzXMLParsingException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}


	/* 
	 * @see tr.edu.iyte.dnmso.service.Service#getServiceName()
	 */
	public String getServiceName() {
		return "MzXmlService";
	}
}
