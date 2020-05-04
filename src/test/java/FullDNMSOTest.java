import org.junit.Test;

import domain.*;
import facade.FacadeFactory;

import java.io.File;
import java.util.*;


public class FullDNMSOTest {

    @Test
    public void createFullDNMSO(){
        DNMSOFactory dnmsoFactory = new DNMSOFactory();
        DNMSO dnmso = dnmsoFactory.createDNMSO();
        dnmso.setVersion("2.0");
        dnmso.setModification(new LinkedList<>());
        dnmso.setPrediction(new LinkedList<>());
        dnmso.setSpectra(new LinkedList<>());
        dnmso.setAminoAcid(new LinkedList<>());
        dnmso.setModifiedAminoAcid(new LinkedList<>());

        dnmso.setVersion("1.0");
        Software software = new Software();
        software.setSoftwareSetting(new LinkedList<>());
        software.setSoftwareName("Example Program");
        software.setSoftwareVersion("1.0");

        SoftwareSetting softwareSetting = new SoftwareSetting();
        softwareSetting.setSoftwareSettingName("Setting");
        softwareSetting.setSoftwareSettingValue("1");
        software.getSoftwareSetting().add(softwareSetting);
        software.setPublication(new LinkedList<>());

        Spectra spectra = new Spectra();
        spectra.setSpectrum(new LinkedList<>());
        List<Spectrum> sources = new LinkedList<>();
        Spectrum spectrum = new Spectrum();
        spectrum.setSpectrumId("1");
        spectrum.setScanId(1);
        spectrum.setCsvData("878.41,1;1.0,1;88.03,1;159.06,1;290.11,1;374.20,1;486.22,1;570.31,1;701.35,1;772.38,1;859.4,1");
        spectrum.setPrecursorMZ(5.0);
        spectrum.setPrecursorIntensity(42.5);
        spectra.getSpectrum().add(spectrum);
        sources.add(spectrum);

        spectrum = new Spectrum();
        spectrum.setSpectrumId("2");
        spectrum.setScanId(2);
        spectrum.setCsvData("912.41,1;22.0,1;12.03,1;132.06,1;142.11,1;23.20,1;456.22,1;45.31,1;567.35,1;132.38,1;789.4,1");
        spectrum.setPrecursorMZ(3.0);
        spectrum.setPrecursorIntensity(123.5);
        spectra.getSpectrum().add(spectrum);
        sources.add(spectrum);

        spectrum = new Spectrum();
        spectrum.setSpectrumId("3");
        spectrum.setScanId(3);
        spectrum.setCsvData("12.41,1;34.0,1;5.03,1;124.06,1;34.11,1;56.20,1;678.22,1;45.31,1;15.35,1;345.38,1;345.4,1");
        spectrum.setPrecursorMZ(8.0);
        spectrum.setPrecursorIntensity(75.5);
        spectra.getSpectrum().add(spectrum);
        sources.add(spectrum);

        spectrum = new Spectrum();
        spectrum.setSpectrumId("4");
        spectrum.setScanId(4);
        spectrum.setCsvData("34.41,1;27.0,1;234.03,1;46.06,1;345.11,1;56.20,1;567.22,1;12.31,1;234.35,1;45.38,1;564.4,1");
        spectrum.setPrecursorMZ(12.0);
        spectrum.setPrecursorIntensity(62.5);
        spectra.getSpectrum().add(spectrum);
        sources.add(spectrum);


        Prediction prediction = new Prediction();
        prediction.setScore(new LinkedList<>());
        prediction.setSpectrum(sources);
        prediction.setSoftware(software);

        Sequence sequence = new Sequence();
        sequence.setSequenceElement(new LinkedList<>());
        sequence.setPeptideSequence("KLS");
        sequence.setCombinedConfidence(0.789);

        prediction.setSequence(sequence);

        AminoAcid aminoAcid = new AminoAcid();
        aminoAcid.setAminoAcidName("K");
        aminoAcid.setSingleLetterCode("K");
        aminoAcid.setAverageMass(128.1741);
        aminoAcid.setMonoIsotopicMass(128.09496);
        dnmso.getAminoAcid().add(aminoAcid);

        SEAminoAcid seAminoAcid = new SEAminoAcid();

        PeakValue peakValue = new PeakValue();
        peakValue.setPeakMZ(34.41);
        peakValue.setPeakIntensity(1.0);
        seAminoAcid.setProof(peakValue);
        seAminoAcid.setAminoAcid(aminoAcid);
        sequence.getSequenceElement().add(seAminoAcid);

        SEGap seGap = new SEGap();
        seGap.setConfidence(0.54);
        seGap.setPositionInSequence(2);
        seGap.setGapValue(345.11);
        seGap.setProof(peakValue);

        peakValue = new PeakValue();
        peakValue.setPeakMZ(678.22);
        peakValue.setPeakIntensity(1.0);
        seGap.setProof(peakValue);

        sequence.getSequenceElement().add(seGap);

        ModifiedAminoAcidFactory modifiedAminoAcidFactory = new ModifiedAminoAcidFactory();
        ModifiedAminoAcid modifiedAminoAcid = modifiedAminoAcidFactory.createModifiedAminoAcid("K",100.0,100.0);
        SEModifiedAminoAcid seModifiedAminoAcid = new SEModifiedAminoAcid();
        seModifiedAminoAcid.setConfidence(0.84);
        seModifiedAminoAcid.setPositionInSequence(3);
        seModifiedAminoAcid.setModifiedAminoAcid(modifiedAminoAcid);
        dnmso.getModifiedAminoAcid().add(modifiedAminoAcid);

        peakValue = new PeakValue();
        peakValue.setPeakMZ(678.22);
        peakValue.setPeakIntensity(1.0);
        seModifiedAminoAcid.setProof(peakValue);

        sequence.getSequenceElement().add(seModifiedAminoAcid);
        prediction.setSequence(sequence);

        modifiedAminoAcid = modifiedAminoAcidFactory.createModifiedAminoAcid("K",100.0,100.0);

        seModifiedAminoAcid.setModifiedAminoAcid(modifiedAminoAcid);
        seModifiedAminoAcid.setConfidence(0.84);
        seModifiedAminoAcid.setPositionInSequence(3);
        dnmso.getModification().add(modifiedAminoAcid.getModification());
        dnmso.getModifiedAminoAcid().add(modifiedAminoAcid);
        dnmso.getPrediction().add(prediction);
        dnmso.getSpectra().add(spectra);

        FacadeFactory facadeFactory = new FacadeFactory();
        facadeFactory.getFacade().write(dnmso,"data"+ File.separator+"case.dnmso");
    }
}
