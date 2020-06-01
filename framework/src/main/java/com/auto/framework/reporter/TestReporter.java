package com.auto.framework.reporter;

import com.auto.framework.env.TestEnvironment;
import com.auto.framework.operation.commmand.CommandRequest;
import com.auto.framework.reporter.appender.TestFileAppender;
import com.auto.framework.reporter.appender.startegy.SeparateFilePerCommandStrategy;
import com.auto.framework.reporter.appender.startegy.SingleFileStrategy;
import com.auto.framework.reporter.iface.IOutputFileStrategy;
import com.auto.framework.runner.console.ConsoleStyle;
import com.auto.framework.env.RegressionEnvironment;
import com.auto.framework.iface.ITestCase;
import com.auto.framework.operation.OpResult;
import com.auto.framework.operation.http.HttpOpRequest;
import com.auto.framework.reporter.appender.TestConsoleAppender;
import com.auto.framework.reporter.appender.TestOutputStreamAppender;
import com.auto.framework.reporter.iface.TestReporterAppender;
import com.auto.framework.utils.FileUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.*;
import org.junit.Assert;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestReporter {

    public final static String OPERATIONS_DIR = "operations";
    public static final String CONTENT_DIR = "content";
    private final static String TEST_LOG = "test.log";
    private static final String STDOUT_APPENDER = "STDOUT_APPENDER";
    private static final String FILE_APPENDER = "FILE_APPENDER";
    private static final List<String> lstIgnoreErrMsg = Lists.newArrayList();
    private static ITestCase testCase = null;
    private static ErrorReporter reporter = null;
    private static boolean fatalOnFail = false;
    private static boolean reconfigLog = true;
    private static Map<String, TestReporterAppender> appenders = newHashMap();

    static {
        BasicConfigurator.configure();
        if (RegressionEnvironment.isRegression()) {
            IOutputFileStrategy strategy = new SingleFileStrategy();
            appenders.put(FILE_APPENDER, new TestFileAppender(strategy));
            //appenders.put(STDOUT_APPENDER, new TestOutputStreamAppender(strategy));
        } else {
            IOutputFileStrategy strategy = new SeparateFilePerCommandStrategy();
            appenders.put(FILE_APPENDER, new TestFileAppender(strategy));
            appenders.put(STDOUT_APPENDER, new TestConsoleAppender(strategy));
        }
    }

    public static void redirectOutput(OutputStream oOut) {
        ((TestOutputStreamAppender) (appenders.get(STDOUT_APPENDER))).redirectOutput(oOut);
        reconfigLog = true;
    }

    public static void init(ITestCase tTest) {
        testCase = tTest;
        initOutputDir();
        initAppenders();
        TraceLink("INIT: running with base dir", tTest.getSandBoxDir());
        TRACE("Java home: " + System.getProperty("java.home"));
        initErrorReporter();
    }

    private static void initErrorReporter() {
        reporter = new ErrorReporter(getTestCase(), getOutputDir());
    }

    public static void start() {
        logMessage("START: " + getTestCase().getTestName());
    }

    public static void finish() {
        if (!testFailed() && !TestEnvironment.shouldKeepLog()) {
            FileUtil.delete(getOutputDir());
        } else {
            reporter.dump();
            TraceLink("TEST DIR: ", getOutputDir());
        }

        logMessage("END: " + getTestCase().getTestName());
        logMessage("TEST_REPORT: " + ConsoleStyle.YELLOW  + ConsoleStyle.BOLD + (testFailed() ?   " FAILED " + ConsoleStyle.CROSS : " PASSED " + ConsoleStyle.TICK) + ConsoleStyle.RESET);

        if (testFailed()) {
            if (testFailedNoFatal()) {
                Assert.fail(reporter.getFirstFailure());
            } else {
                if (reporter.getFatalThrowable() instanceof RuntimeException) {
                    throw (RuntimeException) reporter.getFatalThrowable();
                }
                if (reporter.getFatalThrowable() instanceof Error) {
                    throw (Error) reporter.getFatalThrowable();
                }
                throw new RuntimeException(reporter.getFatalThrowable());
            }
        }
    }

    private static boolean shouldKeepLog() {
        return true;
    }

    public static void TRACE(String sMessage) {
        logMessage(sMessage);
    }

    public static void TRACE(Throwable e) {
        String message = e.getMessage() == null ? e.toString() : e.getMessage();
        logMessage("exception caught: " + message, e);
    }

    public static void PASS(String sMessage) {
        logMessage("PASS : " + sMessage);
        reporter.pass();
    }

    public static void FAIL(String sMessage) {
        if (fatalOnFail) {
            FATAL(sMessage);
        } else {
            logMessage("FAIL : " + sMessage, true);
            reporter.fail(sMessage);
        }
    }

    public static void FATAL(Throwable e) {
        if (ignoredErrorMessageExist(e.getMessage())) {
            return;
        }
        String message = e.getMessage() == null ? e.toString() : e.getMessage();
        logMessage("FATAL ERROR: " + message, e);
        fatal(e);
    }

    public static TestFailException FATAL(String sMessage) {
        if (ignoredErrorMessageExist(sMessage)) {
            return null;
        }

        TestFailException t = new TestFailException(sMessage);
        logMessage("FATAL ERROR: " + sMessage, t);
        return fatal(t);
    }

    public static void FAIL(String... sMessage) {
        FAIL(String.join("\n", sMessage));
    }

    private static TestFailException fatal(Throwable t) {
        reporter.fatal(t);
        if (getTestCase().isRunning()) {
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            if (t instanceof Error) {
                throw (Error) t;
            }
            throw new TestFailException(t);
        }
        return null;
    }

    public static void TraceLink(String sMessage, String link) {
        logMessage(sMessage + ": file://" + link);
    }

    private static void logMessage(String sMessage) {
        logMessage(sMessage, false);
    }

    private static boolean ignoredErrorMessageExist(String sMessage) {
        for (String errMsg : lstIgnoreErrMsg) {
            if (StringUtils.contains(sMessage, errMsg)) {
                return true;
            }
        }
        return false;
    }

    private static void initAppenders() {
        if (!reconfigLog) {
            return;
        }
        Logger.getRootLogger().removeAllAppenders();
        for (TestReporterAppender appender : appenders.values()) {
            appender.init();
        }

        try {
            BasicConfigurator.configure(new FileAppender(new TTCCLayout("ISO8601"), getOutputDir() + File.separator + TEST_LOG));
        } catch (Exception e) {
        }
        Logger.getRootLogger().setLevel(Level.INFO);
        reconfigLog = false;
    }

    private static ITestCase getTestCase() {
        return testCase;
    }

    private static void initOutputDir() {
        new File(getOutputDir()).mkdirs();
        new File(getOutputDir() + File.separator + OPERATIONS_DIR).mkdir();
        new File(getOutputDir() + File.separator + CONTENT_DIR).mkdir();
    }

    public static String getOutputDir() {
        return getTestCase().getSandBoxDir();
    }

    public static boolean testFailed() {
        return ((reporter.getFail() + reporter.getFatal() > 0) || (reporter.getPass() <= 0));
    }

    public static boolean testFailedNoFatal() {
        return ((reporter.getFail() > 0 && reporter.getFatal() == 0) &&
                testWithValidResults()
        );
    }

    private static boolean testWithValidResults() {
        return reporter.getFail() + reporter.getFatal() + reporter.getPass() > 0;
    }

    public static void setFatalOnFail() {
        fatalOnFail = true;
    }

    public static void addIgnoredErrorMessage(String errMsg) {
        Preconditions.checkNotNull(errMsg);
        lstIgnoreErrMsg.add(errMsg);
    }

    private static void logMessage(String sMessage, boolean bError) {
        for (TestReporterAppender appender : appenders.values()) {
            appender.logMessage(sMessage, bError);
        }
    }

    private static void logMessage(String sMessage, Throwable t) {
        for (TestReporterAppender appender : appenders.values()) {
            appender.logMessage(sMessage, t);
        }
    }

    public static ErrorReporter getErrorReporter() {
        return reporter;
    }

    public static void disable() {
        appenders.clear();
    }

    public static void traceExecution(CommandRequest cRequest, OpResult rResult) {
        for (TestReporterAppender appender : appenders.values()) {
            appender.traceExecution(cRequest, rResult);
        }
    }

    public static void traceExecution(HttpOpRequest request) {
        for (TestReporterAppender appender : appenders.values()) {
            appender.traceExecution(request);
        }
    }
}
