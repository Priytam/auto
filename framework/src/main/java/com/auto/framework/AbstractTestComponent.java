package com.auto.framework;

import com.auto.framework.check.Check;
import com.auto.framework.iface.ITestComponent;
import com.auto.framework.operation.OpResult;
import com.auto.framework.operation.Operation;
import com.auto.framework.reporter.TestReporter;
import com.auto.framework.utils.ThreadUtils;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public abstract class AbstractTestComponent implements ITestComponent {
    private final int port;
    private final String host;
    private final String logDir;
    private static final int NUMBER_OF_TIMES_TRY_TO_START = 5;
    private final String server;
    private final String installationDir;
    private final String resourcePath;
    private final TestComponentData componentData;

    protected AbstractTestComponent(TestComponentData data) {
        this.host = data.getHost();
        this.port = data.getPort();
        this.logDir = data.getLogDir();
        this.server = data.getServer();
        this.installationDir = data.getInstallationDir();
        this.resourcePath = data.getResourcePath();
        this.componentData = data;
    }

    @Override
    public void start() {
        TestReporter.TRACE("Starting component " + this);
        executeStartOperation();
        TestReporter.TRACE("Component started " + this);
    }

    @Override
    public void stop() {
        performStopOperation();
        verifyProcessStopped();
    }

    protected void executeStartOperation() {
        Operation op = getStartOperation();
        int tried = 0;
        do {
            if (tried > 0) {
                ThreadUtils.sleep(1L);
            }
            performOperation(op);
            tried++;
        } while (!isRunning() && tried < NUMBER_OF_TIMES_TRY_TO_START);

        if (!isRunning()) {
            TestReporter.FATAL("Couldn't start component " + this + "\n" + op.getResult());
        }
    }

    protected void performStopOperation() {
        TestReporter.TRACE("Stopping component " + this);
        Operation op = getStopOperation();
        performOperation(op);
    }

    public void verifyProcessStopped() {
        Check.assertBusyWait((b) -> isRunning(), () -> "", 4, 3, "Unable to stop component");
        TestReporter.TRACE("Component stopped " + this);
    }

    public final OpResult performOperation(Operation oOperation) {
        oOperation.execute();
        return oOperation.getResult();
    }

    public OpResult performFailedOperation(Operation oOperation) {
        OpResult result = performOperation(oOperation);
        if (result.getExitStatus() == 0) {
            TestReporter.FAIL("Succeeded to perform " + oOperation.getName() + "operation when should have failed. Command result: " + result);
        }
        return result;
    }

    public OpResult performSuccessOperation(Operation oOperation) {
        OpResult result = performOperation(oOperation);
        if (result.getExitStatus() != 0) {
            TestReporter.FAIL("Failed to perform ('" + oOperation.getName() + "') operation when should have succeeded. Command result: " + result);
        }
        return result;
    }

    protected abstract Operation getStartOperation();

    protected abstract Operation getStopOperation();

    @Override
    public void restart() {
        stop();
        verifyProcessStopped();
        start();
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getLogDir() {
        return logDir;
    }

    @Override
    public String getServer() {
        return server;
    }

    @Override
    public String getComponentName() {
        return getClass().getSimpleName();
    }

    @Override
    public Integer getCleanOrder() {
        return 0;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getInstallationDir() {
        return installationDir;
    }

    @Override
    public String getResourcePath() {
        return resourcePath;
    }

    @Override
    public TestComponentData getComponentData() {
        return componentData;
    }
}