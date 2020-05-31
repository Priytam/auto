package com.auto.redis.operation;

import com.auto.framework.operation.commmand.AbstractCommandOperation;
import com.auto.framework.operation.commmand.CommandRequest;

/**
 * User: Priytam Jee Pandey
 * Date: 31/05/20
 * Time: 11:15 am
 * email: priytam.pandey@cleartrip.com
 */
public class RedisFlushDbOperation extends AbstractCommandOperation {

    public RedisFlushDbOperation(String installationDir) {
        super(installationDir, new CommandRequest(new String[]{"redis-cli", "flushdb"}));
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
