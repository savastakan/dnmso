package domain;

public class PeakValue extends Proof {

	private double peakMZ;
	private double peakIntensity;

	public double getPeakMZ() {
		return this.peakMZ;
	}

	public void setPeakMZ(double peakMZ) {
		this.peakMZ = peakMZ;
	}

	public double getPeakIntensity() {
		return this.peakIntensity;
	}

	public void setPeakIntensity(double peakIntensity) {
		this.peakIntensity = peakIntensity;
	}

}