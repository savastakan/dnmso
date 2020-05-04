/*
 * 
 */
package service;

import domain.*;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static java.lang.Integer.parseInt;

public class LutefiskXPService extends AbstractService {
	HashMap<String,ModifiedAminoAcid> modifiedAminoAcidHashMap = new HashMap<>();
	HashMap<String,AminoAcid> aminoAcidHashMap = new HashMap<>();
	private DNMSO read() {
		String predictionFilePath = getProperties().get(ServiceTag.PREDICTION_FILE_PATH.toString());
		File predictionFile = new File(predictionFilePath);
		DNMSOFactory dnmsoFactory = new DNMSOFactory();
		// List<String> ids = createIndexList(params);
		//System.out.println(path);
		String size = getProperties().get(ServiceTag.NUMBER_OF_PREDICTION.toString());
		modifiedAminoAcidHashMap = parseModifiedAminoAcids(predictionFile.getParent() +File.separator +"Lutefisk.residues");

		Pattern pattern = Pattern
				.compile("\\A\\b(LutefiskXP)\\b (v\\d+\\.\\d+\\.\\d+)|\\A Filename: (\\S+)|(\\bMolecular Weight Tolerance\\b|\\bMolecular Weight\\b|\\bFragment Ion Tolerance\\b|\\bIon Offset\\b|\\bCharge State\\b|\\bCysteine residue mass\\b|Ions per window\\b|\\bExtension Threshold\\b|\\bExtension Number\\b|\\bGaps\\b|\\bPeak Width\\b|\\bData Threshold\\b|\\bIons per residue\\b|\\bAmino acids known to be present\\b|\\bSequence Tag\\b):?(\\s+\\d+(?:\\.\\d+)?(?:\\s\\(\\d+\\))?)|(QTOF|LCQ)|(\\S+)\\sDigest|\\A(\\S+)+\\s+(\\d)\\s+(\\d\\.\\d\\d\\d)\\s+(\\d\\.\\d\\d\\d)\\s+(\\d\\.\\d\\d\\d)\\s+(\\d\\.\\d\\d\\d)\\s+(\\d\\.\\d\\d\\d)");
		boolean isLutefiskXP = false;
		String fileContent;
		List<Prediction> predictions = new LinkedList<>();
		Software software = dnmsoFactory.createSoftware("LutefiskXP","", new LinkedList<>(),new LinkedList<>());;
		Collection<Spectrum> spectra = new ArrayList<>();

		try {

            // String numberOfPrediction = getProperties().get(ServiceTag.NUMBER_OF_PREDICTION.toString());

			BufferedReader in = new BufferedReader(new FileReader(predictionFile));
			while ((fileContent = in.readLine()) != null  ) {
				Matcher matcher = pattern.matcher(fileContent);
				while (matcher.find()) {
					
					if (matcher.group(1) != null) {// program name
						isLutefiskXP = true;

					} // validate LutefiskXP

					if (matcher.group(2) != null) {// version
						software.setSoftwareVersion(matcher.group(2));
					}
					if (matcher.group(3) != null) { // get file name
						Spectrum spectrum = createSpectrum(predictionFile.getParent(),matcher.group(3));
						spectra.add(spectrum);
					}
					if (matcher.group(4) != null && matcher.group(5) != null) {//add settings
						SoftwareSetting softwareSetting = dnmsoFactory.createSoftwareSetting(matcher.group(4),matcher.group(5));
						software.getSoftwareSetting().add(softwareSetting);
					}
					if (matcher.group(6) != null) {//Machine setting
						SoftwareSetting softwareSetting = dnmsoFactory.createSoftwareSetting("Machine",matcher.group(6));
						software.getSoftwareSetting().add(softwareSetting);
					}
					if (matcher.group(7) != null) {//Digest Setting
						SoftwareSetting softwareSetting = dnmsoFactory.createSoftwareSetting("Digest",matcher.group(7));
						software.getSoftwareSetting().add(softwareSetting);
					}
					if (matcher.group(8) != null
                                && matcher.group(9) != null&& matcher.group(10) != null&& matcher.group(11) != null&& matcher.group(12) != null&& matcher.group(13) != null&& matcher.group(14) != null) {
						Sequence sequence = this.createSequence(Double.parseDouble(matcher.group(9)), matcher.group(8),null,null);
						List<Score> scores = new ArrayList<>();

						Score rankScore = new Score();
                        rankScore.setScoreName("rank");
                        rankScore.setScoreValue(Double.parseDouble(matcher.group(9)));
                        scores.add(rankScore);

                        Score prcScore = new Score();
                        prcScore.setScoreName("Pr(c)");
                        prcScore.setScoreValue(Double.parseDouble(matcher.group(10)));
                        scores.add(prcScore) ;

                        Score pevzScrScore = new Score();
                        pevzScrScore.setScoreName("PevzScr");
                        pevzScrScore.setScoreValue(Double.parseDouble(matcher.group(11)));
                        scores.add(pevzScrScore) ;

                        Score qualityScore = new Score();
                        qualityScore.setScoreName("Quality");
                        qualityScore.setScoreValue(Double.parseDouble(matcher.group(12)));
                        scores.add(qualityScore) ;

                        Score intScrScore = new Score();
                        intScrScore.setScoreName("IntScr");
                        intScrScore.setScoreValue(Double.parseDouble(matcher.group(13)));
                        scores.add(intScrScore) ;

                        Score xcorrScore = new Score();
                        xcorrScore.setScoreName("X-corr");
                        xcorrScore.setScoreValue(Double.parseDouble(matcher.group(14)));
                        scores.add(xcorrScore) ;

						Prediction prediction = dnmsoFactory.createPrediction(spectra,sequence,scores, software);
						if(size == null){
							predictions.add(prediction);
						} else if (predictions.size() < parseInt(size)){
							predictions.add(prediction);
						} else break;
					}

				}

			}
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (isLutefiskXP){
            DNMSO dnmso =  getDNMSO();
			dnmso.getPrediction().addAll(predictions);
			Spectra s = new Spectra();
			s.setSpectrum(spectra);
			dnmso.getSpectra().add(s);
            this.setDNMSO(dnmso);
			return dnmso;
		}
		return null;
	}


	private Spectrum createSpectrum(String path,String fileName) {
		Spectrum spectrum = new Spectrum();
		String line;
		try {
			String csvFormat = "";
			BufferedReader in = new BufferedReader(new FileReader(path+ File.separator + fileName));
			while ((line = in.readLine()) != null) {
				Pattern pattern = Pattern.compile("(\\S+)\\s(\\S+)");
				Matcher matcher = pattern.matcher(line);
				while (matcher.find()) {
					if (matcher.group(1) != null && matcher.group(2) != null)
						csvFormat = csvFormat.concat(matcher.group(1) + ","
								+ matcher.group(2) + ";");
				}
			}
			spectrum.setCsvData(csvFormat);
			in.close();
		}  catch (IOException e) {
			System.out.println("Spectrum is not added");
		}
		spectrum.setSpectrumId(fileName);
		return spectrum;
	}


	private Sequence createSequence(Double combinedConfidence, String peptideSequence, Modification cTerminalModification, Modification nTerminalModification) {
		DNMSOFactory dnmsoFactory = new DNMSOFactory();
		Sequence sequence = new Sequence();
		sequence.setPeptideSequence(peptideSequence);
		sequence.setCombinedConfidence(combinedConfidence);
		sequence.setNTerminalModification(nTerminalModification);
		sequence.setCTerminalModification(cTerminalModification);
		sequence.setSequenceElement(new LinkedList<>());
		Pattern pattern = Pattern.compile("\\[(\\d+\\.\\d+)\\]|(\\S)");
		Matcher matcher = pattern.matcher(peptideSequence);
		Long positionInSequence = 0L;
		while (matcher.find()) {
			positionInSequence++;
			if (matcher.group(1) != null) {
				SEGap gap = dnmsoFactory.createSEGap(combinedConfidence, Double.parseDouble(matcher.group(1)), positionInSequence, null);
				sequence.getSequenceElement().add(gap);
			} else if (matcher.group(2) != null) {
				String singleLetterCode = matcher.group(2);
				if (modifiedAminoAcidHashMap.containsKey(singleLetterCode)){
					ModifiedAminoAcid modifiedAminoAcid = modifiedAminoAcidHashMap.get(singleLetterCode);
					SEModifiedAminoAcid seModifiedAminoAcid = new SEModifiedAminoAcid();
					seModifiedAminoAcid.setPositionInSequence(positionInSequence);
					seModifiedAminoAcid.setConfidence(combinedConfidence);
					seModifiedAminoAcid.setModifiedAminoAcid(modifiedAminoAcid);
					sequence.getSequenceElement().add(seModifiedAminoAcid);
					getDNMSO().getModifiedAminoAcid().add(modifiedAminoAcid);
					getDNMSO().getModification().add(modifiedAminoAcid.getModification());
				} else {
					AminoAcidFactory aminoAcidFactory = new AminoAcidFactory();
					AminoAcid aminoAcid = aminoAcidFactory.createAminoAcid(singleLetterCode);
					SEAminoAcid seAminoAcid = dnmsoFactory.createSEAminoAcid(aminoAcid,combinedConfidence,positionInSequence,null);
					sequence.getSequenceElement().add(seAminoAcid);
					if (!aminoAcidHashMap.containsKey(aminoAcid.getSingleLetterCode())){
						getDNMSO().getAminoAcid().add(aminoAcid);
						aminoAcidHashMap.put(aminoAcid.getSingleLetterCode(),aminoAcid);
					}

				}

			}
		}
		return sequence;
	}

	public DNMSO run(DNMSO targetDNMSO, String[] args) {
		processSettings(targetDNMSO, args);
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
		} catch (IOException e) {
			return false;
		}
		return false;
	}


