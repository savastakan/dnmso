package domain;

import java.util.LinkedList;

public class ObjectFactory {

    public Prediction createPrediction() {
        Prediction prediction = new Prediction();
        prediction.setScore(new LinkedList<>());
        prediction.setSpectrum(new LinkedList<>());
        prediction.setSoftware(new Software());
        prediction.setSequence(new Sequence());
        return prediction;
    }


    public DNMSO createDNMSO(String version, CV cv) {
        DNMSO dnmso = new DNMSO();
        dnmso.setAminoAcid(new LinkedList<>());
        dnmso.setSpectra(new LinkedList<>());
        dnmso.setModifiedAminoAcid(new LinkedList<>());
        dnmso.setPrediction(new LinkedList<>());
        dnmso.setModification(new LinkedList<>());
        dnmso.setVersion(version);
        dnmso.setCv(cv);
        return dnmso;
    }


    public Modification createModification() {
        Modification modification = new Modification();
        modification.setIsTerminalModification(false);
        modification.setAverageModificationMass(0.0);
        modification.setMonoModificationMass(0.0);
        modification.setModificationName("");
        modification.setPsiModRef("");
        modification.setCvParam(createCVParam());
        return modification;
    }


    public Software createSoftware() {
        Software software = new Software();
        software.setPublication(new LinkedList<>());
        software.setSoftwareSetting(new LinkedList<>());
        software.setSoftwareName("");
        software.setSoftwareVersion("");
        software.setSoftwareCVParam(createCVParam());
        return software;
    }


    public SoftwareSetting createSoftwareSettings() {
        SoftwareSetting softwareSetting = new SoftwareSetting();
        softwareSetting.setSoftwareSettingValue("");
        softwareSetting.setSoftwareSettingName("");
        softwareSetting.setSoftwareSettingcvParam(createCVParam());
        return softwareSetting;
    }

    public Sequence createSequence() {
        Sequence sequence = new Sequence();
        sequence.setSequenceElement(new LinkedList<>());
        sequence.setNTerminalModification(createModification());
        sequence.setCTerminalModification(createModification());
        sequence.setPeptideSequence("");
        sequence.setCombinedConfidence(0);
        return sequence;
    }

    public Score createScore() {
        Score score = new Score();
        score.setScoreValue(0.0);
        score.setScoreName("");
        score.setMainScore(false);
        score.setLargerIsBetter(false);
        score.setScoreCVParam(createCVParam());
        return new Score();
    }


    public AminoAcid createAminoAcid() {
        AminoAcid aminoAcid = new AminoAcid();
        aminoAcid.setSingleLetterCode("");
        aminoAcid.setAverageMass(0);
        aminoAcid.setMonoIsotopicMass(0);
        aminoAcid.setAminoAcidName("");
        aminoAcid.setThreeLetterCode("");
        return aminoAcid;
    }


    public Proof createProof() {
        return new Proof();
    }

    public ModifiedAminoAcid createModifiedAminoAcid() {
        ModifiedAminoAcid modifiedAminoAcid = new ModifiedAminoAcid();
        modifiedAminoAcid.setModification(createModification());
        modifiedAminoAcid.setModAAId("");
        modifiedAminoAcid.setAminoAcid(createAminoAcid());
        return new ModifiedAminoAcid();
    }


    public Spectrum createSpectrum() {
        Spectrum spectrum = new Spectrum();
        spectrum.setCsvData("");
        spectrum.setFoundInSpectra(null);
        spectrum.setScanId(0);
        spectrum.setPrecursorMZ(0);
        spectrum.setSpectrumId("");
        spectrum.setPrecursorIntensity(0);
        return spectrum;
    }


    public Spectra createSpectra() {
        Spectra spectra = new Spectra();
        spectra.setSpectrum(new LinkedList<>());
        spectra.setPublication(new LinkedList<>());
        spectra.setQualifierCVParam(createCVParam());
        return spectra;
    }

    public CVParam createCVParam() {
        CVParam cvParam = new CVParam();
        cvParam.setCv(new LinkedList<>());
        cvParam.setCvParamName("");
        cvParam.setCvTermAccession("");
        cvParam.setUnitAccession("");
        cvParam.setUnitCVRef("");
        cvParam.setUnitName("");
        cvParam.setUsrValue("");
        return cvParam;
    }


    public SEGap createSEGap() {
        SEGap seGap = new SEGap();
        seGap.setProof(createProof());
        seGap.setPositionInSequence(0);
        seGap.setGapValue(0);
        seGap.setConfidence(0);
        return seGap;
    }

    public SEModifiedAminoAcid createSEModifiedAminoAcid() {
        SEModifiedAminoAcid seModifiedAminoAcid = new SEModifiedAminoAcid();
        seModifiedAminoAcid.setPositionInSequence(0);
        seModifiedAminoAcid.setConfidence(0.0);
        seModifiedAminoAcid.setModifiedAminoAcid(createModifiedAminoAcid());
        return seModifiedAminoAcid;
    }
}
