package domain;

public class CV {

	private String cvVersion;
	private String fullName;
	private String id;
	private String uri;

	public String getCvVersion() {
		return this.cvVersion;
	}

	public void setCvVersion(String cvVersion) {
		this.cvVersion = cvVersion;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}