	public HashMap<String,ModifiedAminoAcid> parseModifiedAminoAcids(String file){
		HashMap<String,ModifiedAminoAcid> modifiedAminoAcidHashMap = new HashMap<>();
		ModifiedAminoAcidFactory modifiedAminoAcidFactory = new ModifiedAminoAcidFactory();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String fileContent;
			while ((fileContent = in.readLine()) != null){
				try {
					Pattern regex = Pattern.compile("^(\\w)\\s+(\\d+.?\\d*)\\s+(\\d+.?\\d*)\\s+\\d+\\s+/(\\w+)");
					Matcher regexMatcher = regex.matcher(fileContent);
					while (regexMatcher.find()) {
						for (int i = 1; i <= regexMatcher.groupCount(); i++) {
							String singleLetterCode =  regexMatcher.group(1);
							Double monoIsotopic =  Double.parseDouble(regexMatcher.group(2));
							Double average = Double.parseDouble(regexMatcher.group(3));
							String threeLetterCode = regexMatcher.group(4);
							if (threeLetterCode.equals("Modified")){
								if(!modifiedAminoAcidHashMap.containsKey(singleLetterCode)){
									ModifiedAminoAcid modifiedAminoAcid = modifiedAminoAcidFactory.createModifiedAminoAcid(singleLetterCode,monoIsotopic,average);
									modifiedAminoAcidHashMap.put(singleLetterCode,modifiedAminoAcid);
								}
							}
						}
					}
				} catch (PatternSyntaxException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return modifiedAminoAcidHashMap;

	}

	public String getServiceName() {
		// TODO Auto-generated method stub
		return "LutefiskXPService";
	}
}
