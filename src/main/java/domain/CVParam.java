package domain;

import java.util.*;

public class CVParam {

	private Collection<CV> cv;
	private String cvParamName;
	private String cvTermAccession;
	private String unitAccession;
	private String unitCVRef;
	private String unitName;
	private String usrValue;

	public Collection<CV> getCv() {
		return this.cv;
	}

	public void setCv(Collection<CV> cv) {
		this.cv = cv;
	}

	public String getCvParamName() {
		return this.cvParamName;
	}

	public void setCvParamName(String cvParamName) {
		this.cvParamName = cvParamName;
	}

	public String getCvTermAccession() {
		return this.cvTermAccession;
	}

	public void setCvTermAccession(String cvTermAccession) {
		this.cvTermAccession = cvTermAccession;
	}

	public String getUnitAccession() {
		return this.unitAccession;
	}

	public void setUnitAccession(String unitAccession) {
		this.unitAccession = unitAccession;
	}

	public String getUnitCVRef() {
		return this.unitCVRef;
	}

	public void setUnitCVRef(String unitCVRef) {
		this.unitCVRef = unitCVRef;
	}

	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUsrValue() {
		return this.usrValue;
	}

	public void setUsrValue(String usrValue) {
		this.usrValue = usrValue;
	}

}