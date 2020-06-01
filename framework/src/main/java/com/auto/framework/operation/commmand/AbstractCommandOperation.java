package com.auto.framework.operation.commmand;

import com.auto.framework.operation.Operation;
import com.auto.framework.reporter.TestReporter;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.google.common.collect.Maps.newHashMap;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public abstract class AbstractCommandOperation implements Operation {
    private final CommandRequest request;
    private final String installationDir;
    private final Map<String, String> mpEnv = newHashMap();
    private long commandTimeout = TestCommandExecution.DEFAULT_COMMAND_TIMEOUT;
    protected CommandResult result = null;
    private String cwd;

    public AbstractCommandOperation(String installationDir, CommandRequest commandRequest) {
        this.installationDir = installationDir;
        this.request = commandRequest;
        mpEnv.put("TZ", TimeZone.getDefault().getID());
    }

    public AbstractCommandOperation(String installationDir, CommandRequest commandRequest, Map<String, String> mapEnvVariable) {
        this.installationDir = installationDir;
        this.request = commandRequest;
        mpEnv.put("TZ", TimeZone.getDefault().getID());
        mpEnv.putAll(mapEnvVariable);
    }

    @Override
    public void execute() {
        if (shouldRunInBackground()) {
            new Thread(this::prepareAndExecute).start();
        } else {
            prepareAndExecute();
        }
    }

    @Override
    public CommandRequest getRequest() {
        return request;
    }

    @Override
    public CommandResult getResult() {
        return result;
    }

    private void prepareAndExecute() {
        try {
            CommandRunner commandRunner = new CommandRunner(getEnv(), getInstallationDir(), getCommandTimeout(), getCWD());
            commandRunner.runCommand(request);
            result = commandRunner.getCommandResult();
        }  finally {
            TestReporter.traceExecution(request, result);
        }
    }

    protected String getCWD() {
        return cwd;
    }

    public void setCwd(String cwd) {
        this.cwd = cwd;
    }

    protected Map<String, String> getEnv() {
        HashMap<String, String> map = newHashMap(TestCommandExecution.generateCompEnvironment());
        map.putAll(mpEnv);
        return map;
    }

    public void setEnv(String name, String val) {
        mpEnv.put(name, val);
    }

    public String getInstallationDir() {
        return installationDir;
    }

    protected String getHost() {
        return request.getHost();
    }

    public void setCommandTimeout(long commandTimeout) {
        this.commandTimeout = commandTimeout;
    }

    public long getCommandTimeout() {
        return commandTimeout;
    }


}
