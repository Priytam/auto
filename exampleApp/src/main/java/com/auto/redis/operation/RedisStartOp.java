package com.auto.redis.operation;

import com.auto.framework.operation.commmand.AbstractCommandOperation;
import com.auto.framework.operation.commmand.CommandBuilder;

/**
 * User: Priytam Jee Pandey
 * Date: 31/05/20
 * Time: 10:34 am
 * email: priytam.pandey@cleartrip.com
 */
public class RedisStartOp extends AbstractCommandOperation {

    private String installationDir;

    public RedisStartOp(String installationDir) {
        this.installationDir = installationDir;
    }

    @Override
    public boolean shouldRunInBackground() {
        return true;
    }

    @Override
    public String getName() {
        return "RedisStartOp";
    }

    @Override
    protected CommandBuilder getCommandBuilder() {
        return new CommandBuilder().withCommand(new String[]{"redis-server"}).withInstallationDir(installationDir);
    }
}
