package tr.edu.iyte.dnmso;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tr.edu.iyte.dnmso.controller.AllControllerTests;
import tr.edu.iyte.dnmso.facade.FacadeTest;
import tr.edu.iyte.dnmso.service.AllServiceTests;
 
@RunWith(Suite.class)
@SuiteClasses({FacadeTest.class,AllControllerTests.class,AllServiceTests.class})
public class AllDNMSOTests {

}
