package com.auto.framework.runner.job;

/**
 * User: Priytam Jee Pandey
 * Date: 30/05/20
 * Time: 6:20 am
 * email: mrpjpandey@gmail.com
 */
public class TestJobRetryHandler {
    public TestJobResult doWithRetry(int maxAttempts, ITestJob job) {
        TestJobResult result = null;
        for (int count = 0; count < maxAttempts; count++) {
            try {
                job.execute();
                result = job.getResult();
                if (result.getStatus().equals(TestJobResult.JobStatus.SUCCESS) && result.isPass()) {
                    count = maxAttempts; //don't retry
                }
            } catch (Exception e) {
                job.handleException(e);
            }
        }
        return result;
    }
}
