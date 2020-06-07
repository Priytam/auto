package com.auto.framework.iface;

import com.auto.framework.TestComponentData;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public interface ITestComponent {
    void start();

    void stop();

    void restart();

    boolean isRunning();

    String getHost();

    int getPort();

    String getLogDir();

    Integer getCleanOrder();

    String getServer();

    String getComponentName();

    void clean(boolean bForce);

    void prepare();

    String getInstallationDir();

    String getResourcePath();

    TestComponentData getComponentData();
}
