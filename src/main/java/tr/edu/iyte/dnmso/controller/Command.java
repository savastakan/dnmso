/*
 * 
 */
package tr.edu.iyte.dnmso.controller;
import tr.edu.iyte.dnmso.domain.DNMSO;

/**
 * The interface Command.
 */
public interface Command {


	/**
	 * Sets the args.
	 *
	 * @param args the new paremeter
	 */
	public void setArgs(String[] args);
	public void setContainer(DNMSO container);
  
     /**
      * Execute command.
      *
      * @return the DNMSO
      */
     public DNMSO execute();
}
