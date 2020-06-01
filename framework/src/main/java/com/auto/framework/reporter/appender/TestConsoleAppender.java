package com.auto.framework.reporter.appender;

import com.auto.framework.operation.commmand.CommandRequest;
import com.auto.framework.reporter.iface.IOutputFileStrategy;
import com.auto.framework.operation.OpResult;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestConsoleAppender extends TestAppender {

    private boolean startOfLine = true;
    private static ThreadLocal<CheckData> checkData = new ThreadLocal<CheckData>();
    private CommandRequest lastRequest;
    private OpResult lastResult;

    public TestConsoleAppender(IOutputFileStrategy strategy) {
        super(strategy);
    }

    public static CheckData getCheckData() {
        if (null == checkData.get()) {
            checkData.set(new CheckData());
        }
        return checkData.get();
    }

    @Override
    public void logMessage(String sMessage, boolean bError) {
        if (!startOfLine) {
            handlePreviousPrint();
        }
        if (!bError) {
            System.out.println(sMessage + " - called by " + generateCaller());
        } else {
            System.out.println(sMessage + " - called by " + generateCaller() + "\n" + generateException());
        }
        System.out.flush();
    }

    private void handlePreviousPrint() {
        System.out.print("\n");
        CommandRequest request = lastRequest;
        lastRequest = null;
        OpResult result = lastResult;
        lastResult = null;
        startOfLine = true;
        if (null != request) {
            int commandNum = getCommandNum();
            setCommandNum(commandNum - 1);
            super.traceExecution(request, result);
            setCommandNum(commandNum);
        }
    }

    @Override
    public void traceExecution(CommandRequest cRequest, OpResult rResult) {
        if (!getCheckData().isInCheck() || getCheckData().isReportingCheck()) {
            super.traceExecution(cRequest, rResult);
        } else {
            incrementCommandNum();
            System.out.print(".");
            startOfLine = false;
            lastRequest = cRequest;
            lastResult = rResult;
        }
    }

    @Override
    public void logMessage(String sMessage, Throwable t) {
        System.out.println(sMessage + " - called by " + generateCaller());
        t.printStackTrace();
        System.out.flush();
    }
}
