package com.auto.framework.runner.job;

import com.auto.framework.reporter.ErrorReporter;
import com.auto.framework.reporter.TestReporter;
import com.auto.framework.reporter.data.TestDataReporter;

import java.util.function.Consumer;

/**
 * User: Priytam Jee Pandey
 * Date: 30/05/20
 * Time: 6:20 am
 * email: mrpjpandey@gmail.com
 */
public class TestJob implements ITestJob {
    private final String fqcn;
    private TestJobResult result;
    private final Consumer<Exception> exceptionHandler;

    public TestJob(String fqcn, Consumer<Exception> exceptionHandler) {
        this.fqcn = fqcn;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void execute() {
        long start = System.currentTimeMillis();
        TestJobResult.JobStatus status = TestJobResult.JobStatus.FAIL;
        try {
            SingleTestRunner.runTest(fqcn);
            status = TestJobResult.JobStatus.SUCCESS;
        } catch (Exception e) {
            //ignore
        }
        long timeTaken = System.currentTimeMillis() - start;
        ErrorReporter errorReporter = TestReporter.getErrorReporter();
        result = new TestJobResult(!TestReporter.testFailed(), getTime(timeTaken), getTestName(), getClassName(), fqcn, errorReporter.getFirstFailure(), status, TestDataReporter.getTestData());
    }


    private String getTime(long timeTaken) {
        long minutes = (timeTaken / 1000) / 60;
        long seconds = (timeTaken / 1000) % 60;
        return minutes + "min" + seconds + "sec";
    }

    public String getTestName() {
        String[] split = fqcn.split("\\.");
        return split[split.length - 1];
    }

    public String getClassName() {
        String[] split = fqcn.split("\\.");
        return split.length >= 2 ? split[split.length - 2] : "";
    }

    public String getFqcn() {
        return fqcn;
    }

    @Override
    public TestJobResult getResult() {
        return result;
    }

    @Override
    public void handleException(Exception e) {
        if (null != exceptionHandler) {
            exceptionHandler.accept(e);
        }
    }
}
