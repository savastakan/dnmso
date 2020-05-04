package domain;

public class SoftwareSetting {

	private String softwareSettingName;
	private String softwareSettingValue;
	private CVParam softwareSettingcvParam;

	public String getSoftwareSettingName() {
		return this.softwareSettingName;
	}

	public void setSoftwareSettingName(String softwareSettingName) {
		this.softwareSettingName = softwareSettingName;
	}

	public String getSoftwareSettingValue() {
		return this.softwareSettingValue;
	}

	public void setSoftwareSettingValue(String softwareSettingValue) {
		this.softwareSettingValue = softwareSettingValue;
	}

	public CVParam getSoftwareSettingcvParam() {
		return this.softwareSettingcvParam;
	}

	public void setSoftwareSettingcvParam(CVParam softwareSettingcvParam) {
		this.softwareSettingcvParam = softwareSettingcvParam;
	}

}