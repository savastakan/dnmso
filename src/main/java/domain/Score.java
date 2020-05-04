package domain;

public class Score {

	private boolean largerIsBetter;
	private String scoreName;
	private Double scoreValue;
	private boolean mainScore;
	private CVParam scoreCVParam;

	public boolean getLargerIsBetter() {
		return this.largerIsBetter;
	}

	public void setLargerIsBetter(boolean largerIsBetter) {
		this.largerIsBetter = largerIsBetter;
	}

	public String getScoreName() {
		return this.scoreName;
	}

	public void setScoreName(String scoreName) {
		this.scoreName = scoreName;
	}

	public Double getScoreValue() {
		return this.scoreValue;
	}

	public void setScoreValue(Double scoreValue) {
		this.scoreValue = scoreValue;
	}

	public boolean isMainScore() {
		return this.mainScore;
	}

	public void setMainScore(boolean mainScore) {
		this.mainScore = mainScore;
	}

	public CVParam getScoreCVParam() {
		return this.scoreCVParam;
	}

	public void setScoreCVParam(CVParam scoreCVParam) {
		this.scoreCVParam = scoreCVParam;
	}

}