/*
 * 
 */
package facade;

public class FacadeFactory {

    public Facade getFacade() {
        return new FacadeImpl();
    }

}
