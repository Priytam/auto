package com.auto.framework.operation.commmand;

import com.auto.framework.utils.FileUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

/**
 * User: Priytam Jee Pandey
 * Date: 03/06/20
 * Time: 11:31 am
 * email: priytam.pandey@cleartrip.com
 */
public class CommandRunnerTest {

    Map<String, String> env;

    @Before
    public void prepare() {
        prepareCommand(null);
    }

    private void prepareCommand(String line) {
        env = Maps.newHashMap();
        env.put("hello", "world");

        try {
            String pathname = "/tmp/commandRunnerTest.sh";
            File file = new File(pathname);
            FileUtil.delete(file);
            FileWriter fFileWriter = new FileWriter(file, true);
            fFileWriter.append("#!/bin/sh\n");
            fFileWriter.append("echo Hello World\n");
            if (StringUtils.isNotEmpty(line)) {
                fFileWriter.append(line);
            }
            fFileWriter.close();
            FileUtil.chmod(pathname, "777", false);
        } catch (Exception e) {
        }
    }

    @Test
    public void shouldSuccess() {
        CommandRunner commandRunner = new CommandRunner(Maps.newHashMap());
        commandRunner.runCommand(new CommandRequest(new String[]{"/tmp/commandRunnerTest.sh"}));
        CommandResult commandResult = commandRunner.getCommandResult();
        Assert.assertEquals(commandResult.getExitStatus(), 0);
        Assert.assertEquals(commandResult.getStdOut().get(0), "Hello World");
    }

    @Test
    public void emptyError() {
        CommandRunner commandRunner = new CommandRunner(Maps.newHashMap());
        commandRunner.runCommand(new CommandRequest(new String[]{"/tmp/commandRunnerTest.sh"}));
        CommandResult commandResult = commandRunner.getCommandResult();
        Assert.assertEquals(commandResult.getExitStatus(), 0);
        Assert.assertTrue(commandResult.getStdErr().isEmpty());
    }

    @Test
    public void shouldReadEnv() {
        prepareCommand("echo \"$hello\"\n");
        CommandRunner commandRunner = new CommandRunner(env);
        commandRunner.runCommand(new CommandRequest(new String[]{"/tmp/commandRunnerTest.sh"}));
        CommandResult commandResult = commandRunner.getCommandResult();
        Assert.assertEquals(commandResult.getExitStatus(), 0);
        Assert.assertEquals(commandResult.getStdOut().get(1), "world");
    }

    @Test
    public void shouldRunFromInstallationDir() {
        CommandRunner commandRunner = new CommandRunner(Maps.newHashMap(), "/tmp", TestCommandExecution.DEFAULT_COMMAND_TIMEOUT);
        commandRunner.runCommand(new CommandRequest(new String[]{"./commandRunnerTest.sh"}));
        CommandResult commandResult = commandRunner.getCommandResult();
        Assert.assertEquals(commandResult.getExitStatus(), 0);
        Assert.assertEquals(commandResult.getStdOut().get(0), "Hello World");
    }

    @Test
    public void shouldTimeOut() {
        prepareCommand("sleep 10\n");
        CommandRunner commandRunner = new CommandRunner(Maps.newHashMap(), "/tmp", 0);
        commandRunner.runCommand(new CommandRequest(new String[]{"./commandRunnerTest.sh"}));
        CommandResult commandResult = commandRunner.getCommandResult();
        Assert.assertEquals(commandResult.getExitStatus(), 0);
        Assert.assertEquals(commandResult.getStdOut().get(0), "Hello World");
    }

    @Test
    public void shouldFail() {
        CommandRunner commandRunner = new CommandRunner(Maps.newHashMap(), "/tmp", 0);
        commandRunner.runCommand(new CommandRequest(new String[]{"./commandRunnerTest1.sh"}));
        CommandResult commandResult = commandRunner.getCommandResult();
        Assert.assertEquals(commandResult.getExitStatus(), -1);
    }

}