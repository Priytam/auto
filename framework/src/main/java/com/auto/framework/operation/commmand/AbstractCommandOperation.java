package com.auto.framework.operation.commmand;

import com.auto.framework.operation.Operation;
import com.auto.framework.reporter.TestReporter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public abstract class AbstractCommandOperation implements Operation {
    private static final Map<String, String> mpEnv = Collections.synchronizedMap(new HashMap<String, String>());
    private CommandRequest request;
    private CommandResult result = null;
    private CommandBuilder commandBuilder;

    public AbstractCommandOperation() {
        mpEnv.put("TZ", TimeZone.getDefault().getID());
        commandBuilder = getCommandBuilder();
        request = commandBuilder.buildRequest();
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
            CommandRunner commandRunner = new CommandRunner(getEnv(), getInstallationDir(), getCommandTimeout());
            commandRunner.runCommand(request);
            result = commandRunner.getCommandResult();
        }  finally {
            TestReporter.traceExecution(request, result);
        }
    }

    protected Map<String, String> getEnv() {
        Map<String, String> mpUserEnv;
        synchronized (mpEnv) {
            mpUserEnv = new HashMap<>(mpEnv);
        }
        mpUserEnv.putAll(commandBuilder.getMpEnv());
        return mpUserEnv;
    }

    public String getInstallationDir() {
        return commandBuilder.getInstallationDir();
    }

    protected String getHost() {
        return request.getHost();
    }

    public long getCommandTimeout() {
        return commandBuilder.getCommandTimeout();
    }

    public static void setEnv(String sEnv, String sValue) {
        if (null != sValue) {
            mpEnv.put(sEnv, sValue);
        } else {
            mpEnv.remove(sEnv);
        }
    }

    public static void unSetEnv(String sEnv) {
        mpEnv.remove(sEnv);
    }

    public static void reset() {
        mpEnv.clear();
    }


    protected abstract CommandBuilder getCommandBuilder();

}
