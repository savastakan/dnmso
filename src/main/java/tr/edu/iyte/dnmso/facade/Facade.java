/*
 * 
 */
package tr.edu.iyte.dnmso.facade;


import tr.edu.iyte.dnmso.domain.DNMSO;

import java.util.List;


/**
 * The Interface Facade.
 * @author Savas TAKAN
 * @version $Revision: 1.0 $
 */
public interface Facade {

    /**
     * Handle user's wish
     *
     * @param args the parameter
    
     * @return the DNMSO */
    public DNMSO handle(DNMSO container, final String[] args);
    
    /**
     * Handle user's wish
     *
     * @param args the parameter
     * @return the DNMSO
     */
    public DNMSO handle(final String[] args);

    public DNMSO read(String source);

    
    public DNMSO write(DNMSO dnmso,String target);

    public DNMSO convert(String source,String target);

    public DNMSO merge(List<String> inputFiles,String outputFile);

    public DNMSO validate(String source);
    
    public DNMSO read(DNMSO dnmso,String source);

}
