/*
 * 
 */
package service;

import domain.DNMSO;

import java.io.File;



public interface Service {
	public String getServiceName();

	DNMSO run(DNMSO dnmso, String[] args);

	boolean isValid(File file);
	
}
