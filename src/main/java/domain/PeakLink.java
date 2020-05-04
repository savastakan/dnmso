package domain;

public class PeakLink extends Proof {

	private long peakId;
	private Spectrum peakLinkSpectrum;

	public long getPeakId() {
		return this.peakId;
	}

	public void setPeakId(long peakId) {
		this.peakId = peakId;
	}

	public Spectrum getPeakLinkSpectrum() {
		return this.peakLinkSpectrum;
	}

	public void setPeakLinkSpectrum(Spectrum peakLinkSpectrum) {
		this.peakLinkSpectrum = peakLinkSpectrum;
	}

}