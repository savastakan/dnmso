package domain;

import java.util.*;


public class DNMSOFactory {

	public DNMSO createDNMSO(){
		ObjectFactory Objectfactory = new ObjectFactory();
		return Objectfactory.createDNMSO("2.0", null);
	}


	public Software createSoftware(String softwareName,String version,List<Publication> publications, List<SoftwareSetting> softwareSettings){
		Software software = new Software();
		software.setSoftwareName(softwareName);
		software.setPublication(publications);
		software.setSoftwareSetting(softwareSettings);
		software.setSoftwareVersion(version);
		return software;
	}

	public Modification createModification(String modificationName,Double monoModificationMass, Double averageModificationMass,
										   String psiModRef, Boolean isTerminalModification, CVParam cvParam){
		ObjectFactory objectFactory = new ObjectFactory();
		Modification modification = objectFactory.createModification();
		modification.setMonoModificationMass(monoModificationMass);
		modification.setModificationName(modificationName);
		modification.setAverageModificationMass(averageModificationMass);
		modification.setPsiModRef(psiModRef);
		modification.setCvParam(cvParam);
		modification.setIsTerminalModification(isTerminalModification);
		return modification;
	}


	public Prediction createPrediction(Collection<Spectrum> spectra, Sequence sequence,List<Score> scores, Software software) {
		ObjectFactory objectFactory = new ObjectFactory();
		Prediction prediction = objectFactory.createPrediction();
		prediction.setSequence(sequence);
		prediction.setScore(scores);
		prediction.setSoftware(software);
		prediction.setSpectrum(spectra);
		return prediction;
	}

	public Sequence createSequence(Double combinedConfidence, Modification cTerminalModification,
								   Modification nTerminalModification,
								   String peptideSequence,List<SequenceElement> sequenceElements) {
		ObjectFactory objectFactory = new ObjectFactory();
		Sequence sequence = objectFactory.createSequence();
		sequence.setCombinedConfidence(combinedConfidence);
		sequence.setCTerminalModification(cTerminalModification);
		sequence.setNTerminalModification(nTerminalModification);
		sequence.setPeptideSequence(peptideSequence);
		sequence.setSequenceElement(sequenceElements);
		return sequence;
	}

	

	public DNMSO mergeDNMSO(DNMSO sourceDNMSO,DNMSO targetDNMSO){

		for (Modification modification : sourceDNMSO.getModification()) {
			targetDNMSO.getModification().add(modification);
		}
		for (Prediction prediction: sourceDNMSO.getPrediction()){
			targetDNMSO.getPrediction().add(prediction);
		}
		
		for (Spectra spectra: sourceDNMSO.getSpectra()){
			targetDNMSO.getSpectra().add(spectra);
		}
		for (AminoAcid aminoAcid : sourceDNMSO.getAminoAcid()){
			targetDNMSO.getAminoAcid().add(aminoAcid);
		}
		for (ModifiedAminoAcid modifiedAminoAcid : sourceDNMSO.getModifiedAminoAcid()){
			targetDNMSO.getModifiedAminoAcid().add(modifiedAminoAcid);
		}
		return targetDNMSO;
	}

	public AminoAcid createAminoAcid(String aminoAcidName, String singleLetterCode, String threeLetterCode,
									 Double monoIsotopicMass, Double averageMass) {
		ObjectFactory objectFactory = new ObjectFactory();
		AminoAcid aminoAcid = objectFactory.createAminoAcid();
		aminoAcid.setMonoIsotopicMass(monoIsotopicMass);
		aminoAcid.setAverageMass(averageMass);
		aminoAcid.setSingleLetterCode(singleLetterCode);
		aminoAcid.setThreeLetterCode(threeLetterCode);
		aminoAcid.setAminoAcidName(aminoAcidName);
		return aminoAcid;
	}
	public SEAminoAcid createSEAminoAcid(AminoAcid aminoAcid, Double confidence, Long positionInSequence, Proof proof){
		SEAminoAcid seAminoAcid = new SEAminoAcid();
		seAminoAcid.setAminoAcid(aminoAcid);
		seAminoAcid.setConfidence(confidence);
		seAminoAcid.setPositionInSequence(positionInSequence);
		seAminoAcid.setProof(proof);
		return seAminoAcid;
	}

	public ModifiedAminoAcid createModifiedAminoAcid(Modification modification, AminoAcid aminoAcid, String modAAId) {
		ObjectFactory objectFactory = new ObjectFactory();
		ModifiedAminoAcid modifiedAminoAcid = objectFactory.createModifiedAminoAcid();
		modifiedAminoAcid.setModification(modification);
		modifiedAminoAcid.setAminoAcid(aminoAcid);
		modifiedAminoAcid.setModAAId(modAAId);
		return modifiedAminoAcid;
	}


	public SoftwareSetting createSoftwareSetting(String softwareSettingName, String softwareSettingValue) {
		ObjectFactory objectFactory = new ObjectFactory();
		SoftwareSetting softwareSetting = objectFactory.createSoftwareSettings();
		softwareSetting.setSoftwareSettingName(softwareSettingName);
		softwareSetting.setSoftwareSettingValue(softwareSettingValue);
		return softwareSetting;
	}

	public SEGap createSEGap(Double confidence, Double gapValue, Long positionInSequence, Proof proof) {
		ObjectFactory objectFactory = new ObjectFactory();
		SEGap gap = objectFactory.createSEGap();
		gap.setConfidence(confidence);
		gap.setGapValue(gapValue);
		gap.setPositionInSequence(positionInSequence);
		gap.setProof(proof);
		return gap;
	}

    public Spectrum createSpectrum(String spectrumID,Long scanID, Double precursorMZ, Double precursorIntensity,
								  String csvData, Spectra spectra) {
		ObjectFactory objectFactory = new ObjectFactory();
		Spectrum spectrum = objectFactory.createSpectrum();
		spectrum.setSpectrumId(spectrumID);
		spectrum.setScanId(scanID);
		spectrum.setPrecursorMZ(precursorMZ);
		spectrum.setPrecursorIntensity(precursorIntensity);
		spectrum.setCsvData(csvData);
		spectrum.setFoundInSpectra(spectra);
		return spectrum;
    }
}
