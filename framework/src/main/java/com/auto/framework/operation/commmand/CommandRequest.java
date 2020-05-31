package com.auto.framework.operation.commmand;

import com.auto.framework.operation.OpRequest;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class CommandRequest implements OpRequest {
    private String host;
    private String[] arrCommand = null;
    private String sUser = null;
    private String name;

    public CommandRequest(String[] arrCommand) {
        this(arrCommand, null);
    }

    public CommandRequest(String[] arrCommand, String host) {
        super();
        this.arrCommand = arrCommand;
        this.host = host;
    }

    public String[] getCommand() {
        return arrCommand;
    }

    public String getUser() {
        return sUser;
    }

    public String getHost() {
        return host;
    }

    public void setUser(String user) {
        sUser = user;
    }

    @Override
    public String getCommandName() {
        if (null == arrCommand || arrCommand.length <= 0 || StringUtils.isBlank(arrCommand[0])) {
            return "UNKNOWN";
        }
        return arrCommand[0].split(" ")[0].replace("/", "_");
    }

    public String getFullCommand() {
        if (null == arrCommand || arrCommand.length <= 0 || StringUtils.isBlank(arrCommand[0])) {
            return StringUtils.EMPTY;
        }
        return String.join(" ", arrCommand);
    }
}
