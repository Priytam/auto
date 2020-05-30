package com.auto.framework.reporter.iface;

import com.auto.framework.operation.commmand.CommandRequest;
import com.auto.framework.operation.OpResult;
import com.auto.framework.operation.http.HttpOpRequest;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public interface TestReporterAppender {

    void logMessage(String sMessage, boolean bError);

    void logMessage(String sMessage, Throwable t);

    void init();

    void traceExecution(CommandRequest cRequest, OpResult rResult);

    void traceExecution(HttpOpRequest request);
}
