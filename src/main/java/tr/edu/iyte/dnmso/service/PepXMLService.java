package tr.edu.iyte.dnmso.service;


import tr.edu.iyte.dnmso.domain.*;
import tr.edu.iyte.dnmso.domain.DNMSO.Predictions;
import tr.edu.iyte.dnmso.domain.DNMSO.Predictions.Software;
import tr.edu.iyte.dnmso.domain.DNMSO.Predictions.Software.Settings;
import tr.edu.iyte.dnmso.domain.DNMSO.Predictions.Software.Settings.Setting;
import tr.edu.iyte.dnmso.domain.Prediction.Score;
import tr.edu.iyte.dnmso.domain.Prediction.Sources;
import tr.edu.iyte.dnmso.domain.Prediction.Sources.Source;
import tr.edu.iyte.dnmso.domain.pepxml.MsmsPipelineAnalysis;

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

    
    public Object run(Object container, String[] args) {
    	processSettings(container, args);
    	//System.out.println(getProperties());
    	if (getProperties().get(ServiceTag.COMMAND.toString()).equals("read")) return read();
        return null;
    }

    private Object read() {

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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
        }
        return getContainer();
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
    private DNMSO convertMsmsPipelineAnalysisToDNMSO(MsmsPipelineAnalysis msmsPipelineAnalysis,String size) {
    	Integer numberOfPrediction = null;
        HashMap<String,HashSet<String>> files = new HashMap<String, HashSet<String>>();
    	if (size != null){
    		numberOfPrediction = Integer.parseInt(size);
    	}
    	if (getContainer()==null){
            DnmsoFactory dnmsoFactory = new DnmsoFactory();
            setContainer(dnmsoFactory.createDnmso());
        }

        Predictions predictions = new Predictions();
        for (MsmsPipelineAnalysis.MsmsRunSummary msmsRunSummary : msmsPipelineAnalysis.getMsmsRunSummary()){
            //software
        	Software software = new Software();
            for (MsmsPipelineAnalysis.MsmsRunSummary.SearchSummary searchSummary :msmsRunSummary.getSearchSummary() ){
            	
            	software.setName("Peaks");
            	software.setVersion("6.0");
            	software.setSettings(new Settings());
               
                for ( MsmsPipelineAnalysis.MsmsRunSummary.SearchSummary.Parameter parameter : searchSummary.getParameter()){
                    Setting setting = new Setting();
                    setting.setName(parameter.getName());
                    setting.setValue((String) parameter.getValue());
                    software.getSettings().getSetting().add(setting);
                }

                Setting setting = new Setting();
                setting.setName("enzyme");
                setting.setValue(msmsRunSummary.getSampleEnzyme().getName());
                software.getSettings().getSetting().add(setting);  
            }
            predictions.setSoftware(software);
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
                      	prediction.setAssumedCharge(Integer.valueOf(spectrumQuery.getAssumedCharge()));
                      	//Scores
                      	Score score = new Score();
                      	score.setName("HitRank");
                      	score.setValue(String.valueOf(searchHit.getHitRank()));
                      	prediction.getScore().add(score);
                      	
                        //Source
                    	Sources sources = new Sources();
                    	sources.setMerge(false);

                    	for (long i =spectrumQuery.getStartScan(); i<=spectrumQuery.getEndScan();i++){
                    		Source source = new Source();
                            source.setFileName(spectrumQuery.getSpectrum());
                            source.setScanId(String.valueOf(i));
                    		sources.getSource().add(source);
                            HashSet<String> ids = files.get(spectrumQuery.getSpectrum());
                            if (ids!=null){
                                files.get(spectrumQuery.getSpectrum()).add(String.valueOf(i));
                            }   else {
                                ids = new HashSet<String>();
                                ids.add(String.valueOf(i));
                                files.put(spectrumQuery.getSpectrum(),ids);
                            }
                    	}
                    	prediction.getSources().add(sources);
                    	
                    	// Sequence
                    	Prediction.Sequence sequence = new Prediction.Sequence();
                    	sequence.setSequence(searchHit.getPeptide());
                        sequence.setCalculatedMass((double) searchHit.getCalcNeutralPepMass());
                        
                        Double totalconfidance = 0.0;
                        int sizeOf = 0;
                        for (MsmsPipelineAnalysis.MsmsRunSummary.SpectrumQuery.SearchResult.SearchHit.SearchScore searchScore : searchHit.getSearchScore()) {
                            if (searchScore.getName().equals("PeaksDenovoScore")){
                            	score = new Score();
                              	score.setName(searchScore.getName());
                              	score.setValue(String.valueOf(searchScore.getValue()));
                              	prediction.getScore().add(score);
                            }

                            if (searchScore.getName().equals("positional_conf")){
                                StringTokenizer st = new StringTokenizer(searchScore.getValue(), ",");
                                int i=0;
                                while (st.hasMoreTokens()){
                                    String confidence = st.nextToken();
                                    totalconfidance += Double.parseDouble(confidence);
                                    sizeOf++;
                                    AminoAcid aminoAcid = new AminoAcid();
                                    
                                    aminoAcid.setCharacter(String.valueOf(searchHit.getPeptide().charAt(i)));
                                    aminoAcid.setConfidence(Double.valueOf(confidence));
                                    i++;
                                    aminoAcid.setPos(Integer.valueOf(i));
                                    sequence.getAminoAcidOrModifiedAminoAcidOrGap().add(aminoAcid);
                                }

                            }
                        }
                        totalconfidance =  totalconfidance /sizeOf;
                        sequence.setConfidence(totalconfidance);
                        prediction.setSequence(sequence);
                        
                        predictions.getPrediction().add(prediction);

                        }

                    }



                
                }

            for (String filePath : files.keySet()){
                HashSet<String> ids =  files.get(filePath);

                File file = new File(getProperties().get("-p")) ;
                getProperties().put("-s",file.getParent()+File.separator+filePath);
                String indexes = "";
                //System.out.println(ids);
                for (String id : ids) {
                    indexes = indexes.concat(","+id);
                }
                indexes.substring(1);
                getProperties().put("-i", indexes.substring(1));
                SpectraFactory spectraFactory = new SpectraFactory();
                spectraFactory.getSpectra(this);
            }
                DNMSO dnmso = (DNMSO)getContainer();
                dnmso.getPredictions().add(predictions);
                setContainer(dnmso);
        }
        return (DNMSO)getContainer();
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
            
        } catch (FileNotFoundException e) {
           return false;
        } catch (JAXBException e) {
        	return false;
        } catch (XMLStreamException e) {
        	return false;
        } catch (FactoryConfigurationError e) {
        	return false;
        } catch (Exception e) {
			return false;
		}
        return false;

    }
}
