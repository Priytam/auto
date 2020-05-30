package com.auto.framework.iface;

import java.util.List;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public interface ITestCase {
    String getTestName();

    String getTestGroup();

    String getSandBoxDir();

    List<? extends ITestComponent> getTestComponents();

    boolean isRunning();

    void tearDown();
}
