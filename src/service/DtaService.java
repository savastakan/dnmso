package service;

import domain.DNMSO;
import domain.DnmsoFactory;
import domain.Spectrum;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mslib.Peak;
import mslib.MassSpectrum;


public class DtaService extends AbstractService{

	public String getServiceName() {
		// TODO Auto-generated method stub
		return "DTA Service";
	}

	public DNMSO read() {
		DNMSO dnmso =(DNMSO)getContainer();
		if (dnmso == null){
			DnmsoFactory dnmsoFactory = new DnmsoFactory();
			dnmso = dnmsoFactory.createDnmso();
		}
		MassSpectrum spectrum = new MassSpectrum();
		String line;
		String spectrumFilePath = getProperties().get(ServiceTag.SPECTRA_FILE_PATH.toString());
		File spectrumFile = new File(spectrumFilePath);
        if (!findSpectrum(spectrumFile.getName(),null)){
            try {

                String csvFormat = "";
                BufferedReader in = new BufferedReader(new FileReader(spectrumFile));
                while ((line = in.readLine()) != null) {
                    Pattern pattern = Pattern.compile("\\A(\\S+)\\s(\\S+)");
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                    	Peak peak = new Peak();
                        if (matcher.group(1) != null && matcher.group(2) != null){
                        	peak.set(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2)));
                        	spectrum.add(peak);
                        }

                    }

                }

                in.close();
            } catch (FileNotFoundException e) {
                //spectrum.setLink(spectrumFilePath);
            } catch (IOException e) {
                System.out.println("Spectrum is not added");
            }
            spectrum.setLink(spectrumFilePath);
            spectrum.setFileName(spectrumFile.getName());
            dnmso.getSpectra().addSpectrum(spectrum);;
            setContainer(dnmso);
        }

		return dnmso;
	}

	public Object run(Object container, String[] args) {

		processSettings((DNMSO)container, args);
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
