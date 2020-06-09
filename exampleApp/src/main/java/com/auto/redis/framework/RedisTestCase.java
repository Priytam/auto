package com.auto.redis.framework;

import com.auto.framework.AbstractTestCase;
import com.auto.framework.TestComponentData;
import com.auto.framework.iface.ITestComponent;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Priytam Jee Pandey
 * Date: 31/05/20
 * Time: 7:23 pm
 * email: priytam.pandey@cleartrip.com
 */
public class RedisTestCase extends AbstractTestCase {

    private RedisServer server;
    private ArrayList<RedisServer> redisServers = new ArrayList<>();

    protected RedisTestCase() {
        super("RedisServer");
    }

    @Override
    protected void initComponents() {
        TestComponentData componentData = new TestComponentData
                .Builder()
                .withAppConfig(getCurrentApplicationConfig())
                .withResourcePath(getConfig().getResourcePath())
                .build();
        server = new RedisServer(componentData);
        redisServers = Lists.newArrayList(server);
    }

    @Override
    public List<? extends ITestComponent> getTestComponents() {
        return redisServers;
    }

    public RedisServer getServer() {
        return server;
    }
}
