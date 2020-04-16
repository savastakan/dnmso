/*
 * 
 */
package service;

import domain.DNMSO;
import domain.DNMSO.Predictions;
import domain.DNMSO.Predictions.Software;
import domain.DNMSO.Predictions.Software.Settings;
import domain.DnmsoFactory;
import domain.Prediction;
import domain.Prediction.Sequence;
import domain.Prediction.Sources;
import domain.Prediction.Sources.Source;
import domain.Spectrum;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// TODO: Auto-generated Javadoc

/**
 * The Class LutefiskXPService.
 * @author Savas TAKAN
 * @version $Revision: 1.0 $
 */
public class LutefiskXPService extends AbstractService {
	
	
	private boolean check(int peredictionSize, Integer size) {
		if(size!=null){
			if ( peredictionSize < size){
				return true;
			} 
			return false;
		} 
		return true;
	}
	/**
	 * Read.
	 *
	 * @return the dnmso */
	
	private DNMSO read() {
		DNMSO dnmso = (DNMSO) getContainer();
		// List<String> ids = createIndexList(params);
		//System.out.println(path);
		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		Pattern pattern = Pattern
				.compile("\\A\\b(LutefiskXP)\\b (v\\d+\\.\\d+\\.\\d+)|\\A Filename: (\\S+)|(\\bMolecular Weight Tolerance\\b|\\bMolecular Weight\\b|\\bFragment Ion Tolerance\\b|\\bIon Offset\\b|\\bCharge State\\b|\\bCysteine residue mass\\b|Ions per window\\b|\\bExtension Threshold\\b|\\bExtension Number\\b|\\bGaps\\b|\\bPeak Width\\b|\\bData Threshold\\b|\\bIons per residue\\b|\\bAmino acids known to be present\\b|\\bSequence Tag\\b):?(\\s+\\d+(?:\\.\\d+)?(?:\\s\\(\\d+\\))?)|(QTOF|LCQ)|(\\S+)\\sDigest|\\A(\\S+)+\\s+(\\d)\\s+(\\d\\.\\d\\d\\d)\\s+(\\d\\.\\d\\d\\d)\\s+(\\d\\.\\d\\d\\d)\\s+(\\d\\.\\d\\d\\d)\\s+(\\d\\.\\d\\d\\d)");
//		Prediction prediciton = new Prediction();
//		Software software = new Software();
//		software.setSettings(new Settings());
//		prediciton.setSources(new Sources());
//		prediciton.setSoftware(software);

		String version = null;
		Settings settings = dnmsoFactory.createSettings();
		boolean isLutefiskXP = false;
		String fileContent;
		Sequence sequence=null;
		Sources sources = new Sources();
		sources.setMerge(false);
		Spectrum spectrum=null;
		Predictions predictions= new Predictions();
		predictions.setSoftware(new Software());
		
		try {

			String predictionFilePath = getProperties().get(ServiceTag.PREDICTION_FILE_PATH.toString());
            String numberOfPrediction = getProperties().get(ServiceTag.NUMBER_OF_PREDICTION.toString());
            File predictionFile = new File(predictionFilePath);
            Integer size= null;
            if (numberOfPrediction!=null){
                size = Integer.valueOf(numberOfPrediction);
            }


			BufferedReader in = new BufferedReader(new FileReader(predictionFile));
			while ((fileContent = in.readLine()) != null && check(predictions.getPrediction().size() ,size)) {
				
				Matcher matcher = pattern.matcher(fileContent);
				while (matcher.find()&& check(predictions.getPrediction().size() ,size)) {
					
					if (matcher.group(1) != null) {// program name
						isLutefiskXP = true;
					} // validate LutefiskXP

					if (matcher.group(2) != null) {// version
						version = matcher.group(2);
						predictions.getSoftware().setName("LutefiskXP");
						predictions.getSoftware().setVersion(version);
						predictions.getSoftware().setSettings(settings);
					}
					if (matcher.group(3) != null) { // get file name
						getProperties().put(ServiceTag.SPECTRA_FILE_PATH.toString(),predictionFile.getParent()+File.separator+ matcher.group(3));
						SpectraFactory spectraFactory = new SpectraFactory();
						spectraFactory.getSpectra(this);
						Source source = new Source();
						source.setFileName(matcher.group(3));
						sources.getSource().add(source);
						
					}
					if (matcher.group(4) != null && matcher.group(5) != null) {//add settings
						dnmsoFactory.addSetting(settings,matcher.group(4),matcher.group(5));
					}
					if (matcher.group(6) != null) {//Machine setting
						dnmsoFactory.addSetting(settings,"Machine",matcher.group(6));
					}
					if (matcher.group(7) != null) {//Digest Setting
						dnmsoFactory.addSetting(settings,"Digest",matcher.group(7));
					}
					if (matcher.group(8) != null
                                && matcher.group(9) != null&& matcher.group(10) != null&& matcher.group(11) != null&& matcher.group(12) != null&& matcher.group(13) != null&& matcher.group(14) != null) {
						sequence = createSequance(matcher.group(8),matcher.group(9));
                        List<Prediction.Score> scores = new ArrayList<Prediction.Score>();
                        Prediction.Score rankScore = new Prediction.Score();
                        rankScore.setName("rank");
                        rankScore.setValue(matcher.group(9));
                        scores.add(rankScore);

                        Prediction.Score prcScore = new Prediction.Score();
                        prcScore.setName("Pr(c)");
                        prcScore.setValue(matcher.group(10));
                        scores.add(prcScore) ;

                        Prediction.Score pevzScrScore = new Prediction.Score();
                        pevzScrScore.setName("PevzScr");
                        pevzScrScore.setValue(matcher.group(11));
                        scores.add(pevzScrScore) ;

                        Prediction.Score qualityScore = new Prediction.Score();
                        qualityScore.setName("Quality");
                        qualityScore.setValue(matcher.group(12));
                        scores.add(qualityScore) ;

                        Prediction.Score intScrScore = new Prediction.Score();
                        intScrScore.setName("IntScr");
                        intScrScore.setValue(matcher.group(13));
                        scores.add(intScrScore) ;

                        Prediction.Score xcorrScore = new Prediction.Score();
                        xcorrScore.setName("X-corr");
                        xcorrScore.setValue(matcher.group(14));
                        scores.add(xcorrScore) ;

                        dnmsoFactory.addPrediciton(predictions,sources, sequence,scores);
					}

				}

			}
			
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (isLutefiskXP){
            dnmso = (DNMSO) getContainer();
			dnmso.getPredictions().add(predictions);
            setContainer(dnmso);
			//DnmsoFactory.instance().addPrediciton(targetDNMSO, sequence, spectrum.getSpectrumId(), software.getSoftwareId());
			return dnmso;
		}
		return null;
	}



//	/**
//	 * Creates the csv spectrum.
//	 *
//	 * @param path the path
//	 * @param fileName the file name
//	
//	 * @return the spectrum */
//	private Spectrum createCsvSpectrum(String path,String fileName) {
//		DnmsoFactory dnmsoFactory = new DnmsoFactory();
//		Spectrum spectrum = dnmsoFactory.createSpectrum();
//		String line;
//		try {
//			String csvFormat = "";
//			BufferedReader in = new BufferedReader(new FileReader(path+fileName));
//			while ((line = in.readLine()) != null) {
//				Pattern pattern = Pattern.compile("\\A(\\S+)\\s(\\S+)");
//				Matcher matcher = pattern.matcher(line);
//				while (matcher.find()) {
//					if (matcher.group(1) != null && matcher.group(2) != null)
//						csvFormat = csvFormat.concat(matcher.group(1) + ","
//								+ matcher.group(2) + ";");
//				}
//
//			}
//			spectrum.setCsv(csvFormat);
//			in.close();
//		} catch (FileNotFoundException e) {
//			dnmsoFactory.addLink(spectrum, path);
//		} catch (IOException e) {
//			System.out.println("Spectrum is not added");
//		}
//		spectrum.setFileName(fileName);
//		return spectrum;
//	}

	/**
	 * Creates the sequance.
	 *
	 * @param seq the seq
	 * @param confidance the confidance
	
	 * @return the sequence */
	private Sequence createSequance(String seq, String confidance) {

		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		Sequence sequence = dnmsoFactory.createSequence(seq, confidance);
		Pattern pattern = Pattern.compile("\\[(\\d+\\.\\d+)\\]|(\\S)");
		Matcher matcher = pattern.matcher(seq);
		Integer position =  0;
		while (matcher.find()) {
			position++;
			if (matcher.group(1) != null) {
				dnmsoFactory.addGap(sequence,confidance,position,matcher.group(1));
			} else if (matcher.group(2) != null) {
				dnmsoFactory.addAminoAcid(sequence, matcher.group(2), confidance, position);
			}
		}
		return sequence;
	}
	//path read
	/* (non-Javadoc)
	 * @see tr.edu.iyte.dnmso.service.Service#run(java.lang.Object, java.lang.String)
	 */
	public DNMSO run(Object targetDnml, String[] args) {

		processSettings((DNMSO)targetDnml, args);
		if (getProperties().get("command").equals("read")) return read();
		
    	
		return null;
	}


	public boolean isValid(File lut) {
		
		String line;
		try {
			BufferedReader in = new BufferedReader(new FileReader(lut));
			while ((line = in.readLine()) != null) {
				if (line.startsWith("LutefiskXP")){
					in.close();
					return true;
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {		
			return false;
		}
		return false;
	}



	public String getServiceName() {
		// TODO Auto-generated method stub
		return "LutefiskXPService";
	}
}
