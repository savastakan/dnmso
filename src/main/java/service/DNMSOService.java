package service;

import domain.*;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.RDF;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;


public class DNMSOService extends AbstractService {
    private final String baseURI = "https://github.com/savastakan/";
    private final HashMap<String,Spectrum> spectrumHashMap = new HashMap<>();
    private final HashMap<String, Modification> modificationHashMap = new HashMap<>();
    private final HashMap<String,AminoAcid> aminoAcidHashMap = new HashMap<>();
    private final HashMap<String,ModifiedAminoAcid> modifiedAminoAcidHashMap = new HashMap<>();

    @Override
    public String getServiceName() {
        return "DNMSO Service";
    }

    private Prediction getPrediction(OntModel model, Resource predictionIndividual) {
        ObjectFactory objectFactory = new ObjectFactory();
        Prediction prediction = objectFactory.createPrediction();
        for (StmtIterator it = predictionIndividual.listProperties(model.getProperty(baseURI + "hasSource")); it.hasNext(); ) {
            Statement iter = it.next();
            Resource spectrumIndividual = iter.getObject().asResource();
            Spectrum spectrum = getSpectrum(model, spectrumIndividual);
            prediction.getSpectrum().add(spectrum);
        }
        for (StmtIterator it = predictionIndividual.listProperties(model.getProperty(baseURI + "hasScore")); it.hasNext(); ) {
            Statement iter = it.next();
            Score score = getScore(model,iter.getObject().asResource());
            prediction.getScore().add(score);
        }
        Resource softwareIndividual = predictionIndividual.getPropertyResourceValue(model.getProperty(baseURI + "hasSoftware"));
        Software software = getSoftware(model, softwareIndividual);
        prediction.setSoftware(software);

        Resource sequenceIndividual = predictionIndividual.getPropertyResourceValue(model.getProperty(baseURI + "hasSequence"));
        Sequence sequence = getSequence(model, sequenceIndividual);
        prediction.setSequence(sequence);

        return prediction;
    }

    private Sequence getSequence(OntModel model, Resource sequenceIndividual) {
        Sequence sequence = new Sequence();
        sequence.setSequenceElement(new LinkedList<>());
        sequence.setCombinedConfidence(sequenceIndividual.getProperty(model.getProperty(baseURI +"hasCombinedConfidence")).getDouble());

        Statement hasCTerminalModification = sequenceIndividual.getProperty(model.getProperty(baseURI +"hasCTerminalModification"));
        if (hasCTerminalModification!= null){
            Resource cTerminalModificationIndividual = hasCTerminalModification.getObject().asResource();
            Modification cTerminalModification = getModification(model, cTerminalModificationIndividual);
            sequence.setCTerminalModification(cTerminalModification);
        }
        Statement hasNTerminalModification = sequenceIndividual.getProperty(model.getProperty(baseURI +"hasNTerminalModification"));
        if (hasNTerminalModification != null) {
            Resource nTerminalModificationIndividual = hasNTerminalModification.getObject().asResource();
            Modification nTerminalModification = getModification(model, nTerminalModificationIndividual);
            sequence.setNTerminalModification(nTerminalModification);
        }
        sequence.setPeptideSequence(sequenceIndividual.getProperty(model.getProperty(baseURI +"hasPeptideSequence")).getString());
        for (StmtIterator it = sequenceIndividual.listProperties(model.getProperty(baseURI +"hasSequenceElement")); it.hasNext(); ) {
            Statement iter = it.next();
            Resource sequenceElementIndividual = iter.getObject().asResource();
            String type = sequenceElementIndividual.getProperty(RDF.type).getObject().asResource().getLocalName();
            if (type.equals("SEGap")){
                SEGap seGap = getSEGap(model,sequenceElementIndividual);
                sequence.getSequenceElement().add(seGap);
            } else if (type.equals("SEAminoAcid")){
                SEAminoAcid seAminoAcid = getSEAminoAcid(model,sequenceElementIndividual);
                sequence.getSequenceElement().add(seAminoAcid);
            } else {
                SEModifiedAminoAcid seModifiedAminoAcid = getSEModifiedAminoAcid(model,sequenceElementIndividual);
                sequence.getSequenceElement().add(seModifiedAminoAcid);
            }

        }
        return sequence;

    }

