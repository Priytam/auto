package com.auto.framework.reporter.appender;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class CheckData {

    private boolean firstCheck;
    private boolean inCheck;

    public void setFirstCheck(boolean firstCheck) {
        this.firstCheck = firstCheck;
    }

    public boolean isReportingCheck() {
        return firstCheck;
    }

    public boolean isInCheck() {
        return inCheck;
    }

    public void setInCheck(boolean inCheck) {
        this.inCheck = inCheck;
    }

}
