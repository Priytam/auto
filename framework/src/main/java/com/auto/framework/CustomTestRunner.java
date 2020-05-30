package com.auto.framework;


import com.auto.framework.env.TestEnvironment;
import com.auto.framework.reporter.ErrorReporter;
import com.auto.framework.reporter.TestReporter;
import com.auto.framework.rules.parameterized.IParameterizedTest;
import com.auto.framework.utils.ThreadUtils;
import com.auto.framework.env.RegressionEnvironment;
import com.auto.framework.iface.ITestCase;
import com.auto.framework.rules.Repeat;
import com.auto.framework.rules.parameterized.ParameterizedTest;
import com.auto.framework.rules.parameterized.TestParametersManager;
import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class CustomTestRunner extends BlockJUnit4ClassRunner {
    private ITestCase m_testCase;
    private Throwable m_throwable;

    public CustomTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Description describeChild(FrameworkMethod method) {
        if (method.getAnnotation(Repeat.class) != null &&
                method.getAnnotation(Ignore.class) == null) {
            return describeRepeatTest(method);
        }
        if (method.getAnnotation(ParameterizedTest.class) != null &&
                method.getAnnotation(Ignore.class) == null) {
            return describeParameterizedTest(method);
        }
        return super.describeChild(method);
    }

    private Description describeParameterizedTest(FrameworkMethod method) {
        Class<?> clazz = method.getMethod().getDeclaringClass();
        try {
            Object tTest = clazz.newInstance();
            TestParametersManager.init(tTest);
            if (RegressionEnvironment.isRegression()) {
                Description description = Description.createSuiteDescription(
                        testName(method),
                        method.getAnnotations());
                description.addChild(Description.createTestDescription(
                        getTestClass().getJavaClass(),
                        testName(method)));
                return description;
            } else {
                int times = TestParametersManager.getNumOfIterations();
                Description description = Description.createSuiteDescription(
                        testName(method) + " [" + times + " times]",
                        method.getAnnotations());

                for (int i = 1; i <= times; i++) {
                    description.addChild(Description.createTestDescription(
                            getTestClass().getJavaClass(),
                            testName(method) + "[" + i + "] "));
                }
                return description;
            }
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(5);
            return null;
        }
    }

    @Override
    protected Statement methodBlock(final FrameworkMethod method) {
        final Statement statement = super.methodBlock(method);
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    TestEnvironment.setTestMethod(method.getMethod());
                    TestParametersManager.next();
                    try {
                        if (TestEnvironment.haltOnError()) {
                            ErrorReporter.setSaveInformation(false);
                        }
                        statement.evaluate();
                    } catch (Throwable th) {
                        m_throwable = th;
                    }
                    TestEnvironment.setDoneTime();
                    if (null != m_throwable) {
                        if (TestReporter.testFailed()) {
                            throw m_throwable;
                        } else {
                            TestReporter.FATAL(m_throwable);
                        }
                    }
                } finally {
                    if (!shouldHalt()) {
                        m_testCase.tearDown();
                    } else {
                        flush();
                        System.out.println("Test stopped because of error!!!");
                    }
                    TestEnvironment.setEndTime();
                }
            }

            private void flush() {
                ThreadUtils.sleep(500);
                System.err.flush();
                System.out.flush();
                ThreadUtils.sleep(500);
            }
        };
    }

    @Override
    protected Object createTest() throws Exception {
        m_throwable = null;
        m_testCase = (ITestCase) super.createTest();
        return m_testCase;
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        notifier.addListener(new RunListener() {
            @Override
            public void testFailure(Failure failure) throws Exception {
                if (shouldHalt()) {
                    System.exit(5);
                }
            }
        });
        Description description = describeChild(method);
        if ((method.getAnnotation(Repeat.class) != null || method.getAnnotation(ParameterizedTest.class) != null) &&
                method.getAnnotation(Ignore.class) == null) {
            runRepeatedly(methodBlock(method), description, notifier);
            return;
        }
        super.runChild(method, notifier);
    }

    protected boolean shouldHalt() {
        return null != m_throwable && TestEnvironment.haltOnError();
    }

    private Description describeRepeatTest(FrameworkMethod method) {
        int times = method.getAnnotation(Repeat.class).value();

        Description description = Description.createSuiteDescription(
                testName(method) + " [" + times + " times]",
                method.getAnnotations());

        for (int i = 1; i <= times; i++) {
            description.addChild(Description.createTestDescription(
                    getTestClass().getJavaClass(),
                    testName(method) + "[" + i + "] "));
        }
        return description;
    }


    private void runRepeatedly(Statement statement, Description description, RunNotifier notifier) {
        for (Description desc : description.getChildren()) {
            runLeaf(statement, desc, notifier);
        }
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        if (!RegressionEnvironment.isRegression()) {
            Class<?> jTestClass = getTestClass().getJavaClass();
            for (Class<?> cInterface : jTestClass.getInterfaces()) {
                if (cInterface == IParameterizedTest.class) {
                    return;
                }
            }
        }
        super.filter(filter);
    }
}
