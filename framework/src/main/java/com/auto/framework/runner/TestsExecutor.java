package com.auto.framework.runner;

import com.auto.framework.env.RegressionEnvironment;
import com.auto.framework.reporter.TestReporter;
import com.auto.framework.runner.console.ConsoleStyle;
import com.auto.framework.runner.console.progressbar.ProgressBar;
import com.auto.framework.runner.console.progressbar.ProgressBarStyle;
import com.auto.framework.runner.data.ExecutionResult;
import com.auto.framework.runner.data.ExecutionSummary;
import com.auto.framework.runner.job.TestJob;
import com.auto.framework.runner.job.TestJobResult;
import com.auto.framework.runner.job.TestJobRetryHandler;
import com.auto.framework.runner.mail.MailConfig;
import com.auto.framework.runner.mail.MailTemplateBuilder;
import com.auto.framework.runner.report.ConsoleReporter;
import com.auto.framework.runner.report.MailReporter;
import com.auto.framework.runner.testlist.TestListBuilder;
import com.auto.framework.runner.testlist.TestListBuilderImpl;
import com.google.common.collect.Lists;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestsExecutor {
    protected static final Logger logger = Logger.getLogger(TestsExecutor.class);

    private String testsXml;
    private int pass = 0;
    private int fail = 0;
    private int fatal = 0;
    private int retryCount = 0;
    private ExecutionSummary summary;
    private final List<TestJobResult> results = Lists.newArrayList();
    private TestListBuilder testListBuilder = new TestListBuilderImpl();
    private Consumer<Exception> testFailureExceptionHandler = null;
    private Consumer<Collection<String>> beforeExecution = (tests) -> System.out.println("***Starting test executor");
    private Consumer<ExecutionResult> afterExecution = (result) -> System.out.println("***Shutting down test executor");
    private Consumer<Exception> onExecutionFailure = (result) -> System.out.println("**Test executed failed");
    private Consumer<ExecutionResult> onExecutionSuccess = (result) -> System.out.println("**Test successfully executed");
    private final List<Consumer<ExecutionResult>> reportConsumers = Lists.newArrayList();
    private boolean mailReportEnabled = false;
    private MailConfig mailConfig;
    private Function<ExecutionResult, List<MailTemplateBuilder.TableBuilder>> tablesBuilderFunction;
    private Consumer<TestJobResult> onTestCompletion;
    private Consumer<? super Path> testLogDirHandler;

    public void execute(String packagePrefix) {
        init();
        Collection<String> tests = buildTests(packagePrefix);
        ExecutionResult executionResult = null;
        beforeExecution(tests);
        try (ProgressBar pb = new ProgressBar("", tests.size(), 1000, System.out, ProgressBarStyle.COLORFUL_UNICODE_BLOCK, "", 1)) {
            executeTests(tests, pb);
            executionResult = new ExecutionResult(summary, results, testsXml);
        } catch (Exception e) {
            onExecutionFailure(e);
        } finally {
            onExecutionSuccess(executionResult);
        }
        afterExecution(executionResult);
    }

    private void calculatePassFail() {
        pass = pass + TestReporter.getErrorReporter().getPass();
        fail = fail + TestReporter.getErrorReporter().getFail();
        fatal = fatal + TestReporter.getErrorReporter().getFatal();
    }

    private void init() {
        System.setProperty(RegressionEnvironment.REGRESSION.name(), "true");
        BasicConfigurator.configure();
    }

    private Collection<String> buildTests(String packagePrefix) {
        Collection<String> tests = testListBuilder.build(packagePrefix);
        testsXml = testListBuilder.getXmlString(tests);
        return tests;
    }

    private void executeTests(Collection<String> tests, ProgressBar pb) {
        for (String test : tests) {
            pb.step();
            TestJob testJob = new TestJob(test, testFailureExceptionHandler);
            String currentTest = ConsoleStyle.decorate(ConsoleStyle.PURPLE, "[ " + testJob.getTestName() + " ]");
            pb.setExtraMessage(currentTest);
            TestJobResult result = new TestJobRetryHandler().doWithRetry(retryCount + 1, testJob);
            results.add(result);
            onTestCompletion(result);
        }
        summary = new ExecutionSummary(pass, fail, fatal);
    }

    private void onTestCompletion(TestJobResult result) {
        calculatePassFail();
        handleLogFile();
        if (null != onTestCompletion) {
            onTestCompletion.accept(result);
        }
    }

    private void handleLogFile() {
        if (null != testLogDirHandler) {
            try (Stream<Path> paths = Files.walk(Paths.get(TestReporter.getOutputDir()))) {
                paths.forEach(testLogDirHandler);
            } catch (Exception e) {

            }
        }
    }

    private void onExecutionFailure(Exception e) {
        onExecutionFailure.accept(e);
    }

    private void onExecutionSuccess(ExecutionResult executionResult) {
        report(executionResult);
        onExecutionSuccess.accept(executionResult);
    }

    private void beforeExecution(Collection<String> tests) {
        beforeExecution.accept(tests);
    }

    private void afterExecution(ExecutionResult executionResult) {
        afterExecution.accept(executionResult);
    }

    private void report(ExecutionResult executionResult) {
        reportConsumers.add(new ConsoleReporter());
        if (mailReportEnabled) {
            MailReporter reporter = Objects.nonNull(tablesBuilderFunction)
                    ? new MailReporter(mailConfig, tablesBuilderFunction.apply(executionResult)) :  new MailReporter(mailConfig);
            reportConsumers.add(reporter);
        }
        reportConsumers.parallelStream().forEach(consumer -> consumer.accept(executionResult));
    }

    public TestsExecutor withTestListBuilder(TestListBuilder builder) {
        testListBuilder = builder;
        return this;
    }

    public TestsExecutor withBeforeExecution(Consumer<Collection<String>> consumer) {
        beforeExecution = consumer;
        return this;
    }

    public TestsExecutor withAfterExecution(Consumer<ExecutionResult> consumer) {
        afterExecution = consumer;
        return this;
    }

    public TestsExecutor withTestRetryCount(int count) {
        retryCount = count;
        return this;
    }

    public TestsExecutor withTestFailureExceptionHandler(Consumer<Exception> exceptionHandler) {
        testFailureExceptionHandler = exceptionHandler;
        return this;
    }

    public TestsExecutor withOnExecutionFailure(Consumer<Exception> onFailure) {
        onExecutionFailure = onFailure;
        return this;
    }

    public TestsExecutor withOnExecutionSuccess(Consumer<ExecutionResult> onSuccess) {
        onExecutionSuccess = onSuccess;
        return this;
    }

    public TestsExecutor withEnableMail(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
        mailReportEnabled = true;
        return this;
    }

    public TestsExecutor withMailTablesBuilder(Function<ExecutionResult, List<MailTemplateBuilder.TableBuilder>> tablesBuilderFunction) {
        this.tablesBuilderFunction = tablesBuilderFunction;
        return this;
    }

    public TestsExecutor withOnTestCompletion(Consumer<TestJobResult> onTestCompletion) {
        this.onTestCompletion = onTestCompletion;
        return this;
    }

    public TestsExecutor withLogFileHandler(Consumer<Path> testLogHandler) {
        this.testLogDirHandler = testLogHandler;
        return this;
    }
}
