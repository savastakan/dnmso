/*
 * 
 */
package controller;

import domain.DNMSO;

public interface Command {

	public void setArgs(String[] args);
	public void setDNMSO(DNMSO dnmso);
     public DNMSO execute();
}
