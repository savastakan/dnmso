package domain;

public class Spectrum {

	private String spectrumId;
	private long scanId;
	private double precursorMZ;
	private double precursorIntensity;
	private Spectra foundInSpectra;
	private String csvData;

	public String getSpectrumId() {
		return this.spectrumId;
	}

	public void setSpectrumId(String spectrumId) {
		this.spectrumId = spectrumId;
	}

	public long getScanId() {
		return this.scanId;
	}

	public void setScanId(long scanId) {
		this.scanId = scanId;
	}

	public double getPrecursorMZ() {
		return this.precursorMZ;
	}

	public void setPrecursorMZ(double precursorMZ) {
		this.precursorMZ = precursorMZ;
	}

	public double getPrecursorIntensity() {
		return this.precursorIntensity;
	}

	public void setPrecursorIntensity(double precursorIntensity) {
		this.precursorIntensity = precursorIntensity;
	}

	public Spectra getFoundInSpectra() {
		return this.foundInSpectra;
	}

	public void setFoundInSpectra(Spectra foundInSpectra) {
		this.foundInSpectra = foundInSpectra;
	}

	public String getCsvData() {
		return this.csvData;
	}

	public void setCsvData(String csvData) {
		this.csvData = csvData;
	}
	
}