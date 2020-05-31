package com.auto.framework.runner.data;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class ExecutionSummary {
    private int pass;
    private int fail;
    private int fatal;

    public ExecutionSummary(int pass, int fail, int fatal) {
        this.pass = pass;
        this.fail = fail;
        this.fatal = fatal;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public int getFail() {
        return fail;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }

    public int getFatal() {
        return fatal;
    }

    public void setFatal(int fatal) {
        this.fatal = fatal;
    }

    @Override
    public String toString() {
        return "ExecutionSummary{" +
                "pass=" + pass +
                ", fail=" + fail +
                ", fatal=" + fatal +
                '}';
    }
}
