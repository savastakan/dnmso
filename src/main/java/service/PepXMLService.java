package service;


import domain.*;
import domain.pepxml.MsmsPipelineAnalysis;
import uk.ac.ebi.pride.tools.jmzreader.JMzReaderException;
import uk.ac.ebi.pride.tools.mzxml_parser.MzXMLFile;
import uk.ac.ebi.pride.tools.mzxml_parser.MzXMLParsingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

/**
 * User: st
 * Date: 06.08.2012
 * Time: 15:28
 * To change this template use File | Settings | File Templates.
 */
public class PepXMLService extends AbstractService {

   
    public String getServiceName() {

        return "PepXML Service";
    }

    
    public DNMSO run(DNMSO container, String[] args) {
    	processSettings(container, args);
    	System.out.println(getProperties());
    	if (getProperties().get(ServiceTag.COMMAND.toString()).equals("read")) return read();
        return null;
    }

    private DNMSO read() {

        String size = getProperties().get(ServiceTag.NUMBER_OF_PREDICTION.toString());
        String path = getProperties().get(ServiceTag.PREDICTION_FILE_PATH.toString());
        
        MsmsPipelineAnalysis msmsPipelineAnalysis;
        try {
            Reader reader = new FileReader(new File(path));

            JAXBContext context = JAXBContext.newInstance(MsmsPipelineAnalysis.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance()
                    .createXMLStreamReader(reader);

            JAXBElement<MsmsPipelineAnalysis> root = unmarshaller.unmarshal(xmlStreamReader,MsmsPipelineAnalysis.class);
            msmsPipelineAnalysis = root.getValue();
            convertMsmsPipelineAnalysisToDNMSO(msmsPipelineAnalysis,size);

        } catch (FileNotFoundException | JAXBException | XMLStreamException | FactoryConfigurationError e) {
            e.printStackTrace();
        }
        return getDNMSO();
    }
    /*
    public Source  addSpectrum(DNMSO.Spectra spectra,String fileName, long scanId){
        Source source= new Source();
            source.setFileName(fileName);
            source.setScanId(String.valueOf(scanId));
         for (Spectrum savedSpectrum : spectra.getSpectrum()){
             if (savedSpectrum.getFileName().equals(fileName) && savedSpectrum.getScanId().equals(scanId)){
                 return source;
             }
         }
         Spectrum spectrum = new Spectrum();
         spectrum.setFileName(fileName);
         spectrum.setScanId(String.valueOf(scanId));
         return source;
    }

    private DNMSO addSpectrum(DNMSO dnmso,String fileName, String scanId){
        for (Spectrum spectrum : dnmso.getSpectra().getSpectrum()){
            if (spectrum.getFileName().equals(fileName)&& spectrum.getScanId().equals(scanId)){
                return dnmso;
            }
        }
             Spectrum spectrum = new Spectrum();
         spectrum.setFileName(fileName);
         spectrum.setScanId(scanId);
         dnmso.getSpectra().getSpectrum().add(spectrum);
         return dnmso;
    }
    */
    private void convertMsmsPipelineAnalysisToDNMSO(MsmsPipelineAnalysis msmsPipelineAnalysis, String size) {
    	Integer numberOfPrediction = null;

        HashMap<String,HashSet<String>> files = new HashMap<>();
    	if (size != null){
    		numberOfPrediction = Integer.parseInt(size);
    	}
    	if (getDNMSO()==null){
            DNMSOFactory dnmsoFactory = new DNMSOFactory();
            setDNMSO(dnmsoFactory.createDNMSO());
        }
    	DNMSO dnmso = this.getDNMSO();
        Software software = new Software();
        for (MsmsPipelineAnalysis.MsmsRunSummary msmsRunSummary : msmsPipelineAnalysis.getMsmsRunSummary()){
            //software

            for (MsmsPipelineAnalysis.MsmsRunSummary.SearchSummary searchSummary :msmsRunSummary.getSearchSummary() ){
            	
            	software.setSoftwareName("Peaks");
            	software.setSoftwareVersion("6.0");
            	software.setSoftwareSetting(new LinkedList<>());
               
                for ( MsmsPipelineAnalysis.MsmsRunSummary.SearchSummary.Parameter parameter : searchSummary.getParameter()){
                    SoftwareSetting setting = new SoftwareSetting();
                    setting.setSoftwareSettingName(parameter.getName());
                    setting.setSoftwareSettingValue(parameter.getValue());
                    software.getSoftwareSetting().add(setting);
                }

                SoftwareSetting setting = new SoftwareSetting();
                setting.setSoftwareSettingName("enzyme");
                setting.setSoftwareSettingValue(msmsRunSummary.getSampleEnzyme().getName());
                software.getSoftwareSetting().add(setting);
            }
            int index =0;
            for (MsmsPipelineAnalysis.MsmsRunSummary.SpectrumQuery spectrumQuery :msmsRunSummary.getSpectrumQuery() ){
            	if (numberOfPrediction!=null){
            		if (numberOfPrediction <= index ){
                		break;
                	}
            	}
                index++;

                for (MsmsPipelineAnalysis.MsmsRunSummary.SpectrumQuery.SearchResult searchResult : spectrumQuery.getSearchResult()) {
                    for (MsmsPipelineAnalysis.MsmsRunSummary.SpectrumQuery.SearchResult.SearchHit searchHit : searchResult.getSearchHit()) {
                    	Prediction prediction = new Prediction();
                    	prediction.setSpectrum(new LinkedList<>());
                        prediction.setScore(new LinkedList<>());
                      	//Scores
                      	Score score = new Score();
                      	score.setScoreName("HitRank");
                      	score.setScoreValue( (double) searchHit.getHitRank());
                      	prediction.getScore().add(score);
                      	
                        //Source
                    	for (long i =spectrumQuery.getStartScan(); i<=spectrumQuery.getEndScan();i++){
                    		Spectrum spectrum = new Spectrum();
                            spectrum.setSpectrumId(spectrumQuery.getSpectrum());
                            spectrum.setScanId(i);
                    		prediction.getSpectrum().add(spectrum);
                            HashSet<String> ids = files.get(spectrumQuery.getSpectrum());
                            if (ids!=null){
                                files.get(spectrumQuery.getSpectrum()).add(String.valueOf(i));
                            }   else {
                                ids = new HashSet<>();
                                ids.add(String.valueOf(i));
                                files.put(spectrumQuery.getSpectrum(),ids);
                            }
                    	}
                    	
                    	// Sequence
                    	Sequence sequence = new Sequence();
                        sequence.setSequenceElement(new LinkedList<>());
                    	sequence.setPeptideSequence(searchHit.getPeptide());
                        // sequence.setCalculatedMass((double) searchHit.getCalcNeutralPepMass());
                        
                        double totalconfidance = 0.0;
                        int sizeOf = 0;
                        for (MsmsPipelineAnalysis.MsmsRunSummary.SpectrumQuery.SearchResult.SearchHit.SearchScore searchScore : searchHit.getSearchScore()) {
                            if (searchScore.getName().equals("PeaksDenovoScore")){
                            	score = new Score();
                              	score.setScoreName(searchScore.getName());
                              	score.setScoreValue(Double.parseDouble(searchScore.getValue()));
                              	prediction.getScore().add(score);
                            }

                            if (searchScore.getName().equals("positional_conf")){
                                StringTokenizer st = new StringTokenizer(searchScore.getValue(), ",");
                                int i=0;
                                while (st.hasMoreTokens()){
                                    String confidence = st.nextToken();
                                    totalconfidance += Double.parseDouble(confidence);
                                    sizeOf++;
                                    AminoAcidFactory aminoAcidFactory = new AminoAcidFactory();
                                    AminoAcid aminoAcid = aminoAcidFactory.createAminoAcid(String.valueOf(searchHit.getPeptide().charAt(i)));
                                    SEAminoAcid seAminoAcid = new SEAminoAcid();
                                    seAminoAcid.setAminoAcid(aminoAcid);
                                    seAminoAcid.setConfidence(Double.parseDouble(confidence));
                                    i++;
                                    seAminoAcid.setPositionInSequence(i);
                                    sequence.getSequenceElement().add(seAminoAcid);
                                }

                            }
                        }
                        totalconfidance =  totalconfidance /sizeOf;
                        sequence.setCombinedConfidence(totalconfidance);
                        prediction.setSequence(sequence);
                        dnmso.getPrediction().add(prediction);
                        }

                    }
                }

            for (String filePath : files.keySet()){
                HashSet<String> ids =  files.get(filePath);

                File file = new File(getProperties().get("-p")) ;
                String indexes = "";
                //System.out.println(ids);
                for (String id : ids) {
                    indexes = indexes.concat(","+id);
                }
                Spectra spectra = createSpectra(file.getParent()+File.separator+filePath,indexes.substring(1));
                dnmso.getSpectra().add(spectra);
            }

            this.setDNMSO(dnmso);
        }
    }

    private Spectra createSpectra(String spectrumFilePath, String indexes){
        MzXMLFile mzxmlFile;
        Spectra spectra = new Spectra();
        spectra.setSpectrum(new LinkedList<>());
        try {
            File spectrumFile = new File(spectrumFilePath);
            mzxmlFile = new MzXMLFile(spectrumFile);
            List<String> indexList=null;
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
        return spectra;
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

    public boolean isValid(File file) {
        MsmsPipelineAnalysis msmsPipelineAnalysis;
        try {
            Reader reader = new FileReader(file);

            JAXBContext context = JAXBContext.newInstance(MsmsPipelineAnalysis.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance()
                    .createXMLStreamReader(reader);

            JAXBElement<MsmsPipelineAnalysis> root = unmarshaller.unmarshal(xmlStreamReader,MsmsPipelineAnalysis.class);
            msmsPipelineAnalysis = root.getValue();
            if (msmsPipelineAnalysis.getMsmsRunSummary().get(0).getSpectrumQuery().size()>0){
            	return true;
            }
            
        } catch (FactoryConfigurationError | Exception e) {
           return false;
        }
        return false;

    }
}
