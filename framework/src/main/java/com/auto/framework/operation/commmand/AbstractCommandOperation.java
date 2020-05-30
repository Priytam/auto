package com.auto.framework.operation.commmand;

import com.auto.framework.operation.OpResult;
import com.auto.framework.operation.Operation;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public abstract class AbstractCommandOperation implements Operation {
    private final CommandRequest request;
    private String installationDir;
    protected CommandResult rResult = null;
    private long commandTimeout = TestCommandExecution.DEFAULT_COMMAND_TIMEOUT;
    private Map<String, String> mpEnv = newHashMap();

    public AbstractCommandOperation() {
        this("", new CommandRequest(new String[0]));
    }

    public AbstractCommandOperation(String installationDir, CommandRequest commandRequest) {
        this.installationDir = installationDir;
        this.request = commandRequest;
    }

    @Override
    public void execute() {
        rResult = TestCommandExecution.runCommand(request.getCommand(), request.getHost() /*getCommandTimeout(), getEnv(), shouldRunInBackground(), getCWD()*/);
    }

    @Override
    public OpResult getResult() {
        return rResult;
    }

    protected String getCWD() {
        return installationDir;
    }

    protected Map<String, String> getEnv() {
        HashMap<String, String> $ = newHashMap(TestCommandExecution.generateCompEnvironment());
        $.putAll(mpEnv);
        return $;
    }

    public CommandResult executeInteractive() {
        return TestCommandExecution.runCommandInteractively(request.getCommand(), request.getHost(), getCommandTimeout());
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

    public void setEnv(String name, String val) {
        mpEnv.put(name, val);
    }
}
