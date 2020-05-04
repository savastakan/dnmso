
package facade;


import domain.DNMSO;

import java.util.List;

public interface Facade {


    public DNMSO handle(DNMSO container, final String[] args);

    public DNMSO handle(final String[] args);

    public DNMSO read(String source);

    
    public DNMSO write(DNMSO dnmso, String target);

    public DNMSO convert(String source, String target);

    public DNMSO merge(List<String> inputFiles, String outputFile);

    public DNMSO validate(String source);
    
    public DNMSO read(DNMSO dnmso, String source);

}
