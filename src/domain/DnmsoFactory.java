/*
 *
 */
package domain;

import domain.DNMSO.Modifications;
import domain.DNMSO.Predictions;
import domain.DNMSO.Predictions.Software;
import domain.DNMSO.Predictions.Software.Settings;
import domain.DNMSO.Predictions.Software.Settings.Setting;
import domain.DNMSO.Spectra;
import domain.Modification.ModAminoAcid;
import domain.Prediction.Sequence;
import domain.Prediction.Sequence.Gap;
import domain.Prediction.Sources;
import domain.Prediction.Sources.Source;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mslib.Peak;
import mslib.MassSpectrum;




// TODO: Auto-generated Javadoc
/**
 * A factory for creating Dnml objects.
 * @author  ST
 * @version  $Revision: 1.0 $
 */
public class DnmsoFactory {
	
	
	/**
	 * Creates a new Dnml object.
	 *

	 * @return the dnmso */
	public DNMSO createDnmso(){
		ObjectFactory Objectfactory = new ObjectFactory();
		DNMSO DNMSO = Objectfactory.createDNML();
		DNMSO.setVersion("1.0");
		DNMSO.setSpectra(new Spectra());
		DNMSO.setModifications(new Modifications());
		return DNMSO;
	}
	
	public DNMSO createHashTableForSpectra(DNMSO dnmso){
		for (Spectrum spectrum : dnmso.getSpectra().spectrum){
			MassSpectrum massSpectrum = new MassSpectrum();
			massSpectrum.setPrecursorIntensity(spectrum.precursorIntensity);
			massSpectrum.setPrecursorMass(spectrum.precursorMZ);		
			massSpectrum.setScanNumber(spectrum.scanId);
			
			if (spectrum.csv != null){
				StringTokenizer st = new StringTokenizer(",;");
				while (st.hasMoreElements()) {
					String x = (String) st.nextElement();
					String y =  (String) st.nextElement();
					Peak peak = new Peak();
					peak.set(Double.parseDouble(x), Double.parseDouble(y));
					massSpectrum.add(peak);
					
				}
			}
			massSpectrum.setLink(spectrum.link);
			massSpectrum.setFileName(spectrum.fileName);
			dnmso.getSpectra().massSpectra.put( spectrum.fileName+":"+ spectrum.scanId, massSpectrum);
		}
		
		return dnmso;
	}

	/**
	 * Adds the software.
	 *
	 * @param targetDNMSO the target dnmso
	 * @param softwareName the software name
	 * @param version the version
	 * @return the software
	 */
	public Software addSoftware(DNMSO targetDNMSO, String softwareName,String version){
		Software software = new Software();
		software.setSettings(new Settings());
		software.setName(softwareName);
		software.setVersion(version);
		return software;
	}
	/**
	 * Adds the modification.
	 *
	 * @param targetDNMSO the target dnmso
	 * @param character the character
	 * @param difference the difference
	 * @param confidance the confidance

	 * @return the long */
	public String addModification(DNMSO targetDNMSO,String character,String difference,String confidance){
		if (targetDNMSO.getModifications()==null) {
			targetDNMSO.setModifications(new Modifications());
		} else {
			for (Modification modification : targetDNMSO.getModifications().getModification()){
				if (modification.getName() != character){
					return modification.getName();
				}
			}
		}
		AminoAcidMass aminoAcidMass = getAminoAcidMass(character);
		//aminoAcidMassService.run(aminoAcidMass, "D:\\projects\\dnmso\\workspace\\dnmso\\data\\aminoAcidMasses.txt", "read "+character);
		Modification modification = new Modification();
		ModAminoAcid modAminoAcid = new ModAminoAcid();
		modAminoAcid.setCharacter(character);
		modAminoAcid.setAverageMass(aminoAcidMass.getAverage()+Double.valueOf(difference));
		modAminoAcid.setMonoIsotopicMass(aminoAcidMass.getMonoisotopic()+Double.valueOf(difference));
		modification.setName(aminoAcidMass.getName());
		modification.setModAminoAcid(modAminoAcid);
		targetDNMSO.getModifications().getModification().add(modification);
		return modification.getName();
	}

