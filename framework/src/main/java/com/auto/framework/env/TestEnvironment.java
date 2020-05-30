package com.auto.framework.env;

import com.auto.framework.auto.AutoConf;
import com.auto.framework.iface.ITestCase;
import com.auto.framework.reporter.TestReporter;
import com.auto.framework.reporter.data.TestDataReporter;
import org.junit.rules.TemporaryFolder;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestEnvironment {

    private static String baseDir;
    private static TemporaryFolder tmpFolder;
    private static ITestCase testCase;
    private static long initTime;
    private static boolean haltOnError;
    private static String currentApplication;
    private static boolean shouldKeepLog;
    private static Method testMethod;
    private static long readyTime;
    private static long doneTime;
    private static long endTime;
    private static AutoConf config;

    public static void init(ITestCase tTestCase, String sBaseDir, TemporaryFolder sTmpFolder) {
        baseDir = sBaseDir;
        tmpFolder = sTmpFolder;
        testCase = tTestCase;
        initTime = System.currentTimeMillis();
        //TestReporter.TRACE("Test started at : " + new Date(initTime));
    }

    public static void setReadyTime() {
        readyTime = System.currentTimeMillis();
        TestReporter.TRACE("Test Sequence Ready to execute at : " + new Date(readyTime));
    }

    public static boolean haltOnError() {
        return haltOnError;
    }

    public static void setCurrentApplication(String sCurrentApplication) {
        currentApplication = sCurrentApplication;
    }

    public static void setHaltOnError(boolean bHaltOnError) {
        haltOnError = bHaltOnError;
    }

    public static void setShouldKeepLog(boolean bShouldKeepLog) {
        shouldKeepLog = bShouldKeepLog;
    }

    public static void setTestMethod(Method method) {
        testMethod = method;
    }

    public static void setDoneTime() {
        doneTime = System.currentTimeMillis();
        TestReporter.TRACE("Test Sequence Finished at : " + new Date(doneTime));
        TestDataReporter.addData("duration", (double) getTestTime());
    }

    public static void setEndTime() {
        endTime = System.currentTimeMillis();
        TestReporter.TRACE("Test Completed at : " + new Date(endTime));
    }

    public static String getBaseDir() {
        return baseDir;
    }

    public static TemporaryFolder getTmpFolder() {
        return tmpFolder;
    }

    public static ITestCase getTestCase() {
        return testCase;
    }

    public static long getInitTime() {
        return initTime;
    }

    public static long getReadyTime() {
        return readyTime;
    }

    public static long getTestTime() {
        return (doneTime - readyTime) / 1000;
    }

    public static long getDoneTime() {
        return doneTime;
    }

    public static long getEndTime() {
        return endTime;
    }

    public static String getCurrentApplication() {
        return currentApplication;
    }

    public static boolean shouldKeepLog() {
        return shouldKeepLog;
    }

    public static Method getTestMethod() {
        return testMethod;
    }

    public static AutoConf getConfig() {
        return config;
    }

    public static void setConfig(AutoConf config) {
        TestEnvironment.config = config;
    }

    public static String getResourcePath(){
        return config.getResourcePath();
    }
}
