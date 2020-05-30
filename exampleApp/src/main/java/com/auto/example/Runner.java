package com.auto.example;

import com.auto.framework.runner.TestsExecutor;
import com.auto.framework.runner.mail.MailConfig;
import com.auto.framework.utils.JsonUtil;

import java.io.InputStream;

public class Runner {

    public static void main(String[] args) {
        InputStream in = Runner.class.getResourceAsStream("/mailconfig.json");
        new TestsExecutor()
                .withEnableMail(JsonUtil.serialize(in, MailConfig.class))
                .execute("com.auto.example.tests");
    }
}
