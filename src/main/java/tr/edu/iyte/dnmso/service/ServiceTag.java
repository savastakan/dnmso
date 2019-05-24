package tr.edu.iyte.dnmso.service;

/**
 * @author   st
 */
public enum ServiceTag {
	/**
	 * @uml.property  name="sPECTRA_FILE_PATH"
	 * @uml.associationEnd  
	 */
	SPECTRA_FILE_PATH("-s"),
	/**
	 * @uml.property  name="pREDICTION_FILE_PATH"
	 * @uml.associationEnd  
	 */
	PREDICTION_FILE_PATH("-p"),
	/**
	 * @uml.property  name="nUMBER_OF_PREDICTION"
	 * @uml.associationEnd  
	 */
	NUMBER_OF_PREDICTION("-n"),
    /**
	 * @uml.property  name="dNMSO"
	 * @uml.associationEnd  
	 */
    DNMSO("-d"),
	/**
	 * @uml.property  name="oUTPUT"
	 * @uml.associationEnd  
	 */
	OUTPUT("-o"),
	/**
	 * @uml.property  name="sPECTRUM_INDEX"
	 * @uml.associationEnd  
	 */
	SPECTRUM_INDEX("-i"),
	/**
	 * @uml.property  name="cOMMAND"
	 * @uml.associationEnd  
	 */
	COMMAND("command")
	;
	
	/**
	 * @uml.property  name="tag"
	 */
	private String tag;
	
	ServiceTag(String tag){
		this.tag = tag;
	}

	/**
	 * @return
	 * @uml.property  name="tag"
	 */
	public String getTag(){
		return this.tag;
	}

	
	@Override
	public String toString(){
		return this.tag;
	}

}
