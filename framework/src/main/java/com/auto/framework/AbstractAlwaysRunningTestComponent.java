package com.auto.framework;

import com.auto.framework.operation.Operation;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public abstract class AbstractAlwaysRunningTestComponent extends AbstractTestComponent {
    private boolean isRunning = false;

    protected AbstractAlwaysRunningTestComponent(TestComponentData dData) {
        super(dData);
    }

    @Override
    public void start() {
        isRunning = true;
    }


    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    protected Operation getStartOperation() {
        return null;
    }

    protected Operation getStopOperation() {
        return null;
    }
}
