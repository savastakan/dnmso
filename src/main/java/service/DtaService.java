package service;

import domain.*;


import java.io.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DtaService extends AbstractService{

	public String getServiceName() {
		// TODO Auto-generated method stub
		return "DTA Service";
	}


	public DNMSO read() {
		DNMSO dnmso = getDNMSO();
		if (dnmso == null){
			DNMSOFactory dnmsoFactory = new DNMSOFactory();
			dnmso = dnmsoFactory.createDNMSO();
		}

		String line;
		String spectrumFilePath = getProperties().get(ServiceTag.SPECTRA_FILE_PATH.toString());
		Random random = new Random();
		File spectrumFile = new File(spectrumFilePath);
		Spectrum spectrum = new Spectrum();
		spectrum.setSpectrumId(String.valueOf(random.nextLong()));
		spectrum.setScanId(random.nextLong());

		try {
			String csvData = "";
			BufferedReader in = new BufferedReader(new FileReader(spectrumFile));
			while ((line = in.readLine()) != null) {
				Pattern pattern = Pattern.compile("\\A(\\S+)\\s(\\S+)");
				Matcher matcher = pattern.matcher(line);
				while (matcher.find()) {
					if (matcher.group(1) != null && matcher.group(2) != null){
						csvData = csvData.concat(matcher.group(1) + "," + matcher.group(2) + ";");
					}
				}
			}
			spectrum.setCsvData(csvData);
			in.close();
		} catch (IOException e) {
			System.out.println("Spectrum is not added");
		}
		Spectra spectra = new Spectra();
		spectra.setSpectrum(new LinkedList<>());
		spectra.getSpectrum().add(spectrum);
		dnmso.getSpectra().add(spectra);
		return dnmso;
	}

	public DNMSO run(DNMSO dnmso, String[] args) {

		processSettings(dnmso, args);
		if (getProperties().get(ServiceTag.COMMAND.toString()).equals("read")) return read();
		return null;
	}

	public boolean isValid(File file) {
		if (file.getName().endsWith(".dta")){
			return true;
		}
		return false;
	}
}
