package com.auto.reqres;

import com.auto.framework.runner.TestsExecutor;

public class Runner {

    public static void main(String[] args) {
        new TestsExecutor()
                .withOnExecutionFailure((e -> System.out.println(e.getMessage())))
                .execute("com.auto.reqres.tests");
    }
}
