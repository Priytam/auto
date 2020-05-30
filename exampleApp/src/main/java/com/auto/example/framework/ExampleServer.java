package com.auto.example.framework;

import com.auto.framework.AbstractAlwaysRunningTestComponent;
import com.auto.framework.TestComponentData;

/**
 * User: Priytam Jee Pandey
 * Date: 30/05/20
 * Time: 8:45 am
 * email: mrpjpandey@gmail.com
 */
public class ExampleServer extends AbstractAlwaysRunningTestComponent {

    protected ExampleServer(TestComponentData dData) {
        super(dData);
    }

    @Override
    public void clean(boolean bForce) {

    }

    @Override
    public void prepare() {

    }
}
