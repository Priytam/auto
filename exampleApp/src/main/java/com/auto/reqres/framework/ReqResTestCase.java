package com.auto.reqres.framework;

import com.auto.framework.AbstractTestCase;
import com.auto.framework.TestComponentData;
import com.auto.framework.iface.ITestComponent;

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
public class ReqResTestCase extends AbstractTestCase {

    private ReqResServer server;
    private ArrayList<ITestComponent> lstComponents;

    protected ReqResTestCase() {
        super("ReqRes");
    }

    @Override
    protected void initComponents() {
        TestComponentData testComponentData = new TestComponentData
                .Builder()
                .withAppConfig(getCurrentApplicationConfig())
                .withResourcePath(getConfig().getResourcePath())
                .build();
        server = new ReqResServer(testComponentData);

        lstComponents = new ArrayList<>();
        lstComponents.add(server);
        lstComponents.addAll(initComponentsSpecific());

    }

    protected Collection<? extends ITestComponent> initComponentsSpecific() {
        return Collections.emptyList();
    }

    @Override
    public List<? extends ITestComponent> getTestComponents() {
        return lstComponents;
    }


    public ReqResServer getServer() {
        return server;
    }
}
