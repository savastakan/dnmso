
package service;
import domain.*;
import domain.DNMSO.Predictions;
import domain.DNMSO.Predictions.Software;
import domain.DNMSO.Predictions.Software.Settings;
import domain.DNMSO.Predictions.Software.Settings.Setting;
import domain.Prediction.Score;
import domain.Prediction.Sequence;
import domain.Prediction.Sequence.Gap;
import domain.Prediction.Sources;
import domain.Prediction.Sources.Source;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mslib.Peak;
import mslib.MassSpectrum;


// TODO: Auto-generated Javadoc
/**
 * The Class PepNovoService.
 */
public class PepNovoService extends AbstractService {

//    private DNMSO addSpectrum(DNMSO dnmso,String fileName, String scanId){
//    	for (Spectrum spectrum : dnmso.getSpectra().getSpectrum()){
//    		if (spectrum.getScanId().equals(scanId)){
//    			return dnmso;
//    		}
//    	}
//   	 	 Spectrum spectrum = new Spectrum();
//		 spectrum.setFileName(fileName);
//		 spectrum.setScanId(scanId);
//		 dnmso.getSpectra().getSpectrum().add(spectrum);
//		 return dnmso;
//    }

    /**
     *  read  Pep Novo Plus
     * @return DNMSO
     */
	