    private SEModifiedAminoAcid getSEModifiedAminoAcid(OntModel model, Resource sequenceElementIndividual) {
        SEModifiedAminoAcid seModifiedAminoAcid = new SEModifiedAminoAcid();
        seModifiedAminoAcid.setModifiedAminoAcid(getModifiedAminoAcid(model,sequenceElementIndividual.getPropertyResourceValue(model.getProperty(baseURI +"hasModifiedAminoAcid"))));
        seModifiedAminoAcid.setConfidence(sequenceElementIndividual.getProperty(model.getProperty(baseURI +"hasConfidence")).getLong());
        seModifiedAminoAcid.setPositionInSequence(sequenceElementIndividual.getProperty(model.getProperty(baseURI +"hasPositionInSequence")).getLong());
        if(sequenceElementIndividual.getPropertyResourceValue(model.getProperty(baseURI +"hasProof"))!=null){
            seModifiedAminoAcid.setProof(getProof(model,sequenceElementIndividual.getPropertyResourceValue(model.getProperty(baseURI +"hasProof"))));
        }
        return seModifiedAminoAcid;
    }

    private SEAminoAcid getSEAminoAcid(OntModel model, Resource sequenceElementIndividual) {
        SEAminoAcid seAminoAcid = new SEAminoAcid();
        seAminoAcid.setAminoAcid(getAminoAcid(model,sequenceElementIndividual.getPropertyResourceValue(model.getProperty(baseURI +"hasAminoAcid"))));
        seAminoAcid.setPositionInSequence(sequenceElementIndividual.getProperty(model.getProperty(baseURI +"hasPositionInSequence")).getLong());
        if(sequenceElementIndividual.getPropertyResourceValue(model.getProperty(baseURI +"hasProof"))!= null){
            seAminoAcid.setProof(getProof(model,sequenceElementIndividual.getPropertyResourceValue(model.getProperty(baseURI +"hasProof"))));
        }
        seAminoAcid.setConfidence(sequenceElementIndividual.getProperty(model.getProperty(baseURI +"hasConfidence")).getLong());
        return seAminoAcid;
    }

    private SEGap getSEGap(OntModel model, Resource sequenceElementIndividual) {
        SEGap seGap = new SEGap();
        seGap.setGapValue(sequenceElementIndividual.getProperty(model.getProperty(baseURI +"hasGapValue")).getDouble());
        seGap.setConfidence(sequenceElementIndividual.getProperty(model.getProperty(baseURI +"hasConfidence")).getDouble());
        seGap.setPositionInSequence(sequenceElementIndividual.getProperty(model.getProperty(baseURI +"hasPositionInSequence")).getLong());
        seGap.setProof(getProof(model,sequenceElementIndividual.getProperty(model.getProperty(baseURI +"hasProof")).getObject().asResource()));
        return seGap;
    }

    private Proof getProof(OntModel model, Resource proofIndividual) {

        String type = proofIndividual.getProperty(RDF.type).getObject().asResource().getLocalName();
        if (type.equals("PeakValue")){
            PeakValue peakValue = new PeakValue();
            peakValue.setPeakIntensity(proofIndividual.getProperty(model.getProperty(baseURI +"hasPeakIntensity")).getDouble());
            peakValue.setPeakMZ(proofIndividual.getProperty(model.getProperty(baseURI +"hasPeakMZ")).getDouble());
            return peakValue;
        } else if (type.equals("PeakLink")){ // PeakLink
           PeakLink peakLink = new PeakLink();
           peakLink.setPeakId(proofIndividual.getProperty(model.getProperty(baseURI +"hasPeakId")).getLong());
           peakLink.setPeakLinkSpectrum(getSpectrum(model,proofIndividual.getProperty(model.getProperty(baseURI +"hasPeakLinkSpectrum")).getResource()));
           return peakLink;
        }
        return null;
    }



    private Software getSoftware(OntModel model, Resource softwareIndividual) {
        Software software = new Software();
        software.setSoftwareSetting(new LinkedList<>());
        software.setPublication(new LinkedList<>());
        software.setSoftwareVersion(softwareIndividual.getProperty(model.getProperty(baseURI +"hasSoftwareVersion")).getLiteral().getString());
        software.setSoftwareName(softwareIndividual.getProperty(model.getProperty(baseURI +"hasSoftwareName")).getLiteral().getString());
        for (StmtIterator it = softwareIndividual.listProperties(model.getProperty(baseURI +"hasPublication")); it.hasNext(); ) {
            Statement iter = it.next();
            Resource publicationIndividual = iter.getObject().asResource();
            Publication publication = getPublication(model,publicationIndividual);
            software.getPublication().add(publication);
        }

        for(StmtIterator it = softwareIndividual.listProperties(model.getProperty(baseURI +"hasSoftwareSetting")); it.hasNext(); ){
            Statement iter = it.next();
            Resource softwareSettingIndividual = iter.getObject().asResource();
            SoftwareSetting softwareSetting = getSoftwareSetting(model,softwareSettingIndividual);
            software.getSoftwareSetting().add(softwareSetting);
        }
        return software;
    }

