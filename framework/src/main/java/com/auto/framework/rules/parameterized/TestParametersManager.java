package com.auto.framework.rules.parameterized;

import com.auto.framework.reporter.TestReporter;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestParametersManager {
    private static List<Map<String, String>> m_lstParams = Lists.newArrayList();
    private static int m_iIteration = -1;

    public static void next() {
        m_iIteration++;
        report();
    }

    public static void init(Object testCase) {
        generateParams(testCase);
    }

    public static void initIteration(int iIteration) {
        m_iIteration = iIteration - 1;
    }

    private static void generateParams(Object testCase) {
        if (testCase instanceof IParameterizedTest) {
            IParametersGenerator generator = ((IParameterizedTest) testCase).getParametersGenerator();
            m_lstParams = generator.getParametersList();
        }
    }

    public static String getParam(String sKey) {
        return m_lstParams.get(m_iIteration).get(sKey);
    }

    public static int getNumOfIterations() {
        return m_lstParams.size();
    }

    public static void report() {
        if (m_iIteration < m_lstParams.size()) {
            TestReporter.TRACE("Parameters set for iteration #" + m_iIteration + ": " + m_lstParams.get(m_iIteration));
        }
    }
}
