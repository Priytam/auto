package com.auto.redis.operation;

import com.auto.framework.operation.commmand.AbstractCommandOperation;
import com.auto.framework.operation.commmand.CommandBuilder;

/**
 * User: Priytam Jee Pandey
 * Date: 31/05/20
 * Time: 11:15 am
 * email: priytam.pandey@cleartrip.com
 */
public class RedisPingOperation extends AbstractCommandOperation {

    private String installationDir;

    public RedisPingOperation(String installationDir) {
        this.installationDir = installationDir;
    }

    @Override
    public boolean shouldRunInBackground() {
        return false;
    }

    @Override
    public String getName() {
        return "RedisPingOp";
    }

    @Override
    protected CommandBuilder getCommandBuilder() {
        return new CommandBuilder().withCommand(new String[]{"redis-cli", "ping"}).withInstallationDir(installationDir);
    }
}
