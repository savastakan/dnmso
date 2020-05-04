package domain;

import java.util.*;

public class Spectra {

	private Collection<Spectrum> spectrum;
	private Collection<Publication> publication;
	private CVParam qualifierCVParam;

	public Collection<Spectrum> getSpectrum() {
		return this.spectrum;
	}

	public void setSpectrum(Collection<Spectrum> spectrum) {
		this.spectrum = spectrum;
	}

	public Collection<Publication> getPublication() {
		return this.publication;
	}

	public void setPublication(Collection<Publication> publication) {
		this.publication = publication;
	}

	public CVParam getQualifierCVParam() {
		return this.qualifierCVParam;
	}

	public void setQualifierCVParam(CVParam qualifierCVParam) {
		this.qualifierCVParam = qualifierCVParam;
	}

}