    private SoftwareSetting getSoftwareSetting(OntModel model, Resource softwareSettingIndividual) {
        SoftwareSetting softwareSetting = new SoftwareSetting();
        softwareSetting.setSoftwareSettingName(softwareSettingIndividual.getProperty(model.getProperty(baseURI +"hasSoftwareSettingName")).getString());
        softwareSetting.setSoftwareSettingValue(softwareSettingIndividual.getProperty(model.getProperty(baseURI +"hasSoftwareSettingValue")).getString());
        return softwareSetting;
    }

    private Publication getPublication(OntModel model, Resource publicationIndividual) {
        Publication publication = new Publication();
        publication.setPubUrl(publicationIndividual.getProperty(model.getProperty(baseURI +"hasPubUrl")).getString());
        return publication;
    }

    private Score getScore(OntModel model, Resource scoreIndividual) {
        Score score = new Score();
        score.setLargerIsBetter(scoreIndividual.getProperty(model.getProperty(baseURI + "hasLargerIsBetter")).getLiteral().getBoolean());
        if (scoreIndividual.getProperty(model.getProperty(baseURI + "hasMainScore"))!= null){
            score.setMainScore(scoreIndividual.getProperty(model.getProperty(baseURI + "hasMainScore")).getLiteral().getBoolean());
        }
        score.setScoreName(scoreIndividual.getProperty(model.getProperty(baseURI + "hasScoreName")).getLiteral().getString());
        score.setScoreValue(scoreIndividual.getProperty(model.getProperty(baseURI + "hasScoreValue")).getLiteral().getDouble());
        return score;
    }

    private ModifiedAminoAcid getModifiedAminoAcid(OntModel model, Resource modifiedAminoAcidIndividual){

        String id = modifiedAminoAcidIndividual.getProperty(model.getProperty(baseURI+"hasModAAId")).getLiteral().getString();
        if(modifiedAminoAcidHashMap.containsKey(id)){
            return modifiedAminoAcidHashMap.get(id);
        } else  {
            ModifiedAminoAcid modifiedAminoAcid = new ModifiedAminoAcid();
            modifiedAminoAcid.setModAAId(id);
            AminoAcid aminoAcid = getAminoAcid(model,modifiedAminoAcidIndividual.getProperty(model.getProperty(baseURI+"hasAminoAcid")).getObject().asResource());
            Modification modification = getModification(model,modifiedAminoAcidIndividual.getProperty(model.getProperty(baseURI+"hasModification")).getObject().asResource());
            modifiedAminoAcid.setAminoAcid(aminoAcid);
            modifiedAminoAcid.setModification(modification);
            modifiedAminoAcidHashMap.put(modifiedAminoAcid.getModAAId(),modifiedAminoAcid);
            return modifiedAminoAcid;
        }
    }
    private AminoAcid getAminoAcid(OntModel model, Resource aminoAcidIndividual){
        Statement singleLetterCodeStatement = aminoAcidIndividual.getProperty(model.getProperty(baseURI+"hasSingleLetterCode"));
        if (singleLetterCodeStatement == null ){
            return null;
        }
        String singleLetterCode = singleLetterCodeStatement.getLiteral().getString();
        if (aminoAcidHashMap.containsKey(singleLetterCode)){
            return aminoAcidHashMap.get(singleLetterCode);
        } else {
            AminoAcid aminoAcid = new AminoAcid();
            aminoAcid.setSingleLetterCode(singleLetterCode);
            aminoAcid.setAminoAcidName(aminoAcidIndividual.getProperty(model.getProperty(baseURI+"hasAminoAcidName")).getLiteral().getString());
            aminoAcid.setThreeLetterCode(aminoAcidIndividual.getProperty(model.getProperty(baseURI+"hasThreeLetterCode")).getLiteral().getString());
            aminoAcid.setAverageMass(aminoAcidIndividual.getProperty(model.getProperty(baseURI+"hasAverageMass")).getLiteral().getDouble());
            aminoAcid.setMonoIsotopicMass(aminoAcidIndividual.getProperty(model.getProperty(baseURI+"hasMonoIsotopicMass")).getLiteral().getDouble());
            aminoAcidHashMap.put(singleLetterCode,aminoAcid);
            return aminoAcid;
        }
    }
    private Spectrum getSpectrum(OntModel model, Resource spectrumResource){
        String spectrumId = spectrumResource.getProperty(model.getProperty(baseURI+"hasSpectrumId")).getLiteral().getString();
        if(spectrumHashMap.containsKey(spectrumId)){
            return spectrumHashMap.get(spectrumId);
        }else{
            Spectrum spectrum = new Spectrum();
            spectrum.setSpectrumId(spectrumId);
            spectrum.setPrecursorMZ(spectrumResource.getProperty(model.getProperty(baseURI+"hasPrecursorMZ")).getLiteral().getDouble());
            spectrum.setScanId(spectrumResource.getProperty(model.getProperty(baseURI+"hasScanId")).getLiteral().getLong());
            spectrum.setPrecursorIntensity(spectrumResource.getProperty(model.getProperty(baseURI+"hasPrecursorIntensity")).getLiteral().getDouble());
            spectrum.setCsvData(spectrumResource.getProperty(model.getProperty(baseURI+"hasCsvData")).getLiteral().getString());
            spectrumHashMap.put(spectrumId, spectrum);
            return spectrum;
        }
    }

