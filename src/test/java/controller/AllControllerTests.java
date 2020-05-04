package controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ConvertCommandTest.class, MergeCommandTest.class,
		ReadCommandTest.class, WriteCommandTest.class })
public class AllControllerTests {

}
