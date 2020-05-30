package com.auto.framework.runner.job;

public class TestJobResult {
    private final boolean pass;
    private final String time;
    private final String name;
    private final String className;
    private final String fqcn;
    private final String errorMessage;
    private final JobStatus status;

    public TestJobResult(boolean pass, String time, String name, String className, String fqcn, String errorMessage, JobStatus status) {
        this.pass = pass;
        this.time = time;
        this.name = name;
        this.className = className;
        this.fqcn = fqcn;
        this.errorMessage = errorMessage;
        this.status = status;
    }


    public enum JobStatus {
        SUCCESS, FAIL
    }

    public boolean isPass() {
        return pass;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getFqcn() {
        return fqcn;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public JobStatus getStatus() {
        return status;
    }
}
