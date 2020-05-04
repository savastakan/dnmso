package domain;

public class ModifiedAminoAcid {

	private Modification modification;
	private AminoAcid aminoAcid;
	private String modAAId;

	public Modification getModification() {
		return this.modification;
	}

	public void setModification(Modification modification) {
		this.modification = modification;
	}

	public AminoAcid getAminoAcid() {
		return this.aminoAcid;
	}

	public void setAminoAcid(AminoAcid aminoAcid) {
		this.aminoAcid = aminoAcid;
	}

	public String getModAAId() {
		return this.modAAId;
	}

	public void setModAAId(String modAAId) {
		this.modAAId = modAAId;
	}

}