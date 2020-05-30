package com.auto.framework.runner.job;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public interface ITestJob {
    void execute();

    TestJobResult getResult();

    void handleException(Exception e);
}
