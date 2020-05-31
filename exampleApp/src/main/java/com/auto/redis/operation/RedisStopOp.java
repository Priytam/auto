package com.auto.redis.operation;

import com.auto.framework.operation.commmand.AbstractCommandOperation;
import com.auto.framework.operation.commmand.CommandRequest;

public class RedisStopOp extends AbstractCommandOperation {

    public RedisStopOp(String installationDir) {
        super(installationDir, new CommandRequest(new String[]{"redis-cli", "shutdown"}));
    }

    @Override
    public boolean shouldRunInBackground() {
        return false;
    }

    @Override
    public String getName() {
        return "RedisStopOp";
    }
}