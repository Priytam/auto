package com.auto.framework.operation.commmand;

import com.auto.framework.reporter.TestReporter;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestCommandExecution {
    public static final long DEFAULT_COMMAND_TIMEOUT = 1000;
    private static Map<String, String> mpEnv = Collections.synchronizedMap(new HashMap<String, String>());
    private static Map<String, String> mpDefaultEnv = new HashMap<String, String>();

    static {
        initDefaultEnvMap();
    }

    private static void initDefaultEnvMap() {
        mpDefaultEnv.put("TZ", TimeZone.getDefault().getID());
    }

    public static CommandResult runCommandWithoutTrace(String... s) {
        CommandRequest cRequest = new CommandRequest(s);
        CommandRunner commandRunner = new CommandRunner(generateCompEnvironment());
        commandRunner.runCommand(cRequest);
        return commandRunner.getCommandResult();
    }

    public static CommandResult runCommand(String... arrCmd) {
        CommandRequest cRequest = new CommandRequest(arrCmd);
        CommandRunner commandRunner = new CommandRunner(generateCompEnvironment());
        commandRunner.runCommand(cRequest);
        TestReporter.traceExecution(cRequest, commandRunner.getCommandResult());
        return commandRunner.getCommandResult();
    }

    public static CommandResult runCommand(String[] arrCmd, String host) {
        CommandRequest cRequest = new CommandRequest(arrCmd, host);
        CommandRunner commandRunner = new CommandRunner(generateCompEnvironment());
        commandRunner.runCommand(cRequest);
        TestReporter.traceExecution(cRequest, commandRunner.getCommandResult());
        return commandRunner.getCommandResult();
    }

    public static CommandResult runCommand(String[] arrCmd, Map<String, String> mapEnv) {
        CommandRequest cRequest = new CommandRequest(arrCmd);
        Map<String, String> env = generateCompEnvironment();
        env.putAll(mapEnv);
        CommandRunner commandRunner = new CommandRunner(env);
        commandRunner.runCommand(cRequest);
        TestReporter.traceExecution(cRequest, commandRunner.getCommandResult());
        return commandRunner.getCommandResult();
    }

    public static CommandResult runCommand(String[] arrCmd, Map<String, String> mapEnv, String host) {
        CommandRequest cRequest = new CommandRequest(arrCmd, host);
        Map<String, String> env = generateCompEnvironment();
        env.putAll(mapEnv);
        CommandRunner commandRunner = new CommandRunner(env);
        commandRunner.runCommand(cRequest);
        TestReporter.traceExecution(cRequest, commandRunner.getCommandResult());
        return commandRunner.getCommandResult();
    }

    //TODO
    public static CommandResult runCommandInteractively(String[] toArray, String sHost, long commandTimeout) {
        return null;
    }

    public final static void reset() {
        mpEnv.clear();
    }

    public static Map<String, String> generateCompEnvironment() {
        Map<String, String> mpUserEnv = new HashMap<String, String>();
        synchronized (mpEnv) {
            mpUserEnv.putAll(mpEnv);
        }
        if (!mpUserEnv.isEmpty()) {
            for (Map.Entry<String, String> e : mpDefaultEnv.entrySet()) {
                if (!mpUserEnv.containsKey(e.getKey())) {
                    mpUserEnv.put(e.getKey(), e.getValue());
                }
            }
        }
        return mpUserEnv;
    }

    public static void setenv(String sEnv, String sValue) {
        if (null != sValue) {
            mpEnv.put(sEnv, sValue);
        } else {
            mpEnv.remove(sEnv);
        }
    }

    public static void unSetEnv(String sEnv) {
        if (mpEnv.containsKey(sEnv)) {
            mpEnv.remove(sEnv);
        }
    }

    public static void unSetDefaultEnv(String sEnv) {
        if (mpDefaultEnv.containsKey(sEnv)) {
            mpDefaultEnv.remove(sEnv);
        }
    }

    public static void SetDefaultEnv(String sKey, String sValue) {
        mpDefaultEnv.put(sKey, sValue);
    }
}