	/**
	 * Read.
	 *
	 * @param character the character
	 * @return the amino acid mass
	 */
	private AminoAcidMass getAminoAcidMass(String character) {
		AminoAcidMass aminoAcidMass = new AminoAcidMass();
		// List<String> ids = createIndexList(params);
		// System.out.println(path);
		Pattern pattern = Pattern.compile("\\A(" + character+ ");(\\w{3});(.*);(.*);(\\d+\\.\\d+);(\\d+\\.\\d+);$");
		String fileContent;
		// PTM ptm = new PTM();
		try {
			//System.out.println("path:"+this.getClass().getResource("/").getPath());
			//System.out.println("file:"+this.getClass().getProtectionDomain().getCodeSource().getLocation().toString());
			BufferedReader in = new BufferedReader(new InputStreamReader(this.getClass().getResource("/aminoAcidMasses.txt").openStream()));
			while ((fileContent = in.readLine()) != null) {
				Matcher matcher = pattern.matcher(fileContent);
				while (matcher.find()) {
					aminoAcidMass.setOneLaterCode(matcher.group(1));
					aminoAcidMass.setTreeLaterCode(matcher.group(2));
					aminoAcidMass.setName(matcher.group(3));
					aminoAcidMass.setChemicalFormula(matcher.group(4));
					aminoAcidMass.setMonoisotopic(Double.valueOf(matcher.group(5)));
					aminoAcidMass.setAverage(Double.valueOf(matcher.group(6)));
				}
			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return aminoAcidMass;
	}

	/**
	 * Adds the prediciton.
	 *
	 * @param sequence the sequence

	 * @return the dNML
	 */
	public Predictions addPrediciton(Predictions predictions,Sources sources, Sequence sequence,List<Prediction.Score> scores) {
		Prediction prediction = new Prediction();
		if (scores!=null ){
			prediction.getScore().addAll(scores);
		}
		prediction.setSequence(sequence);
        prediction.getSources().add(sources);
		predictions.getPrediction().add(prediction);
		return predictions;
	}
	

	/**
	 * Creates a new Dnml object.
	 *

	 * @return the spectrum */
	public MassSpectrum createSpectrum(){
		return new MassSpectrum();
	}
	/**
	 * Creates a new Dnml object.
	 *
	 * @param seq the seq

	 * @return the sequence */
	public Sequence createSequence(String seq) {
		Sequence sequence = new Sequence();
		sequence.setSequence(seq);
		return sequence;
	}
	/**
	 * Creates a new Dnml object.
	 *
	 * @param seq the seq
	 * @param confidance the confidance

	 * @return the sequence */
	public Sequence createSequence(String seq, String confidance) {
		Sequence sequence = new Sequence();
		sequence.setSequence(seq);
		sequence.setConfidence(Double.valueOf(confidance));
		return sequence;
	}

	/**
	 * Removes the spectrum.
	 *
	 * @param targetDNMSO the target dnmso
	 * @return the dNML
	 */

	/**
	 * Removes the prediction.
	 *
	 * @param targetDNMSO the target dnmso

	 * @return the dNML
	 */
	public DNMSO removePrediction(DNMSO targetDNMSO,int predictionsId, int predictionId){
		targetDNMSO.getPredictions().get(predictionsId).getPrediction().remove(predictionId);

		return targetDNMSO;
	}
	

	
	/**
	 * Removes the modification.
	 *
	 * @param targetDNMSO the target dnmso
	 * @return the dNML
	 */
	public DNMSO removeModification(DNMSO targetDNMSO,String modificationName){
		for (int i =0; i< targetDNMSO.getModifications().getModification().size();i++){
			if (targetDNMSO.getModifications().getModification().get(i).getName().equals(modificationName)){
				targetDNMSO.getModifications().getModification().remove(i);
			}
		}
		return targetDNMSO;
	}
	
	/**
	 * Generate �d.
	 *
	 * @return the string
	 */
	public String generateID(){
		UUID id = UUID.randomUUID();	
		return id.toString();
	}
	
	/**
	 * Merge dnmso.
	 *
	 * @param sourceDNMSO the source dnmso
	 * @param targetDNMSO the target dnmso
	 * @return the dNML
	 */
	public DNMSO mergeDNML(DNMSO sourceDNMSO,DNMSO targetDNMSO){

		for (int i =0; i < sourceDNMSO.getModifications().getModification().size();i++){
			targetDNMSO.getModifications().getModification().add(sourceDNMSO.getModifications().getModification().get(i));
		}
		for (int i =0; i< sourceDNMSO.getPredictions().size();i++){
			targetDNMSO.getPredictions().add(sourceDNMSO.getPredictions().get(i));
		}
		
		for (int i = 0; i< sourceDNMSO.getSpectra().getSpectrum().size();i++){
			targetDNMSO.getSpectra().spectrum.add(sourceDNMSO.getSpectra().spectrum.get(i));
		}
		return targetDNMSO;
	}

	/**
	 * Adds the source.
	 *
	 * @param prediction the prediction
	 * @param id the id

	 * @return the prediction */
	public Prediction addSource(Prediction prediction,Sources sources, String id, String fileName){
		
		Source source= new Source();
		source.setScanId(id);
		source.setFileName(fileName);
		sources.getSource().add(source);
        prediction.getSources().add(sources);
		return prediction;
	}



	/**
	 * Adds the amino acid.
	 *
	 * @param sequence the sequence
	 * @param character the character
	 * @param confidance the confidance
	 * @param pos the pos

	 * @return the sequence */
	public Sequence addAminoAcid(Sequence sequence, String character, String confidance, Integer pos) {
		AminoAcid aminoAcid = new AminoAcid();
		aminoAcid.setCharacter(character);
		aminoAcid.setConfidence(Double.parseDouble(confidance));
		aminoAcid.setPos(pos);
		sequence.getAminoAcidOrModifiedAminoAcidOrGap().add(aminoAcid);
		return sequence;
	}

	/**
	 * Adds the modified amino acid.
	 *
	 * @param sequence the sequence
	 * @param confidance the confidance
	 * @param id the id
	 * @param pos the pos
	 */
	public void addModifiedAminoAcid(Sequence sequence, String confidance, String id, Integer pos,String modificationName) {
		ModifiedAminoAcid modifiedAminoAcid = new ModifiedAminoAcid();
		
		modifiedAminoAcid.setPos(pos);
		modifiedAminoAcid.setModificationName(modificationName);
		modifiedAminoAcid.setConfidence(Double.valueOf(confidance));
		sequence.getAminoAcidOrModifiedAminoAcidOrGap().add(modifiedAminoAcid);
	}

	/**
	 * Adds the link.
	 *
	 * @param spectrum the spectrum
	 * @param fileName the file name

	 * @return the spectrum */
	public MassSpectrum addLink(MassSpectrum spectrum, String fileName) {
		spectrum.setLink(fileName);
		return spectrum;
	}

	/**
	 * Creates a new Dnml object.
	 *

	 * @return the settings */
	public Settings createSettings() {
		return new Settings();
	}

	/**
	 * Adds the setting.
	 *
	 * @param settings the settings
	 * @param settingName the setting name
	 * @param value the value

	 * @return the settings */
	public Settings addSetting(Settings settings, String settingName, String value) {
		Setting setting = new Setting();
		setting.setName(settingName);
		setting.setValue(value);
		settings.getSetting().add(setting);
		return settings;
	}

	/**
	 * Adds the gap.
	 *
	 * @param sequence the sequence
	 * @param confidance the confidance
	 * @param position the position
	 * @param value the value

	 * @return the sequence */
	public Sequence addGap(Sequence sequence, String confidance, Integer position,
			String value) {
		Gap gap = new Gap();
		gap.setConfidence(Double.parseDouble(confidance));
		gap.setPos(position);
		gap.setValue(value);
		sequence.getAminoAcidOrModifiedAminoAcidOrGap().add(gap);
		return sequence;
	}

}
