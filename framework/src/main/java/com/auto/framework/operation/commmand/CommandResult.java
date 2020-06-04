package com.auto.framework.operation.commmand;

import com.auto.framework.operation.OpResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class CommandResult implements OpResult {

    private static final long serialVersionUID = 1L;

    private final int exitStatus;
    private final List<String> lstStdOut = new ArrayList<String>();
    private final List<String> lstStdErr = new ArrayList<String>();
    private long executionTime = 0;

    /**
     * @param exitStatus command exist status code 0 if ran successfully -1  if timed out
     * @param output command output per line as new element in list
     * @param errorOutput error output per line as new element in list
     * @param lExecutionTime time took to execute command in millisec
     */
    public CommandResult(int exitStatus, List<String> output, List<String> errorOutput, long lExecutionTime) {
        this.exitStatus = exitStatus;
        lstStdOut.addAll(output);
        lstStdErr.addAll(errorOutput);
        executionTime = lExecutionTime;
    }

    @Override
    public int getExitStatus() {
        return exitStatus;
    }

    @Override
    public List<String> getStdOut() {
        return Collections.unmodifiableList(lstStdOut);
    }

    @Override
    public List<String> getStdErr() {
        return Collections.unmodifiableList(lstStdErr);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExitStatus: " + getExitStatus() + "\n");
        sb.append("StdOut:\n");
        sb.append(String.join("\n", getStdOut()));
        sb.append("\nStdErr:\n");
        sb.append(String.join("\n", getStdErr()));
        return sb.toString();
    }

    @Override
    public long getExecutionTime() {
        return executionTime;
    }


    @Override
    public String toStringAsOneLine() {
        return "CommandResult [exitStatus=" + exitStatus + ", lstStdOut=" + lstStdOut + ", lstStdErr=" + lstStdErr + ", executionTime=" + executionTime + "]";
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
}
