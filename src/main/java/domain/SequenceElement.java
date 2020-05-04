package domain;

public class SequenceElement {

	private Proof proof;
	private double confidence;
	private long positionInSequence;

	public Proof getProof() {
		return this.proof;
	}

	public void setProof(Proof proof) {
		this.proof = proof;
	}

	public double getConfidence() {
		return this.confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public long getPositionInSequence() {
		return this.positionInSequence;
	}

	public void setPositionInSequence(long positionInSequence) {
		this.positionInSequence = positionInSequence;
	}

}