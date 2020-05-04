package domain;

import java.util.*;

public class Sequence {

	private Collection<SequenceElement> sequenceElement;
	private double combinedConfidence;
	private String peptideSequence;
	private Modification cTerminalModification;
	private Modification nTerminalModification;

	public Collection<SequenceElement> getSequenceElement() {
		return this.sequenceElement;
	}

	public void setSequenceElement(Collection<SequenceElement> sequenceElement) {
		this.sequenceElement = sequenceElement;
	}

	public double getCombinedConfidence() {
		return this.combinedConfidence;
	}

	public void setCombinedConfidence(double combinedConfidence) {
		this.combinedConfidence = combinedConfidence;
	}

	public String getPeptideSequence() {
		return this.peptideSequence;
	}

	public void setPeptideSequence(String peptideSequence) {
		this.peptideSequence = peptideSequence;
	}

	public Modification getCTerminalModification() {
		return this.cTerminalModification;
	}

	public void setCTerminalModification(Modification cTerminalModification) {
		this.cTerminalModification = cTerminalModification;
	}

	public Modification getNTerminalModification() {
		return this.nTerminalModification;
	}

	public void setNTerminalModification(Modification nTerminalModification) {
		this.nTerminalModification = nTerminalModification;
	}

}