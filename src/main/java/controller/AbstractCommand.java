/*
 * 
 */
package controller;


import domain.DNMSO;

public abstract class AbstractCommand implements Command {

	private String[] args;

	private DNMSO container;

    public DNMSO getContainer() {
		return container;
	}

	public void setDNMSO(DNMSO container) {
		this.container = container;
	}

    public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

}
