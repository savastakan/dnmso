/*
 * 
 */
package tr.edu.iyte.dnmso.controller;

import tr.edu.iyte.dnmso.domain.DNMSO;


/**
 * Each command need to inherit this class.
 * @author  Savas TAKAN
 * @version  $Revision: 1.0 $
 */
public abstract class AbstractCommand implements Command {
 
	/**
	 * parameters for command.
	 * @param  args
	 * @uml.property  name="args"
	 */
	private String[] args;
	/**
	 * @uml.property  name="container"
	 * @uml.associationEnd  
	 */
	private DNMSO container;
	/**
	 * Use to get dnmso container.
	 * @return   the dnmso container
	 * @uml.property  name="container"
	 */
    public DNMSO getContainer() {
		return container;
	}


	/**
	 * @param container
	 * @uml.property  name="container"
	 */
	public void setContainer(DNMSO container) {
		this.container = container;
	}
	/**
	 * Use to get parameters.
	 * @return   the parameters
	 * @uml.property  name="args"
	 */
    public String[] getArgs() {
		return args;
	}

	/**
	 * Use to set parameters.
	 * @param args  the new args
	 * @uml.property  name="args"
	 */
	public void setArgs(String[] args) {
		this.args = args;
	}

}
