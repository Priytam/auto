package com.auto.reqres;

import com.auto.framework.runner.TestsExecutor;

public class Runner {

    public static void main(String[] args) {
        new TestsExecutor()
                .withTestRetryCount(2)
                .execute("com.auto.reqres.tests");
    }
}
