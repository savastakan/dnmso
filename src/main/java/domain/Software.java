package domain;

import java.util.*;

public class Software {

	private Collection<Publication> publication;
	private Collection<SoftwareSetting> softwareSetting;
	private String softwareName;
	private String softwareVersion;
	private CVParam softwareCVParam;

	public Collection<Publication> getPublication() {
		return this.publication;
	}

	public void setPublication(Collection<Publication> publication) {
		this.publication = publication;
	}

	public Collection<SoftwareSetting> getSoftwareSetting() {
		return this.softwareSetting;
	}

	public void setSoftwareSetting(Collection<SoftwareSetting> softwareSetting) {
		this.softwareSetting = softwareSetting;
	}

	public String getSoftwareName() {
		return this.softwareName;
	}

	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	public String getSoftwareVersion() {
		return this.softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public CVParam getSoftwareCVParam() {
		return this.softwareCVParam;
	}

	public void setSoftwareCVParam(CVParam softwareCVParam) {
		this.softwareCVParam = softwareCVParam;
	}

}