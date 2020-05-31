package com.auto.reqres.framework;

import com.auto.framework.AbstractTestCase;
import com.auto.framework.TestComponentData;
import com.auto.framework.auto.AutoConf;
import com.auto.framework.iface.ITestComponent;
import com.auto.framework.reporter.TestReporter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Priytam Jee Pandey
 * Date: 30/05/20
 * Time: 8:46 am
 * email: mrpjpandey@gmail.com
 */
public class ReqResTestCase extends AbstractTestCase {

    private ReqResServer server;

    protected ReqResTestCase() {
        super("ReqRes");
    }

    @Override
    protected void initComponents() {
        AutoConf.Application currentApplicationConfig = getCurrentApplicationConfig();
        TestComponentData testComponentData = new TestComponentData.Builder()
                .build(currentApplicationConfig.getServer(), currentApplicationConfig.getLogDir());
        server = new ReqResServer(testComponentData);
    }

    @Override
    public List<? extends ITestComponent> getTestComponents() {
        TestReporter.TRACE("init example components");
        List<ITestComponent> lstComponents = new ArrayList<>();
        lstComponents.add(server);
        return lstComponents;
    }


    public ReqResServer getServer() {
        return server;
    }
}