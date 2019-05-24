package tr.edu.iyte.dnmso.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DnmsoOboServiceTest.class,DnmlServiceTest.class, LutefiskXPServiceTest.class,
		MzMLServiceTest.class, MzXmlServiceTest.class, PepNovoServiceTest.class,PepXmlServiceTest.class })
public class AllServiceTests {

}
