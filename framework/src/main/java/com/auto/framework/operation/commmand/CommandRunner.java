package com.auto.framework.operation.commmand;

import com.auto.framework.operation.commmand.runner.CommandProcess;
import com.auto.framework.utils.ThreadMonitor;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Collections;
import java.util.List;
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

    /**
     * With default timeout, null cwd and null installation dir
     *
     * @param env environment value for command if any
     */
    public CommandRunner(Map<String, String> env) {
        this.envVariable = env;
    }

    /**
     * @param env             environment value for command if any
     * @param installationDir command binary path
     * @param commandTimeout  command time out in seconds
     */
    public CommandRunner(Map<String, String> env, String installationDir, long commandTimeout) {
        this.envVariable = env;
        this.installationDir = installationDir;
        this.commandTimeout = commandTimeout;
    }

    /**
     * @param commandRequest runs command as FutureTask in thread and waits till
     *                       commandTimeout for it return, If doesn't return status
     *                       will be -1
     */

    @Deprecated
    public void runCommandOld(CommandRequest commandRequest) {
        long timeTaken;
        long startTime = System.currentTimeMillis();

        File wd = null;
        if (StringUtils.isNotEmpty(installationDir)) {
            wd = new File(installationDir);
        }
        String[] environment = null;
        if (MapUtils.isNotEmpty(envVariable)) {
            environment = envVariable.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).toArray(String[]::new);
        }
        try {

            ByteArrayOutputStream ourStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

            FutureTask<Integer> task = new FutureTask<>(new CommandProcess(commandRequest.getCommand(), environment, wd, ourStream, errorStream));
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

    /**
     * @param commandRequest runs command as FutureTask in thread and waits till
     *                       commandTimeout for it return, If doesn't return status
     *                       will be -1
     */
    public void runCommand(CommandRequest commandRequest) {
        long timeTaken;
        long startTime = System.currentTimeMillis();

        String[] command = commandRequest.getCommand();
        File wd = null;
        if (StringUtils.isNotEmpty(installationDir)) {
            wd = new File(installationDir);
        }

        String[] environment = null;
        if (MapUtils.isNotEmpty(envVariable)) {
            environment = envVariable.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).toArray(String[]::new);
        }

        List<String> listsResult = Lists.newArrayList();
        List<String> listsError = Lists.newArrayList();
        Process process = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        InputStream errorStream = null;
        InputStreamReader errorStreamReader = null;
        OutputStream outputStream = null;
        try {
            Thread monitor = ThreadMonitor.start(commandTimeout);

            process = Runtime.getRuntime().exec(command, environment, wd);
            outputStream = process.getOutputStream();
            inputStream = process.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                listsResult.add(line);
            }
            errorStream = process.getErrorStream();
            errorStreamReader = new InputStreamReader(errorStream);
            reader = new BufferedReader(errorStreamReader);
            String error;
            while ((error = reader.readLine()) != null) {
                listsError.add(error);
            }
            int exitVal = process.waitFor();

            ThreadMonitor.stop(monitor);

            long endTime = System.currentTimeMillis();
            timeTaken = endTime - startTime;
            commandResult = new CommandResult(exitVal, listsResult, listsError, timeTaken);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            timeTaken = endTime - startTime;
            commandResult = new CommandResult(-1, Collections.emptyList(), Lists.newArrayList(e.getMessage()), timeTaken);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(inputStreamReader);
            IOUtils.closeQuietly(errorStream);
            IOUtils.closeQuietly(errorStreamReader);
            IOUtils.closeQuietly(outputStream);
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * @param command array of string
     * @param element the elements to be added
     * @return String[] returns array with element added first
     */
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
