package com.auto.framework.operation;

import java.util.List;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public interface OpResult {
    int getExitStatus();

    List<String> getStdOut();

    List<String> getStdErr();

    long getExecutionTime();

    String toStringAsOneLine();
}
