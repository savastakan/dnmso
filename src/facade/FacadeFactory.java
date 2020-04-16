/*
 * 
 */
package facade;

/**
 * A factory for creating Facade objects.
 * @author  Savas TAKAN
 * @version  $Revision: 1.0 $
 */
public class FacadeFactory {

    /**
     * Gets the facade.
     *
    
     * @return the facade */
    public Facade getFacade() {
        return new FacadeImpl();
    }

}
