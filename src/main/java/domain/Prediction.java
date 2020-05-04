package domain;

import java.util.*;

public class Prediction {

	private Collection<Score> score;
	private Sequence sequence;
	private Software software;
	private Collection<Spectrum> spectrum;

	public Collection<Score> getScore() {
		return this.score;
	}

	public void setScore(Collection<Score> score) {
		this.score = score;
	}

	public Sequence getSequence() {
		return this.sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}

	public Software getSoftware() {
		return this.software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}

	public Collection<Spectrum> getSpectrum() {
		return this.spectrum;
	}

	public void setSpectrum(Collection<Spectrum> spectrum) {
		this.spectrum = spectrum;
	}

}