package service;

/**
 * @author   st
 */
public enum ServiceTag {

	SPECTRA_FILE_PATH("-s"),

	PREDICTION_FILE_PATH("-p"),

	NUMBER_OF_PREDICTION("-n"),

    DNMSO("-d"),

	OUTPUT("-o"),

	SPECTRUM_INDEX("-i"),

	COMMAND("command")
	;
	

	private String tag;
	
	ServiceTag(String tag){
		this.tag = tag;
	}


	public String getTag(){
		return this.tag;
	}

	
	@Override
	public String toString(){
		return this.tag;
	}

}
