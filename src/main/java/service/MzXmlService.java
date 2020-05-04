
package service;

import domain.DNMSO;
import domain.Spectra;
import domain.Spectrum;
import uk.ac.ebi.pride.tools.jmzreader.JMzReaderException;
import uk.ac.ebi.pride.tools.mzxml_parser.MzXMLFile;
import uk.ac.ebi.pride.tools.mzxml_parser.MzXMLParsingException;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MzXmlService extends AbstractService {

	private DNMSO read(){
    	MzXMLFile mzxmlFile;
    	Spectra spectra = new Spectra();
    	spectra.setSpectrum(new LinkedList<>());
    	DNMSO dnmso = getDNMSO();
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
            for (String index : indexList){
                    try{
                        uk.ac.ebi.pride.tools.jmzreader.model.Spectrum mzxmlSpectrum = mzxmlFile.getSpectrumById(index);
                        Spectrum spectrum = createSpectrum(mzxmlSpectrum);
						spectra.getSpectrum().add(spectrum);
                    } catch (JMzReaderException e) {
                       // System.out.println("index:"+index);
                    }
            }
		} catch (MzXMLParsingException e) {
			System.out.println("mzXML parse error : MzXMLParsingException ");
		}
		dnmso.getSpectra().add(spectra);
        return dnmso;
    }

	private Spectrum createSpectrum(uk.ac.ebi.pride.tools.jmzreader.model.Spectrum mzxmlSpectrum){
		Spectrum spectrum = new Spectrum();
		String csvData = "";
		for (Double mz : mzxmlSpectrum.getPeakList().keySet()) {
			csvData = csvData.concat(mz + ","	+ mzxmlSpectrum.getPeakList().get(mz)+ ";");
		}
		spectrum.setCsvData(csvData);
		spectrum.setPrecursorMZ(mzxmlSpectrum.getPrecursorMZ());
		spectrum.setPrecursorIntensity(mzxmlSpectrum.getPrecursorIntensity());

		spectrum.setScanId(Integer.parseInt(mzxmlSpectrum.getId()));
		spectrum.setSpectrumId(mzxmlSpectrum.getId());
		return spectrum;
	}




//read 1,,2,3,4
    public DNMSO run(DNMSO targetDnml, String[] args) {
    	processSettings(targetDnml, args);
  
    	if (getProperties().get("command").equals("read")) return read();
		return null;
	}

	/* (non-Javadoc)
	 * @see tr.edu.iyte.dnmso.service.Service#isValid(java.io.File)
	 */
	public boolean isValid(File mzXML) {
		try {
			MzXMLFile mzXMLFile = new MzXMLFile(mzXML);
			return mzXMLFile.getSpectraCount() > 0;
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
