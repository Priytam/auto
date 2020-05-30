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

    public static CommandResult runCommand(String... s) {
        String join = String.join(" ", s);
        CommandRequest cRequest = new CommandRequest(s);
        CommandResult commandResult = runCommand(join, generateCompEnvironment());
        TestReporter.traceExecution(cRequest, commandResult);
        return commandResult;
    }

    public static CommandResult runCommandWithoutTrace(String... s) {
        String join = String.join(" ", s);
        CommandRequest cRequest = new CommandRequest(s);
        return runCommand(join, generateCompEnvironment());
    }

    public static CommandResult runCommand(String[] arrCmd, String mHost) {
        String join = String.join(" ", arrCmd);
        CommandRequest cRequest = new CommandRequest(arrCmd, mHost);
        CommandResult commandResult = runCommand(join);
        TestReporter.traceExecution(cRequest, commandResult);
        return commandResult;
    }

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


    //TODO use command process
    private static CommandResult runCommand(String command, Map<String, String> env) {

        List<String> listsResult = Lists.newArrayList();
        List<String> listsError = Lists.newArrayList();
        long timeTaken = 0;
        long startTime = System.currentTimeMillis();
        try {
            Process start = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(start.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                listsResult.add(line);
            }

            reader = new BufferedReader(new InputStreamReader(start.getErrorStream()));
            String error;
            while ((error = reader.readLine()) != null) {
                listsError.add(error);
            }
            int exitVal = start.waitFor();
            long endTime = System.currentTimeMillis();
            timeTaken = endTime - startTime;
            return new CommandResult(exitVal, listsResult, listsError, timeTaken);

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            timeTaken = endTime - startTime;
            return new CommandResult(-500, Collections.emptyList(), Lists.newArrayList(e.getMessage()), timeTaken);
        }
    }

}
