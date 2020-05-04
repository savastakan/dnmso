package domain;

public class AminoAcid {

	private String aminoAcidName;
	private double averageMass;
	private double monoIsotopicMass;
	private String singleLetterCode;
	private String threeLetterCode;

	public String getAminoAcidName() {
		return this.aminoAcidName;
	}

	public void setAminoAcidName(String aminoAcidName) {
		this.aminoAcidName = aminoAcidName;
	}

	public double getAverageMass() {
		return this.averageMass;
	}

	public void setAverageMass(double averageMass) {
		this.averageMass = averageMass;
	}

	public double getMonoIsotopicMass() {
		return this.monoIsotopicMass;
	}

	public void setMonoIsotopicMass(double monoIsotopicMass) {
		this.monoIsotopicMass = monoIsotopicMass;
	}

	public String getSingleLetterCode() {
		return this.singleLetterCode;
	}

	public void setSingleLetterCode(String singleLetterCode) {
		this.singleLetterCode = singleLetterCode;
	}

	public String getThreeLetterCode() {
		return this.threeLetterCode;
	}

	public void setThreeLetterCode(String threeLetterCode) {
		this.threeLetterCode = threeLetterCode;
	}

}