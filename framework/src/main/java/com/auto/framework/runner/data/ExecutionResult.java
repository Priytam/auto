package com.auto.framework.runner.data;

import com.auto.framework.runner.job.TestJobResult;

import java.util.List;

/**
 * User: Priytam Jee Pandey
 * Date: 30/05/20
 * Time: 6:01 am
 * email: mrpjpandey@gmail.com
 */
public class ExecutionResult {
    private final ExecutionSummary summary;
    private final List<TestJobResult> jobResults;
    private final String testList;

    public ExecutionResult(ExecutionSummary summary, List<TestJobResult> jobResults, String testList) {
        this.summary = summary;
        this.jobResults = jobResults;
        this.testList = testList;
    }

    public ExecutionSummary getSummary() {
        return summary;
    }

    public String getTestList() {
        return testList;
    }

    public List<TestJobResult> getJobResults() {
        return jobResults;
    }

    @Override
    public String toString() {
        return "ExecutionResult{" +
                "testList='" + testList + '\'' +
                ", summary=" + summary +
                ", jobResults=" + jobResults +
                '}';
    }
}