    private Modification getModification(OntModel model,Resource modificationIndividual){
        Statement idStatement =modificationIndividual.getProperty(model.getProperty(baseURI+ "hasPsiModRef"));
        if(idStatement == null){
            return null;
        }
        String id = idStatement.getLiteral().getString();
        if (modificationHashMap.containsKey(id)){
            return modificationHashMap.get(id);
        } else {
            Modification modification = new Modification();
            modification.setPsiModRef(id);
            modification.setMonoModificationMass(modificationIndividual.getProperty(model.getProperty(baseURI +"hasMonoModificationMass")).getLiteral().getDouble());
            if(modificationIndividual.getProperty(model.getProperty(baseURI+"hasAverageModificationMass"))!=null){
                modification.setAverageModificationMass(modificationIndividual.getProperty(model.getProperty(baseURI+"hasAverageModificationMass")).getLiteral().getDouble());
            }
            modification.setModificationName(modificationIndividual.getProperty(model.getProperty(baseURI+ "hasModificationName")).getLiteral().getString());
            modificationHashMap.put(modification.getModificationName(), modification);
            return  modification;
        }
    }


    public DNMSO read(File file) {


        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);

        FileManager.get().readModel(model, file.getPath() , baseURI, "TURTLE");

        Resource dnmsoR = model.getResource(baseURI + "DNMSO");
        String version = dnmsoR.getProperty(model.getProperty(baseURI + "hasVersion")).getLiteral().getString();
        DNMSOFactory dnmsoFactory = new DNMSOFactory();
        DNMSO dnmso = dnmsoFactory.createDNMSO();
        dnmso.setVersion(version);
        Resource modificationResource = model.getOntResource(baseURI + "Modification");
        if(modificationResource != null){
            for (ExtendedIterator<Individual> it = model.listIndividuals(modificationResource); it.hasNext(); ) {
                Resource modificationIndividual = it.next();
                Modification modification = getModification(model,modificationIndividual);
                dnmso.getModification().add(modification);
            }
        }
        Resource spectraResource = model.getOntResource(baseURI + "Spectra");
        if(spectraResource != null ){
            for (ExtendedIterator<Individual> it = model.listIndividuals(spectraResource); it.hasNext(); ) {
                Resource spectraIndividual = it.next();
                ObjectFactory objectFactory = new ObjectFactory();
                Spectra spectra = objectFactory.createSpectra();
                for (StmtIterator iter = spectraIndividual.listProperties(model.getProperty(baseURI + "hasSpectrum")); iter.hasNext(); ) {
                    RDFNode spectrumIndividual = iter.next().getObject();
                    Spectrum spectrum = getSpectrum(model,spectrumIndividual.asResource());
                    spectra.getSpectrum().add(spectrum);
                }
                dnmso.getSpectra().add(spectra);
            }
        }

        for (ExtendedIterator<Individual> it = model.listIndividuals(model.getOntResource(baseURI + "AminoAcid")); it.hasNext(); ) {
            Resource aminoAcidIndividual = it.next();
            AminoAcid aminoAcid = getAminoAcid(model,aminoAcidIndividual);
            dnmso.getAminoAcid().add(aminoAcid);
        }

        Resource modifiedAminoAcidResource = model.getOntResource(baseURI + "ModifiedAminoAcid");
        if(modifiedAminoAcidResource != null){
            for (ExtendedIterator<Individual> it = model.listIndividuals(modifiedAminoAcidResource); it.hasNext(); ) {
                Resource modifiedAminoAcidIndividual = it.next();
                ModifiedAminoAcid modifiedAminoAcid = getModifiedAminoAcid(model,modifiedAminoAcidIndividual);
                dnmso.getModifiedAminoAcid().add(modifiedAminoAcid);
            }
        }
        Resource predictionResource = model.getOntResource(baseURI + "Prediction");
        if (predictionResource != null){
            for (ExtendedIterator<Individual> it = model.listIndividuals(predictionResource); it.hasNext(); ) {
                Resource predictionAminoAcidIndividual = it.next();
                Prediction prediction = getPrediction(model,predictionAminoAcidIndividual);
                dnmso.getPrediction().add(prediction);
            }
        }
        // model.write(System.out,"TURTLE");

