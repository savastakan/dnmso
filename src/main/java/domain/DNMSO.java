package domain;

import java.util.*;

public class DNMSO {

	private Collection<Spectra> spectra;
	private String version;
	private Collection<Prediction> prediction;
	private Collection<Modification> modification;
	private CV cv;
	private Collection<AminoAcid> aminoAcid;
	private Collection<ModifiedAminoAcid> modifiedAminoAcid;

	public Collection<Spectra> getSpectra() {
		return this.spectra;
	}

	public void setSpectra(Collection<Spectra> spectra) {
		this.spectra = spectra;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Collection<Prediction> getPrediction() {
		return this.prediction;
	}

	public void setPrediction(Collection<Prediction> prediction) {
		this.prediction = prediction;
	}

	public Collection<Modification> getModification() {
		return this.modification;
	}

	public void setModification(Collection<Modification> modification) {
		this.modification = modification;
	}

	public CV getCv() {
		return this.cv;
	}

	public void setCv(CV cv) {
		this.cv = cv;
	}

	public Collection<AminoAcid> getAminoAcid() {
		return this.aminoAcid;
	}

	public void setAminoAcid(Collection<AminoAcid> aminoAcid) {
		this.aminoAcid = aminoAcid;
	}

	public Collection<ModifiedAminoAcid> getModifiedAminoAcid() {
		return this.modifiedAminoAcid;
	}

	public void setModifiedAminoAcid(Collection<ModifiedAminoAcid> modifiedAminoAcid) {
		this.modifiedAminoAcid = modifiedAminoAcid;
	}

}