package com.auto.framework.operation.commmand;

import com.auto.framework.operation.commmand.runner.CommandProcess;
import com.auto.framework.reporter.TestReporter;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

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
    private long commandTimeout = TestCommandExecution.DEFAULT_COMMAND_TIMEOUT;
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

    public void runCommand(CommandRequest commandRequest) {
        long timeTaken;
        long startTime = System.currentTimeMillis();

        String[] command = commandRequest.getCommand();
        if (StringUtils.isNotEmpty(installationDir)) {
            command = addFirst(command, installationDir);
        }
        File wd = null;
        if (StringUtils.isNotEmpty(cwd)) {
            wd = new File(cwd);
        }
        String[] environment = null;
        if (MapUtils.isNotEmpty(envVariable)) {
            environment = envVariable.entrySet().stream().map(entry -> entry.getKey() + "+" + entry.getValue()).toArray(String[]::new);
        }
        try {

            ByteArrayOutputStream ourStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

            FutureTask<Integer> task = new FutureTask<>(new CommandProcess(command, environment, wd, ourStream, errorStream));
            Thread t = new Thread(task);
            t.start();
            Integer integer = task.get(commandTimeout, TimeUnit.SECONDS);

            Iterable<String> iterableOut = Splitter.on(System.getProperty("line.separator")).split(new String(ourStream.toByteArray()));
            Iterable<String> iterableErr = Splitter.on(System.getProperty("line.separator")).split(new String(ourStream.toByteArray()));

            long endTime = System.currentTimeMillis();
            timeTaken = endTime - startTime;
            commandResult = new CommandResult(integer, Lists.newArrayList(iterableOut), Lists.newArrayList(iterableErr), timeTaken);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            timeTaken = endTime - startTime;
            commandResult = new CommandResult(-1, Collections.emptyList(), Lists.newArrayList(e.getMessage()), timeTaken);
        }
    }

    /* public void runCommand(CommandRequest request) {
         CommandProcess commandProcess = new CommandProcess();
        commandProcess.runProcess(request.getCommand());

        String command = request.getFullCommand();
        if (StringUtils.isNotEmpty(installationDir)) {
            command = installationDir + command;
        }
        List<String> listsResult = Lists.newArrayList();
        List<String> listsError = Lists.newArrayList();
        long timeTaken;
        long startTime = System.currentTimeMillis();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                listsResult.add(line);
            }

            reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String error;
            while ((error = reader.readLine()) != null) {
                listsError.add(error);
            }
            //int exitVal = process.waitFor();
            if (!process.waitFor(TimeUnit.MILLISECONDS.toSeconds(commandTimeout), TimeUnit.SECONDS)) {
                process.destroy();
            }
            long endTime = System.currentTimeMillis();
            timeTaken = endTime - startTime;
            commandResult = new CommandResult(process.exitValue(), listsResult, listsError, timeTaken);

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            timeTaken = endTime - startTime;
            commandResult = new CommandResult(0, Collections.emptyList(), Lists.newArrayList(e.getMessage()), timeTaken);
        }
    }*/
    private String[] addFirst(String command[], String element) {
        String[] arr = new String[command.length + 1];
        arr[0] = element;
        System.arraycopy(command, 0, arr, 1, command.length);
        return arr;
    }

    public CommandResult getCommandResult() {
        return commandResult;
    }

    public Map<String, String> getEnvVariable() {
        return envVariable;
    }
}
