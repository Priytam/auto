package com.auto.framework.operation.commmand;

import com.auto.framework.operation.OpRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */

/**
 * Request for Commandline based operation
 * host if needs to be run on remote
 * arrCommand actual command to be tun
 * sUser if run as specific user
 * name readable name of command
 */
public class CommandRequest implements OpRequest {
    private final String host;
    private final String[] arrCommand;
    private String sUser = null;

    /**
     * @param arrCommand command to run each word as new element of array
     */
    public CommandRequest(String[] arrCommand) {
        this(arrCommand, null);
    }

    /**
     * @param arrCommand  command to run each word as new element of array
     * @param host hostname on which command needs to be
     *             If host is provided make sure ssh script
     *             is configure otherwise command will fail to run
     */
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

    /**
     *  returns name of command usually first word
     */
    @Override
    public String getCommandName() {
        if (null == arrCommand || arrCommand.length <= 0 || StringUtils.isBlank(arrCommand[0])) {
            return "UNKNOWN";
        }
        return arrCommand[0].split(" ")[0].replace("/", "_");
    }

    /**
     * @return {@link String} full command as string
     */
    public String getFullCommand() {
        if (null == arrCommand || arrCommand.length <= 0 || StringUtils.isBlank(arrCommand[0])) {
            return StringUtils.EMPTY;
        }
        return String.join(" ", arrCommand);
    }
}
