package com.auto.example.framework;

import com.auto.framework.AbstractTestCase;
import com.auto.framework.TestComponentData;
import com.auto.framework.iface.ITestComponent;
import com.auto.framework.reporter.TestReporter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * User: Priytam Jee Pandey
 * Date: 30/05/20
 * Time: 8:46 am
 * email: mrpjpandey@gmail.com
 */
public class ExampleTestCase extends AbstractTestCase {

    private ExampleServer server;

    protected ExampleTestCase() {
        super("example");
    }

    @Override
    protected void initComponents() {
        TestComponentData testComponentData = new TestComponentData.Builder().build(getCurrentApplicationConfig().getHost(), getCurrentApplicationConfig().getPort(), getCurrentApplicationConfig().getLogDir());
        server = new ExampleServer(testComponentData);
    }

    @Override
    public List<? extends ITestComponent> getTestComponents() {
        TestReporter.TRACE("init example components");
        List<ITestComponent> lstComponents = new ArrayList<>();
        lstComponents.add(server);
        lstComponents.addAll(getTestComponentsSpecific());
        return lstComponents;
    }

    protected Collection<? extends ITestComponent> getTestComponentsSpecific() {
        return Collections.emptyList();
    }

    public ExampleServer getServer() {
        return server;
    }
}
