package com.auto.framework.reporter.appender.startegy;

import com.auto.framework.reporter.iface.IOutputFileStrategy;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class SeparateFilePerCommandStrategy implements IOutputFileStrategy {

    @Override
    public String getCommandFile(String commandNum) {
        return "operation_" + commandNum;
    }

}
