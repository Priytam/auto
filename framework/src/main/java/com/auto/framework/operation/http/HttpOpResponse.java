package com.auto.framework.operation.http;


import com.auto.framework.operation.OpResult;

import java.util.List;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class HttpOpResponse implements OpResult {

    private int exitStatus;
    private List<String> stdOut;
    private List<String> stdErr;
    private long executionTime;

    public HttpOpResponse(int exitStatus, List<String> stdOut, List<String> stdErr, long executionTime) {
        this.exitStatus = exitStatus;
        this.stdOut = stdOut;
        this.stdErr = stdErr;
        this.executionTime = executionTime;
    }

    @Override
    public int getExitStatus() {
        return exitStatus;
    }

    @Override
    public List<String> getStdOut() {
        return stdOut;
    }

    @Override
    public List<String> getStdErr() {
        return stdErr;
    }

    @Override
    public long getExecutionTime() {
        return executionTime;
    }

    @Override
    public String toStringAsOneLine() {
        return stdOut.get(0);
    }
}
