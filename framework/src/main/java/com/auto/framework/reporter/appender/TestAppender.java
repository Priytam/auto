package com.auto.framework.reporter.appender;

import com.auto.framework.operation.commmand.CommandRequest;
import com.auto.framework.reporter.TestReporter;
import com.auto.framework.reporter.iface.IOutputFileStrategy;
import com.auto.framework.reporter.iface.TestReporterAppender;
import com.auto.framework.CustomTestRunner;
import com.auto.framework.operation.OpResult;
import com.auto.framework.operation.http.HttpOpRequest;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestAppender implements TestReporterAppender {
    protected static final Logger log = Logger.getLogger(TestAppender.class);

    private int iCommandNum = 0;
    private IOutputFileStrategy fileStrategy;

    public TestAppender(IOutputFileStrategy strategy) {
        fileStrategy = strategy;
    }

    public void incrementCommandNum() {
        iCommandNum++;
    }

    @Override
    public void logMessage(String sMessage, boolean bError) {
        if (!bError) {
            log.info(generateCaller() + " - " + sMessage);
        } else {
            log.error(generateCaller() + " - " + sMessage + generateException());
        }
    }

    @Override
    public void logMessage(String sMessage, Throwable t) {
        log.error(generateCaller() + " - " + sMessage, t);
    }


    protected String generateCaller() {
        try {
            StackTraceElement ste[] = new Exception().getStackTrace();
            for (int i = 0; i < ste.length; i++) {
                String fileName = ste[i].getFileName().replace(".java", "");
                if (fileName.equals(TestReporter.class.getSimpleName())
                        || fileName.equals(TestAppender.class.getSimpleName())
                        || fileName.equals(TestOutputStreamAppender.class.getSimpleName())
                        || fileName.equals(TestConsoleAppender.class.getSimpleName())
                        || fileName.equals(CustomTestRunner.class.getSimpleName())
                ) {
                    continue;
                }
                return fileName + "." + ste[i].getMethodName() + "()";
            }
        } catch (final Throwable t) {
        }
        return "";
    }

    protected String generateException() {
        try {
            StackTraceElement ste[] = new Exception().getStackTrace();
            String sException = "";
            for (int i = 0; i < ste.length; i++) {
                if (ste[i].getClassName().equals(TestReporter.class.getName())) {
                    continue;
                }
                if (!ste[i].getClassName().startsWith("ct.air.")) {
                    break;
                }
                sException += "\n\tat " + ste[i].getClassName() + "." + ste[i].getMethodName() + "(" + ste[i].getFileName() + ":" + ste[i].getLineNumber() + ")";
            }
            return sException;
        } catch (final Throwable t) {
        }
        return "";
    }

    @Override
    public void init() {
        resetCounters();
    }

    @Override
    public void traceExecution(CommandRequest cRequest, OpResult rResult) {
        String sCommandResultPath = dumpCommandResults(cRequest, rResult);
        logMessage("executed command " + iCommandNum + ": " + String.join(" ", cRequest.getCommand()), false);
        logMessage("result in: file://" + sCommandResultPath, false);
    }

    @Override
    public void traceExecution(HttpOpRequest request) {
        String sResultPath = dumpWebResults(request.getCommandName(), request.getUrl().toString(), request.getContent(), request.getStatusCode(), request.getOutput().toString());
        logMessage("executed request " + request.getUrl(), false);
        logMessage("result in file " + " file://" + sResultPath, false);
    }

    private synchronized String dumpCommandResults(CommandRequest cRequest, OpResult rResult) {
        iCommandNum++;
        String sCommandFile = fileStrategy.getCommandFile(cRequest.getCommandName() + "_" + iCommandNum);
        String sFileName = TestReporter.getOutputDir() + File.separator + TestReporter.COMMANDS_DIR + File.separator + sCommandFile;
        writeToFile(cRequest, rResult, sFileName);
        return sFileName;
    }

    private synchronized String dumpWebResults(String sName, String sUrl, String sRequest, int iStatusCode, String bOutput) {
        iCommandNum++;
        String sCommandFile = fileStrategy.getCommandFile(sName + "_" + iCommandNum);
        String sFileName = TestReporter.getOutputDir() + File.separator + TestReporter.COMMANDS_DIR + File.separator + sCommandFile;
        writeToFile(sRequest, iStatusCode, bOutput, sUrl, sFileName);
        return sFileName;
    }

    protected void writeToFile(String sRequest, int iStatusCode, String bOutput, String sUrl, String sFileName) {


    }

    protected void writeToFile(CommandRequest cRequest, OpResult rResult, String sFileName) {

    }

    private void resetCounters() {
        iCommandNum = 0;
    }

    protected int getCommandNum() {
        return iCommandNum;
    }

    protected synchronized void setCommandNum(int iCommandNum) {
        this.iCommandNum = iCommandNum;
    }
}
