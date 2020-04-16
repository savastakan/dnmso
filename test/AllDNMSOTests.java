import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import controller.AllControllerTests;
import facade.FacadeTest;
import service.AllServiceTests;
 
@RunWith(Suite.class)
@SuiteClasses({FacadeTest.class,AllControllerTests.class,AllServiceTests.class})
public class AllDNMSOTests {

}
