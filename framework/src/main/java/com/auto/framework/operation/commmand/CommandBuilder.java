package com.auto.framework.operation.commmand;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * User: Priytam Jee Pandey
 * Date: 07/06/20
 * Time: 4:58 pm
 * email: priytam.pandey@cleartrip.com
 */
public class CommandBuilder {
    private String installationDir;
    private Map<String, String> mpEnv = newHashMap();
    private long commandTimeout = TestCommandExecution.DEFAULT_COMMAND_TIMEOUT;
    private String host;
    private String[] arrCommand;

    public CommandBuilder withCommand(String[] arrCommand) {
        this.arrCommand = arrCommand;
        return this;
    }

    public CommandBuilder withHost(String host) {
        this.host = host;
        return this;
    }

    public CommandBuilder withInstallationDir(String installationDir) {
        this.installationDir = installationDir;
        return this;
    }

    public CommandBuilder withEnvVariable(Map<String, String> mpEnv) {
        this.mpEnv.putAll(mpEnv);
        return this;
    }

    public CommandBuilder withTimeout(long timeout) {
        this.commandTimeout = timeout;
        return this;
    }

    public CommandRequest buildRequest() {
        return StringUtils.isNotEmpty(host) ? new CommandRequest(arrCommand, host) : new CommandRequest(arrCommand);
    }

    public String getInstallationDir() {
        return installationDir;
    }

    public Map<String, String> getMpEnv() {
        return mpEnv;
    }

    public long getCommandTimeout() {
        return commandTimeout;
    }
}
