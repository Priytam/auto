package com.auto.redis.operation;

import com.auto.framework.operation.commmand.AbstractCommandOperation;
import com.auto.framework.operation.commmand.CommandRequest;

/**
 * User: Priytam Jee Pandey
 * Date: 31/05/20
 * Time: 11:15 am
 * email: priytam.pandey@cleartrip.com
 */
public class RedisPingOperation extends AbstractCommandOperation {


    public RedisPingOperation(String installationDir) {
        super(installationDir, new CommandRequest(new String[]{"redis-cli", "ping"}));
    }

    @Override
    public boolean shouldRunInBackground() {
        return false;
    }

    @Override
    public String getName() {
        return "RedisPingOp";
    }
}
