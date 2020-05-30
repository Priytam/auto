package com.auto.framework.operation.commmand.impl;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public abstract class CommandProcess extends Process {

    public abstract void timeout();

    public abstract boolean isTimedOut();

    public abstract int getPID() throws Exception;

}
