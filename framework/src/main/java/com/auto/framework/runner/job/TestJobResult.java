package com.auto.framework.runner.job;

import com.auto.framework.reporter.data.TestDataReporterItem;

import java.util.List;

public class TestJobResult {
    private final boolean pass;
    private final String time;
    private final String name;
    private final String className;
    private final String fqcn;
    private final String errorMessage;
    private final JobStatus status;
    private final List<TestDataReporterItem> customTestData;

    public TestJobResult(boolean pass, String time, String name, String className, String fqcn, String errorMessage, JobStatus status, List<TestDataReporterItem> customTestData) {
        this.pass = pass;
        this.time = time;
        this.name = name;
        this.className = className;
        this.fqcn = fqcn;
        this.errorMessage = errorMessage;
        this.status = status;
        this.customTestData = customTestData;
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

    public List<TestDataReporterItem> getCustomTestData() {
        return customTestData;
    }

    @Override
    public String toString() {
        return "TestJobResult{" +
                "pass=" + pass +
                ", time='" + time + '\'' +
                ", name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", fqcn='" + fqcn + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", status=" + status +
                ", customTestData=" + customTestData +
                '}';
    }
}
