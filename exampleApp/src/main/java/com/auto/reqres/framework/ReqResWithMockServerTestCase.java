package com.auto.reqres.framework;

import com.auto.framework.TestComponentData;
import com.auto.framework.iface.ITestComponent;
import com.auto.framework.mock.MockServer;
import com.auto.reqres.model.User;
import com.google.common.collect.Lists;

import java.util.Collection;

/**
 * User: Priytam Jee Pandey
 * Date: 07/06/20
 * Time: 1:20 pm
 * email: mrpjpandey@gmail.com
 */
public class ReqResWithMockServerTestCase extends ReqResTestCase {


    private MockServer mockServer;

    @Override
    protected Collection<? extends ITestComponent> initComponentsSpecific() {
        TestComponentData testComponentData = new TestComponentData
                .Builder()
                .withAppConfig(getConfigForApp("Mock Server"))
                .withResourcePath(getConfig().getResourcePath())
                .build();

        mockServer = new MockServer(testComponentData, mockRequestResponseRule.getConfig());
        return Lists.newArrayList(mockServer);
    }

    public MockServer getMockServer() {
        return mockServer;
    }

    protected User getMockedUser(int userId) {
        return getServer().getUser(getMockServer().getServer(), getMockServer().getHost(), getMockServer().getPort(), userId);
    }
}
