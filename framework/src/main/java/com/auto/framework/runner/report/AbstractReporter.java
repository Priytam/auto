package com.auto.framework.runner.report;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public abstract class AbstractReporter implements TestJobReporter {
    protected static final String[] SUMMARY_COLUMN_NAMES = {"PASS", "FATAL", "FAIL"};
    protected static final String[] DETAIL_COLUMN_NAMES = { "Class", "Test", "Status", "Message"};
}
