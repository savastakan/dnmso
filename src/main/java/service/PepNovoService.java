
package service;

import domain.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


// TODO: Auto-generated Javadoc

/**
 * The Class PepNovoService.
 */
public class PepNovoService extends AbstractService {


	private DNMSO readPepNovoPlus(){
		ObjectFactory objectFactory = new ObjectFactory();
		String predictionFilePath = getProperties().get(ServiceTag.PREDICTION_FILE_PATH.toString());
		File predictionFile = new File(predictionFilePath);
		DNMSO targetDNMSO = getDNMSO();
		String size = getProperties().get(ServiceTag.NUMBER_OF_PREDICTION.toString());
		
		Pattern pattern = Pattern.compile("^PepNovo\\+\\sBuild\\s(\\d+)|^Fragment tolerance :\\s([+-]?\\d+.?\\d+)|^PM tolernace \\s+:\\s+([+-]?\\d+.?\\d+)|^>>\\s(\\d+)\\s(\\d+)\\s+\\(SQS|^(\\d+)\\s([+-]?\\d*.?\\d*)\\s([+-]?\\d*.?\\d*)\\s([+-]?\\d*.?\\d*)\\s([+-]?\\d*.?\\d*)\\s([+-]?\\d*.?\\d*)\\s([+-]?\\d*.?\\d*)\\s(\\w+)");
		
		String fileContent;

		if (targetDNMSO==null){
			DNMSOFactory dnmsoFactory = new DNMSOFactory();
			targetDNMSO = dnmsoFactory.createDNMSO();
		}

		int beginScan = 0;
		int endScan;
		String nGap = null;
		String cGap = null;
		Score scoreRnkScr = null;
		Score scorePnvScr = null;
		Collection<Spectrum> spectra = new LinkedList<>();
		Software software =objectFactory.createSoftware();
		software.setSoftwareSetting(new LinkedList<>());
		software.setSoftwareName("Pepnovo+");

		try {
			BufferedReader in = new BufferedReader(new FileReader(predictionFile));
			while ((fileContent = in.readLine()) != null && check(targetDNMSO.getPrediction().size(),size)) {
				Matcher matcher = pattern.matcher(fileContent);
				while (matcher.find() && check(targetDNMSO.getPrediction().size(),size) ) {
					Prediction prediction = new Prediction();
					prediction.setSoftware(software);
					prediction.setScore(new LinkedList<>());
					if(matcher.group(1) != null){
						software.setSoftwareVersion(matcher.group(1));
                    }
                    if (matcher.group(2) != null) {
						String fragmentTolerance = matcher.group(2);//Fragment tolerance
						SoftwareSetting setting = new SoftwareSetting();
						setting.setSoftwareSettingName("Fragment Tolerance");
						setting.setSoftwareSettingValue(fragmentTolerance);
						prediction.getSoftware().getSoftwareSetting().add(setting);
					}
					if (matcher.group(3) != null) {
						String pMTolernace = matcher.group(3) ;
						SoftwareSetting setting = new SoftwareSetting();
						setting.setSoftwareSettingName("PM Tolerance");
						setting.setSoftwareSettingValue(pMTolernace);
						prediction.getSoftware().getSoftwareSetting().add(setting);
					}

					if (matcher.group(4) != null) {
						beginScan =Integer.parseInt( matcher.group(4));
					}
					if (matcher.group(5) != null) {
						endScan = Integer.parseInt(matcher.group(5));
						for (int i =beginScan; i<=endScan;i++ ){
							getProperties().put("-i", String.valueOf(i));
							//SpectraFactory spectraFactory = new SpectraFactory();
							//spectraFactory.getSpectra(this);
							//spectra.addAll(getLastElement(this.getDNMSO().getSpectra()).getSpectrum());
						}
					}
					
					if (matcher.group(7) != null) {
						Double rnkScr =Double.parseDouble(matcher.group(7));
						scoreRnkScr = new Score();
						scoreRnkScr.setScoreName("RnkScr");
						scoreRnkScr.setScoreValue(rnkScr);
					}
					if (matcher.group(8) != null) {
						Double pnvScr = Double.parseDouble(matcher.group(8));
						scorePnvScr = new Score();
						scorePnvScr.setScoreName("PnvScr");
						scorePnvScr.setScoreValue(pnvScr);
						
					}					
					if (matcher.group((9)) != null) {
						nGap =matcher.group(9);
					}
					
					if (matcher.group(10) != null) {
						cGap = matcher.group(10);
					}
					
					/*if (matcher.group(11) != null) {
						mplush =matcher.group(11);
						
					}
					if (matcher.group(12) != null) {
						charge =matcher.group(12);
						
					}*/
					if (matcher.group(13) != null) {
						
						String sequenceStr = matcher.group(13);
						prediction.setSpectrum(spectra);
						prediction.getScore().add(scoreRnkScr);
						prediction.getScore().add(scorePnvScr);
						Sequence sequence = new Sequence();
						sequence.setSequenceElement(new LinkedList<>());
						SEGap gap = new SEGap();
						
						gap.setPositionInSequence(1);
						gap.setGapValue(Double.parseDouble(nGap));
						sequence.getSequenceElement().add(gap);
						
						sequence.setPeptideSequence(sequenceStr);
						for (int i=0; i< sequenceStr.length();i++){
							SEAminoAcid seAminoAcid = new SEAminoAcid();
							AminoAcidFactory aminoAcidFactory = new AminoAcidFactory();
							AminoAcid aminoAcid = aminoAcidFactory.createAminoAcid(String.valueOf(sequenceStr.charAt(i)));
							seAminoAcid.setPositionInSequence(i + 2);
							seAminoAcid.setAminoAcid(aminoAcid);
							sequence.getSequenceElement().add(seAminoAcid);
						}
						gap = new SEGap();
						gap.setPositionInSequence(sequenceStr.length() + 2);
						gap.setGapValue(Double.parseDouble(cGap));
						sequence.getSequenceElement().add(gap);
						prediction.setSequence(sequence);
						targetDNMSO.getPrediction().add(prediction);
					}
				}
			}
			in.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return targetDNMSO;
	}

	private boolean check(int peredictionSize, String size) {
		if(size!=null){
			return peredictionSize < Integer.parseInt(size);
		} 
		return true;
	}
	private DNMSO readPepNovo() {
		ObjectFactory objectFactory = new ObjectFactory();
		DNMSO targetDNMSO = getDNMSO();
		if (targetDNMSO==null){
			targetDNMSO = objectFactory.createDNMSO("2.0",null);
		}

		String predictionFilePath = getProperties().get(ServiceTag.PREDICTION_FILE_PATH.toString());
		String size = getProperties().get(ServiceTag.NUMBER_OF_PREDICTION.toString());
		Pattern pattern = Pattern
				.compile("\\A(PepNovo)\\s(v\\d+\\.\\d+)|\\AFile \\d+: (\\S+)$|\\A\\d+\\.\\d+\\s+(\\S+)$");
		boolean isPepNovo = false;
		String fileContent;
		Software software = objectFactory.createSoftware();
		Spectrum spectrum = null;
		Spectra spectra = objectFactory.createSpectra();

		try {
			BufferedReader in = new BufferedReader(new FileReader(predictionFilePath));
			while ((fileContent = in.readLine()) != null && check(targetDNMSO.getPrediction().size(), size)) {
				Matcher matcher = pattern.matcher(fileContent);
				while (matcher.find() && check(targetDNMSO.getPrediction().size(), size)) {
					if (matcher.group(1) != null) {//name
						software.setSoftwareName(matcher.group(1));
						isPepNovo = true;
					} // validate PepNovo
					if (matcher.group(2) != null) {// version
						software.setSoftwareVersion(matcher.group(2));
					}
					if (matcher.group(3) != null) { // get file name
						// new prediction is created
						spectrum = createSpectrum(predictionFilePath,matcher.group(3));
						spectra.getSpectrum().add(spectrum);
					}
					if (matcher.group(4) != null) {// whole sequence
						String peptideSequence = matcher.group(4);
						Sequence sequence = objectFactory.createSequence();
						Prediction prediction = objectFactory.createPrediction();
						prediction.getSpectrum().add(spectrum);
						PepNovoPTMFactory pepNovoPTMFactory = new PepNovoPTMFactory();
						AminoAcidFactory aminoAcidFactory = new AminoAcidFactory();
						int position = 0;
						try {
							Pattern regex = Pattern.compile("\\w\\+\\d+|\\w");
							Matcher regexMatcher = regex.matcher(peptideSequence);
							while (regexMatcher.find()) {
								position += 1;
								String sequenceElement = regexMatcher.group();
								if (sequenceElement.length() > 1){
									ModifiedAminoAcid modifiedAminoAcid = pepNovoPTMFactory.createPTM(sequenceElement);
									SEModifiedAminoAcid seModifiedAminoAcid = objectFactory.createSEModifiedAminoAcid();
									seModifiedAminoAcid.setModifiedAminoAcid(modifiedAminoAcid);
									seModifiedAminoAcid.setPositionInSequence(position);
									sequence.getSequenceElement().add(seModifiedAminoAcid);
								} else {
									SEAminoAcid seAminoAcid = new SEAminoAcid();
									seAminoAcid.setPositionInSequence(position);
									seAminoAcid.setAminoAcid(aminoAcidFactory.createAminoAcid(sequenceElement));
								}
							}
						} catch (PatternSyntaxException ex) {
							System.out.println("problem");
						}

 						sequence.setPeptideSequence(peptideSequence);
						prediction.setSequence(sequence);
						prediction.getSpectrum().add(spectrum);
						prediction.setSoftware(software);
						targetDNMSO.getPrediction().add(prediction);
					}
				}


			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (isPepNovo) return targetDNMSO;
		return null;
	}

	private Spectrum createSpectrum(String path,String fileName) {
		ObjectFactory objectFactory = new ObjectFactory();
		Spectrum spectrum = objectFactory.createSpectrum();
		spectrum.setSpectrumId(fileName);
		String line;
		try {
			BufferedReader in = new BufferedReader(new FileReader(path+fileName));
			while ((line = in.readLine()) != null) {
				Pattern pattern = Pattern.compile("\\A(\\S+)\\s(\\S+)");
				Matcher matcher = pattern.matcher(line);
				String csvData = "";
				while (matcher.find()) {
					if (matcher.group(1) != null && matcher.group(2) != null){
						csvData = csvData.concat(matcher.group(1) + "," + matcher.group(2) + ";");
					}
				}
				spectrum.setCsvData(csvData);
			}
			in.close();
		} catch (IOException e) {
			System.out.println("spectrum is not found!");
		}
		return spectrum;
	}


	public boolean isValid(File pepnovo) {
		String line;
		try {
			BufferedReader in = new BufferedReader(new FileReader(pepnovo));
			while ((line = in.readLine()) != null) {
				if (line.startsWith("PepNovo")) {
					in.close();
					return true;
				}
			}
			in.close();
		} catch (IOException e) {
			return false;
		}
		return false;
	}


	public DNMSO run(DNMSO targetDnml, String[] args) {
		processSettings(targetDnml, args);
		if (getProperties().get(ServiceTag.COMMAND.toString()).equals("read")) return read();
		return null;
	}


	public DNMSO read() {
		String predictionFilePath = getProperties().get(ServiceTag.PREDICTION_FILE_PATH.toString());

		String line;
		try {
			BufferedReader in = new BufferedReader(new FileReader(predictionFilePath));
			while ((line = in.readLine()) != null) {
				if (line.startsWith("PepNovo+")) {
					in.close();
					return readPepNovoPlus();
				}
				else if(line.startsWith("PepNovo")){
					in.close();
					return readPepNovo();
				}
			}
			in.close();
		} catch (IOException e) {
			return null;
		}
		return null;
	}

	public String getServiceName() {
		return "PepNovoService";
	}
}
