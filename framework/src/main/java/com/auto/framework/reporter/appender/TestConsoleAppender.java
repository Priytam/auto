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

    private boolean m_bStartOfLine = true;
    private static ThreadLocal<CheckData> m_checkData = new ThreadLocal<CheckData>();
    private CommandRequest m_lastRequest;
    private OpResult m_lastResult;
    private int m_CountMissed = 0;

    public TestConsoleAppender(IOutputFileStrategy strategy) {
        super(strategy);
    }

    public static CheckData getCheckData() {
        if (null == m_checkData.get()) {
            m_checkData.set(new CheckData());
        }
        return m_checkData.get();
    }

    @Override
    public void logMessage(String sMessage, boolean bError) {
        if (!m_bStartOfLine) {
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
        CommandRequest request = m_lastRequest;
        m_lastRequest = null;
        OpResult result = m_lastResult;
        m_lastResult = null;
        m_bStartOfLine = true;
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
            m_bStartOfLine = false;
            m_lastRequest = cRequest;
            m_lastResult = rResult;

        }
    }

    @Override
    public void logMessage(String sMessage, Throwable t) {
        System.out.println(sMessage + " - called by " + generateCaller());
        t.printStackTrace();
        System.out.flush();
    }
}
