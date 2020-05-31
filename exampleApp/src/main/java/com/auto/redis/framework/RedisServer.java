package com.auto.redis.framework;

import com.auto.framework.AbstractTestComponent;
import com.auto.framework.TestComponentData;
import com.auto.framework.operation.OpResult;
import com.auto.framework.operation.Operation;
import com.auto.redis.operation.RedisFlushDbOperation;
import com.auto.redis.operation.RedisPingOperation;
import com.auto.redis.operation.RedisStartOp;
import com.auto.redis.operation.RedisStopOp;
import org.apache.commons.collections.CollectionUtils;

/**
 * User: Priytam Jee Pandey
 * Date: 31/05/20
 * Time: 10:27 am
 * email: priytam.pandey@cleartrip.com
 */
public class RedisServer extends AbstractTestComponent {

    protected RedisServer(TestComponentData data) {
        super(data);
    }

    @Override
    protected Operation getStartOperation() {
        return new RedisStartOp(getInstallationDir());
    }

    @Override
    protected Operation getStopOperation() {
        return new RedisStopOp(getInstallationDir());
    }

    @Override
    public boolean isRunning() {
        OpResult opResult = performOperation(new RedisPingOperation(getInstallationDir()));
        return CollectionUtils.isNotEmpty(opResult.getStdOut()) && opResult.getStdOut().contains("PONG");
    }

    @Override
    public void clean(boolean bForce) {
        performOperation(new RedisFlushDbOperation(getInstallationDir()));
    }

    @Override
    public void prepare() {

    }

    public void put(String testKey, String testValue) {

    }

    public String get(String testKey) {
        return null;
    }
}
