package com.auto.framework.utils;


import com.auto.framework.AbstractTestCase;
import com.auto.framework.reporter.TestReporter;
import com.auto.framework.iface.ITestCase;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestBaseDirectory {

    public static String generate(ITestCase tTestCase) {
        String sRandomDirName = getRandomTestDirName(tTestCase);
        return AbstractTestCase.BASE_DIR_ROOT + sRandomDirName;
    }

    private static String getRandomTestDirName(ITestCase tTestCase) {
        String sRandomDirName = tTestCase.getClass().getSimpleName();
        if (tTestCase.getTestName() == null || !tTestCase.getTestName().startsWith("_")) {
            if (tTestCase.getTestName() == null) {
                TestReporter.TRACE("method name is null, should only happen when running repeat" + tTestCase);
            }
            sRandomDirName = sRandomDirName + "_" + tTestCase.getTestName();
        }
        return sRandomDirName;
    }
}
