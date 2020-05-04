package domain;

public class Modification {

	private Double averageModificationMass;
	private String modificationName;
	private CVParam cvParam;
	private String psiModRef;
	private boolean isTerminalModification;
	private Double monoModificationMass;

	public Double getAverageModificationMass() {
		return this.averageModificationMass;
	}

	public void setAverageModificationMass(Double averageModificationMass) {
		this.averageModificationMass = averageModificationMass;
	}

	public String getModificationName() {
		return this.modificationName;
	}

	public void setModificationName(String modificationName) {
		this.modificationName = modificationName;
	}

	public CVParam getCvParam() {
		return this.cvParam;
	}

	public void setCvParam(CVParam cvParam) {
		this.cvParam = cvParam;
	}

	public String getPsiModRef() {
		return this.psiModRef;
	}

	public void setPsiModRef(String psiModRef) {
		this.psiModRef = psiModRef;
	}

	public boolean isIsTerminalModification() {
		return this.isTerminalModification;
	}

	public void setIsTerminalModification(boolean isTerminalModification) {
		this.isTerminalModification = isTerminalModification;
	}

	public Double getMonoModificationMass() {
		return this.monoModificationMass;
	}

	public void setMonoModificationMass(Double monoModificationMass) {
		this.monoModificationMass = monoModificationMass;
	}

}