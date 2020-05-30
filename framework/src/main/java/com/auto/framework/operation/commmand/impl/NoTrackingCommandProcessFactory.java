package com.auto.framework.operation.commmand.impl;

import java.io.File;
import java.io.IOException;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
class NoTrackingCommandProcessFactory implements CommandProcessFactory {
    @Override
    public NoTrackingCommandProcess createAndExec(String[] cmd, File cwd, String[] env, String user) throws IOException {
        NoTrackingCommandProcess ret = new NoTrackingCommandProcess();
        ret.exec(cmd, cwd, env, user);
        return ret;
    }
}