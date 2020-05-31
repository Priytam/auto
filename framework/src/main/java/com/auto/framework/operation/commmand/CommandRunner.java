package com.auto.framework.operation.commmand;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * User: Priytam Jee Pandey
 * Date: 31/05/20
 * Time: 11:07 am
 * email: priytam.pandey@cleartrip.com
 */
public class CommandRunner {

    private final Map<String, String> envVariable;
    private CommandResult commandResult;
    private String installationDir;
    private long commandTimeout;
    private String cwd;

    public CommandRunner(Map<String, String> env) {
        this.envVariable = env;
    }

    public CommandRunner(Map<String, String> env, String installationDir, long commandTimeout, String cwd) {
        this.envVariable = env;
        this.installationDir = installationDir;
        this.commandTimeout = commandTimeout;
        this.cwd = cwd;
    }

    //TODO handle timeout and remote host
    public void runCommand(CommandRequest request) {
        String command = request.getFullCommand();
        if (StringUtils.isNotEmpty(installationDir)) {
            command = installationDir + command;
        }
        List<String> listsResult = Lists.newArrayList();
        List<String> listsError = Lists.newArrayList();
        long timeTaken;
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
            commandResult = new CommandResult(exitVal, listsResult, listsError, timeTaken);

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            timeTaken = endTime - startTime;
            commandResult = new CommandResult(0, Collections.emptyList(), Lists.newArrayList(e.getMessage()), timeTaken);
        }
    }

    public CommandResult getCommandResult() {
        return commandResult;
    }

    public Map<String, String> getEnvVariable() {
        return envVariable;
    }
}
