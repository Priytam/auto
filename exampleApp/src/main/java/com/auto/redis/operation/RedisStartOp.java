package com.auto.redis.operation;

import com.auto.framework.operation.commmand.AbstractCommandOperation;
import com.auto.framework.operation.commmand.CommandRequest;

/**
 * User: Priytam Jee Pandey
 * Date: 31/05/20
 * Time: 10:34 am
 * email: priytam.pandey@cleartrip.com
 */
public class RedisStartOp extends AbstractCommandOperation {

    public RedisStartOp(String installationDir) {
        super(installationDir, new CommandRequest(new String[] {"redis-server"}));
    }

    @Override
    public boolean shouldRunInBackground() {
        return false;
    }

    @Override
    public String getName() {
        return "RedisStartOp";
    }
}
