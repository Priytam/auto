package com.auto.framework.runner.job;

import com.auto.framework.rules.parameterized.TestParametersManager;
import com.auto.framework.utils.regex.RegexParseResult;
import com.auto.framework.utils.regex.RegexParser;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class SingleTestRunner {

    public static final String ALL = "ALL";

    public static void runTest(String sTest) throws Exception {
        if (StringUtils.isEmpty(sTest)) {
            throw new Exception("No test supplied");
        }

        int iIndex = sTest.lastIndexOf(".");

        if (iIndex < 0) {
            throw new Exception("Test doesn't exist");
        }

        String sClassName = sTest.substring(0, iIndex);
        String sTestName = sTest.substring(iIndex + 1);

        RegexParseResult parse = RegexParser.parse("(.*)\\[(\\d+)\\]", sTestName);
        if (parse.isMatch()) {
            sTestName = parse.getParsedItems().get(0);
            String sPermutationIndex = parse.getParsedItems().get(1);
            TestParametersManager.initIteration(Integer.parseInt(sPermutationIndex) - 1);
        }

        try {
            Class clazz = Class.forName(sClassName);
            JUnitCore core = new JUnitCore();
            Result res = null;
            if (sTestName.equalsIgnoreCase(ALL)) {
                res = core.run(Request.aClass(clazz));
            } else {
                res = core.run(Request.method(clazz, sTestName));
            }
        } catch (Exception e) {
            throw new Exception("Couldn't run test: " + e.getMessage(), e);
        }
    }

}
