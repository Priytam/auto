package com.auto.framework.operation;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public interface Operation {
    void execute();

    OpRequest getRequest();

    OpResult getResult();

    boolean shouldRunInBackground();

    String getName();
}
