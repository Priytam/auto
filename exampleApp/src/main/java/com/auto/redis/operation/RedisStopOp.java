package com.auto.redis.operation;

import com.auto.framework.operation.commmand.AbstractCommandOperation;
import com.auto.framework.operation.commmand.CommandBuilder;

public class RedisStopOp extends AbstractCommandOperation {
    private String installationDir;

    public RedisStopOp(String installationDir) {
        this.installationDir = installationDir;
    }

    @Override
    public boolean shouldRunInBackground() {
        return false;
    }

    @Override
    public String getName() {
        return "RedisStopOp";
    }

    @Override
    protected CommandBuilder getCommandBuilder() {
        return new CommandBuilder().withCommand(new String[]{"redis-cli", "shutdown"}).withInstallationDir(installationDir);
    }
}