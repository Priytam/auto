package com.auto.framework.mock;


import com.auto.framework.AbstractTestCase;
import com.auto.framework.reporter.TestReporter;
import com.auto.framework.TestComponentData;
import com.auto.framework.iface.ITestComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class MockServerTestCase extends AbstractTestCase {

    private final TestComponentData.Builder builder;
    private MockServer server;

    protected MockServerTestCase() {
        super("Mock Server");
        builder = new TestComponentData.Builder();
    }

    @Override
    protected void initComponents() {
        server = new MockServer(builder.build(null, 0, null), mockRequestResponseRule.getConfig());
    }

    @Override
    public List<? extends ITestComponent> getTestComponents() {
        TestReporter.TRACE("init test components");
        List<ITestComponent> lstComponents = new ArrayList<>();
        lstComponents.add(server);
        lstComponents.addAll(getTestComponentsSpecific());
        return lstComponents;
    }

    protected Collection<? extends ITestComponent> getTestComponentsSpecific() {
        return Collections.emptyList();
    }

    public MockServer getServer() {
        return server;
    }
}
