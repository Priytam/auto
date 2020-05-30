package com.auto.framework.operation.commmand.impl;

import java.io.File;
import java.io.IOException;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
interface CommandProcessFactory {
    CommandProcess createAndExec(String[] cmd, File cwd, String[] env, String user) throws IOException;
}