	private DNMSO readPepNovoPlus(){
		String predictionFilePath = getProperties().get(ServiceTag.PREDICTION_FILE_PATH.toString());
        String spectrumFilePath = getProperties().get(ServiceTag.SPECTRA_FILE_PATH.toString());
		File predictionFile = new File(predictionFilePath);
        File spectrumFile = new File(spectrumFilePath);
		DNMSO targetDNMSO = (DNMSO) getContainer();
		String size = getProperties().get(ServiceTag.NUMBER_OF_PREDICTION.toString());
		
		Pattern pattern = Pattern.compile("PepNovo\\+\\s+Build\\s(\\d+)|^Fragment tolerance :\\s+([+-]?\\d+.?\\d+)|^PM tolernace \\s+:\\s+([+-]?\\d+.?\\d+)|^>>\\s(\\d+)\\s(\\d+)\\s+\\(SQS|^(\\d+)\\s([+-]?\\d*.?\\d*)\\s([+-]?\\d*.?\\d*)\\s([+-]?\\d*.?\\d*)\\s([+-]?\\d*.?\\d*)\\s([+-]?\\d*.?\\d*)\\s([+-]?\\d*.?\\d*)\\s(\\w+)");
		
		String fileContent;

		if (targetDNMSO==null){
			DnmsoFactory dnmsoFactory = new DnmsoFactory();
			targetDNMSO= dnmsoFactory.createDnmso();
		}

        HashMap<String,HashSet<String>> files = new HashMap<String, HashSet<String>>();

		Predictions  predictions = new Predictions();
		int beginScan = 0;
		int endScan;
		String nGap = null;
		String cGap = null;
		String mplush = null;
		Score scoreRnkScr = null;
		Score scorePnvScr = null;
		String charge = null;
		Sources sources = null;
		Software software =new Software();
		software.setSettings(new Settings());
		software.setName("Pepnovo+");
		predictions.setSoftware(software);

		try {
			BufferedReader in = new BufferedReader(new FileReader(predictionFile));
			while ((fileContent = in.readLine()) != null && check(predictions.getPrediction().size(),size)) {
				Matcher matcher = pattern.matcher(fileContent);
				while (matcher.find() && check(predictions.getPrediction().size(),size) ) {
					if(matcher.group(1) != null){
						predictions.getSoftware().setVersion(matcher.group(1));
                    }
                    if (matcher.group(2) != null) {
						
						String fragmentTolerance = matcher.group(2);//Fragment tolerance
						Setting setting = new Setting();

						setting.setName("Fragment Tolerance");
						setting.setValue(fragmentTolerance);
						predictions.getSoftware().getSettings().getSetting().add(setting);
					}
					if (matcher.group(3) != null) {
						String pMTolernace = matcher.group(3) ;
						Setting setting = new Setting();
						setting.setName("PM Tolerance");
						setting.setValue(pMTolernace);
						predictions.getSoftware().getSettings().getSetting().add(setting);
						

					}

					if (matcher.group(4) != null) {
						beginScan =Integer.valueOf( matcher.group(4));
					}
					if (matcher.group(5) != null) {
						endScan = Integer.valueOf(matcher.group(5));
						sources = new Sources();
                        sources.setMerge(true);

						for (int i =beginScan; i<=endScan;i++ ){


							Source source = new Source();
							source.setFileName(spectrumFile.getName());
							source.setScanId(String.valueOf(i));
							sources.getSource().add(source);
                            HashSet<String> ids = files.get(spectrumFile.getName());
                            if (ids!=null){
                                files.get(spectrumFile.getName()).add(String.valueOf(i));
                            }   else {
                                ids = new HashSet<String>();
                                ids.add(String.valueOf(i));
                                files.put(spectrumFile.getName(),ids);
                            }

						}
					}
					
					if (matcher.group(7) != null) {
						String rnkScr =matcher.group(7);
						scoreRnkScr = new Score();
						scoreRnkScr.setName("RnkScr");
						scoreRnkScr.setValue(rnkScr);
					}
					if (matcher.group(8) != null) {
						String pnvScr =matcher.group(8);
						scorePnvScr = new Score();
						scorePnvScr.setName("PnvScr");
						scorePnvScr.setValue(pnvScr);
						
					}					
					if (matcher.group((9)) != null) {
						nGap =matcher.group(9);
					}
					
					if (matcher.group(10) != null) {
						cGap =matcher.group(10);
					}
					
					if (matcher.group(11) != null) {
						mplush =matcher.group(11);
						
					}
					if (matcher.group(12) != null) {
						charge =matcher.group(12);
						
					}
					if (matcher.group(13) != null) {
						
						String sequenceStr = matcher.group(13);
						Prediction prediction = new Prediction();
						prediction.getSources().add(sources);
						prediction.setAssumedCharge(Integer.valueOf(charge));
						prediction.getScore().add(scoreRnkScr);
						prediction.getScore().add(scorePnvScr);
						Sequence sequence = new Sequence();
						Gap gap = new Gap();
						
						gap.setPos(Integer.valueOf(1));
						gap.setValue(nGap);
						sequence.getAminoAcidOrModifiedAminoAcidOrGap().add(gap);
						sequence.setCalculatedMass(Double.valueOf(mplush));
						
						sequence.setSequence(sequenceStr);
						for (int i=0; i< sequenceStr.length();i++){
							AminoAcid aminoAcid = new AminoAcid();
							aminoAcid.setCharacter(String.valueOf(sequenceStr.charAt(i)));
							aminoAcid.setPos(Integer.valueOf(i+2));
							sequence.getAminoAcidOrModifiedAminoAcidOrGap().add(aminoAcid);
						}
						gap = new Gap();
						gap.setPos(Integer.valueOf(sequenceStr.length()+2));
						gap.setValue(cGap);
						sequence.getAminoAcidOrModifiedAminoAcidOrGap().add(gap);
						prediction.setSequence(sequence);
						predictions.getPrediction().add(prediction);
						
									
					}
					
					
					
				}
			}

            for (String filePath : files.keySet()){
                HashSet<String> ids =  files.get(filePath);
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
			targetDNMSO.getPredictions().add(predictions);
			in.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return targetDNMSO;
		
	}

	private boolean check(int peredictionSize, String size) {
		if(size!=null){
			if ( peredictionSize < Integer.parseInt(size)){
				return true;
			} 
			return false;
		} 
		return true;
	}
	private DNMSO readPepNovo() {
		String predictionFilePath = getProperties().get(ServiceTag.PREDICTION_FILE_PATH.toString());
		DNMSO targetDNMSO = (DNMSO) getContainer();
		String size = getProperties().get(ServiceTag.NUMBER_OF_PREDICTION.toString());
		// List<String> ids = createIndexList(params);
		//System.out.println(path);
		Pattern pattern = Pattern
				.compile("\\A(PepNovo)\\s(v\\d+\\.\\d+)|\\AFile \\d+: (\\S+)$|\\A\\d+\\.\\d+\\s+(\\S+)$|\\A\\d+\\.\\d+\\s(\\w)(\\S+)?\\s(\\d\\.\\d+)$");
		Sequence sequence = null;
		String fileName = null;
		boolean isPepNovo = false;
		String fileContent;
		String version = null;
		String softwareName = null;
		Predictions predictions = new Predictions();
		predictions.setSoftware(new Software());
		Double totalConfidence= 0.0;
		
		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		if (targetDNMSO==null){
			targetDNMSO = dnmsoFactory.createDnmso();
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader(predictionFilePath));
			while ((fileContent = in.readLine()) != null && check(predictions.getPrediction().size(), size)) {
				Matcher matcher = pattern.matcher(fileContent);
				while (matcher.find() && check(predictions.getPrediction().size(), size)) {
					if (matcher.group(1) != null) {//name
						softwareName = matcher.group(1);
						isPepNovo = true;
					} // validate PepNovo
					if (matcher.group(2) != null) {// version
						version = matcher.group(2);
						predictions.getSoftware().setName(softwareName);
						predictions.getSoftware().setVersion(version);
						
					}
					if (matcher.group(3) != null) { // get file name
						// new prediction is created
						if (fileName!=null) addPrediction(targetDNMSO,predictions,sequence,predictionFilePath,fileName);
						fileName = matcher.group(3);
						

					}
					if (matcher.group(4) != null) {// whole sequence
						sequence = dnmsoFactory.createSequence(matcher.group(4));

					}
					if (matcher.group(5) != null
							&& matcher.group(7) != null) {//get amino acid, gap, modified amino acid
						if (matcher.group(6) != null){//there are modification
							String name = matcher.group(5);
							 
							String confidence =matcher.group(7);
							totalConfidence += Double.parseDouble(confidence);
							dnmsoFactory.addModification(targetDNMSO,name,matcher.group(6), confidence);
							ModifiedAminoAcid modifiedAminoAcid = new ModifiedAminoAcid();
							modifiedAminoAcid.setConfidence(Double.valueOf(confidence));
							modifiedAminoAcid.setModificationName(name);
							sequence.getAminoAcidOrModifiedAminoAcidOrGap().add(modifiedAminoAcid);
						} else {
							AminoAcid aminoAcid = new AminoAcid();
							aminoAcid.setCharacter(matcher.group(5));
							aminoAcid.setConfidence(Double.valueOf(matcher.group(7)));
							totalConfidence += Double.parseDouble(matcher.group(7));
							sequence.getAminoAcidOrModifiedAminoAcidOrGap().add(aminoAcid);

						}
					}
				}

			}
			
			if (sequence!=null && check(predictions.getPrediction().size(), size)){
				sequence.setConfidence( (totalConfidence/sequence.getAminoAcidOrModifiedAminoAcidOrGap().size()));
				addPrediction(targetDNMSO,predictions,sequence,predictionFilePath,fileName);
				totalConfidence =0.0;
			}
			
			targetDNMSO.getPredictions().add(predictions);
			in.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (isPepNovo) return targetDNMSO;
		return null;
	}

	private void addPrediction(DNMSO dmnso,Predictions predictions, Sequence sequence,String path,String fileName) {
		
		int pos=0;
		double totalConfidance = 0;
		for (Object element : sequence.getAminoAcidOrModifiedAminoAcidOrGap()) {
			pos++;
			if (element instanceof AminoAcid){
				AminoAcid aminoAcid = (AminoAcid) element;
				aminoAcid.setPos(Integer.valueOf(pos));
				totalConfidance +=aminoAcid.getConfidence();
			} else if (element instanceof ModifiedAminoAcid ){
				totalConfidance +=((ModifiedAminoAcid) element).getConfidence();
				((ModifiedAminoAcid) element).setPos(Integer.valueOf(pos));
			}else if (element instanceof Gap ){
				totalConfidance +=((Gap) element).getConfidence();
				((Gap) element).setPos(Integer.valueOf(pos));
			}

		}
		
		MassSpectrum spectrum = createCsvSpectrum(path,fileName);
		dmnso.getSpectra().addSpectrum(spectrum);
		sequence.setConfidence(totalConfidance/pos);
		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		Sources sources = new Sources();
        sources.setMerge(false);
		Source source = new Source();
		source.setFileName(fileName);

		dnmsoFactory.addPrediciton(predictions,sources, sequence,null);
	

	}


	/**
	 * Creates the csv spectrum.
	 *
	 * @param path the path
	 * @param fileName the file name

	 * @return the spectrum */
	private MassSpectrum createCsvSpectrum(String path,String fileName) {

		DnmsoFactory dnmsoFactory = new DnmsoFactory();
		MassSpectrum spectrum = dnmsoFactory.createSpectrum();
		String line;
		try {
			BufferedReader in = new BufferedReader(new FileReader(path+fileName));
			while ((line = in.readLine()) != null) {
				Pattern pattern = Pattern.compile("\\A(\\S+)\\s(\\S+)");
				Matcher matcher = pattern.matcher(line);
				while (matcher.find()) {
					if (matcher.group(1) != null && matcher.group(2) != null){
						Peak peak = new Peak();
						peak.set(Double.valueOf(matcher.group(1)) , Double.valueOf(matcher.group(2)));
						spectrum.add(peak);
					}

				}

			}
			in.close();
		} catch (FileNotFoundException e) {
			dnmsoFactory.addLink(spectrum,fileName);
		} catch (IOException e) {
			System.out.println("Spectrum is not added");
		}
		spectrum.setFileName(fileName);
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
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {			
			return false;
		}
		return false;
	}

	/* 
	 * @see tr.edu.iyte.dnmso.service.Service#run(java.lang.Object, java.lang.String[])
	 */
	public DNMSO run(Object targetDnml,String[] args) {
		processSettings((DNMSO)targetDnml, args);
		if (getProperties().get(ServiceTag.COMMAND.toString()).equals("read")) return read();
		return null;
	}


	/* 
	 * @see tr.edu.iyte.dnmso.service.Service#isValid(java.io.File)
	 */
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
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {			
			return null;
		}
		return null;
	}


	/* 
	 * @see tr.edu.iyte.dnmso.service.Service#getServiceName()
	 */
	public String getServiceName() {
		// TODO Auto-generated method stub
		return "PepNovoService";
	}
}