        return dnmso;
    }


    private Resource getSpectrumIndividual(OntModel model, Spectrum spectrum) {
        Resource spectrumIndividual = model.getIndividual(spectrum.getSpectrumId());
        if (spectrumIndividual == null) {
            spectrumIndividual = model.createIndividual(spectrum.getSpectrumId(), model.createResource("Spectrum"));
            spectrumIndividual.addProperty(model.createProperty("hasSpectrumId"), model.createTypedLiteral(spectrum.getSpectrumId(), XSDDatatype.XSDstring));
            spectrumIndividual.addProperty(model.createProperty("hasPrecursorIntensity"), model.createTypedLiteral(spectrum.getPrecursorIntensity(), XSDDatatype.XSDdouble));
            spectrumIndividual.addProperty(model.createProperty("hasPrecursorMZ"), model.createTypedLiteral(spectrum.getPrecursorMZ(), XSDDatatype.XSDdouble));
            spectrumIndividual.addProperty(model.createProperty("hasScanId"), model.createTypedLiteral(spectrum.getScanId(), XSDDatatype.XSDlong));
            spectrumIndividual.addProperty(model.createProperty("hasCsvData"), model.createTypedLiteral(spectrum.getCsvData(), XSDDatatype.XSDstring));
        }
        return spectrumIndividual;
    }

    private Resource getSoftwareIndividual(OntModel model, Software software) {
        Resource softwareIndividual = model.createIndividual(model.createResource("Software"));
        softwareIndividual.addProperty(model.createProperty("hasSoftwareName"), model.createTypedLiteral(software.getSoftwareName(), XSDDatatype.XSDstring));
        softwareIndividual.addProperty(model.createProperty("hasSoftwareVersion"), model.createTypedLiteral(software.getSoftwareVersion(), XSDDatatype.XSDstring));
        for (Publication publication : software.getPublication()) {
            Resource publicationIndividual = model.createIndividual(model.createResource("Publication"));
            publicationIndividual.addProperty(model.createProperty("hasPubUrl"), model.createTypedLiteral(publication.getPubUrl(), XSDDatatype.XSDanyURI));
            softwareIndividual.addProperty(model.createProperty("hasPublication"), publicationIndividual);
        }
        for (SoftwareSetting softwareSetting : software.getSoftwareSetting()) {
            Resource softwareSettingIndividual = model.createIndividual(model.createResource("SoftwareSetting"));
            softwareSettingIndividual.addProperty(model.createProperty("hasSoftwareSettingName"), model.createTypedLiteral(softwareSetting.getSoftwareSettingName(), XSDDatatype.XSDstring));
            softwareSettingIndividual.addProperty(model.createProperty("hasSoftwareSettingValue"), model.createTypedLiteral(softwareSetting.getSoftwareSettingValue(), XSDDatatype.XSDstring));
            softwareIndividual.addProperty(model.createProperty("hasSoftwareSetting"), softwareSettingIndividual);
        }
        return softwareIndividual;
    }

    private Resource getSEGapIndividual(OntModel model, SEGap seGap) {
        Resource gapIndividual = model.createIndividual(model.createResource("SEGap"));
        gapIndividual.addProperty(model.createProperty("hasGapValue"), model.createTypedLiteral(seGap.getGapValue(), XSDDatatype.XSDdouble));
        gapIndividual.addProperty(model.createProperty("hasConfidence"), model.createTypedLiteral(seGap.getConfidence(), XSDDatatype.XSDdouble));
        gapIndividual.addProperty(model.createProperty("hasPositionInSequence"), model.createTypedLiteral(seGap.getPositionInSequence(), XSDDatatype.XSDlong));
        Resource proofIndividual = model.createIndividual(model.createResource("Proof"));
        gapIndividual.addProperty(model.createProperty("hasProof"), proofIndividual);
        return gapIndividual;
    }

    private Resource getAminoAcidIndividual(OntModel model, AminoAcid aminoAcid) {
        Resource aminoAcidIndividual = model.getIndividual(aminoAcid.getAminoAcidName());
        if (aminoAcidIndividual == null) {
            aminoAcidIndividual = model.createIndividual(aminoAcid.getSingleLetterCode(), model.createResource("AminoAcid"));
            aminoAcidIndividual.addProperty(model.createProperty("hasAminoAcidName"), model.createTypedLiteral(aminoAcid.getAminoAcidName(), XSDDatatype.XSDstring));
            aminoAcidIndividual.addProperty(model.createProperty("hasAverageMass"), model.createTypedLiteral(aminoAcid.getAverageMass(), XSDDatatype.XSDdouble));
            aminoAcidIndividual.addProperty(model.createProperty("hasMonoIsotopicMass"), model.createTypedLiteral(aminoAcid.getMonoIsotopicMass(), XSDDatatype.XSDdouble));
            aminoAcidIndividual.addProperty(model.createProperty("hasSingleLetterCode"), model.createTypedLiteral(aminoAcid.getSingleLetterCode(), XSDDatatype.XSDstring));
            if (aminoAcid.getThreeLetterCode() != null) {
                aminoAcidIndividual.addProperty(model.createProperty("hasThreeLetterCode"), model.createTypedLiteral(aminoAcid.getThreeLetterCode(), XSDDatatype.XSDstring));
            }
        }
        return aminoAcidIndividual;
    }

    private Resource getSEAminoAcidIndividual(OntModel model, SEAminoAcid seAminoAcid) {
        Resource seAminoAcidIndividual = model.createIndividual(model.createResource("SEAminoAcid"));
        if (seAminoAcid.getAminoAcid() != null) {
            seAminoAcidIndividual.addProperty(model.createProperty("hasAminoAcid"), getAminoAcidIndividual(model, seAminoAcid.getAminoAcid()));
        }
        seAminoAcidIndividual.addProperty(model.createProperty("hasConfidence"), model.createTypedLiteral(seAminoAcid.getConfidence(), XSDDatatype.XSDdouble));
        seAminoAcidIndividual.addProperty(model.createProperty("hasPositionInSequence"), model.createTypedLiteral(seAminoAcid.getPositionInSequence(), XSDDatatype.XSDlong));
        if (seAminoAcid.getAminoAcid() != null) {
            seAminoAcidIndividual.addProperty(model.createProperty("hasAminoAcid"), getAminoAcidIndividual(model, seAminoAcid.getAminoAcid()));
        }

        Proof proof = seAminoAcid.getProof();
        if (proof != null) {
            if (proof instanceof PeakLink) {
                PeakLink peakLink = (PeakLink) proof;
                Resource peakLinkIndividual = model.createIndividual(model.createResource("PeakLink"));
                peakLinkIndividual.addProperty(model.createProperty("hasPeakId"), model.createTypedLiteral(peakLink.getPeakId(), XSDDatatype.XSDlong));
                peakLinkIndividual.addProperty(model.createProperty("hasPeakLinkSpectrum"), model.createTypedLiteral(peakLink.getPeakLinkSpectrum(), XSDDatatype.XSDlong));
                seAminoAcidIndividual.addProperty(model.createProperty("hasProof"), peakLinkIndividual);
            }
            if (proof instanceof PeakValue) {
                PeakValue peakValue = (PeakValue) proof;
                Resource peakValueIndividual = model.createIndividual(model.createResource("PeakValue"));
                peakValueIndividual.addProperty(model.createProperty("hasPeakIntensity"), model.createTypedLiteral(peakValue.getPeakIntensity(), XSDDatatype.XSDdouble));
                peakValueIndividual.addProperty(model.createProperty("hasPeakMZ"), model.createTypedLiteral(peakValue.getPeakMZ(), XSDDatatype.XSDdouble));
                seAminoAcidIndividual.addProperty(model.createProperty("hasProof"), peakValueIndividual);
            }
        }
        return seAminoAcidIndividual;
    }

    private Resource getModificationIndividual(OntModel model, Modification modification) {
        Resource modificationIndividual = model.getIndividual(modification.getModificationName());
        if (modificationIndividual == null) {
            modificationIndividual = model.createIndividual(modification.getPsiModRef(), model.createResource("Modification"));
            modificationIndividual.addProperty(model.createProperty("hasModificationName"), model.createTypedLiteral(modification.getModificationName(), XSDDatatype.XSDstring));
            if (modification.getAverageModificationMass() != null) modificationIndividual.addProperty(model.createProperty("hasAverageModificationMass"), model.createTypedLiteral(modification.getAverageModificationMass(), XSDDatatype.XSDdouble));
            if(modification.getMonoModificationMass() != null) modificationIndividual.addProperty(model.createProperty("hasMonoModificationMass"), model.createTypedLiteral(modification.getMonoModificationMass(), XSDDatatype.XSDdouble));
            if (modification.getPsiModRef() != null) {
                modificationIndividual.addProperty(model.createProperty("hasPsiModRef"), model.createTypedLiteral(modification.getPsiModRef(), XSDDatatype.XSDstring));
            }
        }
        return modificationIndividual;

    }

    private Resource getModifiedAminoAcidIndividual(OntModel model, ModifiedAminoAcid modifiedAminoAcid) {
        Resource modifiedAminoAcidIndividual = model.createIndividual(modifiedAminoAcid.getModAAId(), model.createResource("ModifiedAminoAcid"));
        AminoAcid aminoAcid = modifiedAminoAcid.getAminoAcid();
        if (aminoAcid != null) {
            modifiedAminoAcidIndividual.addProperty(model.createProperty("hasAminoAcid"), getAminoAcidIndividual(model, aminoAcid));
        }

        Modification modification = modifiedAminoAcid.getModification();
        if (modification != null) {
            modifiedAminoAcidIndividual.addProperty(model.createProperty("hasModification"), getModificationIndividual(model, modification));

        }
        modifiedAminoAcidIndividual.addProperty(model.createProperty("hasModAAId"), model.createTypedLiteral(modifiedAminoAcid.getModAAId(), XSDDatatype.XSDstring));
        return modifiedAminoAcidIndividual;
    }

    private Resource getProofIndividual(OntModel model, Proof proof) {
        Resource proofIndividual = null;
        if (proof instanceof PeakLink) {
            PeakLink peakLink = (PeakLink) proof;
            proofIndividual = model.createIndividual(model.createResource("PeakLink"));
            proofIndividual.addProperty(model.createProperty("hasPeakId"), model.createTypedLiteral(peakLink.getPeakId(), XSDDatatype.XSDlong));
            proofIndividual.addProperty(model.createProperty("hasPeakLinkSpectrum"), model.createTypedLiteral(peakLink.getPeakLinkSpectrum(), XSDDatatype.XSDlong));
        }
        if (proof instanceof PeakValue) {
            PeakValue peakValue = (PeakValue) proof;
            proofIndividual = model.createIndividual(model.createResource("PeakValue"));
            proofIndividual.addProperty(model.createProperty("hasPeakIntensity"), model.createTypedLiteral(peakValue.getPeakIntensity(), XSDDatatype.XSDdouble));
            proofIndividual.addProperty(model.createProperty("hasPeakMZ"), model.createTypedLiteral(peakValue.getPeakMZ(), XSDDatatype.XSDdouble));
        }
        return proofIndividual;
    }

    private Resource getSEModifiedAminoAcidIndividual(OntModel model, SEModifiedAminoAcid seModifiedAminoAcid) {
        Resource seModifiedAminoAcidIndividual = model.createIndividual(model.createResource("SEModifiedAminoAcid"));
        ModifiedAminoAcid modifiedAminoAcid = seModifiedAminoAcid.getModifiedAminoAcid();
        if (modifiedAminoAcid != null) {
            seModifiedAminoAcidIndividual.addProperty(model.createProperty("hasModifiedAminoAcid"), getModifiedAminoAcidIndividual(model, modifiedAminoAcid));
        }
        Proof proof = seModifiedAminoAcid.getProof();
        if (proof != null) {
            seModifiedAminoAcidIndividual.addProperty(model.createProperty("hasProof"), getProofIndividual(model, proof));
        }
        seModifiedAminoAcidIndividual.addProperty(model.createProperty("hasConfidence"), model.createTypedLiteral(seModifiedAminoAcid.getConfidence(), XSDDatatype.XSDdouble));
        seModifiedAminoAcidIndividual.addProperty(model.createProperty("hasPositionInSequence"), model.createTypedLiteral(seModifiedAminoAcid.getPositionInSequence(), XSDDatatype.XSDlong));
        return seModifiedAminoAcidIndividual;

    }


    private Resource getSequenceIndividual(OntModel model, Sequence sequence) {
        Resource sequenceIndividual = model.createIndividual(model.createResource("Sequence"));
        sequenceIndividual.addProperty(model.createProperty("hasPeptideSequence"), model.createTypedLiteral(sequence.getPeptideSequence(), XSDDatatype.XSDstring));
        sequenceIndividual.addProperty(model.createProperty("hasCombinedConfidence"), model.createTypedLiteral(sequence.getCombinedConfidence(), XSDDatatype.XSDdouble));
        if (sequence.getCTerminalModification() != null) {
            sequenceIndividual.addProperty(model.createProperty("hasCTerminalModification"), getModificationIndividual(model, sequence.getCTerminalModification()));
        }
        if (sequence.getNTerminalModification() != null) {
            sequenceIndividual.addProperty(model.createProperty("hasNTerminalModification"), getModificationIndividual(model, sequence.getNTerminalModification()));
        }

        for (SequenceElement sequenceElement : sequence.getSequenceElement()) {
            if (sequenceElement instanceof SEGap) {
                sequenceIndividual.addProperty(model.createProperty("hasSequenceElement"), getSEGapIndividual(model, (SEGap) sequenceElement));
            }
            if (sequenceElement instanceof SEAminoAcid) {
                sequenceIndividual.addProperty(model.createProperty("hasSequenceElement"), getSEAminoAcidIndividual(model, (SEAminoAcid) sequenceElement));
            }
            if (sequenceElement instanceof SEModifiedAminoAcid) {
                sequenceIndividual.addProperty(model.createProperty("hasSequenceElement"), getSEModifiedAminoAcidIndividual(model, (SEModifiedAminoAcid) sequenceElement));
            }
        }
        return sequenceIndividual;
    }

    private Resource getScoreIndividual(OntModel model, Score score) {
        Resource scoreIndividual = model.createIndividual(model.createResource("Score"));
        scoreIndividual.addProperty(model.createProperty("hasScoreName"), model.createTypedLiteral(score.getScoreName(), XSDDatatype.XSDstring));
        scoreIndividual.addProperty(model.createProperty("hasLargerIsBetter"), model.createTypedLiteral(score.getLargerIsBetter(), XSDDatatype.XSDboolean));
        scoreIndividual.addProperty(model.createProperty("hasScoreValue"), model.createTypedLiteral(score.getScoreValue(), XSDDatatype.XSDdouble));
        return scoreIndividual;
    }

    private Resource getPredictionIndividual(OntModel model, Prediction prediction) {
        Resource predictionIndividual = model.createIndividual(model.createResource("Prediction"));
        for (Spectrum spectrum : prediction.getSpectrum()) {
            predictionIndividual.addProperty(model.createProperty("hasSource"), getSpectrumIndividual(model, spectrum));
        }
        for (Score score : prediction.getScore()) {
            predictionIndividual.addProperty(model.createProperty("hasScore"), getScoreIndividual(model, score));
        }
        predictionIndividual.addProperty(model.createProperty("hasSoftware"), getSoftwareIndividual(model, prediction.getSoftware()));
        predictionIndividual.addProperty(model.createProperty("hasSequence"), getSequenceIndividual(model, prediction.getSequence()));
        return predictionIndividual;
    }

    public DNMSO write() {
        String path = getProperties().get(ServiceTag.OUTPUT.toString());
        DNMSO dnmso = getDNMSO();
        OntModel model = ModelFactory.createOntologyModel();
        //model.setNsPrefix("dnmso","https://github.com/savastakan/");
        Resource dnmsoResource = model.createResource("DNMSO");
        dnmsoResource.addLiteral(model.createProperty("hasVersion"), model.createTypedLiteral("2.0", XSDDatatype.XSDdouble));
        for (Prediction prediction : dnmso.getPrediction()) {
            dnmsoResource.addProperty(model.createProperty("hasPrediction"), getPredictionIndividual(model, prediction));
        }
        for (ModifiedAminoAcid modifiedAminoAcid : dnmso.getModifiedAminoAcid()) {
            dnmsoResource.addProperty(model.createProperty("hasModifiedAminoAcid"), getModifiedAminoAcidIndividual(model, modifiedAminoAcid));
        }
        for (Modification modification : dnmso.getModification()) {
            dnmsoResource.addProperty(model.createProperty("hasModification"), getModificationIndividual(model, modification));
        }

        for (Spectra spectra : dnmso.getSpectra()) {
            Resource spectraIndividual = model.createIndividual(model.createResource("Spectra"));
            for (Spectrum spectrum : spectra.getSpectrum()) {
                spectraIndividual.addProperty(model.createProperty("hasSpectrum"), getSpectrumIndividual(model, spectrum));
            }
            dnmsoResource.addProperty(model.createProperty("hasSpectra"), spectraIndividual);
        }
        for (AminoAcid aminoAcid : dnmso.getAminoAcid()) {
            dnmsoResource.addProperty(model.createProperty("hasAminoAcid"), getAminoAcidIndividual(model, aminoAcid));
        }

        OutputStream out = null;
        try {
            out = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        model.write(out, "TURTLE");
        return getDNMSO();
    }
    private DNMSO read(){
        String path = getProperties().get(ServiceTag.PREDICTION_FILE_PATH.toString());
        return read(new File(path));
    }

    @Override
    public DNMSO run(DNMSO dnmso, String[] args) {
        processSettings(dnmso, args);
        if (getProperties().get(ServiceTag.COMMAND.toString()).equals("write")) return write();
        if (getProperties().get(ServiceTag.COMMAND.toString()).equals("read")) return read();
        return null;
    }

    @Override
    public boolean isValid(File file) {
        try{
            read(file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
