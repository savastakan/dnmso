import mslib.Peak;
import mslib.MassSpectrum;

import org.junit.Test;

import domain.*;
import facade.FacadeFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;


public class FullDNMSOTest {
	
    public Collection<Peak> convertCvsToPeak(String cvs){
    	Peak peak = new Peak();
    	StringTokenizer st = new StringTokenizer(cvs,",;");
    	Collection<Peak> peaks = new ArrayList<Peak>();
    	while (st.hasMoreElements()) {
			String x = (String) st.nextElement();
			String y = (String) st.nextElement();
			peak.set(Double.valueOf(x), Double.valueOf(y));
			peaks.add(peak);
		}
    	return peaks;
    }
    @Test
    public void createFullDNMSO(){
        DnmsoFactory dnmsoFactory = new DnmsoFactory();
        DNMSO dnmso = dnmsoFactory.createDnmso();

        dnmso.setVersion("1.0");
        DNMSO.Predictions predictions = new DNMSO.Predictions();
        DNMSO.Predictions.Software software = new DNMSO.Predictions.Software();
        software.setName("Example Program");
        software.setVersion("1.0");

        DNMSO.Predictions.Software.Settings settings = new DNMSO.Predictions.Software.Settings();
        DNMSO.Predictions.Software.Settings.Setting setting = new DNMSO.Predictions.Software.Settings.Setting();
        setting.setName("Setting");
        setting.setValue("1");
        settings.getSetting().add(setting);
        software.setSettings(settings);
        predictions.setSoftware(software);

        MassSpectrum spectrum = new MassSpectrum();
        spectrum.setFileName("example.data");
        spectrum.setScanNumber(1);
        spectrum.addAll(convertCvsToPeak("878.41,1;1.0,1;88.03,1;159.06,1;290.11,1;374.20,1;486.22,1;570.31,1;701.35,1;772.38,1;859.4,1"));;
        spectrum.setPrecursorMass(5.0);
        spectrum.setPrecursorIntensity(42.5);
        dnmso.getSpectra().addSpectrum(spectrum);

        spectrum = new MassSpectrum();
        spectrum.setFileName("example.data");
        spectrum.setScanNumber(2);
        spectrum.addAll(convertCvsToPeak("912.41,1;22.0,1;12.03,1;132.06,1;142.11,1;23.20,1;456.22,1;45.31,1;567.35,1;132.38,1;789.4,1"));
        spectrum.setPrecursorMass(3.0);
        spectrum.setPrecursorIntensity(123.5);
        dnmso.getSpectra().addSpectrum(spectrum);

        spectrum = new MassSpectrum();
        spectrum.setFileName("example.data");
        spectrum.setScanNumber(3);
        spectrum.addAll(convertCvsToPeak("12.41,1;34.0,1;5.03,1;124.06,1;34.11,1;56.20,1;678.22,1;45.31,1;15.35,1;345.38,1;345.4,1"));
        spectrum.setPrecursorMass(8.0);
        spectrum.setPrecursorIntensity(75.5);
        dnmso.getSpectra().addSpectrum(spectrum);

        spectrum = new MassSpectrum();
        spectrum.setFileName("example.data");
        spectrum.setScanNumber(4);
        spectrum.addAll(convertCvsToPeak("34.41,1;27.0,1;234.03,1;46.06,1;345.11,1;56.20,1;567.22,1;12.31,1;234.35,1;45.38,1;564.4,1"));
        spectrum.setPrecursorMass(12.0);
        spectrum.setPrecursorIntensity(62.5);
        dnmso.getSpectra().addSpectrum(spectrum);


        Prediction prediction = new Prediction();
        prediction.setAssumedCharge(1);

        Prediction.Sources sources = new Prediction.Sources();
        sources.setMerge(true);
        Prediction.Sources.Source source = new Prediction.Sources.Source();
        source.setFileName("example.data");
        source.setScanId("1");
        sources.getSource().add(source);
        source = new Prediction.Sources.Source();
        source.setFileName("example.data");
        source.setScanId("2");
        sources.getSource().add(source);
        prediction.getSources().add(sources);


        sources = new Prediction.Sources();
        sources.setMerge(false);
        source = new Prediction.Sources.Source();
        source.setFileName("example.data");
        source.setScanId("3");
        sources.getSource().add(source);

        source = new Prediction.Sources.Source();
        source.setFileName("example.data");
        source.setScanId("4");
        sources.getSource().add(source);
        prediction.getSources().add(sources);

        Prediction.Sequence sequence = new Prediction.Sequence();
        sequence.setSequence("KLS");
        sequence.setCalculatedMass(134.7);
        sequence.setConfidence(0.789);
        AminoAcid aminoAcid = new AminoAcid();
        aminoAcid.setCharacter("K");
        aminoAcid.setConfidence(0.54);
        aminoAcid.setPos(1);

        Proof proof = new Proof();
        proof.setMz(34.41);
        proof.setIntensity(1.0);
        proof.setPos(3);
        aminoAcid.getProof().add(proof);

        proof = new Proof();
        proof.setMz(234.03);
        proof.setIntensity(1.0);
        proof.setPos(3);
        aminoAcid.getProof().add(proof);
        sequence.getAminoAcidOrModifiedAminoAcidOrGap().add(aminoAcid);

        Prediction.Sequence.Gap gap = new Prediction.Sequence.Gap();
        gap.setConfidence(0.54);
        gap.setPos(2);
        gap.setValue("345.11");

        proof = new Proof();
        proof.setMz(678.22);
        proof.setIntensity(1.0);
        proof.setPos(2);
        gap.getProof().add(proof);

        proof = new Proof();
        proof.setMz(15.35);
        proof.setIntensity(1.0);
        proof.setPos(2);
        gap.getProof().add(proof);
        sequence.getAminoAcidOrModifiedAminoAcidOrGap().add(gap);

        ModifiedAminoAcid modifiedAminoAcid = new ModifiedAminoAcid();
        modifiedAminoAcid.setConfidence(0.84);
        modifiedAminoAcid.setModificationName("ExampleModification");
        modifiedAminoAcid.setPos(3);

        proof = new Proof();
        proof.setMz(678.22);
        proof.setIntensity(1.0);
        proof.setPos(1);
        modifiedAminoAcid.getProof().add(proof);

        proof = new Proof();
        proof.setMz(15.35);
        proof.setIntensity(1.0);
        proof.setPos(1);
        modifiedAminoAcid.getProof().add(proof);
        sequence.getAminoAcidOrModifiedAminoAcidOrGap().add(modifiedAminoAcid);
        prediction.setSequence(sequence);

        Modification modification = new Modification();
        modification.setName("ExampleModification");
        modification.setMass(12.5);
        Modification.ModAminoAcid modAminoAcid = new Modification.ModAminoAcid();
        modAminoAcid.setAverageMass(12.5);
        modAminoAcid.setCharacter("S");
        modAminoAcid.setMonoIsotopicMass(15.4);
        predictions.getPrediction().add(prediction);
        modification.setModAminoAcid(modAminoAcid);
        dnmso.getModifications().getModification().add(modification) ;
        dnmso.getPredictions().add(predictions);


        FacadeFactory facadeFactory = new FacadeFactory();
        facadeFactory.getFacade().write(dnmso,"data"+ File.separator+"case.dnmso");
    }
}
